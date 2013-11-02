package com.mccraftaholics.warpportals.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class CmdPortalMaterial extends CommandHandlerObject {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		if (args.length == 2) {
			try {
				PortalInfo portal = main.mPortalManager.getPortalInfo(args[0].trim());
				if (portal != null) {
					/*
					 * Get the block type specified as the 3rd argument for the
					 * portal's material type
					 */
					Material blockType = Material.matchMaterial(args[1]);
					// Test to see if that is a valid material type
					if (blockType != null) {
						/*
						 * Test to see if it is a valid block type (not a
						 * fishing rod for example)
						 */
						if (!blockType.isBlock()) {
							sender.sendMessage(main.mCC + "" + blockType
									+ " is not a block. You can create a WarpPortal using it but that may not be the best idea.");
						}
						/*
						 * Test to see if the block is solid, recommend to the
						 * player that they don't use it
						 */
						if (blockType.isSolid()) {
							sender.sendMessage(main.mCC + "" + blockType + " is solid. You can create a WarpPortal using it but that may not be the best idea.");
						}
						main.mPortalManager.changeMaterial(blockType, portal.blockCoordArray, new Location(portal.tpCoords.world, 0, 0, 0));
						sender.sendMessage(ChatColor.RED + portal.name + ChatColor.WHITE + " portal material changed to " + ChatColor.AQUA + blockType);
					} else
						sender.sendMessage(main.mCC + "You have to provide a valid BLOCK_NAME to create the WarpPortal out of.");
				} else
					sender.sendMessage(main.mCC + "\"" + args[0].trim() + "\" is not a WarpPortal!");
			} catch (Exception e) {
				sender.sendMessage(main.mCC + "Error modifying WarpPortal type");
			}
		} else
			return false;
		return true;
	}

}
