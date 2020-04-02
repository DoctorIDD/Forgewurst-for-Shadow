package net.wurstclient.forge.hacks;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.KeybindList.Keybind;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

public class TwerkHack extends Hack{
	private final SliderSetting twerkSpeed = new SliderSetting("Twerk speed",
			"I came in like a wreeecking baaall...", 5, 1, 10, 1,
			ValueDisplay.INTEGER);
	private final SliderSetting tick =new SliderSetting("Tick", 10, 10, 40, 5, ValueDisplay.DECIMAL);
	private int timer;
	private int interval=0;
	public TwerkHack() {
		super("Twerk","be like a dancer");
		setCategory(Category.FUN);
		addSetting(twerkSpeed);
	}
	@Override
	protected void onEnable() {
		timer=0;
		MinecraftForge.EVENT_BUS.register(this);
	}
	@Override 
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		timer++;
		interval++;

		if(timer < 10 - twerkSpeed.getValueI())
			return;
		if(interval>=tick.getValueI()) {
			interval=0;
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
		}else {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
		}
		
		
		timer = -1;
	}
}
