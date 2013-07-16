package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;

public class CmdDelTool {
	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		boolean isPlayer = sender instanceof Player;
		if (isPlayer) {
			try {
				Player player = (Player) sender;
				ItemStack curItem = player.getItemInHand();
				if (!curItem.getType().isBlock()) {
					main.mPortalManager.addDeleting(player.getName(), curItem.getType());
				} else
					sender.sendMessage(main.mCC + "You can't use a block for that! Try using something like the fishing rod.");
			} catch (Exception e) {
				sender.sendMessage(main.mCC + "Error deleting Portal");
			}
		} else
			sender.sendMessage(main.mCC + "You're a server silly!");
		return true;
	}
}
