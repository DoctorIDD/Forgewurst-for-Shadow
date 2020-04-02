package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

public class CancelPacketHack extends Hack {
	public CancelPacketHack() {
		super("CancelPacket","Cancel all packets");
		setCategory(Category.OTHER);
	}
	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	@SubscribeEvent
	public void onUpdate(WPacketOutputEvent event) {
		if(event.getPacket() instanceof CPacketConfirmTeleport) {
			 event.isCanceled();
		}
	}
}
