package com.mccraftaholics.warpportals.commands;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import org.bukkit.command.CommandSender;

public class CmdHelp extends CommandHandlerObject {

    private static final String[] ALIASES = {"wp", "wp-help", "phelp"};
    private static final String PERMISSION = "warpportals.admin.listcommands";
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
        sender.getServer().dispatchCommand(sender, "help WarpPortals" + (args.length == 1 ? " " + args[0] : ""));
        return true;
    }

}
