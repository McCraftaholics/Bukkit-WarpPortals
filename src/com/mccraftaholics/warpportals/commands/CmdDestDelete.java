package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;

public class CmdDestDelete extends CommandHandlerObject {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		if (args.length == 1) {
			try {
				main.mPortalManager.removeDestination(args[0]);
				sender.sendMessage(main.mCC + "Destionation \"" + args[0] + "\" removed.");
			} catch (Exception e) {
				sender.sendMessage(main.mCC + "Error removing Portal destination");
			}
		} else
			return false;
		return true;
	}

}
