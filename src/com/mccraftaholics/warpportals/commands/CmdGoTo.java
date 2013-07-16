package com.mccraftaholics.warpportals.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class CmdGoTo {
	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		boolean isPlayer = sender instanceof Player;
		if (isPlayer) {
			if (args.length == 2) {
				if (args[1].matches(Regex.PORTAL_DEST_NAME)) {
					if (args[0].matches("d")) {
						try {
							CoordsPY tpCoords = main.mPortalManager.getDestCoords(args[1]);
							if (tpCoords == null) {
								sender.sendMessage(main.mCC + "\"" + args[1] + "\" is not a valid Destination.");
							} else {
								Player player = (Player) sender;
								Location loc = player.getLocation();
								Utils.coordsToLoc(tpCoords, loc);
								player.teleport(loc);
								sender.sendMessage(main.mCC + "Teleported to \"" + args[1] + "\" @ " + tpCoords.toNiceString());
							}
						} catch (Exception e) {
							sender.sendMessage(main.mCC + "Error teleporting to Destination \"" + args[1] + "\"");
						}
					} else if (args[0].matches("p")) {
						try {
							CoordsPY tpCoords = null;
							PortalInfo portal = main.mPortalManager.getPortalInfo(args[0]);
							if (portal != null) {
								Coords midCrds = portal.blockCoordArray.get(portal.blockCoordArray.size() > 1 ? portal.blockCoordArray.size() / 2 : 0);
								tpCoords = new CoordsPY(new Coords(midCrds.world, midCrds.x, midCrds.y, midCrds.z));
								tpCoords.z += 10;
								for (Coords crd : portal.blockCoordArray) {
									if (tpCoords.equals(crd)) {
										tpCoords.z += 100;
										tpCoords.y += 10;
									}
								}
							} else
								sender.sendMessage(main.mCC + "\"" + args[1] + "\" is not a valid Portal.");
							if (tpCoords != null) {
								Player player = (Player) sender;
								Location loc = player.getLocation();
								Utils.coordsToLoc(tpCoords, loc);
								player.teleport(loc);
								sender.sendMessage(main.mCC + "Teleported to \"" + args[1] + "\" @ " + tpCoords.toNiceString());
							} else
								throw new Exception();
						} catch (Exception e) {
							sender.sendMessage(main.mCC + "Error teleporting to Portal \"" + args[1] + "\"");
						}
					} else
						sender.sendMessage(main.mCC + "First param must be either \"p\" for a Portal or \"d\" for a Dest");
				} else
					sender.sendMessage(main.mCC + "Second param must be a valid alpha-numeric Portal/Destination name.");
			} else
				sender.sendMessage(main.mCC + "/pgoto {d|p] [destname|portalname]");
		} else
			sender.sendMessage(main.mCC + "You're a server silly!");
		return true;
	}
}
