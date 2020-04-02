package net.wurstclient.forge.hacks;

import org.lwjgl.input.Mouse;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketOutputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.hacks.SuperFlyHack.FlightMode;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.EnumSetting;
import net.wurstclient.forge.utils.PlayerControllerUtils;
import net.wurstclient.forge.utils.Wrapper;

public class TeleportHack extends Hack {
	private final EnumSetting<MODE> mode = new EnumSetting<MODE>("Mode", MODE.values(), MODE.REACH);
	private final CheckboxSetting math =new CheckboxSetting("math", false);
	
	public boolean passPacket = false;
	private BlockPos teleportPosition = null;
	private boolean canDraw;
	private int delay;
	float reach = 0;

	public TeleportHack() {
		super("Teleport", "");
		setCategory(Category.COMBAT);
		addSetting(math);
		addSetting(mode);
	}

	public enum MODE {
		REACH, FLIGHT
	}
	public boolean isValidBlock(Block block) {
        return block == Blocks.PORTAL 
        		|| block == Blocks.SNOW_LAYER 
        		|| block instanceof BlockTripWireHook 
        		|| block instanceof BlockTripWire 
        		|| block instanceof BlockDaylightDetector 
        		|| block instanceof BlockRedstoneComparator 
        		|| block instanceof BlockRedstoneRepeater 
        		|| block instanceof BlockSign 
        		|| block instanceof BlockAir 
        		|| block instanceof BlockPressurePlate 
        		|| block instanceof BlockTallGrass 
        		|| block instanceof BlockFlower 
        		|| block instanceof BlockMushroom 
        		|| block instanceof BlockDoublePlant 
        		|| block instanceof BlockReed 
        		|| block instanceof BlockSapling 
        		|| block == Blocks.CARROTS 
        		|| block == Blocks.WHEAT
        		|| block == Blocks.NETHER_WART
        		|| block == Blocks.POTATOES
        		|| block == Blocks.PUMPKIN_STEM
        		|| block == Blocks.MELON_STEM
        		|| block == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE
        		|| block == Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE
        		|| block == Blocks.REDSTONE_WIRE
        		|| block instanceof BlockTorch 
        		|| block instanceof BlockRedstoneTorch 
        		|| block == Blocks.LEVER 
        		|| block instanceof BlockButton;
    }
	@Override
	protected void onEnable() {
		if(mode.getSelected()==MODE.REACH) {
			EntityPlayerSP player=mc.player;
			if(player!=null)
			reach = (float) Wrapper.getPlayer().getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		}
		MinecraftForge.EVENT_BUS.register(this);
	}
	@Override
	protected void onDisable() {
		if(mode.getSelected()==MODE.FLIGHT) {
			Wrapper.getPlayer().noClip = false;
			passPacket = false;
			teleportPosition = null;
			return;
		}
		PlayerControllerUtils.setReach(Wrapper.getPlayer(), reach);
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
		if(mode.getSelected()==MODE.FLIGHT) {
			RayTraceResult object = Wrapper.getMinecraft().objectMouseOver;
			if(object == null) {
				return;
			}
			EntityPlayerSP player = Wrapper.getPlayer();
			GameSettings settings = Wrapper.getMinecraft().gameSettings;
			if(!passPacket) {
		 		if(settings.keyBindAttack.isKeyDown() && object.typeOfHit == RayTraceResult.Type.BLOCK) {
		 			if(PlayerControllerUtils.isBlockMaterial(object.getBlockPos(), Blocks.AIR)) {
		 				return;
		 			}
		 			teleportPosition = object.getBlockPos();
					passPacket = true;
				}
				return;
			}
			player.noClip = false;
			if(settings.keyBindSneak.isKeyDown() && player.onGround) {
				if(math.isChecked()) {
		            double[] playerPosition = new double[]{Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ};
		            double[] blockPosition = new double[]{teleportPosition.getX() + 0.5F, teleportPosition.getY() + getOffset(Wrapper.getMinecraft().world.getBlockState(teleportPosition).getBlock(), teleportPosition) + 1.0F, teleportPosition.getZ() + 0.5F};

		            PlayerControllerUtils.teleportToPosition(playerPosition, blockPosition, 0.25D, 0.0D, true, true);
		            Wrapper.getPlayer().setPosition(blockPosition[0], blockPosition[1], blockPosition[2]);

		            teleportPosition = null;
				} else {
		            double x = teleportPosition.getX();
		            double y = teleportPosition.getY() + 1;
		            double z = teleportPosition.getZ();
		            
		            Wrapper.getPlayer().setPosition(x, y, z);
		            for(int i = 0; i < 1; i++) {
		            	Wrapper.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, Wrapper.getPlayer().onGround));
		            }
				}
	            
			}
			return;
		}
		 if ((!Mouse.isButtonDown(0) && Wrapper.getMinecraft().inGameHasFocus || !Wrapper.getMinecraft().inGameHasFocus) && Wrapper.getPlayer().getItemInUseCount() == 0) {
			 PlayerControllerUtils.setReach(Wrapper.getPlayer(), 100.0);
             canDraw = true;
         } else {
             canDraw = false;
             PlayerControllerUtils.setReach(Wrapper.getPlayer(), reach);
         }
		if (teleportPosition != null && delay == 0 && Mouse.isButtonDown(1)) {
			if(math.isChecked()) {
	            double[] playerPosition = new double[]{Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ};
	            double[] blockPosition = new double[]{teleportPosition.getX() + 0.5F, teleportPosition.getY() + getOffset(Wrapper.getMinecraft().world.getBlockState(teleportPosition).getBlock(), teleportPosition) + 1.0F, teleportPosition.getZ() + 0.5F};

	            PlayerControllerUtils.teleportToPosition(playerPosition, blockPosition, 0.25D, 0.0D, true, true);
	            Wrapper.getPlayer().setPosition(blockPosition[0], blockPosition[1], blockPosition[2]);

	            teleportPosition = null;
			} else {
	            double x = teleportPosition.getX();
	            double y = teleportPosition.getY() + 1;
	            double z = teleportPosition.getZ();
	            
	            Wrapper.getPlayer().setPosition(x, y, z);
	            for(int i = 0; i < 1; i++) {
	            	Wrapper.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, Wrapper.getPlayer().onGround));
	            }
			}
            delay = 5;
        }

        if (delay > 0) {
            delay--;
        }
		
	}
	@SubscribeEvent
	public boolean Out(WPacketOutputEvent event) {
		Object packet=event.getPacket();
		if(mode.getSelected()==MODE.FLIGHT) {
			if(packet instanceof CPacketPlayer
                || packet instanceof CPacketPlayer.Position
                || packet instanceof CPacketPlayer.Rotation
                || packet instanceof CPacketPlayer.PositionRotation) {
				return passPacket;
			}
		}
		return true;
	}
	public double getOffset(Block block, BlockPos pos) {
        IBlockState state = Wrapper.getMinecraft().world.getBlockState(pos);

        double offset = 0;

        if (block instanceof BlockSlab && !((BlockSlab) block).isDouble()) {
            offset -= 0.5F;
        } else if (block instanceof BlockEndPortalFrame) {
            offset -= 0.2F;
        } else if (block instanceof BlockBed) {
            offset -= 0.44F;
        } else if (block instanceof BlockCake) {
            offset -= 0.5F;
        } else if (block instanceof BlockDaylightDetector) {
            offset -= 0.625F;
        } else if (block instanceof BlockRedstoneComparator || block instanceof BlockRedstoneRepeater) {
            offset -= 0.875F;
        } else if (block instanceof BlockChest || block == Blocks.ENDER_CHEST) {
            offset -= 0.125F;
        } else if (block instanceof BlockLilyPad) {
            offset -= 0.95F;
        } else if (block == Blocks.SNOW_LAYER) {
            offset -= 0.875F;
            offset += 0.125F * ((Integer) state.getValue(BlockSnow.LAYERS) - 1);
        } else if (isValidBlock(block)) {
            offset -= 1.0F;
        }

        return offset;
    }
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if(mode.getSelected()!=MODE.FLIGHT) {
			return;
		}
		EntityPlayerSP player = Wrapper.getPlayer();
		GameSettings settings = Wrapper.getMinecraft().getMinecraft().gameSettings;
		if(!passPacket) {
			player.noClip = true;
			player.fallDistance = 0;
			player.onGround = true;
			player.capabilities.isFlying = false;
			player.motionX = 0.0F;
			player.motionY = 0.0F;
			player.motionZ = 0.0F;
			float speed = 0.5f;
			if(settings.keyBindJump.isKeyDown()) {
				player.motionY += speed;
			}
			if(settings.keyBindSneak.isKeyDown()) {
				player.motionY -= speed;
			}
			double d5 = player.rotationPitch + 90F;
	 		double d7 = player.rotationYaw + 90F;
	 		boolean flag4 = settings.keyBindForward.isKeyDown();
	 		boolean flag6 = settings.keyBindBack.isKeyDown();
	 		boolean flag8 = settings.keyBindLeft.isKeyDown();
	 		boolean flag10 = settings.keyBindRight.isKeyDown();
	 		if (flag4) {
	 			if (flag8) {
	 				d7 -= 45D;
	 			} else if (flag10) {
	 				d7 += 45D;
	 			}
	 		} else if (flag6) {
	 			d7 += 180D;
	 			if (flag8) {
	 				d7 += 45D;
	 			} else if (flag10) {
	 				d7 -= 45D;
	 			}
	 		} else if (flag8) {
	 			d7 -= 90D;
	 		} else if (flag10) {
	 			d7 += 90D;
	 		}
	 		if (flag4 || flag8 || flag6 || flag10) {
	 			player.motionX = Math.cos(Math.toRadians(d7));
	 			player.motionZ = Math.sin(Math.toRadians(d7));
	 		}
		}
	}
}
