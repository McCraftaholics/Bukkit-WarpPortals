package com.mccraftaholics.warpportals.commands;

import java.util.HashMap;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.objects.CoordsPY;

public class CmdLoad extends CommandHandlerObject {

	private static final String[] ALIASES = { "wp-load", "wpl", "pload" };
	private static final String PERMISSION = "warpportals.admin.op.load";
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
		main.mPortalManager.mPortalDataManager.clearPortalMap();
		main.mPortalManager.mPortalDestManager.mPortalDestMap = new HashMap<String, CoordsPY>();
		main.mPortalManager.loadData();
		sender.sendMessage(main.mCC + "Portal data loaded from portals.yml.");
		return true;
	}

}
