package net.wurstclient.forge.hacks;

import java.util.Random;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.EnumSetting;

public class MerryGoRoundHack extends Hack {
	public static float tempYaw;
	   public static float tempPitch;
	   public static Random r = new Random();
	   private int count;
	   int a = 0;
	   int b = 0;
	   int c = 0;
	private final EnumSetting<MODE> mode = new EnumSetting<MODE>("Mode", MODE.values(), MODE.MODE1);

	public MerryGoRoundHack() {
		super("MerryGoRound", "Comically spinning and jumping");
		setCategory(Category.FUN);
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
	public void onUpdate(WUpdateEvent event) {
		switch (mode.getSelected()) {
		case MODE1:
			EntityPlayerSP player=event.getPlayer();
			float f1 = (new Random()).nextFloat() * 360.0F;
		      float f2 = (new Random()).nextFloat() * 360.0F;
		      float f3 = (new Random()).nextFloat() * 360.0F;
		      int i1 = (new Random()).nextInt(7);
		      int i2 = (new Random()).nextInt(1) + 1;
		      player.connection.sendPacket(new CPacketEntityAction(player, Action.START_SNEAKING));
		     
			break;
		case MODE2:
			break;
		case MODE3:
			break;
		}
	}

	public enum MODE {
		MODE1, MODE2, MODE3
	}
}
