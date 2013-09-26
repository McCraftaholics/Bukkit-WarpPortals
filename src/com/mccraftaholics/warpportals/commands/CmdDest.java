package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.objects.CoordsPY;

public class CmdDest {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		boolean isPlayer = sender instanceof Player;
		if (isPlayer) {
			if (args.length == 1) {
				if (args[0].matches(Regex.PORTAL_DEST_NAME)) {
					try {
						Player player = (Player) sender;
						CoordsPY destCoords = new CoordsPY(player.getLocation());
						if (main.mPortalManager.getDestCoords(args[0].trim()) == null) {
							main.mPortalManager.addDestination(args[0].trim(), destCoords);
							sender.sendMessage(main.mCC + "Destionation \"" + args[0] + "\" created at " + destCoords.toNiceString());
						} else {
							sender.sendMessage("A Destination already exists by the name of " + args[0].trim());
						}
					} catch (Exception e) {
						sender.sendMessage(main.mCC + "Error saving Portal destination");
					}
				} else
					sender.sendMessage(main.mCC + "Destination names can only be letters and numbers.");
			} else
				sender.sendMessage(main.mCC + "/pdest [destname]");
		} else
			sender.sendMessage(main.mCC + "You're a server silly!");
		return true;
	}

}
