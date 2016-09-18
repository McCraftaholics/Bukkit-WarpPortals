package com.mccraftaholics.warpportals.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.NullWorldException;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class PersistanceManager {

	Logger mLogger;
	File mDataFile;
	Plugin mPlugin;

	PersistanceManager(Logger logger, File file, Plugin plugin) {
		mLogger = logger;
		mDataFile = file;
		mPlugin = plugin;
	}

	public void loadDataFile(PortalDataManager portalIM, PortalCDManager portalCDM, HashMap<String, CoordsPY> destMap) {
		loadDataFile(portalIM, portalCDM, destMap, mDataFile);
	}

	public void loadDataFile(PortalDataManager portalIM, PortalCDManager portalCDM, HashMap<String, CoordsPY> destMap, File dataFile) {
		try {
			// Read portals.yml file to string "data"
			String data = Utils.readFile(dataFile.getAbsolutePath(), Charset.forName("UTF-8"));

			boolean needToBackup = false;

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
							destMap.put(destEntry.getKey(), new CoordsPY((String) destEntry.getValue()));
						} catch (NullWorldException e) {
							mLogger.severe("The destination \"" + destEntry.getKey() + "\" has been deleted because the world \"" + e.getWorldName()
									+ "\" does not exist anymore.");
							/*
							 * Destination is "deleted" because it isn't added
							 * to the destination map; effectively deleting it.
							 */
							needToBackup = true;
						}
					}
				} else if (dataGroup.getKey().equals("portals")) {
					// Portals
					for (Entry<String, ?> portalEntry : dataGroup.getValue().entrySet()) {
						LinkedHashMap<String, Object> portalEntryData = (LinkedHashMap<String, Object>) portalEntry.getValue();
						// Each portal
						PortalInfo portal = new PortalInfo();
						portal.name = portalEntry.getKey();
						try {
							for (String b : (ArrayList<String>) portalEntryData.get("blocks")) {
								portal.blockCoordArray.add(new Coords(b));
							}

							try {
								portal.tpCoords = new CoordsPY((String) portalEntryData.get("tpCoords"));

								portalIM.addPortalNoSave(portal.name, portal);
							} catch (NullWorldException e) {
								mLogger.severe("The destination for portal \"" + portal.name + "\" is in a non-existant world \"" + e.getWorldName()
										+ "\". The portal has been deactivated.");
								/*
								 * Delete portal using
								 * PortalCreateDestroyManager. Portal blocks are
								 * set to default gold state.
								 */
								Location loc = new Location(portal.blockCoordArray.get(0).world, 0, 0, 0);
								portalCDM.changeMaterial(Material.GOLD_BLOCK, portal.blockCoordArray, loc);
								/* Trigger a backup of the pre-existing data. */
								needToBackup = true;
							}
						} catch (NullWorldException e) {
							mLogger.severe("The portal \"" + portal.name + "\" has been deleted because the world \"" + e.getWorldName()
									+ "\" does not exist anymore.");
							/*
							 * Portal is "deleted" because it isn't added to the
							 * portal map; effectively deleting it.
							 */
							needToBackup = true;
						}
					}
				}
			}
			mLogger.info(String.valueOf(portalIM.getPortalCount()) + " Portals loaded!");
			mLogger.info(String.valueOf(destMap.size()) + " Destinations loaded!");

			if (needToBackup) {
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

		} catch (Exception e) {
			try {
				loadDataFileOld(portalIM, destMap, dataFile);
				mLogger.warning("WarpPortal data loaded using the old data-structure. The next save will migrate it to the latest portals.yml version.");
			} catch (Exception e2) {
				mLogger.severe("Can't load data from WarpPortal's data file!");
			}
		}
	}

	public void loadDataFileOld(PortalDataManager portalIM, HashMap<String, CoordsPY> destMap, File dataFile) throws IOException, ClassCastException {
		String data = Utils.readFile(dataFile.getAbsolutePath(), Charset.forName("UTF-8"));
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
									PortalInfo portalInfo = new PortalInfo();
									for (String attr : attrs) {
										String attrT = attr.trim();
										try {
											if (attrT.contains("tpCoords"))
												portalInfo.tpCoords = new CoordsPY(attrT.split(":")[1].trim());
											else if (attrT.contains("blocks"))
												portalInfo.parseBlockCoordArr(attrT.split(":")[1].trim());
											else
												portalInfo.name = attrT.replace(":", "").trim();
										} catch (Exception e) {
											mLogger.info("Error in Portal's data file with String \"" + attrT + "\".");
										}
									}
									portalIM.addPortalNoSave(portalInfo.name, portalInfo);
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
										destMap.put(destd[0].trim(), new CoordsPY(destd[1].trim()));
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
		}
	}

	public boolean saveDataFile(HashMap<String, PortalInfo> portalMap, HashMap<String, CoordsPY> destMap) {
		return saveDataFile(portalMap, destMap, mDataFile);
	}

	public boolean saveDataFile(HashMap<String, PortalInfo> portalMap, HashMap<String, CoordsPY> destMap, File dataFile) {
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
			for (Entry<String, PortalInfo> portal : portalMap.entrySet()) {
				// Turn portalInfo into a Map
				HashMap<String, Object> portalInfoMap = new HashMap<String, Object>();
				portalInfoMap.put("tpCoords", portal.getValue().tpCoords.toString());
				// Turn BlockCoordArray into a List<String>
				ArrayList<String> blocks = new ArrayList<String>();
				for (Coords block : portal.getValue().blockCoordArray) {
					blocks.add(block.toString());
				}
				portalInfoMap.put("blocks", blocks);

				// Put the portal data into the DataStructure Map
				dataStructure.get("portals").put(portal.getKey(), portalInfoMap);
			}

			/*
			 * Convert destMap to a simpler, less direct-representation, format
			 * for saving
			 */
			dataStructure.put("destinations", new HashMap<String, Object>());
			for (Entry<String, CoordsPY> dest : destMap.entrySet()) {
				dataStructure.get("destinations").put(dest.getKey(), dest.getValue().toString());
			}

			// Dump WarpPortal data to Yaml encoded Strings
			String yamlDataString = yaml.dump(dataStructure);

			// Write data to file
			sb.append("\n");
			sb.append(yamlDataString);

			return saveStringToFile(sb.toString(), dataFile);
		} catch (Exception e) {
			mLogger.severe("Error saving WarpPortal data! " + e.getMessage());
			return false;
		}
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

	public boolean backupDataFile(HashMap<String, PortalInfo> portalMap, HashMap<String, CoordsPY> destMap, String backupName) {
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
}
