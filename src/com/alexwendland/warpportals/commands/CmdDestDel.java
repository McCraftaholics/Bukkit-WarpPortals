package com.alexwendland.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.alexwendland.warpportals.bukkit.CommandHandler;

public class CmdDestDel {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		if (args.length == 1) {
			try {
				main.mPortalManager.removeDestination(args[0]);
				sender.sendMessage(main.mCC + "Destionation \"" + args[0] + "\" removed.");
			} catch (Exception e) {
				sender.sendMessage(main.mCC + "Error removing Portal destination");
			}
		} else
			sender.sendMessage(main.mCC + "/pdestdel [destname]");
		return true;
	}

}
