package com.mccraftaholics.warpportals.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalCreate;

public class CmdPortalCreate extends CommandHandlerObject {

	public static boolean handle(Player sender, String[] args, CommandHandler main) {
		if (args.length == 3) {
			try {
				if (args[0].matches(Regex.PORTAL_DEST_NAME)) {
					if (main.mPortalManager.getPortalInfo(args[0].trim()) == null) {
						CoordsPY tpCoords = null;
						if (args[1].matches("\\([0-9]+,[0-9]+,[0-9]+\\)")) {
							tpCoords = new CoordsPY(new Coords(args[1]));
						} else if (args[1].matches(Regex.PORTAL_DEST_NAME)) {
							tpCoords = main.mPortalManager.getDestCoords(args[1]);
							if (tpCoords == null)
								sender.sendMessage(main.mCC + "Couldn't find the WarpPortal Destination \"" + args[1] + "\"");
						}
						if (tpCoords != null) {
							/*
							 * Get the block type specified as the 3rd argument
							 * for the portal's material type
							 */
							Material blockType = Material.matchMaterial(args[2]);
							// Test to see if that is a valid material type
							if (blockType != null) {
								/*
								 * Test to see if it is a valid block type (not
								 * a fishing rod for example)
								 */
								if (!blockType.isBlock()) {
									sender.sendMessage(main.mCC + "" + blockType
											+ " is not a block. You can create a WarpPortal using it but that may not be the best idea.");
								}
								/*
								 * Test to see if the block is solid, recommend
								 * to the player that they don't use it
								 */
								if (blockType.isSolid()) {
									sender.sendMessage(main.mCC + "" + blockType
											+ " is solid. You can create a WarpPortal using it but that may not be the best idea.");
								}
								// Get current item in the player's hand
								ItemStack curItem = sender.getItemInHand();
								/*
								 * Test if curItem is a tool or other non-block
								 * item
								 */
								if (!curItem.getType().isBlock()) {
									PortalCreate portalCreate = new PortalCreate();
									portalCreate.toolType = curItem.getType();
									portalCreate.portalName = args[0];
									portalCreate.tpCoords = tpCoords;
									portalCreate.blockType = blockType;
									main.mPortalManager.addCreating(sender.getName(), portalCreate);
									sender.sendMessage(ChatColor.AQUA + "Right-click on a Gold Block wall\n - Tool: \"" + curItem.getType().name() + "\"\n "
											+ ChatColor.WHITE + "-" + ChatColor.AQUA + " WarpPortal Name: " + ChatColor.RED + portalCreate.portalName
											+ ChatColor.WHITE + "\n - " + ChatColor.AQUA + "WarpPortal Dest: " + ChatColor.YELLOW + args[1]);
								} else
									sender.sendMessage(main.mCC + "You can't use a block for that! Try using something like the fishing rod.");
							} else
								sender.sendMessage(main.mCC + "You have to provide a valid BLOCK_NAME to create the WarpPortal out of.");
						} else
							sender.sendMessage(main.mCC
									+ "The 2nd param is the WarpPortal Destination. It must be in the format (x,y,z) or the name of a WarpPortal Destination set with /wp-destination-create [name]");
					} else
						sender.sendMessage(main.mCC + "\"" + args[0].trim() + "\" is already a Portal!");
				} else
					sender.sendMessage(main.mCC + "The first param is the Portal Name. You can only use a-z, A-Z, 0-9.");
			} catch (Exception e) {
				sender.sendMessage(main.mCC + "Error creating Portal");
			}
		} else
			return false;
		return true;
	}

}
