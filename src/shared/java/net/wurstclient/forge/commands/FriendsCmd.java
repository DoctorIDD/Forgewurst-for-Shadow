package net.wurstclient.forge.commands;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.loader.ModFriendsLoader;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.Command;
import net.wurstclient.forge.Command.CmdError;
import net.wurstclient.forge.Command.CmdException;
import net.wurstclient.forge.Command.CmdSyntaxError;

public class FriendsCmd extends Command{
	private static final ForgeWurst wurst = ForgeWurst.getForgeWurst();
	public FriendsCmd()
	{
		super("friend", "add friends you don't want to attack.", "Syntax: .friend add <name> <desc>",
			".friend add <name> <desc>", ".friend remove <name>",
			".friend list [<page>]", ".friend remove-all", ".friend reset",
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
			ModFriendsLoader.friendList.clear();
			ModFriendsLoader.writeFriendList();
			wurst.getFriends().removeAll();
			ChatUtils.message("All keybinds removed.");
			break;
			
			case "reset":
			wurst.getFriends().loadDefaults();
			ChatUtils.message("All keybinds reset to defaults.");
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
		
		String oldCommands = ForgeWurst.getForgeWurst().getFriends().getCommands(name);
		if(oldCommands == null)
			throw new CmdError("Nothing to remove.");
		ModFriendsLoader.friendList.remove(name);
		ModFriendsLoader.writeFriendList();
		wurst.getFriends().remove(name);
		ChatUtils.message("Keybind removed: " + name + " -> " + oldCommands);
	}
	private void add(String[] args) throws CmdException
	{
		if(args.length < 3)
			throw new CmdSyntaxError();
		
		String name = args[1].toUpperCase();
		
		
		String commands =
			String.join(" ", Arrays.copyOfRange(args, 2, args.length));
		ModFriendsLoader.friendList.add(commands);
		ModFriendsLoader.writeFriendList();
		wurst.getFriends().add(name, commands);
		ChatUtils.message("Friend set: " + name + " -> " + commands);
	}
	
}
