package com.mccraftaholics.warpportals.commands;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CmdDestTeleport extends CommandHandlerObject {

    private static final String[] ALIASES = {"wp-destination-teleport", "wp-destination-tp", "wp-dest-teleport", "wp-dest-tp", "wpdtp"};
    private static final String PERMISSION = "warpportals.admin.destination.teleport";
    private static final boolean REQUIRES_PLAYER = true;

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
    boolean command(Player player, String[] args, CommandHandler main) {
        if (args.length == 1) {
            if (args[0].matches(Regex.ALPHANUMERIC_NS_TEXT)) {
                try {
                    CoordsPY tpCoords = main.mPortalManager.getDestCoords(args[0]);
                    if (tpCoords == null) {
                        player.sendMessage(main.mCC + "\"" + args[0] + "\" is not a valid Destination.");
                    } else {
                        Location loc = player.getLocation();
                        Utils.coordsToLoc(tpCoords, loc);
                        player.teleport(loc);
                        player.sendMessage(main.mCC + "Teleported to \"" + args[0] + "\" @ " + tpCoords.toNiceString());
                    }
                } catch (Exception e) {
                    player.sendMessage(main.mCC + "Error teleporting to WarpPortal Location \"" + args[0] + "\"");
                }
            } else
                player.sendMessage(main.mCC + "Command argument must be a valid alpha-numeric WarpPortal Location name.");
        } else
            return false;
        return true;
    }
}
