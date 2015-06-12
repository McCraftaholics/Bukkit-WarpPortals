package com.mccraftaholics.warpportals.manager;


import com.google.gson.JsonSyntaxException;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.helpers.persistance.DeadWarpPortalPersistedData;
import com.mccraftaholics.warpportals.helpers.persistance.PortalPersistenceLayer;
import com.mccraftaholics.warpportals.helpers.persistance.WarpPortalPersistedData;
import com.mccraftaholics.warpportals.manager.old.OldPersistenceManager;
import com.mccraftaholics.warpportals.objects.DestinationInfo;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class PortalPersistanceManager {

    static final String FILE_EXTENSION = "json";
    static final String MAIN_DATA_FILE_PREFIX = "warpportals_data";
    static final String MAIN_DATA_FILE_NAME = MAIN_DATA_FILE_PREFIX + FILE_EXTENSION;

    final PortalPersistenceLayer portalPersistance = new PortalPersistenceLayer();
    final Logger logger;
    final String dataFolder;

    public PortalPersistanceManager(Logger logger, String dataFolder) {
        this.logger = logger;
        this.dataFolder = dataFolder;
    }

    public WarpPortalPersistedData loadData() {
        if (new File(MAIN_DATA_FILE_NAME).exists()) {
            return loadData(MAIN_DATA_FILE_NAME);
        } else {
            final OldPersistenceManager old = new OldPersistenceManager(this.logger, this.dataFolder);
            try {
                return old.loadDataFile();
            } catch (OldPersistenceManager.ShouldBackupException) {
                logger.severe("Unable to load WarpPortals data due to invalid data structure \"" + dataFileName);
                try {
                    final String unparsedData; = readFromFile(getProperFile(MAIN_DATA_FILE_NAME));
                    backupData(unparsedData);
                } catch (IOException e) {
                    // Errors have already been reported by readFromFile()
                    // Return blank data structure
                    return new WarpPortalPersistedData();
                }
            }
        }
    }

    public WarpPortalPersistedData loadData(String dataFileName) {
        final File dataFile = getProperFile(dataFileName);

        // Read from data file
        final String unparsedData;
        try {
            unparsedData = readFromFile(dataFile);
        } catch (IOException e) {
            // Errors have already been reported by readFromFile()
            // Return blank data structure
            return new WarpPortalPersistedData();
        }

        final WarpPortalPersistedData deserializedData;
        try {
            // Deserialize WarpPortal data
            deserializedData = portalPersistance.deserialize(unparsedData);
        } catch (JsonSyntaxException e) {
            // Backup the current JSON file
            backupData(unparsedData);
            // Alert to error loading JSON
            logger.severe("Unable to load WarpPortals data due to invalid JSON structure of \"" + dataFileName + "\"");
            // Return blank data structure
            return new WarpPortalPersistedData();
        }


        /* Process loaded data to remove non-existent portals and destinations */
        DeadWarpPortalPersistedData deadItems = deserializedData.cleanupDeadEntries();
        // Process portals in non-existent worlds
        for (PortalInfo nullPortal : deadItems.portalsInNullWorlds) {
            logger.info(
                    "Removed portal \""
                            + nullPortal.name
                            + "\" because it resided in a deleted world.");
        }
        // Process destinations in non-existent worlds
        for (DestinationInfo nullDest : deadItems.destinationsInNullWorlds) {
            logger.info(
                    "Removed destination \""
                            + nullDest.name
                            + "\" because it resided in a deleted world.");
        }
        // Process portals with destinations in non-existent worlds
        for (PortalInfo portalWithNoDestination : deadItems.portalsWithNullTPCoords) {
            logger.info(
                    "Reverted portal \""
                            + portalWithNoDestination.name
                            + "\" to GOLD_BLOCK state because it teleports to a location in a deleted world."
            );
            PortalCDManager.changeMaterial(
                    Material.GOLD_BLOCK,
                    portalWithNoDestination.blocks,
                    portalWithNoDestination.blocks.get(0).toLocation(),
                    null
            );
        }


        // Report number of portals loaded
        logger.info(ChatColor.AQUA + String.valueOf(deserializedData.getPortals().size()) + " Portals loaded!");
        logger.info(ChatColor.AQUA + String.valueOf(deserializedData.getDestinations().size()) + " Destinations loaded!");

        return deserializedData;
    }

    public void saveData(WarpPortalPersistedData data) {
        saveData(data, MAIN_DATA_FILE_NAME);
    }

    public void saveData(WarpPortalPersistedData data, String dataFileName) {
        String serializedData = portalPersistance.serialize(data);
        writeToFile(serializedData, getProperFile(dataFileName));
    }

    private File getProperFile(String path) {
        File dataFile = new File(path);
        if (!dataFile.isAbsolute()) {
            return new File(this.dataFolder, path);
        }
        return dataFile;
    }

    // Standard format for backup file timestamps
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_kk-mm-ss");

    private File getBackupFile() {
        String backupName = MAIN_DATA_FILE_PREFIX
                + "_"
                + sdf.format(new Date())
                + "."
                + FILE_EXTENSION
                + ".bac";
        return new File(this.dataFolder, backupName);
    }

    private void backupData(String data) {
        writeToFile(data, getBackupFile());
    }

    private String readFromFile(File dataFile) throws IOException {
        if (!dataFile.exists()) {
            logger.info(
                    "No WarpPortal data to load from \""
                            + dataFile.getAbsolutePath()
                            + "\" because the file does not yet exist."
            );
            throw new IOException();
        }
        try {
            return Utils.readFile(dataFile.getAbsolutePath(), "UTF-8");
        } catch (IOException e) {
            logger.severe(
                    "Error loading WarpPortal data from \""
                            + dataFile.getAbsolutePath()
                            + "\".\n\t"
                            + e.getMessage()
            );
            throw new IOException();
        }
    }

    private boolean writeToFile(String data, File dataFile) {
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                logger.severe(
                        "Can't save WarpPortals data! WarpPortals does not have write access to the save location \""
                                + dataFile.getAbsolutePath() + "\"." + "\n\t"
                                + e.getMessage()
                );
                return false;
            }
        }
        if (!dataFile.canWrite()) {
            logger.severe(
                    "Can't save WarpPortals data! WarpPortals does not have write access to the save location \""
                            + dataFile.getAbsolutePath()
                            + "\"."
            );
            return false;
        }
        try {
            Utils.writeToFile(data, dataFile);
            return true;
        } catch (IOException e) {
            logger.severe("Error saving WarpPortal data!");
            return false;
        }
    }
}
