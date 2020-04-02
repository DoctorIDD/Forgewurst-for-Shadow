package net.wurstclient.forge.hacks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

public class AntiWeather extends Hack{
	public AntiWeather() {
		super("AntiWeather","Removes rain from your world");
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
		if (mc.world.isRaining())
            mc.world.setRainStrength(0);
	}
}
