package net.wurstclient.forge.commands;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.wurstclient.forge.Command;
import net.wurstclient.forge.Command.CmdSyntaxError;
import net.wurstclient.forge.utils.ChatUtils;

public class PositionCmd extends Command {
	public PositionCmd() {
		super("position", "Show your current location", "Syntax: .position <player>");
	}

	@Override
	public void call(String[] args) throws CmdException {
		if (args.length < 1)
			throw new CmdSyntaxError();
		switch (args[0].toLowerCase()) {
		case "me":
			me(args);
			break;
		
		}
	}

	/*
	 * private void a(String[] args) throws PlayerNotFoundException,
	 * CommandException {
	 * 
	 * EntityPlayerSP player = FMLClientHandler.instance().getClientPlayerEntity();
	 * if(!player.isServerWorld()) { ChatUtils.error("Server only"); return; }
	 * 
	 * if(player.isServerWorld()) { EntityPlayerMP entityPlayerMP = args.length > 0
	 * ? CommandBase.getPlayer(player.getServer(), player, args[0]) :
	 * CommandBase.getCommandSenderAsPlayer(player); if (entityPlayerMP == null)
	 * return;
	 * 
	 * Vec3d pos = entityPlayerMP.getPositionVector();
	 * ChatUtils.message(entityPlayerMP.getName() +
	 * pos+entityPlayerMP.getPlayerIP()); }
	 * 
	 * 
	 * }
	 */

	private void me(String[] args) {
		EntityPlayerSP player = FMLClientHandler.instance().getClientPlayerEntity();
		Vec3d pos = player.getPositionVector();
		ChatUtils.message(player.getName() + pos + player.getEntityWorld());
	}

}
