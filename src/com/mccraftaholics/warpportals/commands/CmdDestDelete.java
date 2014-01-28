package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;

public class CmdDestDelete extends CommandHandlerObject {

	private static final String[] ALIASES = { "wp-destination-delete", "wp-dest-delete", "wpdd", "pdestdel" };
	private static final String PERMISSION = "warpportals.admin.destination.delete";
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
		if (args.length == 1) {
			try {
				main.mPortalManager.removeDestination(args[0]);
				sender.sendMessage(main.mCC + "Destionation \"" + args[0] + "\" removed.");
			} catch (Exception e) {
				sender.sendMessage(main.mCC + "Error removing Portal destination");
			}
		} else
			return false;
		return true;
	}

}
