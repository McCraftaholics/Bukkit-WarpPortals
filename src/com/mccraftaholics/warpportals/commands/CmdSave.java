package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.bukkit.CommandHandler.CommandHandlerObject;

public class CmdSave extends CommandHandlerObject {
	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		try {
			main.mPortalManager.saveDataFile(main.mPortalPlugin.mPortalDataFile);
			sender.sendMessage(main.mCC + "Force saved current Portal data.");
		} catch (Exception e) {
			sender.sendMessage(main.mCC + "Error saving Portal data");
		}
		return true;
	}
}
