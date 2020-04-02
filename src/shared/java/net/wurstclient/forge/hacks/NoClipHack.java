package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

public class NoClipHack extends Hack {
	private final SliderSetting speed = new SliderSetting("speed", 0.2, 0.1, 5, 0.1, ValueDisplay.DECIMAL);

	public NoClipHack() {
		super("NoClip",
				"Allows you to freely move through blocks.\n"
						+ "A block (e.g. sand) must fall on your head to activate it.\n\n"
						+ "\u00a7c\u00a7lWARNING:\u00a7r You will take damage while moving through blocks!");
		setCategory(Category.OTHER);
		addSetting(speed);
	}

	@Override
	protected void onEnable() {
		if(mc.player==null)
			return;
		mc.player.noClip=true;
		mc.player.onGround=true;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		mc.player.noClip=false;
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	/*
	 * @SubscribeEvent public void onUpdate(WUpdateEvent event) { EntityPlayerSP
	 * player =event.getPlayer(); player.noClip = true; player.fallDistance = 0;
	 * player.onGround = false;
	 * 
	 * player.capabilities.isFlying = false; player.setVelocity(0, 0, 0);
	 * 
	 * 
	 * player.capabilities.setFlySpeed(speed.getValueF());
	 * 
	 * if(mc.gameSettings.keyBindJump.isPressed())
	 * player.addVelocity(0,speed.getValue(),0);
	 * if(mc.gameSettings.keyBindSneak.isPressed()) player.addVelocity(0,
	 * -speed.getValue(), 0); }
	 */
}
