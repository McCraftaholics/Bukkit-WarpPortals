package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;
import com.mccraftaholics.warpportals.objects.PortalTool;

public class CmdPortalDelTool extends CommandHandlerObject {
	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		if (sender.hasPermission("warpportals.admin.portal.delete.tool")) {
			try {
				Player player = (Player) sender;
				ItemStack curItem = player.getItemInHand();
				if (!curItem.getType().isBlock()) {
					PortalTool tool = new PortalTool(curItem.getType(), PortalTool.Action.DELETE);
					main.mPortalManager.addTool(player.getName(), tool);
					sender.sendMessage(main.mCC + "Portal delete tool equipped to \"" + curItem.getType() + "\"");
				} else
					sender.sendMessage(main.mCC + "You can't use a block for that! Try using something like the fishing rod.");
			} catch (Exception e) {
				sender.sendMessage(main.mCC + "Error deleting Portal");
			}
		}
		return true;
	}
}
