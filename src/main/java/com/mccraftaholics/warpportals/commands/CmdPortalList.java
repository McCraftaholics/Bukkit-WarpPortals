package com.mccraftaholics.warpportals.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.objects.PortalInfo;

import java.util.UUID;

public class CmdPortalList extends CommandHandlerObject {

	private static final String[] ALIASES = { "wp-portal-list", "wppl", "plist" };
	private static final String PERMISSION = "warpportals.admin.destination.list";
	private static final boolean REQUIRES_PLAYER = false;

	@Override
	public String getPermission() {
		return PERMISSION;
	}

	@Override
	public String[] getAliases() {
		return ALIASES;
	}

	@Override
	public boolean doesRequirePlayer() {
		return REQUIRES_PLAYER;
	}

	@Override
	boolean command(CommandSender sender, String[] args, CommandHandler main) {
		StringBuilder sblist = new StringBuilder();
		sblist.append("Portals:");
		for (PortalInfo portal : main.mPortalManager.getPortals()) {
			// Retrieve linked Teleport Destination
			String destText = main.mPortalManager.getDestinationName(portal.tpCoords);
			/*
			 * If the teleport destination does not have a name, show the
			 * coordinates
			 */
			if (destText == null)
				destText = portal.tpCoords.toNiceString();
			try {
				sblist.append(ChatColor.WHITE + "\n - " + ChatColor.RED + portal.name + ChatColor.YELLOW + " ("
						+ portal.blocks.get(0).world.getName() + ") " + ChatColor.WHITE + "-> " + ChatColor.AQUA + destText);
			} catch (Exception e) {
				// Catches exceptions when blockCoordArray is 0 in length
			}
		}
		sender.sendMessage(main.mCC + sblist.toString());
		return true;
	}
}
