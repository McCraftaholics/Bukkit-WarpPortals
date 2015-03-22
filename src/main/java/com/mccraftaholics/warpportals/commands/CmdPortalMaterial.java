package com.mccraftaholics.warpportals.commands;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class CmdPortalMaterial extends CommandHandlerObject {

    private static final String[] ALIASES = {"wp-portal-material", "wppm"};
    private static final String PERMISSION = "warpportals.admin.portal.material";
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
        if (args.length != 2) {
            return false;
        }
        try {
            PortalInfo portal = main.mPortalManager.getPortal(args[0].trim());
            if (portal == null) {
                sender.sendMessage(main.mCC + "\"" + args[0].trim() + "\" is not a WarpPortal!");
                return true;
            }
            String argsMaterial = args[1];
            String argsMaterialData = null;
            if (argsMaterial.contains(":")) {
                argsMaterialData = argsMaterial.substring(argsMaterial.indexOf(":") + 1);
                argsMaterial = argsMaterial.substring(0, argsMaterial.indexOf(":"));
            }
            Material blockType = Material.matchMaterial(argsMaterial.toUpperCase());
            /* Test to see if that is a valid material type */
            if (blockType == null) {
                sender.sendMessage(main.mCC + "You have to provide a valid BLOCK_NAME to create the WarpPortal out of.\nTry PORTAL, WATER, or SUGAR_CANE_BLOCK.");
                return true;
            }
            /*
             * Test to see if it is a valid block type
             * (not a fishing rod for example)
             */
            if (!blockType.isBlock()) {
                sender.sendMessage(main.mCC + "WarpPortals can only be created out of blocks, you can't use other items.");
                return true;
            }
            /*
             * Test if material data is a valid byte type
             */
            Byte materialData = null;
            if (argsMaterialData != null) {
                try {
                    materialData = Byte.parseByte(argsMaterialData);
                } catch (Exception e) {
                    sender.sendMessage(main.mCC + "Invalid block data \"" + argsMaterialData + "\".");
                    return true;
                }
            }
            /*
             * If user is creating a WarpPortal out of PORTAL blocks,
             * let them know about the direction data attribute
             */
            if (blockType.name().equals("PORTAL")) {
                sender.sendMessage(main.mCC + "You can set the portal direction by using either \"PORTAL\" or \"PORTAL:2\" as the material.");
            }
            /*
             * Test to see if the block is solid,
             * recommend to the player that they
             * don't use it
             */
            if (blockType.isSolid()) {
                sender.sendMessage(main.mCC + "" + blockType
                        + " is solid. You can create a WarpPortal using it but that may not be the best idea.");
            }
            main.mPortalManager.changeMaterial(blockType, portal.blocks, new Location(portal.tpCoords.world, 0, 0, 0), materialData);
            sender.sendMessage(ChatColor.RED + portal.name + ChatColor.WHITE + " portal material changed to " + ChatColor.AQUA + blockType + (materialData != null ? ":" + materialData : ""));
            return true;
        } catch (Exception e) {
            sender.sendMessage(main.mCC + "Error modifying WarpPortal type");
            return true;
        }
    }

}
