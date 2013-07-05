package com.alexwendland.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.alexwendland.warpportals.bukkit.CommandHandler;

public class CmdSave {
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
