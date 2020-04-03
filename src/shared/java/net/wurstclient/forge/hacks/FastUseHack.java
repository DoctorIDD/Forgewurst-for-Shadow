package net.wurstclient.forge.hacks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.EnumSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;
import net.wurstclient.forge.utils.Wrapper;

public class FastUseHack extends Hack{
	private final SliderSetting tick=new SliderSetting("Ticks", 12, 1, 20, 1, ValueDisplay.DECIMAL);
	private final EnumSetting<MODE> mode=new EnumSetting<FastUseHack.MODE>("Mode", MODE.values(), MODE.PACKET);
	public FastUseHack() {
		super("FastUse","Use items faster");
		setCategory(Category.PLAYER);
		addSetting(mode);
		addSetting(tick);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		Wrapper.setDefaultTimer();
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		switch (mode.getSelected()) {
		case PACKET:
			if(mc.player.getItemInUseCount() >=  tick.getValue() && canUseItem(mc.player.getHeldItemMainhand().getItem())) {
				for (int i = 0; i < 30; i++) {
                    mc.player.connection.sendPacket(new CPacketPlayer(true));
                }
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                
			}
			break;

		case TIMER:
			if(mc.player.getItemInUseCount() >=  tick.getValue() && canUseItem(mc.player.getHeldItemMainhand().getItem())) {
                
                Wrapper.setTickLength(50/1.3555f);
            } else if (mc.getTickLength() == 50/1.3555f) {
               Wrapper.setDefaultTimer();
            }else {
            	Wrapper.setDefaultTimer();
            }
			break;
		}
	}
	private enum MODE{
		PACKET,TIMER;
	}
	 private boolean canUseItem(Item item) {
	    	boolean result = !((item instanceof ItemSword) || (item instanceof ItemBow));
	        return result;
	    }
}
