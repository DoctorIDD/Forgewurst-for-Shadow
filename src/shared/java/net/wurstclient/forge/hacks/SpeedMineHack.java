package net.wurstclient.forge.hacks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;

public class SpeedMineHack extends Hack{
	private double speed;
    private double lastDist;
    public static int stage;
    private final CheckboxSetting step = new CheckboxSetting("STEP", "Disables speed while stepping up multiple stairs/slabs.", false);
    int steps;
	public SpeedMineHack() {
		super("SpeedMine","");
		setCategory(Category.COMBAT);
	}
	@Override
	protected void onEnable() {
		if (mc.player != null) {
            this.speed = this.defaultSpeed();
        }
        this.lastDist = 0.0;
        stage = 2;
		MinecraftForge.EVENT_BUS.register(this);
	}
	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	@SubscribeEvent 
	public void onUpdate(WUpdateEvent event) {
		
	}
	private double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }
}
