package com.mccraftaholics.warpportals.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;
import com.mccraftaholics.warpportals.objects.CoordsPY;

public class CmdDestList extends CommandHandlerObject {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		if (sender.hasPermission("warpportals.admin.destination.list")) {
			StringBuilder sbdest = new StringBuilder();
			sbdest.append("Destionations:");
			for (String destName : main.mPortalManager.getDestinations()) {
				CoordsPY destCoords = main.mPortalManager.getDestCoords(destName);
				try {
					sbdest.append(ChatColor.WHITE + "\n - " + ChatColor.AQUA + destName + ChatColor.WHITE + " in " + ChatColor.YELLOW
							+ destCoords.getWorldName());
				} catch (Exception e) {
					// Catch when destCoords == null
				}
			}
			sender.sendMessage(main.mCC + sbdest.toString());
		}
		return true;
	}

}
