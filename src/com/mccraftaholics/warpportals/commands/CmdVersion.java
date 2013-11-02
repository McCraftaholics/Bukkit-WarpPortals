package com.mccraftaholics.warpportals.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;

public class CmdVersion extends CommandHandlerObject {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		if (sender.hasPermission("warpportals.version")) {
			sender.sendMessage(ChatColor.RED + "WarpPortals " + ChatColor.YELLOW + "v" + main.mPortalPlugin.getDescription().getVersion());
		}
		return true;
	}

}
