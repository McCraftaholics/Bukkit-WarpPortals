package com.mccraftaholics.warpportals.manager;

import com.mccraftaholics.warpportals.bukkit.PortalPlugin;
import com.mccraftaholics.warpportals.objects.*;
import com.mccraftaholics.warpportals.remote.reports.ReportManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class PortalManager {

    public PortalDestManager mPortalDestManager;
    public PortalCDManager mPortalCDManager;
    public PortalDataManager mPortalDataManager;
    public PortalInteractManager mPortalInteractManager;
    public PortalToolManager mPortalToolManager;
    PortalPlugin mPortalPlugin;
    Logger mLogger;
    YamlConfiguration mPortalConfig;
    public ReportManager analytics;

    public PortalManager(Logger logger, YamlConfiguration portalConfig, ReportManager reportManager, PortalPlugin plugin) {
        mPortalPlugin = plugin;
        mLogger = logger;
        mPortalConfig = portalConfig;
        this.analytics = reportManager;

        mPortalDataManager = new PortalDataManager(this, mLogger);
        mPortalToolManager = new PortalToolManager(this, mPortalConfig);
        mPortalCDManager = new PortalCDManager(mPortalDataManager, mPortalToolManager, analytics, mPortalConfig);
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

    public boolean backupDataFile() {
        return mPersistanceManager.backupDataFile(mPortalDataManager.getPortalMap(), mPortalDestManager.mPortalDestMap, null);
    }

    public void playerItemRightClick(PlayerInteractEvent e) {
        mPortalToolManager.playerItemRightClick(e);
    }

    public PortalInfo isLocationInsidePortal(Location location) {
        return mPortalInteractManager.isLocationInsidePortal(location);
    }

    public Collection<PortalInfo> getPortals() {
        return mPortalDataManager.getPortals();
    }

    public PortalInfo getPortal(UUID portalUuid) {
        return mPortalDataManager.getPortal(portalUuid);
    }

    public PortalInfo getPortal(Coords coords) {
        return mPortalDataManager.getPortal(coords);
    }

    public PortalInfo getPortal(String name) {
        return mPortalDataManager.getPortal(name);
    }

    public boolean isPortalNameUsed(String portalName) {
        return mPortalDataManager.isNameUsed(portalName);
    }

    public void addCreating(UUID playerUUID, PortalCreate portalCreate) {
        mPortalToolManager.addCreating(playerUUID, portalCreate);
    }

    public boolean changeMaterial(Material material, List<Coords> blockCoordArray, Location location, Byte data) {
        return mPortalCDManager.changeMaterial(material, blockCoordArray, location, data);
    }

    public boolean deletePortal(UUID portalUuid) {
        return mPortalCDManager.deletePortal(portalUuid);
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
