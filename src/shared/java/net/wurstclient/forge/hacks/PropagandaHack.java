package net.wurstclient.forge.hacks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

public class PropagandaHack extends Hack{
	private final SliderSetting interval = new SliderSetting("interval", 100, 20, 500, 10, ValueDisplay.DECIMAL);
	 public int nu;
	public PropagandaHack() {
		super("Propaganda","");
		setCategory(Category.CHAT);
		addSetting(interval);
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
	public void onChat(WUpdateEvent event) {
		nu++;
		 if(nu>=interval.getValueI()) {
			 String message = "@" + "qq群897496163加入平A軍團！！";
				WMinecraft.getPlayer().sendChatMessage(message);
				nu=0;
		 }
	}
}
