package com.alexwendland.warpportals.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.alexwendland.warpportals.commands.CmdBackup;
import com.alexwendland.warpportals.commands.CmdCreate;
import com.alexwendland.warpportals.commands.CmdDelTool;
import com.alexwendland.warpportals.commands.CmdDelete;
import com.alexwendland.warpportals.commands.CmdDest;
import com.alexwendland.warpportals.commands.CmdDestDel;
import com.alexwendland.warpportals.commands.CmdDestList;
import com.alexwendland.warpportals.commands.CmdHelp;
import com.alexwendland.warpportals.commands.CmdList;
import com.alexwendland.warpportals.commands.CmdLoad;
import com.alexwendland.warpportals.commands.CmdSave;
import com.alexwendland.warpportals.commands.CmdGoTo;
import com.alexwendland.warpportals.helpers.Defaults;
import com.alexwendland.warpportals.manager.PortalManager;

public class CommandHandler {
	public PortalPlugin mPortalPlugin;
	public PortalManager mPortalManager;
	public YamlConfiguration mPortalConfig;
	public String mCC;

	public CommandHandler(PortalPlugin mainPlugin, PortalManager portalManager, YamlConfiguration portalConfig) {
		mPortalPlugin = mainPlugin;
		mPortalManager = portalManager;
		mPortalConfig = portalConfig;

		mCC = mPortalConfig.getString("portals.general.textColor", Defaults.CHAT_COLOR);
	}

	public boolean handleCommand(CommandSender sender, Command command, String label, String[] args) {
		String cmdName = command.getName().toLowerCase();
		if (sender.hasPermission("warpportal.admin") || !(sender instanceof Player)) {
			switch (cmdName) {
			case "phelp":
				return CmdHelp.handle(sender, args, this);
			case "pcreate":
				return CmdCreate.handle(sender, args, this);
			case "pdelete":
				return CmdDelete.handle(sender, args, this);
			case "pdeltool":
				return CmdDelTool.handle(sender, args, this);
			case "pdest":
				return CmdDest.handle(sender, args, this);
			case "pdestdel":
				return CmdDestDel.handle(sender, args, this);
			case "plist":
				return CmdList.handle(sender, args, this);
			case "pdestlist":
				return CmdDestList.handle(sender, args, this);
			case "pgoto":
				return CmdGoTo.handle(sender, args, this);
			case "psave":
				return CmdSave.handle(sender, args, this);
			case "pload":
				return CmdLoad.handle(sender, args, this);
			case "pbackup":
				return CmdBackup.handle(sender, args, this);
			}
		} else {
			sender.sendMessage("You do not have permission to use a Warp Portal command");
			return true;
		}
		return false;
	}
}
