package com.alexwendland.warpportals.commands;

import org.bukkit.command.CommandSender;

import com.alexwendland.warpportals.bukkit.CommandHandler;

public class CmdHelp {

	public static final String[] CMD_USAGES = { "/pcreate [portalname] [destName|(World,x,y,z)]", "/pdelete [portalName]", "/pdeltool", "/pdest [destName]",
			"/pdestdel [destName]", "/plist", "/pdestlist", "/ptp [p|d] [portalName|destName]", "/psave", "/pload", "/pbackup" };

	public static final String[] CMD_DESCS = { "Equip the current item as a Portal Creation tool", "Delete a Portal by name",
			"Bind the Portal Deletion tool to the current item", "Save your current location as a WarpPortal Destination", "Remove the Portal Destination",
			"List all Portals", "List all saved Portal Destinations", "Teleport to a Portal/Destination", "Force save all Portal data", "Force load Portal data from portals.yml", "Backup the current Portal data to \"portals.yml_yyyy-MM-ddTkk-mm-ss.bac\"" };

	public static boolean handle(CommandSender sender, String[] args, CommandHandler main) {
		StringBuilder sb = new StringBuilder();
		sb.append(main.mCC + "Portal Commands:\n");
		for (int i = 0; i < CMD_USAGES.length; i++) {
			sb.append("\n " + CMD_USAGES[i] + "\n - " + CMD_DESCS[i] + "\n");
		}
		sender.sendMessage(sb.toString());
		return true;
	}

}
