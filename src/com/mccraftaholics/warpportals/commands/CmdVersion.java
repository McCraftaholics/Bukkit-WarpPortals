package com.mccraftaholics.warpportals.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;

public class CmdVersion extends CommandHandlerObject {

	private static final String[] ALIASES = { "wp-version", "wpv" };
	private static final String PERMISSION = "warpportals.version";
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
		sender.sendMessage(ChatColor.RED + "WarpPortals " + ChatColor.YELLOW + "v" + main.mPortalPlugin.getDescription().getVersion());
		return true;
	}

}
