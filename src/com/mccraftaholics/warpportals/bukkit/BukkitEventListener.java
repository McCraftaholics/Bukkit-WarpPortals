package com.mccraftaholics.warpportals.bukkit;

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
    String mTPMessage;
    ChatColor mTPC;
    boolean mAllowNormalPortals;

    public BukkitEventListener(JavaPlugin plugin, PortalManager portalManager, YamlConfiguration portalConfig) {
        mPlugin = plugin;
        mPortalManager = portalManager;
        mPortalConfig = portalConfig;

        mCC = ChatColor.getByChar(mPortalConfig.getString("portals.general.textColor", Defaults.CHAT_COLOR));
        mTPMessage = mPortalConfig.getString("portals.teleport.message", Defaults.TP_MESSAGE);
        mTPC = ChatColor.getByChar(mPortalConfig.getString("portals.teleport.messageColor", Defaults.TP_MSG_COLOR));
        mAllowNormalPortals = mPortalConfig.getBoolean("portals.general.allowNormalPortals", Defaults.ALLOW_NORMAL_PORTALS);
    }

    long mSartTime = 0;
    long mTimeTaken = 0;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalEnter(PlayerPortalEvent event) {
        // Get player involved in the event
        Player player = event.getPlayer();
        // Check if player is in a WarpPortal or normal portal
        /* Must be one of the two because this is triggered on the Bukkit PortalEnter event */
        CoordsPY tpCoords = mPortalManager.checkPlayerLoose(player.getLocation());
        // If the player entered a WarpPortal
        if (tpCoords != null) {
            // Check player permissions to use the portal
            if (player.hasPermission("warpportal.enter")) {
                // Cancel event so that it doesn't propagate to default handling
                event.setCancelled(true);
                /* Handle WarpPortal teleportation */
                player.sendMessage(mTPC + mTPMessage);
                Location tpLoc = new Location(tpCoords.world, tpCoords.x, tpCoords.y, tpCoords.z);
                tpLoc.setPitch(tpCoords.pitch);
                tpLoc.setYaw(tpCoords.yaw);
                player.teleport(tpLoc);
            }
        } else {
            // The player did not enter a WarpPortal
            // Check to see if the server is configured to allow normal portal events
            if (!mAllowNormalPortals)
                // If not allowed, cancel the event
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasBlock() && e.hasItem() && e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            mPortalManager.playerItemRightClick(e);
        }
    }

    @EventHandler
    public void o(BlockPhysicsEvent e) {
        if (e.getBlock().getType() == Material.PORTAL) {
            e.setCancelled(true);
        }
    }

}
