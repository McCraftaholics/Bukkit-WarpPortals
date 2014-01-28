package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;

public class CmdSave extends CommandHandlerObject {

	private static final String[] ALIASES = { "wp-save", "wps", "psave" };
	private static final String PERMISSION = "warpportals.admin.op.save";
	private static final boolean REQUIRES_PLAYER = false;

	@Override
	public String getPermission() {
		return PERMISSION;
	}

	@Override
	public String[] getAliases() {
		return ALIASES;
	}

	@Override
	public boolean doesRequirePlayer() {
		return REQUIRES_PLAYER;
	}

	@Override
	boolean command(CommandSender sender, String[] args, CommandHandler main) {
		try {
			main.mPortalManager.saveDataFile(main.mPortalPlugin.mPortalDataFile);
			sender.sendMessage(main.mCC + "Force saved current Portal data.");
		} catch (Exception e) {
			sender.sendMessage(main.mCC + "Error saving Portal data");
		}
		return true;
	}
}
