package net.wurstclient.forge.hacks;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.wurstclient.fmlevents.RenderEvent;
import net.wurstclient.fmlevents.WRenderBlockModelEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;
import net.wurstclient.forge.utils.ColourUtils;
import net.wurstclient.forge.utils.EntityUtils;
import net.wurstclient.forge.utils.HueCycler;

public class TracersHack extends Hack {
	private final CheckboxSetting players = new CheckboxSetting("Players", true);
	private final CheckboxSetting animals = new CheckboxSetting("Animals", true);
	private final CheckboxSetting mobs = new CheckboxSetting("Mobs", false);
	private final CheckboxSetting friends = new CheckboxSetting("Friends", true);
	private final SliderSetting range = new SliderSetting("Range", 200, 0, 400, 50, ValueDisplay.DECIMAL);
	private final SliderSetting opacity = new SliderSetting("opacity ", 1, 0, 1, 1, ValueDisplay.DECIMAL);
	HueCycler cycler = new HueCycler(3600);

	public TracersHack() {
		super("Tracers", "No one can escape your pursuit");
		setCategory(Category.RENDER);
		addSetting(animals);
		addSetting(friends);
		addSetting(mobs);
		addSetting(opacity);
		addSetting(players);
		addSetting(range);
	}

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onUpdate(WUpdateEvent event) {

		cycler.next();

	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onWorldRender(RenderEvent  event ) {
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().world.loadedEntityList.stream().filter(EntityUtils::isLiving)
				.filter(entity -> !EntityUtils.isFakeLocalPlayer(entity))
				.filter(entity -> (entity instanceof EntityPlayer ? players.isChecked() && mc.player != entity
						: (EntityUtils.isPassive(entity) ? animals.isChecked() : mobs.isChecked())))
				.filter(entity -> mc.player.getDistance(entity) < range.getValue()).forEach(entity -> {
					int colour = getColour(entity);
					if (colour == ColourUtils.Colors.RAINBOW) {
						if (!friends.isChecked())
							return;
						colour = cycler.current();
					}
					final float r = ((colour >>> 16) & 0xFF) / 255f;
					final float g = ((colour >>> 8) & 0xFF) / 255f;
					final float b = (colour & 0xFF) / 255f;
					drawLineToEntity(entity, r, g, b, opacity.getValueF());
				});
		GlStateManager.popMatrix();
	}

	private void drawRainbowToEntity(Entity entity, float opacity) {
		Vec3d eyes = new Vec3d(0, 0, 1)
				.rotatePitch(-(float) Math.toRadians(Minecraft.getMinecraft().player.rotationPitch))
				.rotateYaw(-(float) Math.toRadians(Minecraft.getMinecraft().player.rotationYaw));
		double[] xyz = interpolate(entity);
		double posx = xyz[0];
		double posy = xyz[1];
		double posz = xyz[2];
		double posx2 = eyes.x;
		double posy2 = eyes.y + mc.player.getEyeHeight();
		double posz2 = eyes.z;

		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(1.5f);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		cycler.reset();
		cycler.setNext(opacity);
		GlStateManager.disableLighting();
		GL11.glLoadIdentity();
		/* mc.entityRenderer.orientCamera(mc.getRenderPartialTicks()); */

		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex3d(posx, posy, posz);
			GL11.glVertex3d(posx2, posy2, posz2);
			cycler.setNext(opacity);
			GL11.glVertex3d(posx2, posy2, posz2);
			GL11.glVertex3d(posx2, posy2, posz2);
		}

		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor3d(1d, 1d, 1d);
		GlStateManager.enableLighting();
	}

	private int getColour(Entity entity) {

		if (EntityUtils.isPassive(entity))
			return ColourUtils.Colors.GREEN;
		else
			return ColourUtils.Colors.RED;
	}

	public static double interpolate(double now, double then) {
		return then + (now - then) * mc.getRenderPartialTicks();
	}

	public static double[] interpolate(Entity entity) {
		double posX = interpolate(entity.posX, entity.lastTickPosX) - mc.getRenderManager().viewerPosX;
		double posY = interpolate(entity.posY, entity.lastTickPosY) - mc.getRenderManager().viewerPosY;
		double posZ = interpolate(entity.posZ, entity.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
		return new double[] { posX, posY, posZ };
	}

	public static void drawLineToEntity(Entity e, float red, float green, float blue, float opacity) {
		double[] xyz = interpolate(e);
		drawLine(xyz[0], xyz[1], xyz[2], e.height, red, green, blue, opacity);
	}

	public static void drawLine(double posx, double posy, double posz, double up, float red, float green, float blue,
			float opacity) {
		Vec3d eyes = new Vec3d(0, 0, 1)
				.rotatePitch(-(float) Math.toRadians(Minecraft.getMinecraft().player.rotationPitch))
				.rotateYaw(-(float) Math.toRadians(Minecraft.getMinecraft().player.rotationYaw));

		drawLineFromPosToPos(eyes.x, eyes.y + mc.player.getEyeHeight(), eyes.z, posx, posy, posz, up, red, green, blue,
				opacity);
	}

	public static void drawLineFromPosToPos(double posx, double posy, double posz, double posx2, double posy2,
			double posz2, double up, float red, float green, float blue, float opacity) {
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(1.5f);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, opacity);
		GlStateManager.disableLighting();
		GL11.glLoadIdentity();
		/* mc.entityRenderer.orientCamera(mc.getRenderPartialTicks()); */

		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex3d(posx, posy, posz);
			GL11.glVertex3d(posx2, posy2, posz2);
			GL11.glVertex3d(posx2, posy2, posz2);
			GL11.glVertex3d(posx2, posy2 + up, posz2);
		}

		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor3d(1d, 1d, 1d);
		GlStateManager.enableLighting();
	}
}
