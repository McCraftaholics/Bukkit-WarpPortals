package com.mccraftaholics.warpportals.manager;

import java.io.File;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalCreate;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class PortalManager {

	Logger mLogger;
	YamlConfiguration mPortalConfig;

	public PortalDestManager mPortalDestManager;
	public PersistanceManager mPersistanceManager;
	public PortalCDManager mPortalCDManager;
	public PortalInteractManager mPortalInteractManager;

	public PortalManager(Logger logger, YamlConfiguration portalConfig, File dataFile) {
		mLogger = logger;
		mPortalConfig = portalConfig;

		mPersistanceManager = new PersistanceManager(mLogger, dataFile);
		mPortalInteractManager = new PortalInteractManager(mLogger);
		mPortalCDManager = new PortalCDManager(mPortalInteractManager, mPortalConfig);
		mPortalDestManager = new PortalDestManager(mLogger);

		loadDataFromYML();
	}

	public void onDisable() {
		saveDataToYML();
	}

	public void loadDataFromYML() {
		mPersistanceManager.loadDataFile(mPortalInteractManager.mPortalMap, mPortalDestManager.mPortalDestMap);
	}

	public void saveDataToYML() {
		mPersistanceManager.saveDataFile(mPortalInteractManager.mPortalMap, mPortalDestManager.mPortalDestMap);
	}

	public void playerItemRightClick(PlayerInteractEvent e) {
		mPortalCDManager.playerItemRightClick(e);
	}

	public CoordsPY checkPlayerLoose(Location location) {
		return mPortalInteractManager.checkPlayerLoose(location);
	}

	public void addCreating(String playerName, PortalCreate portalCreate) {
		mPortalCDManager.addCreating(playerName, portalCreate);
	}

	public Set<String> getPortalNames() {
		return mPortalInteractManager.getPortalNames();
	}

	public PortalInfo getPortalInfo(String portalName) {
		return mPortalInteractManager.getPortalInfo(portalName);
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

	public boolean saveDataFile(File mPortalDataFile) {
		return mPersistanceManager.saveDataFile(mPortalInteractManager.mPortalMap, mPortalDestManager.mPortalDestMap, mPortalDataFile);
	}

	public boolean saveDataFile() {
		return mPersistanceManager.saveDataFile(mPortalInteractManager.mPortalMap, mPortalDestManager.mPortalDestMap);
	}

	public CoordsPY getDestCoords(String destName) {
		return mPortalDestManager.getDestCoords(destName);
	}

	public Set<String> getDestinations() {
		return mPortalDestManager.getDestinations();
	}

}
