package com.mccraftaholics.warpportals.manager;

import com.mccraftaholics.warpportals.bukkit.PortalPlugin;
import com.mccraftaholics.warpportals.helpers.persistance.WarpPortalPersistedData;
import com.mccraftaholics.warpportals.objects.*;
import com.mccraftaholics.warpportals.remote.reports.ReportManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class PortalManager {

    public PortalDestManager mPortalDestManager;
    public PortalCDManager mPortalCDManager;
    public PortalDataManager mPortalDataManager;
    public PortalInteractManager mPortalInteractManager;
    public PortalToolManager mPortalToolManager;
    private PortalPersistanceManager mPortalPersistenceManager;
    PortalPlugin mPortalPlugin;
    Logger mLogger;
    YamlConfiguration mPortalConfig;
    public ReportManager analytics;

    public PortalManager(Logger logger, YamlConfiguration portalConfig, ReportManager reportManager, PortalPlugin plugin) {
        mPortalPlugin = plugin;
        mLogger = logger;
        mPortalConfig = portalConfig;
        this.analytics = reportManager;

        mPortalPersistenceManager = new PortalPersistanceManager(logger, mPortalPlugin.getDataFolder().getAbsolutePath());
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
        WarpPortalPersistedData persistedData = mPortalPersistenceManager.loadData();

        mPortalDestManager.addDestinations(
                false,
                persistedData.destinations.toArray(
                        new DestinationInfo[persistedData.destinations.size()]
                )
        );

        mPortalDataManager.addPortalNoSave(
                persistedData.portals.toArray(
                        new PortalInfo[persistedData.portals.size()]
                )
        );
    }

    private WarpPortalPersistedData getDataToPersist() {
        WarpPortalPersistedData dataToPersist = new WarpPortalPersistedData();
        dataToPersist.setDestinations(mPortalDestManager.getDestinations());
        dataToPersist.setPortals(mPortalDataManager.getPortals());
        return dataToPersist;
    }

    public boolean saveDataFile() {
        WarpPortalPersistedData dataToPersist = getDataToPersist();
        return mPortalPersistenceManager.saveData(dataToPersist);
    }

    public boolean saveDataFile(File mPortalDataFile) {
        WarpPortalPersistedData dataToPersist = getDataToPersist();
        return mPortalPersistenceManager.saveData(dataToPersist, mPortalDataFile.getAbsolutePath());
    }

    public boolean backupDataFile() {
        WarpPortalPersistedData dataToPersist = getDataToPersist();
        return mPortalPersistenceManager.backupData(dataToPersist);
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
        mPortalDestManager.addDestination(true, destName, destCoords);
    }

    public void removeDestination(String destName) {
        mPortalDestManager.removeDestination(destName);
    }

    public CoordsPY getDestCoords(String destName) {
        return mPortalDestManager.getDestCoords(destName);
    }

    public Set<String> getDestinationNames() {
        return mPortalDestManager.getDestinationNames();
    }

    public String getDestinationName(CoordsPY coords) {
        return mPortalDestManager.getDestinationName(coords);
    }

}
