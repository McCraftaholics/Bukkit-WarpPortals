package com.mccraftaholics.warpportals.commands;

import org.bukkit.entity.Player;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.objects.CoordsPY;

public class CmdDestCreate extends CommandHandlerObject {

	private static final String[] ALIASES = { "wp-destination-create", "wpdc" };
	private static final String PERMISSION = "warpportals.admin.destination.create";
	private static final boolean REQUIRES_PLAYER = true;

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
	boolean command(Player player, String[] args, CommandHandler main) {
		if (args.length == 1) {
			if (args[0].matches(Regex.PORTAL_DEST_NAME)) {
				try {
					CoordsPY destCoords = new CoordsPY(player.getLocation());
					if (main.mPortalManager.getDestCoords(args[0].trim()) == null) {
						main.mPortalManager.addDestination(args[0].trim(), destCoords);
						player.sendMessage(main.mCC + "Destionation \"" + args[0] + "\" created at " + destCoords.toNiceString());
					} else {
						player.sendMessage(main.mCC + "A Destination already exists by the name of " + args[0].trim());
					}
				} catch (Exception e) {
					player.sendMessage(main.mCC + "Error saving Portal destination");
				}
			} else
				player.sendMessage(main.mCC + "Destination names can only be letters and numbers.");
		} else
			return false;
		return true;
	}

}
