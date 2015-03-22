package com.mccraftaholics.warpportals.commands;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class CmdPortalTPMessage extends CommandHandlerObject {

    private static final String[] ALIASES = {"wp-portal-tpmessage", "wpptm"};
    private static final String PERMISSION = "warpportals.admin.portal.message";
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
        if (args.length < 2) {
            return false;
        }
        try {
            PortalInfo portal = main.mPortalManager.getPortal(args[0].trim());
            if (portal == null) {
                sender.sendMessage(main.mCC + "\"" + args[0].trim() + "\" is not a WarpPortal!");
                return true;
            }
            /*
             * Combine all arguments after the first to create the teleport message
             */
            portal.message = Utils.join(args, " ", 1, args.length).trim();
            sender.sendMessage(main.mCC + portal.name + " will now send \"" + portal.message + "\" upon use");
            return true;
        } catch (Exception e) {
            sender.sendMessage(main.mCC + "Error modifying WarpPortal teleport message");
            return false;
        }
    }

}
