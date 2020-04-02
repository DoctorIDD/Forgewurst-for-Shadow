package net.wurstclient.forge.hacks;

import java.lang.reflect.Field;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;
import net.wurstclient.forge.utils.EntityUtil;
import net.wurstclient.forge.utils.PlayerControllerUtils;
import net.wurstclient.forge.utils.TimerUtil;

public class SpeedHack extends Hack{
	private final SliderSetting speed =new SliderSetting("speed", 2, 1, 20, 0.1, ValueDisplay.DECIMAL);
	public SpeedHack() {
		super("Speed","");
		setCategory(Category.COMBAT);
		addSetting(speed);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		setTickLength(50);
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		EntityPlayerSP player =event.getPlayer();
		if(player.onGround&&!player.isInWater()) {
			setTickLength(50);
			mc.player.jump();
		}else if(!player.isInWater()) {
			setTickLength(50 / speed.getValueF());
		}
	}
	private void setTickLength(float tickLength)
	{
		try
		{
			Field fTimer = mc.getClass().getDeclaredField(
				wurst.isObfuscated() ? "field_71428_T" : "timer");
			fTimer.setAccessible(true);
			
			if(WMinecraft.VERSION.equals("1.10.2"))
			{
				Field fTimerSpeed = Timer.class.getDeclaredField(
					wurst.isObfuscated() ? "field_74278_d" : "timerSpeed");
				fTimerSpeed.setAccessible(true);
				fTimerSpeed.setFloat(fTimer.get(mc), 50 / tickLength);
				
			}else
			{
				Field fTickLength = Timer.class.getDeclaredField(
					wurst.isObfuscated() ? "field_194149_e" : "tickLength");
				fTickLength.setAccessible(true);
				fTickLength.setFloat(fTimer.get(mc), tickLength);
			}
			
		}catch(ReflectiveOperationException e)
		{
			throw new RuntimeException(e);
		}
	}
}
