package net.wurstclient.forge.loader;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeyLoader {
	public static KeyBinding clickgui;
	public ModKeyLoader() {
		clickgui =new KeyBinding("key.cheat.clickgui", Keyboard.KEY_P,"key.categories.cheat");
		
		ClientRegistry.registerKeyBinding(clickgui);
	}
}
