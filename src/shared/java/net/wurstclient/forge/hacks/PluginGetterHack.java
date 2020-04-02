package net.wurstclient.forge.hacks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import joptsimple.internal.Strings;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.commands.UnlockCmd;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.compatibility.WPlayer;
import net.wurstclient.forge.compatibility.WPlayerController;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.Wrapper;

public class PluginGetterHack extends Hack{
	public PluginGetterHack() {
		super("PluginGetter","Get server plugins");
		setCategory(Category.OTHER);
	}

	@Override
	protected void onEnable() {
		if(Wrapper.getPlayer() == null) {
            return;
		}
		if(UnlockCmd.LOCK!=true) {
			ChatUtils.error("");
			return;
		}
	mc.player.connection.sendPacket(new CPacketTabComplete("/", null, false));
	MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	@SubscribeEvent
	public void onPacketInput(WPacketInputEvent event) {
		if(event.getPacket()instanceof SPacketTabComplete) {
			 SPacketTabComplete s3APacketTabComplete = (SPacketTabComplete) event.getPacket();
			 
			 List<String> plugins = new ArrayList<String>();
	         String[] commands = s3APacketTabComplete.getMatches();
	         
	         for(int i = 0; i < commands.length; i++) {
	                String[] command = commands[i].split(":");
	 
	                if(command.length > 1) {
	                    String pluginName = command[0].replace("/", "");
	 
	                    if(!plugins.contains(pluginName)) {
	                        plugins.add(pluginName);
	                    }
	                }
	            }
	         Collections.sort(plugins);
	         if(!plugins.isEmpty()) {
	                ChatUtils.message("Plugins \u00a77(\u00a78" + plugins.size() + "\u00a77): \u00a79" + Strings.join(plugins.toArray(new String[0]), "\u00a77, \u00a79"));
	            }
	            else
	            {
	                ChatUtils.error("No plugins found.");
	            }
	          this.setEnabled(false);   
	         
		}else {
			ChatUtils.message("No found");
			this.setEnabled(false);
		}
	}
	
}
