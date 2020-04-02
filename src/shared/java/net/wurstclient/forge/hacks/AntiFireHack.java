package net.wurstclient.forge.hacks;

import net.minecraftforge.common.MinecraftForge;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;

public class AntiFireHack extends Hack{
	private final CheckboxSetting collisions=new CheckboxSetting("Collisions","Give fire collision boxes", false);
	public AntiFireHack() {
		super("AntiFire","Removes fire");
		setCategory(Category.PLAYER);
		addSetting(collisions);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
		
	}
	
}
