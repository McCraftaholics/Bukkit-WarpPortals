package com.mccraftaholics.warpportals.commands;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import org.bukkit.command.CommandSender;

public class CmdBackup extends CommandHandlerObject {

    private static final String[] ALIASES = {"wp-backup", "wpb", "pbackup"};
    private static final String PERMISSION = "warpportals.admin.op.backup";
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
        try {
            if (main.mPortalManager.backupDataFile())
                sender.sendMessage("WarpPortal data backed up to WarpPortals plugin folder");
            else
                sender.sendMessage("Error backing up WarpPortal data to plugin folder");
        } catch (Exception e) {
            sender.sendMessage("Error backing up WarpPortal data");
        }
        return true;
    }
}
