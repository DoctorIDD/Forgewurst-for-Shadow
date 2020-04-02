package net.wurstclient.forge.hacks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.fmlevents.listener.event.DeathListener;
import net.wurstclient.fmlevents.listener.event.DeathListener.DeathEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

public class AutoRespawnHack extends Hack implements DeathListener {
	public AutoRespawnHack() {
		super("AutoRespawn", "");
		setCategory(Category.COMBAT);
	}

	@Override
	protected void onEnable() {

		MinecraftForge.EVENT_BUS.register(this);
		
	}

	@Override
	protected void onDisable() {

		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@Override
	public void onDeath() {
		mc.player.respawnPlayer();
	
	}

}
