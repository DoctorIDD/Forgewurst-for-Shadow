package net.wurstclient.forge.hacks;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.Timer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.EnumSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;
import net.wurstclient.forge.utils.EntityUtil;
import net.wurstclient.forge.utils.MoveUtils;
import net.wurstclient.forge.utils.Wrapper;

public class SuperFlyHack extends Hack {
	private SliderSetting timerspeed=new SliderSetting("timerspeed", 10, 2, 20, 1, ValueDisplay.DECIMAL);
	private EnumSetting<SpeedWay>speedway=new EnumSetting<SuperFlyHack.SpeedWay>("SpeedWay", SpeedWay.values(), SpeedWay.OldFast);
	private final SliderSetting speed = new SliderSetting("speed", 2.0, 0.25, 5, 0.25, ValueDisplay.DECIMAL);
	private final EnumSetting<FlightMode> mode = new EnumSetting<SuperFlyHack.FlightMode>("Mode", FlightMode.values(),
			FlightMode.PACKET);
	int ticks = 0;
	
    private double flyHeight;
    private double startY;
    double count;
 
    public static double hypixel = 0;
    private float flytimer = 0;
    public static int fastFlew;
    int stage;
    boolean shouldSpeed;

	public SuperFlyHack() {
		super("SuperFly", "Anti Checking,fly more secure");
		setCategory(Category.MOVEMENT);
		addSetting(mode);
		addSetting(speed);
		addSetting(speedway);
		addSetting(timerspeed);
	}

	@Override
	protected void onEnable() {
		 count = 0;
		if (mc.player == null)
			return;
		if(mode.getSelected()==FlightMode.SPEED &&
        		speedway.getSelected()==SpeedWay.Fast2){
        	fastFlew = 0;
        }else{
        	fastFlew = 100;
        }
		if(mode.getSelected()==FlightMode.TIMERSPEED) {
			setTickLength(50/timerspeed.getValueF());
		}
		ticks = 0;
		MinecraftForge.EVENT_BUS.register(this);

	}

	@Override
	protected void onDisable() {
		Wrapper.setDefaultTimer();
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		 double speed1 = Math.max(speed.getValue(), getBaseMoveSpeed());
		 double X = mc.player.posX; double Y = mc.player.posY; double Z = mc.player.posZ;
		switch (mode.getSelected()) {
		case TIMERSPEED:
			EntityPlayerSP player = Wrapper.getPlayer();
			player.motionY = 0.0;
			player.setSprinting(true);
			player.onGround = true;
			ticks++;
			if (ticks == 2 || ticks == 4 || ticks == 6 || ticks == 8 || ticks == 10 || ticks == 12 || ticks == 14
					|| ticks == 16 || ticks == 18 || ticks == 20) {
				player.setPosition(player.posX, player.posY + 0.00000000128, player.posZ);
			}
			if (ticks == 20) {
				ticks = 0;
			}
			break;
		case CUBE2:
			if(!MoveUtils.isOnGround(0.001)){
        		stage ++;
				/* mc.timer.timerSpeed = 0.27f; */
        		Wrapper.setTickLength(0.27f);
                
                mc.player.jumpMovementFactor = 0;
                mc.player.onGround = false;
            	double motion = mc.player.motionY;
            	speed1 = 0;
            	event.setOnGround(false);
            	if(stage == 1){
            		boolean a = mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, 0.4d, 0.0D)).isEmpty();
            	
            		event.setY(Y + (a?0.4:0.2));
            		motion = 0.4;
            	}else if(stage == 2){
            		motion = 0.28;       
            		boolean a = mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, 0.68d, 0.0D)).isEmpty();
            		event.setY(Y + (a?0.68:0.2));
              		if(shouldSpeed)
            			speed1 = 2.4;
            		else{
            			shouldSpeed = true;
            			speed1 = 2.2;
            		}
            	}else if(stage == 3){
            		motion = -0.68;
            	}else if(stage == 4){
            		motion = 0;
            		speed1 = 2.4;
            		stage = 0;
            	}
            	
            	MoveUtils.setMotion(speed1);
            	mc.player.motionY = 0;
        	}else if(shouldSpeed){
        		MoveUtils.setMotion(0);
        	
        		shouldSpeed = !shouldSpeed;
			} /*
				 * else if(Wrapper.get; != 1){ // mc.timer.timerSpeed != 1 mc.timer.timerSpeed =
				 * 1f; }
				 */
			break;
		case SPEED:
			fastFlew ++;
			 if (mc.player.onGround && mc.player.collidedVertically && MoveUtils.isOnGround(0.01)) {
                 
             	if(mc.player.hurtResistantTime == 19){
             		MoveUtils.setMotion(0.3 + MoveUtils.getSpeedEffect() * 0.05f);
             		mc.player.motionY = 0.41999998688698f + MoveUtils.getJumpEffect()*0.1;
             		fastFlew = 25;
					/* hypixel = 13+((Number) settings.get(HYPIXELF).getValue()).doubleValue(); */
             	}else if(fastFlew < 25){
             		mc.player.motionX = 0;
                     mc.player.motionZ = 0;
                     mc.player.jumpMovementFactor = 0;
                     mc.player.onGround = false;
             	}
             	
             }
			 Block block = MoveUtils.getBlockUnderPlayer(mc.player, 0.2);
             if (!MoveUtils.isOnGround(0.0000001) && !(block instanceof BlockGlass)) {
                 mc.player.motionY = 0;
                 mc.player.motionX = 0;
                 mc.player.motionZ = 0;
                 float speedf = 0.29f + MoveUtils.getSpeedEffect() * 0.06f;
                 if (hypixel > 0 ) {
                     if ((mc.player.moveForward == 0 && mc.player.moveStrafing == 0) || mc.player.collidedHorizontally)
                         hypixel = 0;                        
                     speedf += hypixel / 18;
                     if(speedway.getSelected()==SpeedWay.OldFast){
                     	hypixel-= 1.3;
                     }else if(speedway.getSelected()==SpeedWay.Fast3){
                     	hypixel-= 0.175 + MoveUtils.getSpeedEffect()*0.006; //0.152
                     }else{
                     	hypixel-= 0.155 + MoveUtils.getSpeedEffect()*0.006; //0.152
                     }
                     
                 }
                 setSpecialMotion(speedf);
                 mc.player.jumpMovementFactor = 0;
                 mc.player.onGround = false;
                 if (mc.gameSettings.keyBindJump.isPressed()) {
                     mc.player.motionY = 0.4;
                 }
                 count++;
				/* mc.player.lastTickPosY=0; */
                 if (count <= 2) {
                     mc.player.setPosition(mc.player.posX, mc.player.posY + 1.9999E-8, mc.player.posZ);
                 } else if (count == 4){
                 	mc.player.setPosition(mc.player.posX, mc.player.posY + 1E-8, mc.player.posZ);
                 }else if(count >= 5){
                 	mc.player.setPosition(mc.player.posX, mc.player.posY + 1.99999E-8, mc.player.posZ);
                 	count = 0;
                 }

             }

			break;
		case MOTION:
			mc.player.onGround= false;
			event.setOnGround(MoveUtils.isOnGround(0.001)||wurst.getHax().noFallHack.isEnabled());
			if (mc.player.movementInput.jump) {
                mc.player.motionY = speed.getValue() * 0.6;
            } else if (mc.player.movementInput.sneak) {
                mc.player.motionY = -speed.getValue() * 0.6;
            } else {
                mc.player.motionY = 0;
            }
            break;
			
		case FLASH:
			 final Vec3d vectorStart = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);
             final float yaw = -mc.player.rotationYaw;
             final float pitch = -mc.player.rotationPitch;
             final double length = 9.9;
             final Vec3d vectorEnd = new Vec3d(
                     Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * length + vectorStart.x,
                     Math.sin(Math.toRadians(pitch)) * length + vectorStart.y,
                     Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * length + vectorStart.z
             );
             mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(vectorEnd.x,  mc.player.posY + 2 ,vectorEnd.z,
      				mc.player.rotationYaw, mc.player.rotationPitch, true));
             mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(vectorStart.x, mc.player.posY + 2 ,vectorStart.z,
       				mc.player.rotationYaw, mc.player.rotationPitch, true));
             mc.player.motionY = 0;
			break;
		case BOOST:
			 double posX=mc.player.posX;
			double posY=mc.player.posY;
			double posZ=mc.player.posZ;
			for (int i = 0; i < 10; i++) //Imagine flagging to NCP.
               
                 mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(posX, posY , posZ,
         				mc.player.rotationYaw, mc.player.rotationPitch, true));
             double fallDistance = 3.0125; //add 0.0125 to ensure we get the fall dmg
             while (fallDistance > 0) {
            	 mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(posX, posY + 0.0624986421, posZ,
          				mc.player.rotationYaw, mc.player.rotationPitch, false));
            	 mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(posX, posY + 0.0625, posZ,
           				mc.player.rotationYaw, mc.player.rotationPitch, false));
            	 mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(posX, posY + 0.0624986421  , posZ,
            				mc.player.rotationYaw, mc.player.rotationPitch, false));
            	 mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(posX, posY + 0.0000013579 , posZ,
         				mc.player.rotationYaw, mc.player.rotationPitch, false));
                
                 fallDistance -= 0.0624986421;
             }
             mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(posX, posY  , posZ,
      				mc.player.rotationYaw, mc.player.rotationPitch, true));
             mc.player.jump();
             mc.player.posY += 0.42F; // Visual
             break;
		case HYPIXEL:
			EntityPlayerSP player1 = Wrapper.getPlayer();
			player1.motionY = 0.0;
			player1.setSprinting(true);
			player1.onGround = true;
			ticks++;
			if (ticks == 2 || ticks == 4 || ticks == 6 || ticks == 8 || ticks == 10 || ticks == 12 || ticks == 14
					|| ticks == 16 || ticks == 18 || ticks == 20) {
				player1.setPosition(player1.posX, player1.posY + 0.00000000128, player1.posZ);
			}
			if (ticks == 20) {
				ticks = 0;
			}
			break;
		case STATIC:
			mc.player.capabilities.isFlying = false;
			mc.player.motionX = 0;
			mc.player.motionY = 0;
			mc.player.motionZ = 0;
			mc.player.jumpMovementFactor = speed.getValueF();

			if (mc.gameSettings.keyBindJump.isKeyDown())
				mc.player.motionY += speed.getValue();
			if (mc.gameSettings.keyBindSneak.isKeyDown())
				mc.player.motionY -= speed.getValue();
			break;
		case VANILLA:
			mc.player.capabilities.setFlySpeed(speed.getValueF() / 100f);
			mc.player.capabilities.isFlying = true;
			if (mc.player.capabilities.isCreativeMode)
				return;
			mc.player.capabilities.allowFlying = true;
			break;
		case PACKET:
			int angle;

			boolean forward = mc.gameSettings.keyBindForward.isKeyDown();
			boolean left = mc.gameSettings.keyBindLeft.isKeyDown();
			boolean right = mc.gameSettings.keyBindRight.isKeyDown();
			boolean back = mc.gameSettings.keyBindBack.isKeyDown();

			if (left && right)
				angle = forward ? 0 : back ? 180 : -1;
			else if (forward && back)
				angle = left ? -90 : (right ? 90 : -1);
			else {
				angle = left ? -90 : (right ? 90 : 0);
				if (forward)
					angle /= 2;
				else if (back)
					angle = 180 - (angle / 2);
			}

			if (angle != -1 && (forward || left || right || back)) {
				float yaw1 = mc.player.rotationYaw + angle;
				mc.player.motionX = EntityUtil.getRelativeX(yaw1) * speed.getValueF() / 50;
				mc.player.motionZ = EntityUtil.getRelativeZ(yaw1) * speed.getValueF() / 50;
			}

			mc.player.motionY = 0;
			mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX,
					mc.player.posY + (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() ? 0.0622 : 0)
							- (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown() ? 0.0622 : 0),
					mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, false));
			mc.player.connection.sendPacket(
					new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY - 42069,
							mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
			break;
		}
	}

	private void setSpecialMotion(float speedf) {
		double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
        	mc.player.motionX = 0;
        	mc.player.motionZ = 0;
        } else {
            if (forward != 0.0D) {
            	if(hypixel <= 0)
            	 if (strafe > 0.0D) {
                     yaw += (forward > 0.0D ? -45 : 45);
                 } else if (strafe < 0.0D) {
                     yaw += (forward > 0.0D ? 45 : -45);
                 }
                 strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            mc.player.motionX = forward * speed.getValue() * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed.getValue() * Math.sin(Math.toRadians(yaw + 90.0F));
            mc.player.motionZ = forward * speed.getValue() * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed.getValue() * Math.cos(Math.toRadians(yaw + 90.0F));
        }
	}

	public double[] moveLooking() {
		return new double[] { mc.player.rotationYaw * 360.0F / 360.0F * 180.0F / 180.0F, 0.0D };
	}

	public enum FlightMode {
		VANILLA, STATIC, PACKET, HYPIXEL, BOOST,FLASH,MOTION,SPEED,CUBE2,TIMERSPEED
	}
	public enum SpeedWay{
		OldFast,Fast2,Fast3
	}
	public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873D;
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
        }
        return baseSpeed;
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
