package net.wurstclient.forge.hacks;

import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

public class AutoLeaveHack extends Hack{
	public AutoLeaveHack() {
		super("AutoLeave","Auto Leave when you are in the danger");
		setCategory(Category.COMBAT);
	}
	@Override
	protected void onEnable() {
		
	}
	
}
