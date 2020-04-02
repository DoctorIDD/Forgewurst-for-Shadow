package net.wurstclient.forge.hacks;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.EnumSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;
import net.wurstclient.forge.utils.PlayerControllerUtils;
import net.wurstclient.forge.utils.Wrapper;

public class NewSpeedHack extends Hack {
	private final EnumSetting<Mode> mode = new EnumSetting<NewSpeedHack.Mode>("Mode", Mode.values(), Mode.M1);
	private final SliderSetting SPEED = new SliderSetting("Speed", 0.2f, 0.2f, 2f, 0.01f, ValueDisplay.DECIMAL);

	public NewSpeedHack() {
		super("NewSpeed", "");
		setCategory(Category.COMBAT);
		addSetting(SPEED);
		addSetting(mode);
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
		if (mode.getSelected() == Mode.NORMAL) {
			boolean boost = Math.abs(Wrapper.getPlayer().rotationYawHead - Wrapper.getPlayer().rotationYaw) < 90;
			if (Wrapper.getPlayer().moveForward > 0 && Wrapper.getPlayer().hurtTime < 5) {
				if (Wrapper.getPlayer().onGround) {
					Wrapper.getPlayer().jump();
					Wrapper.getPlayer().motionY = 0.405;
					float f = PlayerControllerUtils.getDirection();
					Wrapper.getPlayer().motionX -= (double) (MathHelper.sin(f) * SPEED.getValueF());
					Wrapper.getPlayer().motionZ += (double) (MathHelper.cos(f) * SPEED.getValueF());
				} else {
					double currentSpeed = Math.sqrt(Wrapper.getPlayer().motionX * Wrapper.getPlayer().motionX
							+ Wrapper.getPlayer().motionZ * Wrapper.getPlayer().motionZ);
					double speed = boost ? 1.0064 : 1.001;

					double direction = PlayerControllerUtils.getDirection();

					Wrapper.getPlayer().motionX = -Math.sin(direction) * speed * currentSpeed;
					Wrapper.getPlayer().motionZ = Math.cos(direction) * speed * currentSpeed;
				}
			} else if (mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()
					|| mc.gameSettings.keyBindRight.isKeyDown()) {
				if (Wrapper.getPlayer().onGround) {
					Wrapper.getPlayer().jump();
					Wrapper.getPlayer().motionY = 0.405;
					float f = PlayerControllerUtils.getDirection();
					Wrapper.getPlayer().motionX -= (double) (MathHelper.sin(f) * SPEED.getValueF());
					Wrapper.getPlayer().motionZ += (double) (MathHelper.cos(f) * SPEED.getValueF());
				} else {
					double currentSpeed = Math.sqrt(Wrapper.getPlayer().motionX * Wrapper.getPlayer().motionX
							+ Wrapper.getPlayer().motionZ * Wrapper.getPlayer().motionZ);
					double speed = boost ? 1.0064 : 1.001;

					double direction = PlayerControllerUtils.getDirection();

					Wrapper.getPlayer().motionX = -Math.sin(direction) * speed * currentSpeed;
					Wrapper.getPlayer().motionZ = Math.cos(direction) * speed * currentSpeed;

				}
			}
		} else if (mode.getSelected() == Mode.M1) {
			// return if sneaking or not walking
			/*
			 * if(mc.player.isSneaking() || mc.player.moveForward == 0) return;
			 */
			if (Wrapper.getPlayer().moveForward > 0 && Wrapper.getPlayer().hurtTime < 5) {
				if (Wrapper.getPlayer().onGround) {
					mc.player.jump();
					// activate sprint if walking forward
					/*
					 * if(mc.player.moveForward > 0 && !mc.player.collidedHorizontally)
					 * mc.player.setSprinting(true);
					 */

					// activate mini jump if on ground

					double x = mc.player.motionX;
					double y = mc.player.motionY;
					double z = mc.player.motionZ;
					mc.player.jump();
					mc.player.setVelocity(x * 1.8, y, z * 1.8);

					double currentSpeed = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));

					// limit speed to highest value that works on NoCheat+ version
					// 3.13.0-BETA-sMD5NET-b878
					// UPDATE: Patched in NoCheat+ version 3.13.2-SNAPSHOT-sMD5NET-b888
					double maxSpeed = 0.66F;

					if (currentSpeed > maxSpeed)
						mc.player.setVelocity(x / currentSpeed * maxSpeed, y, z / currentSpeed * maxSpeed);
				}else if (mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()||mc.gameSettings.keyBindRight.isKeyDown()){
					if (Wrapper.getPlayer().onGround) {
					
					mc.player.jump();
					// activate sprint if walking forward
					/*
					 * if(mc.player.moveForward > 0 && !mc.player.collidedHorizontally)
					 * mc.player.setSprinting(true);
					 */

					// activate mini jump if on ground

					double x = mc.player.motionX;
					double y = mc.player.motionY;
					double z = mc.player.motionZ;
					mc.player.jump();
					mc.player.setVelocity(x * 1.8, y, z * 1.8);

					double currentSpeed = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));

					// limit speed to highest value that works on NoCheat+ version
					// 3.13.0-BETA-sMD5NET-b878
					// UPDATE: Patched in NoCheat+ version 3.13.2-SNAPSHOT-sMD5NET-b888
					double maxSpeed = 0.66F;

					if (currentSpeed > maxSpeed)
						mc.player.setVelocity(x / currentSpeed * maxSpeed, y, z / currentSpeed * maxSpeed);
					}
				}
			}
		}
	}

	private enum Mode {
		NORMAL, M1
	}

}
