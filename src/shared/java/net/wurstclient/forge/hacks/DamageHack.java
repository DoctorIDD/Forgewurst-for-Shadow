package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

public class DamageHack extends Hack {
	public DamageHack() {
		super("Damage", "");
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

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		if (mc.player.onGround) {
			double posX = mc.player.posX;
			double posY = mc.player.posY;
			double posZ = mc.player.posZ;

			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(posX, posY, posZ, mc.player.rotationYaw,
					mc.player.rotationPitch, false));
			for (int a = 1; a < 11; a++) {
				mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(posX,
						posY + ((a % 3 == 0) ? 0.41999998560698D : 0.41999998688698D), posZ, mc.player.rotationYaw,
						mc.player.rotationPitch, false));
			}
			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(posX, posY+0.3415999853611D, posZ, mc.player.rotationYaw,
					mc.player.rotationPitch, false));
			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(posX, posY+0.1863679808445D, posZ, mc.player.rotationYaw,
					mc.player.rotationPitch, false));
			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(posX, posY, posZ, mc.player.rotationYaw,
					mc.player.rotationPitch, true));
			
		}
	}

}
