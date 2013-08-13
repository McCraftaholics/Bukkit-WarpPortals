package com.mccraftaholics.warpportals.manager;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mccraftaholics.warpportals.helpers.BlockCrawler;
import com.mccraftaholics.warpportals.helpers.Defaults;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.helpers.BlockCrawler.MaxRecursionException;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalCreate;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class PortalCDManager {

	Logger mLogger;
	PortalInteractManager mPIM;
	YamlConfiguration mPortalConfig;
	String mCC;

	public HashMap<String, PortalCreate> mPlayerPortalCreateMap = new HashMap<String, PortalCreate>();
	public HashMap<String, Material> mPlayerPortalDeleteMap = new HashMap<String, Material>();

	public PortalCDManager(PortalInteractManager pim, YamlConfiguration portalConfig) {
		mPIM = pim;
		mPortalConfig = portalConfig;
		mCC = mPortalConfig.getString("portals.general.textColor", Defaults.CHAT_COLOR);
	}

	public void addCreating(String playerName, PortalCreate portalCreate) {
		mPlayerPortalCreateMap.put(playerName, portalCreate);
	}

	public void addDeleting(String playerName, Material type) {
		mPlayerPortalDeleteMap.put(playerName, type);
	}

	public void playerItemRightClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		PortalCreate portalCreate = mPlayerPortalCreateMap.get(player.getName());
		Material delTool = mPlayerPortalDeleteMap.get(player.getName());
		if (portalCreate != null && portalCreate.blockType == player.getItemInHand().getType()) {
			possibleCreatePortal(e.getClickedBlock(), player, portalCreate);
		} else if (delTool != null && delTool == player.getItemInHand().getType())
			possibleDeletePortal(e.getClickedBlock(), player);
	}

	private void possibleDeletePortal(Block block, Player player) {
		if (block.getType() == Material.PORTAL) {
			deletePortal(block.getLocation());
			mPlayerPortalDeleteMap.remove(player.getName());
		} else
			player.sendMessage("Right click on the Portal that you want to delete");
	}

	public void deletePortal(String name) {
		try {
			Location loc = new Location(mPIM.getPortalInfo(name).tpCoords.world, 0, 0, 0);
			for (Coords crd : mPIM.getPortalInfo(name).blockCoordArray) {
				try {
					loc.setWorld(crd.world);
					loc.setX(crd.x);
					loc.setY(crd.y);
					loc.setZ(crd.z);
					loc.getBlock().setType(Material.GOLD_BLOCK);
				} catch (Exception e) {
					// Error changing portal block
				}
			}
		} catch (Exception e) {
			// Error changing portal to gold block
		}
		mPIM.removePortal(name);
	}

	private void deletePortal(Location loc) {
		String delPortalName = "~|~";
		for (String portalName : mPIM.getPortalNames()) {
			for (Coords crd : mPIM.getPortalInfo(portalName).blockCoordArray) {
				if (loc.getX() == crd.x && loc.getY() == crd.y && loc.getZ() == crd.z) {
					delPortalName = portalName;
					break;
				}
			}
		}
		if (!delPortalName.matches("~|~"))
			deletePortal(delPortalName);
	}

	private void possibleCreatePortal(Block block, Player player, PortalCreate portalCreate) {
		if (block.getType() == Material.GOLD_BLOCK || block.getType() == Material.PORTAL) {
			if (mPIM.mPortalMap.get(portalCreate.portalName) == null) {
				boolean isCreationSuccess = createPortal(player, block, portalCreate.portalName, portalCreate.tpCoords);
				if (isCreationSuccess)
					mPlayerPortalCreateMap.remove(player.getName());
			} else
				player.sendMessage("A Portal with the name \"" + portalCreate.portalName + "\" already exists.");
		} else {
			player.sendMessage("The Portal should be made out of Gold Blocks or Portal Blocks originally");
		}
	}

	public boolean createPortal(CommandSender sender, Block block, String portalName, CoordsPY tpCoords) {
		if (portalName.matches(Regex.PORTAL_DEST_NAME)) {
			PortalInfo newPortalInfo = new PortalInfo();
			newPortalInfo.tpCoords = tpCoords;
			int maxPortalSize = mPortalConfig.getInt("portals.create.maxSize", BlockCrawler.DEFAULT_MAX_SIZE);
			BlockCrawler blockSpider = new BlockCrawler(maxPortalSize);
			try {
				blockSpider.start(block, newPortalInfo.blockCoordArray);
				Location loc = block.getLocation();
				for (Coords crd : newPortalInfo.blockCoordArray) {
					loc.setX(crd.x);
					loc.setY(crd.y);
					loc.setZ(crd.z);
					loc.getBlock().setType(Material.GLOWSTONE);
				}
				for (Coords crd : newPortalInfo.blockCoordArray) {
					loc.setX(crd.x);
					loc.setY(crd.y);
					loc.setZ(crd.z);
					loc.getBlock().setType(Material.PORTAL);
				}
				mPIM.addPortal(portalName, newPortalInfo);
				sender.sendMessage(mCC + "\"" + portalName + "\" created and linked to " + tpCoords.toNiceString());
				return true;
			} catch (MaxRecursionException e) {
				sender.sendMessage(mCC + "Portal \"" + portalName + "\" could not be created because it was larger than the max Portal size of "
						+ String.valueOf(maxPortalSize) + ".");
			}
		} else {
			sender.sendMessage(mCC + "There was an error using the Portal name. It wasn't a valid alpha-numeric string.");
		}
		return false;
	}
}
