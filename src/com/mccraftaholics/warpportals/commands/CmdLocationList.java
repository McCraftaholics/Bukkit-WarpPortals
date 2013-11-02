package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;
import com.mccraftaholics.warpportals.objects.CoordsPY;

public class CmdLocationList extends CommandHandlerObject {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		StringBuilder sbdest = new StringBuilder();
		sbdest.append("Destionations:");
		for (String destName : main.mPortalManager.getDestinations()) {
			CoordsPY destCoords = main.mPortalManager.getDestCoords(destName);
			try {
				sbdest.append("\n - " + destName + " in " + destCoords.getWorldName());
			} catch (Exception e) {
				// Catch when destCoords == null
			}
		}
		sender.sendMessage(main.mCC + sbdest.toString());
		return true;
	}

}
