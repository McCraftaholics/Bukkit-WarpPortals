package com.mccraftaholics.warpportals.bukkit;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mccraftaholics.warpportals.api.WarpPortalsEvent;
import com.mccraftaholics.warpportals.api.WarpPortalsTeleportEvent;
import com.mccraftaholics.warpportals.helpers.Defaults;
import com.mccraftaholics.warpportals.manager.PortalManager;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class BukkitEventListener implements Listener {
	JavaPlugin mPlugin;
	PortalManager mPortalManager;
	YamlConfiguration mPortalConfig;

	Logger mLogger;

	final ChatColor mCC;
	final boolean mAllowNormalPortals;
	final boolean mAlertUserAboutPortalPermission;

	public BukkitEventListener(JavaPlugin plugin, PortalManager portalManager, YamlConfiguration portalConfig) {
		mPlugin = plugin;
		mPortalManager = portalManager;
		mPortalConfig = portalConfig;

		mCC = ChatColor.getByChar(mPortalConfig.getString("portals.general.textColor", Defaults.CHAT_COLOR));
		mAllowNormalPortals = mPortalConfig.getBoolean("portals.general.allowNormalPortals", Defaults.ALLOW_NORMAL_PORTALS);
		mAlertUserAboutPortalPermission = mPortalConfig.getBoolean("portals.permission.alertUser", Defaults.ALERT_USER_ABOUT_PORTAL_PERMISSION);

		mLogger = mPlugin.getLogger();
	}

	/**
	 * Watches players to detect if they enter a WarpPortal
	 * 
	 * @param e
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		/*
		 * Watch all player move events and simplify them down to block move
		 * events. Block move events can be listened for as
		 * WarpPortalsPlayerBlockMoveEvent
		 */
		// Check if player is moving between blocks
		boolean isSameBlock = (e.getFrom().getBlockX() == e.getTo().getBlockX()) && (e.getFrom().getBlockY() == e.getTo().getBlockY())
				&& (e.getFrom().getBlockZ() == e.getTo().getBlockZ());
		if (!isSameBlock) {
			// Check if player is in a WarpPortal
			PortalInfo portal = mPortalManager.isLocationInsidePortal(e.getTo());
			// If player is in a WarpPortal
			if (portal != null) {
				// Check player permissions to use portal
				boolean hasPermission = player.hasPermission("warpportals.enter." + portal.name);

				// Create WarpPortalsEvent
				WarpPortalsEvent wpEvent = new WarpPortalsEvent(player, portal, hasPermission);
				// Call WarpPortalsEvent
				Bukkit.getPluginManager().callEvent(wpEvent);

				// Check if event has been cancelled
				// Event status defaults to player permissions to use the portal
				if (!wpEvent.isCancelled()) {
					// Get (possibly modified) teleportation data
					CoordsPY tpCoords = wpEvent.getTeleportCoordsPY();

					// Save player's current location
					Location preTPLocation = player.getLocation();

					/* Teleport player */
					Location tpLoc = new Location(tpCoords.world, tpCoords.x, tpCoords.y, tpCoords.z);
					tpLoc.setPitch(tpCoords.pitch);
					tpLoc.setYaw(tpCoords.yaw);
					player.teleport(tpLoc);

					WarpPortalsTeleportEvent wpTPEvent = new WarpPortalsTeleportEvent(player, preTPLocation);
					// Call WarpPortalsTeleportEvent
					Bukkit.getPluginManager().callEvent(wpTPEvent);

				} else {
					if (!hasPermission) {
						if (mAlertUserAboutPortalPermission) {
							player.sendMessage(mCC + "You do not have permission to use that WarpPortal.");
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPortalEnter(PlayerPortalEvent event) {
		// Get player involved in the event
		Player player = event.getPlayer();
		// Check if player is in a WarpPortal or normal portal
		/*
		 * If the player is in a WarpPortal, let the onPlayerBlockMoveEvent
		 * listener handle it.
		 */
		if (mPortalManager.isLocationInsidePortal(player.getLocation()) == null) {
			// The player did not enter a WarpPortal
			/*
			 * Check to see if the server is configured to allow normal portal
			 * events
			 */
			if (!mAllowNormalPortals)
				// If not allowed, cancel the event
				event.setCancelled(true);
		} else
			// If in a WarpPortal, cancel the event
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.hasBlock() && e.hasItem() && e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			mPortalManager.playerItemRightClick(e);
		}
	}

	/**
	 * Used to allow PORTAL blocks to face any direction, with a contiguous
	 * direction as its adjacent portal blocks.
	 * 
	 * @param e
	 */
	@EventHandler
	public void onBlockPhysicsEvent(BlockPhysicsEvent e) {
		if (e.getBlock().getType() == Material.PORTAL) {
			e.setCancelled(true);
		}
	}

	/**
	 * Used to keep liquid blocks from flowing outside of the portal.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFromTo(BlockFromToEvent event) {
		Block block = event.getBlock();
		if (mPortalManager.isLocationInsidePortal(block.getLocation()) != null) {
			event.setCancelled(true);
		}
	}

	/**
	 * Protects portals from getting destroyed.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (mPortalManager.isLocationInsidePortal(block.getLocation()) != null) {
			if (!event.getPlayer().hasPermission("warpportals.admin.breakblock"))
				event.setCancelled(true);
		}
	}

	/**
	 * Handles deleted or unloaded world events. Keeps WarpPortals from existing
	 * in non-existent worlds.
	 * 
	 * @param e
	 */
	@EventHandler
	public void onWorldUnloadEvent(WorldUnloadEvent e) {
		World unloadedWorld = e.getWorld();

		/* Backup existing data */
		mPortalManager.backupDataFile();

		/* Look for portals in the unloaded world */
		for (Iterator<Map.Entry<String, PortalInfo>> it = mPortalManager.mPortalDataManager.getPortalMap().entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, PortalInfo> entry = it.next();

			PortalInfo portal = entry.getValue();
			/* Test if portal exists in a non-existent or unloaded world */
			World portalWorld = portal.blockCoordArray.get(0).world;
			if (portalWorld == null || portalWorld.equals(unloadedWorld)) {
				/* Remove portal from map */
				it.remove();
				/* Alert console */
				mLogger.warning("The portal \"" + portal.name + "\" has been deleted because the world \"" + unloadedWorld.getName()
						+ "\" does not exist anymore.");
				continue;
			}
			World destWorld = portal.tpCoords.world;
			if (destWorld == null || destWorld.equals(unloadedWorld)) {
				/*
				 * Set Portal blocks to default gold state.
				 */
				Location loc = new Location(portal.blockCoordArray.get(0).world, 0, 0, 0);
				mPortalManager.mPortalCDManager.changeMaterial(Material.GOLD_BLOCK, portal.blockCoordArray, loc);

				/* Remove portal form map */
				it.remove();
				/* Alert console */
				mLogger.severe("The destination for portal \"" + portal.name + "\" is in a non-existant world \"" + unloadedWorld.getName()
						+ "\". The portal has been deactivated.");
			}
		}
		/* Look for destinations in the unloaded world */
		for (Iterator<Entry<String, CoordsPY>> it = mPortalManager.mPortalDestManager.mPortalDestMap.entrySet().iterator(); it.hasNext();) {
			Entry<String, CoordsPY> entry = it.next();

			CoordsPY destCoords = entry.getValue();
			/* Test if destination exists in a non-existent or unloaded world */
			World destWorld = destCoords.world;
			if (destWorld == null || destWorld.equals(unloadedWorld)) {
				/* Remove destination from map */
				it.remove();
				/* Alert console */
				mLogger.warning("The destination \"" + entry.getKey() + "\" has been deleted because the world \"" + unloadedWorld.getName()
						+ "\" does not exist anymore.");
			}
		}
	}

}
