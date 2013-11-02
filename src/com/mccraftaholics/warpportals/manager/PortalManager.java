package com.mccraftaholics.warpportals.manager;

import java.io.File;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalCreate;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class PortalManager {

	Logger mLogger;
	YamlConfiguration mPortalConfig;

	public PortalDestManager mPortalDestManager;
	public PersistanceManager mPersistanceManager;
	public PortalCDManager mPortalCDManager;
	public PortalDataManager mPortalDataManager;
	public PortalInteractManager mPortalInteractManager;

	public PortalManager(Logger logger, YamlConfiguration portalConfig, File dataFile) {
		mLogger = logger;
		mPortalConfig = portalConfig;

		mPersistanceManager = new PersistanceManager(mLogger, dataFile);
		mPortalDataManager = new PortalDataManager(this, mLogger);
		mPortalCDManager = new PortalCDManager(mPortalDataManager, mPortalConfig);
		mPortalDestManager = new PortalDestManager(this, mLogger);
		mPortalInteractManager = new PortalInteractManager(this);

		loadData();
	}

	public void onDisable() {
		saveDataFile();
	}

	public void loadData() {
		mPersistanceManager.loadDataFile(mPortalDataManager, mPortalDestManager.mPortalDestMap);
	}

	public boolean saveDataFile() {
		return mPersistanceManager.saveDataFile(mPortalDataManager.getPortalMap(), mPortalDestManager.mPortalDestMap);
	}

	public boolean saveDataFile(File mPortalDataFile) {
		return mPersistanceManager.saveDataFile(mPortalDataManager.getPortalMap(), mPortalDestManager.mPortalDestMap, mPortalDataFile);
	}

	public void playerItemRightClick(PlayerInteractEvent e) {
		mPortalCDManager.playerItemRightClick(e);
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

	public void addCreating(String playerName, PortalCreate portalCreate) {
		mPortalCDManager.addCreating(playerName, portalCreate);
	}

	public void deletePortal(String portalName) {
		mPortalCDManager.deletePortal(portalName);
	}

	public void addDeleting(String playerName, Material type) {
		mPortalCDManager.addDeleting(playerName, type);
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
