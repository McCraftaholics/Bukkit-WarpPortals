package com.mccraftaholics.warpportals.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class CmdPortalList extends CommandHandlerObject {
	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		StringBuilder sblist = new StringBuilder();
		sblist.append("Portals:");
		for (String portalName : main.mPortalManager.getPortalNames()) {
			// Retrieve Portal Info
			PortalInfo portalInfo = main.mPortalManager.getPortalInfo(portalName);
			// Retrieve linked Teleport Destination
			String destText = main.mPortalManager.getDestinationName(portalInfo.tpCoords);
			/*
			 * If the teleport destination does not have a name, show the
			 * coordinates
			 */
			if (destText == null)
				destText = portalInfo.tpCoords.toNiceString();
			try {
				sblist.append("\n - " + ChatColor.RED + portalName + ChatColor.YELLOW + " (" + portalInfo.blockCoordArray.get(0).world.getName() + ") "
						+ ChatColor.WHITE + "-> " + ChatColor.AQUA + destText);
			} catch (Exception e) {
				// Catches exceptions when blockCoordArray is 0 in length
			}
		}
		sender.sendMessage(main.mCC + sblist.toString());
		return true;
	}
}
