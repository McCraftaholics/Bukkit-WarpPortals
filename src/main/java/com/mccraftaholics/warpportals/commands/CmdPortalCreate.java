package com.mccraftaholics.warpportals.commands;

import com.mccraftaholics.warpportals.bukkit.CommandHandler;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.NullWorldException;
import com.mccraftaholics.warpportals.objects.PortalCreate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdPortalCreate extends CommandHandlerObject {

    private static final String[] ALIASES = {"wp-portal-create", "wppc", "pcreate"};
    private static final String PERMISSION = "warpportals.admin.portal.create";
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
    boolean command(Player sender, String[] args, CommandHandler main) {
        // Check if argument length is long enough
        if (!(args.length >= 3)) {
            return false;
        }
        try {
            String argsPortalName = args[0].trim();
            String argsTpCoords = args[1].trim();
            String argsMaterial = args[2].trim();
            String argsMaterialData = null;
            if (argsMaterial.contains(":")) {
                argsMaterialData = argsMaterial.substring(argsMaterial.indexOf(":") + 1);
                argsMaterial = argsMaterial.substring(0, argsMaterial.indexOf(":"));
            }
            String tpMessage = args.length > 3 ? Utils.join(args, " ", 3, args.length) : "$default";

            // Check if portal name is valid
            if (!argsPortalName.matches(Regex.ALPHANUMERIC_NS_TEXT)) {
                sender.sendMessage(main.mCC + "The first param is the Portal Name. You can only use a-z, A-Z, 0-9.");
                return true;
            }
            // Check if portal name is already used
            if (main.mPortalManager.isPortalNameUsed(argsPortalName.trim())) {
                sender.sendMessage(main.mCC + "\"" + argsPortalName.trim() + "\" is already a Portal!");
                return true;
            }
            // Parse tpCoords from input
            CoordsPY tpCoords = null;
            try {
                // Check if tpCoords were provided in (world, x, y, z) format
                if (args[1].matches(Coords.USER_COORDS)) {
                    tpCoords = new CoordsPY(Coords.createFromUserInput(argsTpCoords));
                    // Check if tpCoords were provided as a DestName
                } else if (argsTpCoords.matches(Regex.ALPHANUMERIC_NS_TEXT)) {
                    tpCoords = main.mPortalManager.getDestCoords(argsTpCoords);
                    // Check if destination is valid
                    if (tpCoords == null) {
                        sender.sendMessage(main.mCC + "Couldn't find the WarpPortal Destination \"" + argsTpCoords + "\"");
                        return true;
                    }
                }
            } catch (NullWorldException e) {
                sender.sendMessage(main.mCC + "The world specified in the destination is invalid. \"" + e.getIdentifier() + "\" does not exist.");
                return true;
            }
            // Only proceed if tpCoords were valid successfully
            if (tpCoords == null) {
                sender.sendMessage(main.mCC
                        + "The 2nd param is the WarpPortal Destination. It must be in the format (x,y,z) or the name of a WarpPortal Destination set with /wp-destination-create [name]");
                return true;
            }
            /*
             * Get the block type specified as the 3rd
             * argument for the portal's material type
             */
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
            /* Get current item in the player's hand */
            ItemStack curItem = sender.getItemInHand();
            /*
             * Test if curItem is a tool or other
             * non-block item
             */
            if (curItem.getType().isBlock()) {
                sender.sendMessage(main.mCC + "You can't use a block for that! Try using something like the fishing rod.");
                return true;
            } else {
                PortalCreate portalCreate = new PortalCreate(curItem.getType(), argsPortalName, tpCoords, blockType, materialData, tpMessage);
                main.mPortalManager.addCreating(sender.getUniqueId(), portalCreate);
                String indentColored = "\n " + ChatColor.WHITE + "- " + ChatColor.AQUA;
                sender.sendMessage(ChatColor.AQUA + "Right-click on a Gold Block wall" +
                        indentColored + "Tool: \"" + curItem.getType().name()+ "\"" +
                        indentColored + "WarpPortal Name: " + ChatColor.RED + portalCreate.portalName +
                        indentColored + "WarpPortal Dest: " + ChatColor.YELLOW + argsTpCoords +
                        indentColored + "WarpPortal Material: " + ChatColor.GREEN + portalCreate.blockType.name() + (portalCreate.blockData != null ? ":" + portalCreate.blockData : "") +
                        indentColored + "Teleport Msg: " + ChatColor.LIGHT_PURPLE + portalCreate.teleportMessage);
                return true;
            }

        } catch (Exception e) {
            sender.sendMessage(main.mCC + "Error creating Portal");
            return false;
        }
    }
}
