package com.mccraftaholics.warpportals.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class CmdPortalTeleport extends CommandHandlerObject {
	public static boolean handle(Player sender, String[] args, CommandHandler main) {
		if (args.length == 1) {
			if (args[0].matches(Regex.PORTAL_DEST_NAME)) {
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
						sender.sendMessage(main.mCC + "\"" + args[0] + "\" is not a valid Portal.");
					if (tpCoords != null) {
						Player player = (Player) sender;
						Location loc = player.getLocation();
						Utils.coordsToLoc(tpCoords, loc);
						player.teleport(loc);
						sender.sendMessage(main.mCC + "Teleported to \"" + args[0] + "\" @ " + tpCoords.toNiceString());
					} else
						throw new Exception();
				} catch (Exception e) {
					sender.sendMessage(main.mCC + "Error teleporting to WarpPortal \"" + args[0] + "\"");
				}
			} else
				sender.sendMessage(main.mCC + "Command argument must be a valid alpha-numeric WarpPortal name.");
		} else
			return false;
		return true;
	}
}
