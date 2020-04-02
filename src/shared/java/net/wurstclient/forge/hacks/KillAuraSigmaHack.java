package net.wurstclient.forge.hacks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.fmlevents.sigmaevent.EventSystem;
import net.wurstclient.fmlevents.sigmaevent.event.EventAttack;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.loader.ModFriendsLoader;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.EnumSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;
import net.wurstclient.forge.utils.MathUtils;
import net.wurstclient.forge.utils.MoveUtils;
import net.wurstclient.forge.utils.RotationUtils;
import net.wurstclient.forge.utils.STimer;

public class KillAuraSigmaHack extends Hack {
	private final EnumSetting<rotatinsMode> mode1 = new EnumSetting<KillAuraSigmaHack.rotatinsMode>("RotatinsMode",
			rotatinsMode.values(), rotatinsMode.Smooth);
	private final EnumSetting<Mode> mode = new EnumSetting<KillAuraSigmaHack.Mode>("Mode", Mode.values(), Mode.Switch);
	private final SliderSetting hitchange = new SliderSetting("HitChange", 90, 1, 100, 1, ValueDisplay.DECIMAL);
	private final SliderSetting range = new SliderSetting("Range", 4, 1, 6, 0.05, ValueDisplay.DECIMAL);
	private final EnumSetting<PRIORITY> priority = new EnumSetting<KillAuraSigmaHack.PRIORITY>("Priority",
			PRIORITY.values(), PRIORITY.Angle);
	private final CheckboxSetting INVISIBLES = new CheckboxSetting("Invisibles", true);
	private final CheckboxSetting WALLS = new CheckboxSetting("Walls", true);
	private final CheckboxSetting PLAYER = new CheckboxSetting("Players", false);
	private final CheckboxSetting OTHERS = new CheckboxSetting("Others", false);
	public static EntityLivingBase target;
	public static EntityLivingBase vip;
	public static EntityLivingBase blockTarget;

	private STimer switchTimer = new STimer();
	public static float sYaw;
	public static float sPitch;
	public static float aacB;
	private double fall;
	int[] randoms = new int[] { 0, 1, 0 };
	private boolean isBlocking = false;
	public static boolean isSetup = false;
	private STimer newTarget = new STimer();
	private STimer lastStep = new STimer();
	private STimer rtimer = new STimer();
	private List loaded = new CopyOnWriteArrayList();
	private int index;
	private int timer;
	private int crits;
	private int groundTicks;

	public KillAuraSigmaHack() {
		super("KASigma", "Killaura but SIGMA");
		setCategory(Category.COMBAT);
		addSetting(INVISIBLES);
		addSetting(OTHERS);
		addSetting(PLAYER);
		addSetting(WALLS);
		addSetting(hitchange);
		addSetting(mode);
		addSetting(mode1);
		addSetting(priority);
		addSetting(range);
	}

	@Override
	protected void onEnable() {
		if (mc.player != null) {
			sYaw = mc.player.rotationYaw;
			sPitch = mc.player.rotationPitch;
			/* this.loaded.clear(); */
			this.groundTicks = MoveUtils.isOnGround(0.01D) ? 1 : 0;
		}

		this.newTarget.reset();
		this.timer = 20;

		aacB = 0.0F;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	protected void onDisable() {
		this.loaded.clear();

		if (mc.player != null) {
			target = null;
			blockTarget = null;
		}
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	public static boolean isSetupTick() {
		return isSetup;
	}

	private EntityLivingBase getOptimalTarget(double range) {
		ArrayList load = new ArrayList();
		Iterator var5 = mc.world.loadedEntityList.iterator();

		while (var5.hasNext()) {
			Object o = var5.next();

			if (o instanceof EntityLivingBase) {
				EntityLivingBase ent = (EntityLivingBase) o;

				if (this.validEntity(ent, range)) {
					if (ent == vip) {
						return ent;
					}

					load.add(ent);
				}
			}
		}

		if (load.isEmpty()) {
			return null;
		} else {
			return this.getTarget(load);
		}
	}

	private void sortList(List<EntityLivingBase> weed) {
		

		switch (priority.getSelected()) {
		case Health:
			

				weed.sort((o1, o2) -> {
					return (int) (o1.getHealth() - o2.getHealth());
				});
			
			break;

		case FOV:
		
				weed.sort(Comparator.comparingDouble((o) -> {
					return (double) RotationUtils.getDistanceBetweenAngles(mc.player.rotationPitch,
							RotationUtils.getRotations(o)[0]);
				}));
			

			break;

		case Angle:
		
				weed.sort((o1, o2) -> {
					float[] rot1 = RotationUtils.getRotations(o1);
					float[] rot2 = RotationUtils.getRotations(o2);
					return (int) (mc.player.rotationYaw - rot1[0] - (mc.player.rotationYaw - rot2[0]));
				});
			

			break;

		case Armor:
	
				weed.sort(Comparator.comparingInt((o) -> {
					return o instanceof EntityPlayer ? ((EntityPlayer) o).inventory.getFieldCount()
							: (int) o.getHealth();
				}));
			

			break;

		case Range:
			
				weed.sort((o1, o2) -> {
					return (int) (o1.getDistanceSq(mc.player) * 1000.0F - o2.getDistanceSq(mc.player) * 1000.0F);
				});
			}
		
	}

	private EntityLivingBase getTarget(List list) {
		this.sortList(list);
		return list.isEmpty() ? null : (EntityLivingBase) list.get(0);
	}

	/*
	 * boolean validEntity(EntityLivingBase entity, double range) { boolean players
	 * = PLAYER.isChecked(); boolean animals = OTHERS.isChecked();
	 * 
	 * if (mc.player.isEntityAlive() && !(entity instanceof EntityPlayerSP) &&
	 * (double)mc.player.getDistanceSq(entity) <= range) { if
	 * (!RotationUtils.canEntityBeSeen(entity) && !WALLS.isChecked()) { return
	 * false; } !AntiBotHack.getInvalid().contains(entity) && else if (
	 * !entity.isPlayerSleeping()) { if
	 * (ModFriendsLoader.friendList.contains(entity.getName())) { return false; }
	 * else { if (entity instanceof EntityPlayer) { if (players) { EntityLivingBase
	 * player = entity;
	 * 
	 * if (!player.isEntityAlive() && (double)player.getHealth() == 0.0D) { return
	 * false; }
	 * 
	 * 
	 * if (TeamUtils.isTeam(mc.player, player) &&
	 * ((Boolean)((Setting)this.settings.get("TEAMS")).getValue()).booleanValue()) {
	 * return false; }
	 * 
	 * 
	 * if (player.isInvisible() && !INVISIBLES.isChecked()) { return false; }
	 * 
	 * 
	 * return true; } } else if (!entity.isEntityAlive()) { return false; }
	 * 
	 * if (!animals || !(entity instanceof EntityMob) && !(entity instanceof
	 * EntityIronGolem) && !(entity instanceof EntityAnimal) && !(entity instanceof
	 * EntityVillager)) { return false; } else if
	 * (entity.getName().equals("Villager") && entity instanceof EntityVillager) {
	 * return false; } else { return true; } } } else { return false; } } else {
	 * return false; } }
	 */
	boolean validEntity(EntityLivingBase entity,double r) {
		r=range.getValue();
		boolean players = PLAYER.isChecked();
		boolean animals = OTHERS.isChecked();

		if ((mc.player.isEntityAlive()) && !(entity instanceof EntityPlayerSP)) {
			if (mc.player.getDistance(entity) <= range.getValue()) {

				if (ModFriendsLoader.friendList.contains(entity.getName())) {
					return false;
				}
				if (entity.isPlayerSleeping()) {
					return false;
				}

				if (entity instanceof EntityPlayer) {
					if (players) {

						EntityPlayer player = (EntityPlayer) entity;
						if (!player.isEntityAlive() && player.getHealth() == 0.0) {
							return false;
						} else if (player.isInvisible() && !INVISIBLES.isChecked()) {
							return false;
						} else if (ModFriendsLoader.friendList.contains(player.getName())) {
							return false;
						} else
							return true;
					}
				} else {
					if (!entity.isEntityAlive()) {

						return false;
					}
				}

				if (entity instanceof EntityMob && animals) {

					return true;
				}
				if ((entity instanceof EntityAnimal || entity instanceof EntityVillager) && animals) {
					if (entity.getName().equals("Villager")) {
						return false;
					}
					return true;
				}
			}
		}

		return false;
	}
	private enum PRIORITY {
		Health, FOV, Angle, Armor, Range
	}

	public void customRots(EntityLivingBase ent) {
		double randomYaw = 0.05D;
		double randomPitch = 0.05D;
		float[] rotsN = this.getCustomRotsChange(sYaw, sPitch, target.posX + (double) randomNumber(1, -1) * randomYaw,
				target.posY + (double) randomNumber(1, -1) * randomPitch,
				target.posZ + (double) randomNumber(1, -1) * randomYaw);
		float targetYaw = rotsN[0];
		float yawFactor = targetYaw * targetYaw / (4.7F * targetYaw);

		if (targetYaw < 5.0F) {
			yawFactor = targetYaw * targetYaw / (3.7F * targetYaw);
		}

		if (Math.abs(yawFactor) > 7.0F) {
			aacB = yawFactor * 7.0F;
			yawFactor = targetYaw * targetYaw / (3.7F * targetYaw);
		} else {
			yawFactor = targetYaw * targetYaw / (6.7F * targetYaw) + aacB;
		}

		/* em.setYaw(sYaw + yawFactor); */
		/* mc.player.setYaw(sYaw + yawFactor); */
		sYaw += yawFactor;
		float targetPitch = rotsN[1];
		float pitchFactor = targetPitch / 3.7F;
		/* em.setPitch(sPitch + pitchFactor); */
		sPitch += pitchFactor;
	}

	public float[] getCustomRotsChange(float yaw, float pitch, double x, double y, double z) {
		double xDiff = x - mc.player.posX;
		double zDiff = z - mc.player.posZ;
		double yDiff = y - mc.player.posY;
		double dist = (double) MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
		double mult = 1.0D / (dist + 1.0E-4D) * 2.0D;

		if (mult > 0.2D) {
			mult = 0.2D;
		}

		if (!mc.world.getEntitiesWithinAABBExcludingEntity(mc.player, mc.player.getEntityBoundingBox())
				.contains(target)) {
			x += 0.3D * (double) this.randoms[0];
			y -= 0.4D + mult * (double) this.randoms[1];
			z += 0.3D * (double) this.randoms[2];
		}

		xDiff = x - mc.player.posX;
		zDiff = z - mc.player.posZ;
		yDiff = y - mc.player.posY;
		float yawToEntity = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
		float pitchToEntity = (float) (-(Math.atan2(yDiff, dist) * 180.0D / Math.PI));
		return new float[] { MathUtils.wrapAngleTo180_float(-(yaw - yawToEntity)),
				-MathUtils.wrapAngleTo180_float(pitch - pitchToEntity) - 2.5F };
	}

	public static int randomNumber(int max, int min) {
		return Math.round((float) min + (float) Math.random() * (float) (max - min));
	}

	private void smoothAim() {
		double randomYaw = 0.05D;
		double randomPitch = 0.05D;
		float targetYaw = RotationUtils.getYawChange(sYaw, target.posX + (double) randomNumber(1, -1) * randomYaw,
				target.posZ + (double) randomNumber(1, -1) * randomYaw);
		float yawFactor = targetYaw / 1.7F;
		/* em.setYaw(sYaw + yawFactor); */
		sYaw += yawFactor;
		float targetPitch = RotationUtils.getPitchChange(sPitch, target,
				target.posY + (double) randomNumber(1, -1) * randomPitch);
		float pitchFactor = targetPitch / 1.7F;
		/* em.setPitch(sPitch + pitchFactor); */
		sPitch += pitchFactor;
	}

	private void hitEntity(EntityLivingBase ent) {

		
		mc.player.swingArm(EnumHand.MAIN_HAND);
		mc.playerController.attackEntity(mc.player, ent);

	}

	private enum Mode {
		Switch, Multi2
	}

	private enum rotatinsMode {
		Smooth, Legit
	}

	private List getTargets(double range) {
		ArrayList targets = new ArrayList();
		Iterator var5 = mc.world.loadedEntityList.iterator();

		while (var5.hasNext()) {
			Object o = var5.next();

			if (o instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) o;

				if (this.validEntity(entity, range)) {
					targets.add(entity);
				}
			}

		}

		this.sortList(targets);
		return targets;
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {
	
		EntityLivingBase newT;
		boolean var33 = (double) randomNumber(0, 100) > hitchange.getValue();
		newT = this.getOptimalTarget(range.getValue());

		if (mode.getSelected() == Mode.Switch) {
			   if (target != null)
               {
                   this.timer = 0;
                   int var35 = randomNumber(1, -1);
                   int var36 = randomNumber(1, -1);
                   int var38 = randomNumber(1, -1);
                   this.randoms[0] = var35;
                   this.randoms[1] = var36;
                   this.randoms[2] = var38;
                   float var39 = RotationUtils.getYawChange(sYaw, target.posX, target.posZ);

                   if (mode1.getSelected()==rotatinsMode.Legit)
                   {
                       var39 = this.getCustomRotsChange(sYaw, sPitch, target.posX, target.posY, target.posZ)[0];
                   }

                   double var41 = 60.0F - mc.player.getDistanceSq(target) * 10.0F;

                   if (mode1.getSelected()==rotatinsMode.Legit)
                   {
                       var41 = 50.0F - mc.player.getDistanceSq(target) * 10.0F;
                   }

                   if (var39 > var41 || var39 < -var41 || !this.newTarget.delay(70.0F) )
                   {
                       var33 = true;
                   }

                   if (var33 && !(mode.getSelected()==Mode.Multi2))
                   {
                       mc.player.swingArm(EnumHand.MAIN_HAND);
                   }
                   else
                   {
                       hitEntity(target);
                   }
               }
		}
	}

	public EntityLivingBase gettarget() {
		return target;
	}
}
