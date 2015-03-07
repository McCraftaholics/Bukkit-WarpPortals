package com.mccraftaholics.warpportals.manager;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mccraftaholics.warpportals.bukkit.PortalPlugin;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalCreate;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import com.mccraftaholics.warpportals.objects.PortalTool;

public class PortalManager {

	PortalPlugin mPortalPlugin;
	Logger mLogger;
	YamlConfiguration mPortalConfig;

	public PortalDestManager mPortalDestManager;
	public PersistanceManager mPersistanceManager;
	public PortalCDManager mPortalCDManager;
	public PortalDataManager mPortalDataManager;
	public PortalInteractManager mPortalInteractManager;
	public PortalToolManager mPortalToolManager;

	public PortalManager(Logger logger, YamlConfiguration portalConfig, File dataFile, PortalPlugin plugin) {
		mPortalPlugin = plugin;
		mLogger = logger;
		mPortalConfig = portalConfig;

		mPersistanceManager = new PersistanceManager(mLogger, dataFile, mPortalPlugin);
		mPortalDataManager = new PortalDataManager(this, mLogger);
		mPortalToolManager = new PortalToolManager(this, mPortalConfig);
		mPortalCDManager = new PortalCDManager(mPortalDataManager, mPortalToolManager, mPortalConfig);
		mPortalDestManager = new PortalDestManager(this, mLogger);
		mPortalInteractManager = new PortalInteractManager(this);

		loadData();
	}

	public void onDisable() {
		saveDataFile();
	}

	public void loadData() {
		mPersistanceManager.loadDataFile(mPortalDataManager, mPortalCDManager, mPortalDestManager.mPortalDestMap);
	}

	public boolean saveDataFile() {
		return mPersistanceManager.saveDataFile(mPortalDataManager.getPortalMap(), mPortalDestManager.mPortalDestMap);
	}

	public boolean saveDataFile(File mPortalDataFile) {
		return mPersistanceManager.saveDataFile(mPortalDataManager.getPortalMap(), mPortalDestManager.mPortalDestMap, mPortalDataFile);
	}

	public boolean backupDataFile() {
		return mPersistanceManager.backupDataFile(mPortalDataManager.getPortalMap(), mPortalDestManager.mPortalDestMap, null);
	}

	public void playerItemRightClick(PlayerInteractEvent e) {
		mPortalToolManager.playerItemRightClick(e);
	}

	public PortalInfo isLocationInsidePortal(Location location) {
		String portalName = mPortalInteractManager.isLocationInsidePortal(location);
		if (portalName != null)
			return getPortalInfo(portalName);
		return null;
	}

	public Set<String> getPortalNames() {
		return mPortalDataManager.getPortalNames();
	}

	public PortalInfo getPortalInfo(String portalName) {
		return mPortalDataManager.getPortalInfo(portalName);
	}

	public String getPortalName(Coords coords) {
		return mPortalDataManager.getPortalName(coords);
	}

	public void addCreating(UUID playerUUID, PortalCreate portalCreate) {
		mPortalToolManager.addCreating(playerUUID, portalCreate);
	}

	public boolean changeMaterial(Material material, List<Coords> blockCoordArray, Location location) {
		return mPortalCDManager.changeMaterial(material, blockCoordArray, location);
	}

	public void deletePortal(String portalName) {
		mPortalCDManager.deletePortal(portalName);
	}

	public void addTool(UUID playerUUID, PortalTool tool) {
		mPortalToolManager.addTool(playerUUID, tool);
	}
	
	public PortalTool getTool(UUID playerUUID) {
		return mPortalToolManager.getTool(playerUUID);
	}
	
	public void removeTool(UUID playerUUID) {
		mPortalToolManager.removeTool(playerUUID);
	}

	public void addDestination(String destName, CoordsPY destCoords) {
		mPortalDestManager.addDestination(destName, destCoords);
	}

	public void removeDestination(String destName) {
		mPortalDestManager.removeDestination(destName);
	}

	public CoordsPY getDestCoords(String destName) {
		return mPortalDestManager.getDestCoords(destName);
	}

	public Set<String> getDestinations() {
		return mPortalDestManager.getDestinations();
	}

	public String getDestinationName(CoordsPY coords) {
		return mPortalDestManager.getDestinationName(coords);
	}

}
