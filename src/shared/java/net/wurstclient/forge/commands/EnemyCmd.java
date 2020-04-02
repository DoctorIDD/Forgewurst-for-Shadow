package net.wurstclient.forge.commands;

import java.util.Arrays;

import net.wurstclient.forge.Command;
import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.Command.CmdError;
import net.wurstclient.forge.Command.CmdException;
import net.wurstclient.forge.Command.CmdSyntaxError;
import net.wurstclient.forge.loader.ModEnemyLoader;
import net.wurstclient.forge.loader.ModFriendsLoader;
import net.wurstclient.forge.utils.ChatUtils;

public class EnemyCmd extends Command{
	private static final ForgeWurst wurst = ForgeWurst.getForgeWurst();
	public EnemyCmd()
	{
		super("enemy", "add enmy you  want to attack.", "Syntax: .enemy add <name> <desc>",
			".enemy add <name> <desc>", ".enemy remove <name>",
			".enemy list [<page>]", ".enemy remove-all", ".enemy reset",
			"Multiple hacks/commands must be separated by ';'.");
	}
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length < 1)
			throw new CmdSyntaxError();
		
		switch(args[0].toLowerCase())
		{
			case "add":
			add(args);
			break;
			
			case "remove":
			remove(args);
			break;
			
			case "remove-all":
			ModEnemyLoader.enemyList.clear();
			ModEnemyLoader.writeEnemyList();
			wurst.getFriends().removeAll();
			ChatUtils.message("All Enemy removed.");
			break;
			
			case "reset":
			wurst.getFriends().loadDefaults();
			ChatUtils.message("All Enemy reset to defaults.");
			break;
			
			default:
			throw new CmdSyntaxError();
		}
	}
	private void remove(String[] args) throws CmdException
	{
		if(args.length != 2)
			throw new CmdSyntaxError();
		
		String name = args[1].toUpperCase();
		
		/*
		 * String oldCommands =
		 * ForgeWurst.getForgeWurst().getFriends().getCommands(name); if(oldCommands ==
		 * null) throw new CmdError("Nothing to remove.");
		 */
		ModEnemyLoader.enemyList.remove(name);
		ModEnemyLoader.writeEnemyList();
		ChatUtils.message("Enemy removed: " + name );
	}
	private void add(String[] args) throws CmdException
	{
		if(args.length < 3)
			throw new CmdSyntaxError();
		
		String name = args[1].toUpperCase();
		
		
		String commands =
			String.join(" ", Arrays.copyOfRange(args, 2, args.length));
		ModEnemyLoader.enemyList.add(commands);
		ModEnemyLoader.writeEnemyList();
		ChatUtils.message("Enemy set: " + name + " -> " + commands);
	}
	
}
