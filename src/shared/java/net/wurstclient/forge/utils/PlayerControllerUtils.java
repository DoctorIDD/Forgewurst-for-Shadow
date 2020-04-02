/*
 * Copyright � 2018 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.utils;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.compatibility.WMinecraft;

public final class PlayerControllerUtils
{
	private static final ForgeWurst wurst = ForgeWurst.getForgeWurst();
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static ItemStack windowClick_PICKUP(int slot)
	{
		return mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP,
			WMinecraft.getPlayer());
	}
	
	public static ItemStack windowClick_QUICK_MOVE(int slot)
	{
		return mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE,
			WMinecraft.getPlayer());
	}
	
	public static ItemStack windowClick_THROW(int slot)
	{
		return mc.playerController.windowClick(0, slot, 1, ClickType.THROW,
			WMinecraft.getPlayer());
	}
	
	public static float getCurBlockDamageMP()
		throws ReflectiveOperationException
	{
		Field field = PlayerControllerMP.class.getDeclaredField(
			wurst.isObfuscated() ? "field_78770_f" : "curBlockDamageMP");
		field.setAccessible(true);
		return field.getFloat(mc.playerController);
	}
	
	public static void setBlockHitDelay(int blockHitDelay)
		throws ReflectiveOperationException
	{
		Field field = PlayerControllerMP.class.getDeclaredField(
			wurst.isObfuscated() ? "field_78781_i" : "blockHitDelay");
		field.setAccessible(true);
		field.setInt(mc.playerController, blockHitDelay);
	}
	
	public static void setIsHittingBlock(boolean isHittingBlock)
		throws ReflectiveOperationException
	{
		Field field = PlayerControllerMP.class.getDeclaredField(
			wurst.isObfuscated() ? "field_78778_j" : "isHittingBlock");
		field.setAccessible(true);
		field.setBoolean(mc.playerController, isHittingBlock);
	}
	public static float getDirection() {
    	float var1 = Wrapper.getPlayer().rotationYaw;
        if (Wrapper.getPlayer().moveForward < 0.0F) {
        	var1 += 180.0F;
        }
        float forward = 1.0F;
        if (Wrapper.getPlayer().moveForward < 0.0F) {
            forward = -0.5F;
        } else if (Wrapper.getPlayer().moveForward > 0.0F) {
            forward = 0.5F;
        }
        if (Wrapper.getPlayer().moveStrafing > 0.0F) {
        	var1 -= 90.0F * forward;
        }
        if (Wrapper.getPlayer().moveStrafing < 0.0F) {
        	var1 += 90.0F * forward;
        }
        var1 *= 0.017453292F;
        return var1;
    }
	public static void assistFaceEntity(Entity entity, float yaw, float pitch) {
	    if (entity == null) {
	    	return;
	    }
	    
	    double diffX = entity.posX - Wrapper.getPlayer().posX;
	    double diffZ = entity.posZ - Wrapper.getPlayer().posZ;
	    double yDifference;
	    
	    if (entity instanceof EntityLivingBase)
	    {
	      EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
	      yDifference = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (
	    		  Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
	    }
	    else
	    {
	      yDifference = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D - (
	    		  Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
	    }
	    
	    double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
	    float rotationYaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
	    float rotationPitch = (float)-(Math.atan2(yDifference, dist) * 180.0D / Math.PI);
	    
	    if(yaw > 0) {
	    	Wrapper.getPlayer().rotationYaw = updateRotation(Wrapper.getPlayer().rotationYaw, rotationYaw, yaw / 4);
	    }
	    if(pitch > 0) {
	    	Wrapper.getPlayer().rotationPitch = updateRotation(Wrapper.getPlayer().rotationPitch, rotationPitch, pitch / 4);
	    }
	}

	public static float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
	    float var4 = MathHelper.wrapDegrees(p_70663_2_ - p_70663_1_);
	    if (var4 > p_70663_3_) {
	      var4 = p_70663_3_;
	    }
	    if (var4 < -p_70663_3_) {
	      var4 = -p_70663_3_;
	    }
	    return p_70663_1_ + var4;
	}

	public static void setReach(Entity entity, double range) {
		class RangePlayerController extends PlayerControllerMP {
			private float range = (float) Wrapper.getPlayer().getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
			public RangePlayerController(Minecraft mcIn, NetHandlerPlayClient netHandler) {
				super(mcIn, netHandler);
			}
			@Override
			public float getBlockReachDistance() {
				return range;
			}
			public void setBlockReachDistance(float range) {
				this.range = range;
			}
		}
	}
	 public static double[] teleportToPosition(double[] startPosition, double[] endPosition, double setOffset, double slack, boolean extendOffset, boolean onGround) {
	        boolean wasSneaking = false;

	        if (Wrapper.getPlayer().isSneaking())
	            wasSneaking = true;

	        double startX = startPosition[0];
	        double startY = startPosition[1];
	        double startZ = startPosition[2];

	        double endX = endPosition[0];
	        double endY = endPosition[1];
	        double endZ = endPosition[2];

	        double distance = Math.abs(startX - startY) + Math.abs(startY - endY) + Math.abs(startZ - endZ);

	        int count = 0;
	        while (distance > slack) {
	            distance = Math.abs(startX - endX) + Math.abs(startY - endY) + Math.abs(startZ - endZ);

	            if (count > 120) {
	                break;
	            }

	            double offset = extendOffset && (count & 0x1) == 0 ? setOffset + 0.15D : setOffset;

	            double diffX = startX - endX;
	            double diffY = startY - endY;
	            double diffZ = startZ - endZ;

	            if (diffX < 0.0D) {
	                if (Math.abs(diffX) > offset) {
	                    startX += offset;
	                } else {
	                    startX += Math.abs(diffX);
	                }
	            }
	            if (diffX > 0.0D) {
	                if (Math.abs(diffX) > offset) {
	                    startX -= offset;
	                } else {
	                    startX -= Math.abs(diffX);
	                }
	            }
	            if (diffY < 0.0D) {
	                if (Math.abs(diffY) > offset) {
	                    startY += offset;
	                } else {
	                    startY += Math.abs(diffY);
	                }
	            }
	            if (diffY > 0.0D) {
	                if (Math.abs(diffY) > offset) {
	                    startY -= offset;
	                } else {
	                    startY -= Math.abs(diffY);
	                }
	            }
	            if (diffZ < 0.0D) {
	                if (Math.abs(diffZ) > offset) {
	                    startZ += offset;
	                } else {
	                    startZ += Math.abs(diffZ);
	                }
	            }
	            if (diffZ > 0.0D) {
	                if (Math.abs(diffZ) > offset) {
	                    startZ -= offset;
	                } else {
	                    startZ -= Math.abs(diffZ);
	                }
	            }

	            if (wasSneaking) {
	            	Wrapper.getMinecraft().getConnection().sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), CPacketEntityAction.Action.STOP_SNEAKING));
	            }
	            Wrapper.getMinecraft().getConnection().getNetworkManager().sendPacket(new CPacketPlayer.Position(startX, startY, startZ, onGround));
	            count++;
	        }

	        if (wasSneaking) {
	        	Wrapper.getMinecraft().getConnection().sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), CPacketEntityAction.Action.START_SNEAKING));
	        }

	        return new double[]{startX, startY, startZ};
	    }
	 public static boolean isBlockMaterial(BlockPos blockPos, Block block) {
	    	return Wrapper.getMinecraft().world.getBlockState(blockPos).getBlock() == Blocks.AIR;
	    }
	    public static boolean isBlockMaterial(BlockPos blockPos, Material material) {
	    	return Wrapper.getMinecraft().world.getBlockState(blockPos).getMaterial() == material;
	    }
	    public static boolean isMoving() {
	        if ((!mc.player.collidedHorizontally) && (!mc.player.isSneaking())) {
	            return ((mc.player.movementInput.moveForward != 0.0F || mc.player.movementInput.moveStrafe != 0.0F));
	        }
	        return false;
	    }
	    public static boolean isMoving2() {
	    	 return ((mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F));
	    }
}
