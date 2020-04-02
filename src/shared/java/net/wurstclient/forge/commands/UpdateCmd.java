package net.wurstclient.forge.commands;

import net.wurstclient.forge.Command;
import net.wurstclient.forge.Command.CmdException;
import net.wurstclient.forge.Command.CmdSyntaxError;
import net.wurstclient.forge.compatibility.WChat;
import net.wurstclient.forge.utils.system.SponsoredList;

public class UpdateCmd extends Command{
	public UpdateCmd() {
		super("update", "Update the Sanction Client", "Syntax: .update");
	}
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 0)
			throw new CmdSyntaxError();
		
		SponsoredList.main(null);
	}
}
