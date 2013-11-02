package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;

public class CmdHelp extends CommandHandlerObject {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		if (sender.hasPermission("warpportals.admin.listcommands")) {
			sender.getServer().dispatchCommand(sender, "help WarpPortals" + (args.length == 1 ? " " + args[0] : ""));
		}
		return true;
	}

}
