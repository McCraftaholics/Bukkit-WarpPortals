package com.mccraftaholics.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class CmdList {
	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		StringBuilder sblist = new StringBuilder();
		sblist.append("Portals:");
		for (String portalName : main.mPortalManager.getPortalNames()) {
			PortalInfo portalInfo = main.mPortalManager.getPortalInfo(portalName);
			try {
				sblist.append("\n - " + portalName + " in " + portalInfo.blockCoordArray.get(0).world.getName());
			} catch (Exception e) {
				// Catches exceptions when blockCoordArray is 0 in length
			}
		}
		sender.sendMessage(main.mCC + sblist.toString());
		return true;
	}
}
