package net.wurstclient.forge.commands;

import net.wurstclient.forge.Command;
import net.wurstclient.forge.Command.CmdException;
import net.wurstclient.forge.Command.CmdSyntaxError;
import net.wurstclient.forge.compatibility.WMinecraft;

public class LogginCmd extends Command{
	public LogginCmd() {
		super("l","login some servers","\"Syntax: .l <password1> <password2>\"");
	}
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length < 1)
			throw new CmdSyntaxError();
		String message= ".l " + String.join(" ", args)+String.join(" ", args);
	
		WMinecraft.getPlayer().sendChatMessage(message);
		System.out.println(message);
		
	}
}
