package net.wurstclient.forge.utils;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

public class ObstacleBypassUtil {

	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public boolean isObstacle() {
		boolean bostacle = false;
		if (mc.objectMouseOver.typeOfHit.BLOCK != null) {
			bostacle = true;
		}

		return bostacle;

	}

	public void up() {
		 try{
	            int upblock=0;
	            while(true){
	                if(Minecraft.getMinecraft().world==null){
	                    return;
	                }
	                EntityPlayerSP p=Minecraft.getMinecraft().player;
	                double X=Math.floor(p.posX);
	                double Y=Math.floor(p.posY);
	                double Z=Math.floor(p.posZ);
	                upblock=upblock+1;
	                if(upblock!=1){
	                	BlockPos pos=new BlockPos(X, Y+upblock, Z);
	                	BlockPos pos1=new BlockPos(X, Y+upblock+1, Z);
	                	BlockPos pos2=new BlockPos(X, Y+upblock-1, Z);
	                    if(Minecraft.getMinecraft().world.getBlockState(pos) instanceof BlockAir &&Minecraft.getMinecraft().world.getBlockState(pos1) instanceof BlockAir){
	                        if(Minecraft.getMinecraft().world.getBlockState(pos2) instanceof BlockAir||Minecraft.getMinecraft().world.getBlockState(pos2) instanceof BlockLiquid){
	                            if(upblock>=256){
	                                return;
	                            }
	                        }else{
	                            p.setPosition(p.posX,p.posY+upblock+1,p.posZ);
	                            return;
	                        }
	                    }else{
	                        if(upblock>=256){
	                            return;
	                        }
	                    }
	                }
	            }
	        }catch (Exception e){e.printStackTrace();}
	    }
	
}
