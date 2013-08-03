package com.mccraftaholics.warpportals.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.mccraftaholics.warpportals.commands.CmdBackup;
import com.mccraftaholics.warpportals.commands.CmdCreate;
import com.mccraftaholics.warpportals.commands.CmdDelTool;
import com.mccraftaholics.warpportals.commands.CmdDelete;
import com.mccraftaholics.warpportals.commands.CmdDest;
import com.mccraftaholics.warpportals.commands.CmdDestDel;
import com.mccraftaholics.warpportals.commands.CmdDestList;
import com.mccraftaholics.warpportals.commands.CmdGoTo;
import com.mccraftaholics.warpportals.commands.CmdHelp;
import com.mccraftaholics.warpportals.commands.CmdList;
import com.mccraftaholics.warpportals.commands.CmdLoad;
import com.mccraftaholics.warpportals.commands.CmdSave;
import com.mccraftaholics.warpportals.helpers.Defaults;
import com.mccraftaholics.warpportals.manager.PortalManager;

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
			if (cmdName.equals("phelp"))
				return CmdHelp.handle(sender, args, this);
			else if (cmdName.equals("pcreate"))
				return CmdCreate.handle(sender, args, this);
			else if (cmdName.equals("pdelete"))
				return CmdDelete.handle(sender, args, this);
			else if (cmdName.equals("pdeltool"))
				return CmdDelTool.handle(sender, args, this);
			else if (cmdName.equals("pdest"))
				return CmdDest.handle(sender, args, this);
			else if (cmdName.equals("pdestdel"))
				return CmdDestDel.handle(sender, args, this);
			else if (cmdName.equals("plist"))
				return CmdList.handle(sender, args, this);
			else if (cmdName.equals("pdestlist"))
				return CmdDestList.handle(sender, args, this);
			else if (cmdName.equals("pgoto"))
				return CmdGoTo.handle(sender, args, this);
			else if (cmdName.equals("psave"))
				return CmdSave.handle(sender, args, this);
			else if (cmdName.equals("pload"))
				return CmdLoad.handle(sender, args, this);
			else if (cmdName.equals("pbackup"))
				return CmdBackup.handle(sender, args, this);
		} else {
			sender.sendMessage("You do not have permission to use a Warp Portal command");
			return true;
		}
		return false;
	}
}
