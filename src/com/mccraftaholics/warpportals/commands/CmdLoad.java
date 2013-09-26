package com.mccraftaholics.warpportals.commands;

import java.util.HashMap;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.objects.CoordsPY;

public class CmdLoad {

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		main.mPortalManager.mPortalInteractManager.clearPortalMap();
		main.mPortalManager.mPortalDestManager.mPortalDestMap = new HashMap<String, CoordsPY>();
		main.mPortalManager.loadData();
		sender.sendMessage(main.mCC + "Portal data loaded from portals.yml.");
		return true;
	}

}
