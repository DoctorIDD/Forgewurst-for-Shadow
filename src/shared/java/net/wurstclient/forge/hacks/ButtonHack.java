package net.wurstclient.forge.hacks;

import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WGuiInventoryButtonEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

public class ButtonHack extends Hack {
	public ButtonHack() {
		super("Button", "");
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
	public void onGuiInventoryInit(WGuiInventoryButtonEvent.Init event) {

		event.getButtonList().add(new GuiButton(-2, mc.currentScreen.width / 2 - 50, mc.currentScreen.height / 2 - 120,
				100, 20, "Sanction"));
	}

	@SubscribeEvent
	public void onGuiInventoryButtonPress(WGuiInventoryButtonEvent.Press event) {
		if (event.getButton().id != -2)
			return;

		wurst.getHax().clickGuiHack.setEnabled(true);
	}

}
