package com.mccraftaholics.warpportals.manager;

import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.NullWorldException;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class OldPersistanceManager {

    Logger mLogger;
    File mDataFile;
    Plugin mPlugin;

    OldPersistanceManager(Logger logger, File file, Plugin plugin) {
        mLogger = logger;
        mDataFile = file;
        mPlugin = plugin;
    }

    private static Coords deserializeCoords(String serialized) throws Exception {
        if (serialized.matches(Coords.SERIALIZED_COORDS)) {
            return Coords.deserialize(serialized);
        } else { // Or the blocks were saved with the old World Name method
            return Coords.createFromUserInput(serialized);
        }
    }

    private static CoordsPY deserializeCoordsPY(String serialized) throws Exception {
        if (serialized.matches(CoordsPY.SERIALIZED_COORDS_PY)) {
            return CoordsPY.deserialize(serialized);
        } else { // Or the blocks were saved with the old World Name method
            return CoordsPY.createFromUserInput(serialized);
        }
    }

    public static PersistedData parseDataFile(String data, Logger logger) {
        PersistedData persistedData = new PersistedData();
        try {
            // Create YAML parsing object
            Yaml yaml = new Yaml();

            // Generate Java Data-Structure from YAML
            LinkedHashMap<String, LinkedHashMap<String, ?>> yamlData = (LinkedHashMap<String, LinkedHashMap<String, ?>>) yaml.load(data);

            // Loop through Portals then Destinations
            for (Entry<String, LinkedHashMap<String, ?>> dataGroup : yamlData.entrySet()) {
                if (dataGroup.getKey().equals("destinations")) {
                    // Destinations
                    for (Entry<String, ?> destEntry : dataGroup.getValue().entrySet()) {
                        try {
                            // Each destination
                            persistedData.destinations.put(destEntry.getKey(), deserializeCoordsPY((String) destEntry.getValue()));
                        } catch (NullWorldException e) {
                            logger.severe("The destination \"" + destEntry.getKey() + "\" has been deleted because the world associated with the identifier\"" + e.getIdentifier()
                                    + "\" does not exist anymore.");
                            /*
                             * Destination is "deleted" because it isn't added
							 * to the destination map; effectively deleting it.
							 */
                            persistedData.needToBackup = true;
                        }
                    }
                } else if (dataGroup.getKey().equals("portals")) {
                    // Portals
                    for (Entry<String, ?> portalEntry : dataGroup.getValue().entrySet()) {
                        LinkedHashMap<String, Object> portalEntryData = (LinkedHashMap<String, Object>) portalEntry.getValue();
                        // Setup portal values
                        UUID uuid;
                        String name;
                        List<Coords> blocks = new ArrayList<Coords>();
                        CoordsPY tpCoords;
                        String tpMessage;
                        Material material;
                        // Check if portal is stored in a UUID based map
                        if (portalEntry.getKey().matches(Regex.IS_UUID)) {
                            uuid = UUID.fromString(portalEntry.getKey());
                            name = (String) portalEntryData.get("name");
                        } else { // Or if it's following the old name based map
                            uuid = UUID.randomUUID();
                            name = portalEntry.getKey();
                        }

                        // Load blocks
                        try {
                            for (String b : (ArrayList<String>) portalEntryData.get("blocks")) {
                                blocks.add(deserializeCoords(b));
                            }
                        } catch (NullWorldException e) {
                            logger.severe("The portal \"" + name + "\" has been deleted because the world associated with the identifier \"" + e.getIdentifier()
                                    + "\" does not exist anymore.");
                            /*
                             * Portal is "deleted" because it isn't added to the
							 * portal map; effectively deleting it.
							 */
                            persistedData.needToBackup = true;
                        }

                        try {
                            // Load tCoords
                            tpCoords = deserializeCoordsPY((String) portalEntryData.get("tpCoords"));
                        } catch (NullWorldException e) {
                            logger.severe("The destination for portal \"" + name + "\"/" + uuid + " is in a non-existent world identified by \"" + e.getIdentifier()
                                    + "\". The portal has been deactivated.");

                            /* Schedule portal's blocks to be set to default gold state. */
                            persistedData.blocksToRevert.addAll(blocks);
                            /* Trigger a backup of the pre-existing data. */
                            persistedData.needToBackup = true;
                            // Stop loading this portal
                            continue;
                        }

                        // Load tpMessage, or default to "$default"
                        tpMessage = portalEntryData.containsKey("message") ? (String) portalEntryData.get("message") : "$default";

                        // Load material, or try to discover based off of firstBlockCoords type
                        if (portalEntryData.containsKey("material")) {
                            material = Material.getMaterial((String) portalEntryData.get("material"));
                         } else {
                            Coords firstBlock = blocks.get(0);
                            Location loc = new Location(firstBlock.world, firstBlock.x, firstBlock.y, firstBlock.z);
                            material = loc.getBlock().getType();
                        }

                        persistedData.portals.add(new PortalInfo(uuid, name, material, tpMessage, blocks, tpCoords));
                    }
                }
            }

        } catch (Exception e) {
            try {
                persistedData = parseDataFileOld(data, logger);
                logger.warning("WarpPortal data loaded using the old data-structure. The next save will migrate it to the latest portals.yml version.");
            } catch (Exception e2) {
                logger.severe("Can't load data from WarpPortal's data file!");
            }
        }
        return persistedData;
    }

    public static PersistedData parseDataFileOld(String data, Logger logger) throws Exception {
        PersistedData persistedData = new PersistedData();
        if (data != null && !data.matches("")) {
            String[] initS = data.split("\n");
            String[] groups = Utils.ymlLevelCleanup(initS, "  ");
            for (String group : groups) {
                if (group != null) {
                    if (group.trim().startsWith("#")) {
                    } else if (group.contains("portals:")) {
                        String[] eLine = group.split("\n");
                        String[] items = Utils.ymlLevelCleanup(eLine, "    ");
                        for (String item : items) {
                            if (item != null) {
                                if (!item.contains("portals:")) {
                                    String[] attrs = item.split("\n");
                                    String name = null;
                                    List<Coords> blocks = new ArrayList<Coords>();
                                    CoordsPY tpCoords = null;
                                    for (String attr : attrs) {
                                        String attrT = attr.trim();
                                        try {
                                            if (attrT.contains("tpCoords"))
                                                tpCoords = CoordsPY.createFromUserInput(attrT.split(":")[1].trim());
                                            else if (attrT.contains("blocks")) {
                                                String[] a = attrT.split(":")[1].trim().split(";");
                                                for (String i : a) {
                                                    try {
                                                        blocks.add(Coords.createFromUserInput(i));
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            } else
                                                name = attrT.replace(":", "").trim();
                                        } catch (Exception e) {
                                            logger.info("Error in Portal's data file with String \"" + attrT + "\".");
                                        }
                                    }
                                    persistedData.portals.add(new PortalInfo(UUID.randomUUID(), name, Material.getMaterial("PORTAL"), "$default", blocks, tpCoords));
                                }
                            }
                        }
                    } else if (group.contains("destinations")) {
                        String[] dests = group.split("\n  ");
                        for (String dest : dests) {
                            if (!dest.contains("destinations")) {
                                String destt = dest.trim();
                                String[] destd = destt.split(":");
                                if (destd.length == 2) {
                                    try {
                                        persistedData.destinations.put(destd[0], CoordsPY.createFromUserInput(destd[1].trim()));
                                    } catch (Exception e) {
										/*
										 * Error loading this Destination from
										 * Memory
										 */
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            throw new Exception("Invalid WarpPortal data string:\n" + data);
        }
        return persistedData;
    }

    public static String serializeData(Map<UUID, PortalInfo> portalMap, Map<String, CoordsPY> destMap, Logger logger) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("# I highly recommend that you don't edit this manually!");

            // Create YAML object for converting portal/destination data
            Yaml yaml = new Yaml();

            // Create a HashMap representing the Yaml data structure
            HashMap<String, HashMap<String, Object>> dataStructure = new HashMap<String, HashMap<String, Object>>();

			/*
			 * Convert portalMap to a simpler, less direct-representation,
			 * format for saving
			 */
            dataStructure.put("portals", new HashMap<String, Object>());
            for (PortalInfo portal : portalMap.values()) {
                // Turn portalInfo into a Map
                HashMap<String, Object> portalInfoMap = new HashMap<String, Object>();
                portalInfoMap.put("tpCoords", portal.tpCoords.serialize());
                // Turn BlockCoordArray into a List<String>
                ArrayList<String> blocks = new ArrayList<String>();
                for (Coords block : portal.blocks) {
                    blocks.add(block.serialize());
                }
                portalInfoMap.put("blocks", blocks);
                portalInfoMap.put("name", portal.name);
                portalInfoMap.put("message", portal.message);
                portalInfoMap.put("material", portal.material);

                // Put the portal data into the DataStructure Map
                dataStructure.get("portals").put(portal.uuid.toString(), portalInfoMap);
            }

			/*
			 * Convert destMap to a simpler, less direct-representation, format
			 * for saving
			 */
            dataStructure.put("destinations", new HashMap<String, Object>());
            for (Entry<String, CoordsPY> dest : destMap.entrySet()) {
                dataStructure.get("destinations").put(dest.getKey(), dest.getValue().serialize());
            }

            // Dump WarpPortal data to Yaml encoded Strings
            String yamlDataString = yaml.dump(dataStructure);

            // Write data to file
            sb.append("\n");
            sb.append(yamlDataString);

            return sb.toString();
        } catch (Exception e) {
            logger.severe("Error saving WarpPortal data! " + e.getMessage());
            return null;
        }
    }

    public void loadDataFile(PortalDataManager portalDataManager, HashMap<String, CoordsPY> destMap) {
        // Read portals.yml file to string "data"
        String data = null;
        try {
            data = Utils.readFile(mDataFile.getAbsolutePath(), "UTF-8");
        } catch (IOException e) {
            mLogger.severe("Unable to read WarpPortal's data file! Stack trace:\n" + Arrays.toString(e.getStackTrace()));
            return;
        }
        PersistedData pd = parseDataFile(data, mLogger);

        // Add the parsed portals to the PortalDataManager
        for (PortalInfo portal : pd.portals) {
            portalDataManager.addPortalNoSave(portal);
        }

        // Add the parsed destinations to the Destinations map
        destMap.putAll(pd.destinations);

        // Revert portals with invalid TP Coords to their GOLD_BLOCK state
        if (pd.blocksToRevert.size() > 0) {
            Location loc = new Location(pd.blocksToRevert.get(0).world, 0, 0, 0);
            for (Coords block : pd.blocksToRevert) {
                loc.setX(block.x);
                loc.setY(block.y);
                loc.setZ(block.z);
                loc.getBlock().setType(Material.GOLD_BLOCK);
            }
        }

        // If a backup was requested
        if (pd.needToBackup) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_kk-mm-ss");
                String backupName = "portals_" + sdf.format(new Date()) + ".bac";
                File backupFile = new File(mPlugin.getDataFolder(), backupName);
                backupFile.createNewFile();

                StringBuilder sb = new StringBuilder();
                sb.append("# I highly recommend that you don't edit this manually!");
                sb.append("Backup was created due to a world being deleted.");
                sb.append(data);

                saveStringToFile(sb.toString(), backupFile);
            } catch (Exception e) {
                mLogger.severe("Can't backup WarpPortals data! " + e.getMessage());
            }
        }

        // Report number of portals loaded
        mLogger.info(String.valueOf(portalDataManager.getPortalCount()) + " Portals loaded!");
        mLogger.info(String.valueOf(destMap.size()) + " Destinations loaded!");
    }

    public boolean saveDataFile(Map<UUID, PortalInfo> portalMap, Map<String, CoordsPY> destMap, File saveFile) {
        String serializedData = serializeData(portalMap, destMap, mLogger);
        if (serializedData == null) {
            return false;
        }
        return saveStringToFile(serializedData, saveFile);
    }

    public boolean saveDataFile(Map<UUID, PortalInfo> portalMap, Map<String, CoordsPY> destMap) {
        return saveDataFile(portalMap, destMap, mDataFile);
    }

    private boolean saveStringToFile(String data, File dataFile) {
        boolean rtn = true;
        if (dataFile.canWrite()) {
            boolean writeSuccess = Utils.writeToFile(data, dataFile);
            if (!writeSuccess)
                mLogger.severe("Error saving WarpPortal data!");
        } else {
            mLogger.severe("Can't save WarpPortals data! WarpPortals does not have write access to the save location \"" + dataFile.getAbsolutePath() + "\".");
            rtn = false;
        }
        return rtn;
    }

    public boolean backupDataFile(Map<UUID, PortalInfo> portalMap, Map<String, CoordsPY> destMap, String backupName) {
        try {
            if (backupName == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_kk-mm-ss");
                backupName = "portals_" + sdf.format(new Date()) + ".yml.bac";
            }
            File backupFile = new File(mPlugin.getDataFolder(), backupName);
            backupFile.createNewFile();
            return saveDataFile(portalMap, destMap, backupFile);
        } catch (IOException e) {
            mLogger.severe("Can't backup WarpPortals data! " + e.getMessage());
            return false;
        }
    }

    public static class PersistedData {
        public List<PortalInfo> portals = new LinkedList<PortalInfo>();
        public List<Coords> blocksToRevert = new LinkedList<Coords>();
        public Map<String, CoordsPY> destinations = new LinkedHashMap<String, CoordsPY>();
        public boolean needToBackup;
    }
}
