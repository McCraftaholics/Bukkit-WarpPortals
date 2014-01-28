package com.mccraftaholics.warpportals.bukkit;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mccraftaholics.warpportals.commands.CmdBackup;
import com.mccraftaholics.warpportals.commands.CmdDestCreate;
import com.mccraftaholics.warpportals.commands.CmdDestDelete;
import com.mccraftaholics.warpportals.commands.CmdDestList;
import com.mccraftaholics.warpportals.commands.CmdDestTeleport;
import com.mccraftaholics.warpportals.commands.CmdHelp;
import com.mccraftaholics.warpportals.commands.CmdLoad;
import com.mccraftaholics.warpportals.commands.CmdPortalCreate;
import com.mccraftaholics.warpportals.commands.CmdPortalDelTool;
import com.mccraftaholics.warpportals.commands.CmdPortalDelete;
import com.mccraftaholics.warpportals.commands.CmdPortalIDTool;
import com.mccraftaholics.warpportals.commands.CmdPortalList;
import com.mccraftaholics.warpportals.commands.CmdPortalMaterial;
import com.mccraftaholics.warpportals.commands.CmdPortalTeleport;
import com.mccraftaholics.warpportals.commands.CmdSave;
import com.mccraftaholics.warpportals.commands.CmdVersion;
import com.mccraftaholics.warpportals.commands.CommandHandlerObject;
import com.mccraftaholics.warpportals.helpers.Defaults;
import com.mccraftaholics.warpportals.manager.PortalManager;

public class CommandHandler {
	public PortalPlugin mPortalPlugin;
	public PortalManager mPortalManager;
	public YamlConfiguration mPortalConfig;
	public ChatColor mCC;

	// CommandHandler objects
	Map<String, CommandHandlerObject> mCmdHandlerMap;

	public CommandHandler(PortalPlugin mainPlugin, PortalManager portalManager, YamlConfiguration portalConfig) {
		mPortalPlugin = mainPlugin;
		mPortalManager = portalManager;
		mPortalConfig = portalConfig;

		mCC = ChatColor.getByChar(mPortalConfig.getString("portals.general.textColor", Defaults.CHAT_COLOR));

		/* *************************** *
		 * Setup CommandHandlerObjects *
		 */
		mCmdHandlerMap = new HashMap<String, CommandHandlerObject>();
		(new CmdBackup()).populate(mCmdHandlerMap);
		(new CmdDestCreate()).populate(mCmdHandlerMap);
		(new CmdDestDelete()).populate(mCmdHandlerMap);
		(new CmdDestList()).populate(mCmdHandlerMap);
		(new CmdDestTeleport()).populate(mCmdHandlerMap);
		(new CmdHelp()).populate(mCmdHandlerMap);
		(new CmdLoad()).populate(mCmdHandlerMap);
		(new CmdPortalCreate()).populate(mCmdHandlerMap);
		(new CmdPortalDelete()).populate(mCmdHandlerMap);
		(new CmdPortalDelTool()).populate(mCmdHandlerMap);
		(new CmdPortalIDTool()).populate(mCmdHandlerMap);
		(new CmdPortalList()).populate(mCmdHandlerMap);
		(new CmdPortalMaterial()).populate(mCmdHandlerMap);
		(new CmdPortalTeleport()).populate(mCmdHandlerMap);
		(new CmdSave()).populate(mCmdHandlerMap);
		(new CmdVersion()).populate(mCmdHandlerMap);
	}

	public boolean handleCommand(CommandSender sender, Command command, String label, String[] args) {

		// Lookup command in HashMap
		CommandHandlerObject cmdHandler = mCmdHandlerMap.get(command.getName().toLowerCase());

		// If it is a valid command (if there is a command handler for it
		if (cmdHandler != null)
			// Then execute the command
			return cmdHandler.handle(sender, args, this);

		// Return false otherwise
		return false;
	}
}
