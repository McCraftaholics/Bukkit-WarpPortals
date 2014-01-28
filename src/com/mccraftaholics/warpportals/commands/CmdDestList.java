package com.mccraftaholics.warpportals.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.objects.CoordsPY;

public class CmdDestList extends CommandHandlerObject {

	private static final String[] ALIASES = { "wp-destination-list", "wp-dest-list", "wp-dests", "wpdl", "pdestlist" };
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
		StringBuilder sbdest = new StringBuilder();
		sbdest.append("Destionations:");
		for (String destName : main.mPortalManager.getDestinations()) {
			CoordsPY destCoords = main.mPortalManager.getDestCoords(destName);
			try {
				sbdest.append(ChatColor.WHITE + "\n - " + ChatColor.AQUA + destName + ChatColor.WHITE + " in " + ChatColor.YELLOW + destCoords.getWorldName());
			} catch (Exception e) {
				// Catch when destCoords == null
			}
		}
		sender.sendMessage(main.mCC + sbdest.toString());
		return true;
	}

}
