package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class CmdPortalDelete extends CommandHandlerObject {

	private static final String[] ALIASES = { "wp-portal-delete", "wppd", "pdelete" };
	private static final String PERMISSION = "warpportals.admin.portal.delete.command";
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
				PortalInfo portal = main.mPortalManager.getPortalInfo(args[0]);
				if (portal != null) {
					main.mPortalManager.deletePortal(args[0]);
				} else {
					sender.sendMessage(main.mCC + args[0] + " is not a valid Portal name.");
				}
			} catch (Exception e) {
				sender.sendMessage(main.mCC + "Error saving Portal destination");
			}
		} else
			return false;
		return true;
	}
}
