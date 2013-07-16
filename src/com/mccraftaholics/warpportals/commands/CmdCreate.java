package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalCreate;

public class CmdCreate {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		boolean isPlayer = sender instanceof Player;
		if (isPlayer) {
			if (args.length == 2) {
				try {
					if (args[0].matches(Regex.PORTAL_DEST_NAME)) {
						if (main.mPortalManager.getPortalInfo(args[0].trim()) == null) {
							CoordsPY tpCoords = null;
							if (args[1].matches("\\([0-9]+,[0-9]+,[0-9]+\\)")) {
								tpCoords = new CoordsPY(new Coords(args[1]));
							} else if (args[1].matches(Regex.PORTAL_DEST_NAME)) {
								tpCoords = main.mPortalManager.getDestCoords(args[1]);
								if (tpCoords == null)
									sender.sendMessage(main.mCC + "Couldn't find the Portal Destination \"" + args[1] + "\"");
							}
							if (tpCoords != null) {
								Player player = (Player) sender;
								ItemStack curItem = player.getItemInHand();
								if (!curItem.getType().isBlock()) {
									PortalCreate portalCreate = new PortalCreate();
									portalCreate.blockType = curItem.getType();
									portalCreate.portalName = args[0];
									portalCreate.tpCoords = tpCoords;
									main.mPortalManager.addCreating(player.getName(), portalCreate);
									sender.sendMessage(main.mCC + "Right-click on a Gold Blocks wall\n - Tool: \"" + curItem.getType().name()
											+ "\"\n - Portal Name: " + portalCreate.portalName + "\n - Portal Dest: " + args[1]);
								} else
									sender.sendMessage(main.mCC + "You can't use a block for that! Try using something like the fishing rod.");

							} else
								sender.sendMessage(main.mCC
										+ "The 2nd param is the Portal Destination. It must be in the format (x,y,z) or the name of a Destination set with /pdest [name]");
						} else
							sender.sendMessage(main.mCC + "\"" + args[0].trim() + "\" is already a Portal!");
					} else
						sender.sendMessage(main.mCC + "The first param is the Portal Name. You can only use a-z and A-Z");
				} catch (Exception e) {
					sender.sendMessage(main.mCC + "Error creating Portal");
				}
			} else
				return false;
		} else
			sender.sendMessage(main.mCC + "You're a server silly!");
		return true;
	}

}
