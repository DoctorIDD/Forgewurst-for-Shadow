package net.wurstclient.forge.utils;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class PointerUtils {
	//37//50
	private static final Minecraft mc = Minecraft.getMinecraft();

	public static Entity raycastEntity(final double range, final IEntityFilter entityFilter) {
        return raycastEntity(range, RotationUtils.serverRotation.getYaw(mc.player.rotationYaw), RotationUtils.serverRotation.getPitch(mc.player.rotationPitch),
                entityFilter);
    }

    private static Entity raycastEntity(final double range, final float yaw, final float pitch, final IEntityFilter entityFilter) {
        final Entity renderViewEntity = mc.getRenderViewEntity();

        if(renderViewEntity != null && mc.world != null) {
            double blockReachDistance = range;
            final Vec3d eyePosition = renderViewEntity.getPositionEyes(1F);

            final float yawCos = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
            final float yawSin = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
            final float pitchCos = -MathHelper.cos(-pitch * 0.017453292F);
            final float pitchSin = MathHelper.sin(-pitch * 0.017453292F);

            final Vec3d entityLook = new Vec3d(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
            final Vec3d vector = eyePosition.addVector(entityLook.x * blockReachDistance, entityLook.y * blockReachDistance, entityLook.z * blockReachDistance);
            final List<Entity> entityList = mc.world.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().grow(entityLook.x * blockReachDistance, entityLook.y * blockReachDistance, entityLook.z * blockReachDistance).expand(1D, 1D, 1D), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity :: canBeCollidedWith));

            Entity pointedEntity = null;

            for(final Entity entity : entityList) {
                if(!entityFilter.canRaycast(entity))
                    continue;

                final float collisionBorderSize = entity.getCollisionBorderSize();
                final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
                final RayTraceResult movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);

                if(axisAlignedBB.contains(eyePosition)) {
                    if(blockReachDistance >= 0.0D) {
                        pointedEntity = entity;
                        blockReachDistance = 0.0D;
                    }
                }else if(movingObjectPosition != null) {
                    final double eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec);

                    if(eyeDistance < blockReachDistance || blockReachDistance == 0.0D) {
                        if(entity == renderViewEntity.getRidingEntity() && !renderViewEntity.canRiderInteract()) {
                            if(blockReachDistance == 0.0D)
                                pointedEntity = entity;
                        }else{
                            pointedEntity = entity;
                            blockReachDistance = eyeDistance;
                        }
                    }
                }
            }

            return pointedEntity;
        }

        return null;
    }

    public interface IEntityFilter {
        boolean canRaycast(final Entity entity);
    }
}
