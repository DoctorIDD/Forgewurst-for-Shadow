package net.wurstclient.forge.hacks;

import java.lang.reflect.Field;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.commands.UnlockCmd;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.EnumSetting;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.ReflectionUtils;
import net.wurstclient.forge.utils.system.Frame;

public class PVPHack extends Hack {
	public final EnumSetting<VelocityMode> velocitymode =new EnumSetting<PVPHack.VelocityMode>("VelocityMode", VelocityMode.values(), VelocityMode.PACKET);
	private final CheckboxSetting precison_hit = new CheckboxSetting("PrecisonHit", true);
	private final CheckboxSetting max_velocity =new CheckboxSetting("MaxVelocity", true);
	long lastLog = System.currentTimeMillis();
	
	public boolean max_velocity_ ;

	public PVPHack() {
		super("PVP", "Give you more superb fighting skills");
		setCategory(Category.COMBAT);
		addSetting(max_velocity);
		addSetting(precison_hit);
		addSetting(velocitymode);
	}

	@Override
	protected void onEnable() {
		
		if (UnlockCmd.LOCK != true) {
			if(mc.player!=null) {
			ChatUtils.error("You do not have permission to use this feature");
			this.setEnabled(false);
			return;
			}
		}
	
		
		MinecraftForge.EVENT_BUS.register(this);
	
	}
	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {

		if (UnlockCmd.LOCK != true) 
			return;
		
		if(max_velocity.isChecked()) {
			max_velocity_=true;
		}else {
			max_velocity_=false;
		}

	}
	public enum VelocityMode{
		REFLECTION,PACKET
	}

}
