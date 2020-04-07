package net.wurstclient.forge.hacks;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.EnumSetting;
import net.wurstclient.forge.utils.EntityUtils;

public class TeamsHack extends Hack{
	public final EnumSetting<Mode> mode=new EnumSetting<TeamsHack.Mode>("Mode", Mode.values(), Mode.Base);
	public TeamsHack() {
		super("Teams","Don't attack your teammates");
		setCategory(Category.COMBAT);
		addSetting(mode);
	}
	public static enum Mode{
		ArmorColor,Base
	}
	public static boolean isTeam(EntityLivingBase entity) {
	
			
			if(entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				if(wurst.getHax().teamsHack.mode.getSelected()==Mode.Base) {
					if(player.getTeam() != null && mc.player.getTeam() != null) {
						if(player.getTeam().isSameTeam(mc.player.getTeam())){
							return false;
						}
					}
				}
				if(wurst.getHax().teamsHack.mode.getSelected()==Mode.ArmorColor) {
					if(!EntityUtils.checkTargetColor(player)) {
						return false;
					}
				}
			}
		
		return true;
	}
}
