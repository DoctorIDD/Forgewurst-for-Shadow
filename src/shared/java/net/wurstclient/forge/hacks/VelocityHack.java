package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.utils.Wrapper;

public class VelocityHack extends Hack{
	public VelocityHack(){
	super("Velocity","velocity");
	setCategory(Category.MOVEMENT);
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
	public void onUpdate(TickEvent.PlayerTickEvent event) {
		EntityPlayerSP player = Wrapper.getPlayer();
		if(player==null)
			return;
		if(player.hurtTime > 0 && player.hurtTime <= 7) {
			player.motionX *= 0.5;
					player.motionZ *= 0.5;
	      }
	      if(player.hurtTime > 0 && player.hurtTime < 6) {
	    	  player.motionX = 0.0;
	    			  player.motionZ = 0.0;
	      }
	}
}
