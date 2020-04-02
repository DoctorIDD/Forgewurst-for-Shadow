package net.wurstclient.forge.hacks;

import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.EnumSetting;

public class NoHurtHack extends Hack {
	private final EnumSetting<Mode> mode = new EnumSetting<NoHurtHack.Mode>("Mode", Mode.values(), Mode.DEATH);

	public NoHurtHack() {
		super("NoHurt", "No Hurt");
		setCategory(Category.COMBAT);
		addSetting(mode);
	}

	@Override
	protected void onEnable() {
		if(mode.getSelected()==Mode.DEATH) {
			if(mc.player!=null) {
			mc.player.isDead=true;
			mc.gameSettings.keyBindForward.isPressed();
			}
		}
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		mc.player.isDead=false;
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onGet(WPacketInputEvent event) {
		if (mode.getSelected() == Mode.PACKET) {

			if (event.getPacket() instanceof SPacketUpdateHealth) {
				event.isCanceled();
			}
		}
	}

	private enum Mode {
		PACKET, DEATH
	}

}
