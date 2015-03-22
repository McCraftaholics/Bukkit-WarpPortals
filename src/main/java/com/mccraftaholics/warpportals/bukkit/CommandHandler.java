package com.mccraftaholics.warpportals.bukkit;

import com.mccraftaholics.warpportals.commands.*;
import com.mccraftaholics.warpportals.helpers.Defaults;
import com.mccraftaholics.warpportals.manager.PortalManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;

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
        (new CmdPortalTPMessage()).populate(mCmdHandlerMap);
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
