package com.alexwendland.warpportals.bukkit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.alexwendland.warpportals.helpers.Defaults;
import com.alexwendland.warpportals.manager.PortalManager;
import com.alexwendland.warpportals.objects.CoordsPY;

public class PlayerListener implements Listener {
	JavaPlugin mPlugin;
	PortalManager mPortalManager;
	YamlConfiguration mPortalConfig;
	String mCC;
	String mTPMessage;
	String mTPC;

	public PlayerListener(JavaPlugin plugin, PortalManager portalManager, YamlConfiguration portalConfig) {
		mPlugin = plugin;
		mPortalManager = portalManager;
		mPortalConfig = portalConfig;

		mCC = mPortalConfig.getString("portals.general.textColor", Defaults.CHAT_COLOR);
		mTPMessage = mPortalConfig.getString("portals.teleport.message", Defaults.TP_MESSAGE);
		mTPC = mPortalConfig.getString("portals.teleport.messageColor", Defaults.TP_MSG_COLOR);
	}

	long mSartTime = 0;
	long mTimeTaken = 0;

	/*
	 * @EventHandler public void onPlayerMove(final PlayerMoveEvent e) { long
	 * startTime = System.nanoTime(); Player player = e.getPlayer(); Coords
	 * tpCoords = mPortalManager.checkPlayer(player.getLocation()); if (tpCoords
	 * != null) mPlugin.getLogger().info("\n\nWoohoo!\n\n"); mTimeTaken +=
	 * System.nanoTime() - startTime; if (System.currentTimeMillis() - mSartTime
	 * > 10 * 1000) { mPlugin.getLogger().info( "Portal has added " +
	 * String.valueOf(mTimeTaken / 1000000f / 1000f) +
	 * "sec of load in the past " + String.valueOf((System.currentTimeMillis() -
	 * mSartTime) / 1000) + " of server runtime."); mTimeTaken = 0; mSartTime =
	 * System.currentTimeMillis(); } }
	 */

	/*
	 * @EventHandler public void onPortalCreate(PortalCreateEvent event,
	 * PlayerEvent player) { event.setCancelled(true); }
	 */

	@EventHandler
	public void onPortalEnter(PlayerPortalEvent event) {
		Player player = event.getPlayer();
		event.setCancelled(true);
		CoordsPY tpCoords = mPortalManager.checkPlayerLoose(player.getLocation());
		if (tpCoords == null) {
			if (player.hasPermission("warpportal.admin"))
				player.sendMessage(mCC + "Use \"/pcreate [portalname] [dest]\" to create a portal.");
		} else {
			if (player.hasPermission("warpportal.enter")) {
				player.sendMessage(mTPC + mTPMessage);
				Location tpLoc = new Location(tpCoords.world, tpCoords.x, tpCoords.y, tpCoords.z);
				tpLoc.setPitch(tpCoords.pitch);
				tpLoc.setYaw(tpCoords.yaw);
				player.teleport(tpLoc);
			}
			// } else
			// player.sendMessage(mCC +
			// "You don't have permission to enter Portals!");
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
