package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;

public class CustomChat extends Hack{
	private final CheckboxSetting commands =new CheckboxSetting("Commands", false);
	 private final String SUFFIX = " \u23D0 \u1D0B\u1D00\u1D0D\u026A";
	public CustomChat() {
		super("CustomChat","Modifies your chat messages");
		setCategory(Category.CHAT);
		addSetting(commands);
		
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
	public void onChat(WPacketOutputEvent event) {
		  if (event.getPacket() instanceof CPacketChatMessage) {
	            String s = ((CPacketChatMessage) event.getPacket()).getMessage();
	            if (s.startsWith("/") && !commands.isChecked()) return;
	            s += SUFFIX;
	            if (s.length() >= 256) s = s.substring(0,256);
	            FMLClientHandler.instance().getClientPlayerEntity().connection.sendPacket(new CPacketChatMessage(s));
		  }
	}
}
