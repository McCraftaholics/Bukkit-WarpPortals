package com.mccraftaholics.warpportals.bukkit;

import com.mccraftaholics.warpportals.api.WarpPortalEnterEvent;
import com.mccraftaholics.warpportals.api.WarpPortalEvent;
import com.mccraftaholics.warpportals.api.WarpPortalTeleportEvent;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mccraftaholics.warpportals.helpers.Defaults;
import com.mccraftaholics.warpportals.manager.PortalManager;
import com.mccraftaholics.warpportals.objects.CoordsPY;

public class BukkitEventListener implements Listener {
    JavaPlugin mPlugin;
    PortalManager mPortalManager;
    YamlConfiguration mPortalConfig;
    ChatColor mCC;
    boolean mAllowNormalPortals;

    public BukkitEventListener(JavaPlugin plugin, PortalManager portalManager, YamlConfiguration portalConfig) {
        mPlugin = plugin;
        mPortalManager = portalManager;
        mPortalConfig = portalConfig;

        mCC = ChatColor.getByChar(mPortalConfig.getString("portals.general.textColor", Defaults.CHAT_COLOR));
        mAllowNormalPortals = mPortalConfig.getBoolean("portals.general.allowNormalPortals", Defaults.ALLOW_NORMAL_PORTALS);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalEnter(PlayerPortalEvent event) {
        // Get player involved in the event
        Player player = event.getPlayer();
        // Check if player is in a WarpPortal or normal portal
        /* Must be one of the two because this is triggered on the Bukkit PortalEnter event */
        PortalInfo portal = mPortalManager.checkPlayerLoose(player.getLocation());
        boolean isWarpPortal = portal != null;

        // Create WarpPortalEnterEvent
        WarpPortalEnterEvent wpee = new WarpPortalEnterEvent(player, isWarpPortal);
        // Call WarpPortalEnterEvent
        Bukkit.getPluginManager().callEvent(wpee);

        /* Check if event has been cancelled */
        // If not, then continue on
        if (!wpee.isCancelled()) {
            // If the player entered a WarpPortal
            if (isWarpPortal) {
                // Cancel Bukkit's PortalEnterEvent so that it doesn't propagate to default handling
                event.setCancelled(true);

                // Check player permissions to use portal
                boolean hasPermission = player.hasPermission("warpportal.enter");

                // Create WarpPortalEvent
                WarpPortalEvent wpEvent = new WarpPortalEvent(player, portal, hasPermission);
                // Call WarpPortalEvent
                Bukkit.getPluginManager().callEvent(wpEvent);

                // Check if event has been cancelled
                // Event status defaults to player permissions to use the portal
                if (wpEvent.isCancelled()) {
                    // Get (possibly modified) teleportation data
                    CoordsPY tpCoords = wpEvent.getTeleportCoordsPY();

                    // Save player's current location
                    Location preTPLocation = player.getLocation();

                    /* Teleport player */
                    Location tpLoc = new Location(tpCoords.world, tpCoords.x, tpCoords.y, tpCoords.z);
                    tpLoc.setPitch(tpCoords.pitch);
                    tpLoc.setYaw(tpCoords.yaw);
                    player.teleport(tpLoc);

                    WarpPortalTeleportEvent wpTPEvent = new WarpPortalTeleportEvent(player, preTPLocation);
                    // Call WarpPortalTeleportEvent
                    Bukkit.getPluginManager().callEvent(wpTPEvent);

                }
            } else {
                // The player did not enter a WarpPortal
                // Check to see if the server is configured to allow normal portal events
                if (!mAllowNormalPortals)
                    // If not allowed, cancel the event
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasBlock() && e.hasItem() && e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            mPortalManager.playerItemRightClick(e);
        }
    }

    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent e) {
        if (e.getBlock().getType() == Material.PORTAL) {
            e.setCancelled(true);
        }
    }

}
