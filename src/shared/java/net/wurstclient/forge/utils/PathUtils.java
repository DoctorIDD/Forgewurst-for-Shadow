package net.wurstclient.forge.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PathUtils {
	 private static final Minecraft mc = Minecraft.getMinecraft();

	public static List<Vec3d> findBlinkPath(final double tpX, final double tpY, final double tpZ) {
	        final List<Vec3d> positions = new ArrayList<>();

	        double curX = mc.player.posX;
	        double curY = mc.player.posY;
	        double curZ = mc.player.posZ;
	        double distance = Math.abs(curX - tpX) + Math.abs(curY - tpY) + Math.abs(curZ - tpZ);

	        for (int count = 0; distance > 0.0D; count++) {
	            distance = Math.abs(curX - tpX) + Math.abs(curY - tpY) + Math.abs(curZ - tpZ);

	            final double diffX = curX - tpX;
	            final double diffY = curY - tpY;
	            final double diffZ = curZ - tpZ;
	            final double offset = (count & 1) == 0 ? 0.4D : 0.1D;

	            final double minX = Math.min(Math.abs(diffX), offset);
	            if (diffX < 0.0D) curX += minX;
	            if (diffX > 0.0D) curX -= minX;

	            final double minY = Math.min(Math.abs(diffY), 0.25D);
	            if (diffY < 0.0D) curY += minY;
	            if (diffY > 0.0D) curY -= minY;

	            double minZ = Math.min(Math.abs(diffZ), offset);
	            if (diffZ < 0.0D) curZ += minZ;
	            if (diffZ > 0.0D) curZ -= minZ;

	            positions.add(new Vec3d(curX, curY, curZ));
	            
	        }

	        return positions;
	    }

	    public static List<Vec3d> findPath(final double tpX, final double tpY, final double tpZ, final double offset) {
	        final List<Vec3d> positions = new ArrayList<>();
	        final float yaw = (float) ((Math.atan2(tpZ - mc.player.posZ, tpX - mc.player.posX) * 180.0 / Math.PI) - 90F);
	        final double steps = getDistance(mc.player.posX, mc.player.posY, mc.player.posZ, tpX, tpY, tpZ) / offset;

	        double curY = mc.player.posY;

	        for(double d = offset; d < getDistance(mc.player.posX, mc.player.posY, mc.player.posZ, tpX, tpY, tpZ); d += offset) {
	            curY -= (mc.player.posY - tpY) / steps;
	            positions.add(new Vec3d(mc.player.posX - (Math.sin(Math.toRadians(yaw)) * d), curY, mc.player.posZ + Math.cos(Math.toRadians(yaw)) * d));
	        }

	        positions.add(new Vec3d(tpX, tpY, tpZ));

	        return positions;
	    }

	    private static double getDistance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
	        final double xDiff = x1 - x2;
	        final double yDiff = y1 - y2;
	        final double zDiff = z1 - z2;
	        return MathHelper.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
	    }
}
