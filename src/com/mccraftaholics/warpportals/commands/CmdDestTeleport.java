package com.mccraftaholics.warpportals.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.objects.CoordsPY;

public class CmdDestTeleport extends CommandHandlerObject {
	public static boolean handle(Player sender, String[] args, CommandHandler main) {
		if (args.length == 1) {
			if (args[0].matches(Regex.PORTAL_DEST_NAME)) {
				try {
					CoordsPY tpCoords = main.mPortalManager.getDestCoords(args[0]);
					if (tpCoords == null) {
						sender.sendMessage(main.mCC + "\"" + args[0] + "\" is not a valid Destination.");
					} else {
						Player player = (Player) sender;
						Location loc = player.getLocation();
						Utils.coordsToLoc(tpCoords, loc);
						player.teleport(loc);
						sender.sendMessage(main.mCC + "Teleported to \"" + args[0] + "\" @ " + tpCoords.toNiceString());
					}
				} catch (Exception e) {
					sender.sendMessage(main.mCC + "Error teleporting to WarpPortal Location \"" + args[0] + "\"");
				}
			} else
				sender.sendMessage(main.mCC + "Command argument must be a valid alpha-numeric WarpPortal Location name.");
		} else
			return false;
		return true;
	}
}
