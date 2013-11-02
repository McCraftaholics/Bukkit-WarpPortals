package com.mccraftaholics.warpportals.commands;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;

public class CmdBackup extends CommandHandlerObject {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_kk-mm-ss");
			File backupFile = new File(main.mPortalPlugin.getDataFolder(), "portals_" + sdf.format(new Date()) + ".bac");
			backupFile.createNewFile();
			if (main.mPortalManager.saveDataFile(backupFile))
				sender.sendMessage("Portal data backup up to \"" + backupFile.getAbsolutePath());
			else
				sender.sendMessage("Error backing up Portal data to \"" + backupFile.getAbsolutePath());
		} catch (Exception e) {
			sender.sendMessage("Error backing up Portal data");
		}
		return true;
	}

}
