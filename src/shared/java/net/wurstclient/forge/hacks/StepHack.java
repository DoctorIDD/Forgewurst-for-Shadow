package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;
import net.wurstclient.forge.utils.Wrapper;

public class StepHack extends Hack {
	public float tempHeight;
	public int ticks = 0;
	private final SliderSetting height = new SliderSetting("Height", 0.5D, 0D, 10D, 0.1D, ValueDisplay.DECIMAL);

	public StepHack() {
		super("Step", "");
		setCategory(Category.MOVEMENT);
		addSetting(height);
	}

	@Override
	protected void onEnable() {
		ticks = 0;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		Wrapper.getPlayer().stepHeight = 0.5f;
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		EntityPlayerSP player = Wrapper.getPlayer();
		if(player.collidedHorizontally) {
			switch(ticks) {
				case 0:
				if(player.onGround)
					player.jump();
					break;
				case 7:
					player.motionY = 0;
					break;
				case 8:
				if(!player.onGround)
					player.setPosition(player.posX, player.posY + 1, player.posZ);
					break;
			}
			ticks++;
		} else {
			ticks = 0;
		}
	}

}
