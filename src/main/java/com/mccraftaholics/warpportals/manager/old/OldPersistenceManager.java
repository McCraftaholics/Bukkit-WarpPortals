package com.mccraftaholics.warpportals.manager.old;

import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.helpers.persistance.WarpPortalPersistedData;
import com.mccraftaholics.warpportals.manager.PortalDataManager;
import com.mccraftaholics.warpportals.objects.*;
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

public class OldPersistenceManager {

    static final String OLD_DATA_FILE_NAME = "portals.yml";

    Logger mLogger;
    String mDataFolder;

    OldPersistenceManager(Logger logger, String dataFolder) {
        mLogger = logger;
        mDataFolder = dataFolder;
    }

    private static Coords deserializeCoords(String serialized) throws Exception {
        if (serialized.matches(OldPersistanceCoords.SERIALIZED_COORDS)) {
            return OldPersistanceCoords.deserialize(serialized);
        } else { // Or the blocks were saved with the old World Name method
            return OldPersistanceCoords.createFromUserInput(serialized);
        }
    }

    private static CoordsPY deserializeCoordsPY(String serialized) throws Exception {
        if (serialized.matches(OldPersistanceCoordsPY.SERIALIZED_COORDS_PY)) {
            return OldPersistanceCoordsPY.deserialize(serialized);
        } else { // Or the blocks were saved with the old World Name method
            return OldPersistanceCoordsPY.createFromUserInput(serialized);
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
                                                tpCoords = OldPersistanceCoordsPY.createFromUserInput(attrT.split(":")[1].trim());
                                            else if (attrT.contains("blocks")) {
                                                String[] a = attrT.split(":")[1].trim().split(";");
                                                for (String i : a) {
                                                    try {
                                                        blocks.add(OldPersistanceCoords.createFromUserInput(i));
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
                                        persistedData.destinations.put(destd[0], OldPersistanceCoordsPY.createFromUserInput(destd[1].trim()));
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

    public void loadDataFile() throws ShouldBackupException {
        // Read portals.yml file to string "data"
        String data = null;
        try {
            File dataFile = new File(this.mDataFolder, OLD_DATA_FILE_NAME);
            data = Utils.readFile(dataFile.getAbsolutePath(), "UTF-8");
        } catch (IOException e) {
            mLogger.severe("Unable to read WarpPortal's data file! Stack trace:\n" + Arrays.toString(e.getStackTrace()));
            return;
        }
        PersistedData pd = parseDataFile(data, mLogger);

        // If a backup was requested
        if (pd.needToBackup) {
            throw new ShouldBackupException();
        }
    }

    public static class ShouldBackupException extends Exception {

    }

    public static class PersistedData {
        public List<PortalInfo> portals = new LinkedList<PortalInfo>();
        public List<Coords> blocksToRevert = new LinkedList<Coords>();
        public Map<String, CoordsPY> destinations = new LinkedHashMap<String, CoordsPY>();
        public boolean needToBackup;

        public WarpPortalPersistedData toModernFormat() {
            WarpPortalPersistedData modern = new WarpPortalPersistedData();
            modern.setPortals(portals);
            List<DestinationInfo> destInfos = new LinkedList<DestinationInfo>();
            for (Entry<String, CoordsPY> dest : destinations.entrySet()) {
                destInfos.add(new DestinationInfo(dest.getKey(), dest.getValue()));
            }
            modern.setDestinations(destInfos);
            return modern;
        }
    }
}
