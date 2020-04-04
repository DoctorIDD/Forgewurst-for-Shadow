package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.LocalPlayerUpdateEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.utils.ChatUtils;

public class TestHack extends Hack{
	public TestHack() {
		super("Test","test");
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
	public void onUpdate(WUpdateEvent event) {
		mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
		
	}
	@SubscribeEvent
	public void onPacketOutput(WPacketInputEvent event) {
		
		
		
		if(event.getPacket() instanceof SPacketChat) {
			ChatUtils.message("OK!Chat");
		}
		if(event.getPacket()instanceof SPacketUpdateHealth) {

			ChatUtils.message("Health update");
		}
		
		EntityPlayerSP player = WMinecraft.getPlayer();
		CPacketChatMessage packet = (CPacketChatMessage)event.getPacket();
		
		Packet<?> newPacket ;
		
	
		
		}
		
	}

