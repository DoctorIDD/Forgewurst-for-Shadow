package net.wurstclient.forge.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

public class BedModeHack extends Hack{
	public BedModeHack() {
		super("BedMode","Sleep while walking");
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
	public void onGuiUpdate(GuiOpenEvent event) {
		 if (event.getGui() instanceof GuiSleepMP) {
		      event.setCanceled(true);
		    }
	}
	
}
