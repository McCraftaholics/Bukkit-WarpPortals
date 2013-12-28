package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;

public class CmdBackup extends CommandHandlerObject {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		if (sender.hasPermission("warpportals.admin.op.backup")) {
			try {
				if (main.mPortalManager.backupDataFile())
					sender.sendMessage("WarpPortal data backed up to WarpPortals plugin folder");
				else
					sender.sendMessage("Error backing up WarpPortal data to plugin folder");
			} catch (Exception e) {
				sender.sendMessage("Error backing up WarpPortal data");
			}
		}
		return true;
	}
}
