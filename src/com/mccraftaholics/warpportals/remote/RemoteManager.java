package com.mccraftaholics.warpportals.remote;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import com.mccraftaholics.warpportals.bukkit.PortalPlugin;
import com.mccraftaholics.warpportals.helpers.Defaults;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import com.mccraftaholics.warpportalscommon.analytics.AnalyticsReport;
import com.mccraftaholics.warpportalscommon.analytics.AnalyticsReportElapsedInstall;
import com.mccraftaholics.warpportalscommon.analytics.AnalyticsReportFirstInstall;
import com.mccraftaholics.warpportalscommon.analytics.AnalyticsReportUsage;

public class RemoteManager {

	static final String DELIMITER = "><(((º>";

	static final class API {
		// Base Url
		static final String BASE = "http://warpportals-awendland.rhcloud.com/WarpPortals-Backend/api/";
		// Analytics reporting
		static final String ANALYTICS_REPORT = BASE + "analytics/report";
		// Plugin data urls
		static final String PLUGIN_LATEST_VERSION = BASE + "plugin/latest_version";
		static final String PLUGIN_CHANGELOG = BASE + "plugin/changelog";
	}

	boolean canRunAnalytics;

	PortalPlugin mPlugin;
	File mAnalyticsFile;
	
	AnalyticsData mAnalyticsData;

	static class AnalyticsData {
		public String uuid;
		public Date lastFileUpdate = new Date(0);
		public Date lastUsageReport = new Date(0);
		public Date lastInstallReport = new Date(0);
		public int numReports;
		public AnalyticsReportUsage analyticsReportUsage;

		static AnalyticsData createNew() {
			AnalyticsData ad = new AnalyticsData();
			ad.uuid = UUID.randomUUID().toString();
			ad.lastInstallReport = new Date();
			ad.numReports = 1;
			ad.analyticsReportUsage = new AnalyticsReportUsage();
			return ad;
		}
	}

	TimerTask mHourlyTask;

	public RemoteManager(PortalPlugin plugin) {
		try {
			mPlugin = plugin;

			canRunAnalytics = mPlugin.mPortalConfig.getBoolean("portals.reporting.allowAnalytics", Defaults.ALLOW_ANALYTICS);

			if (canRunAnalytics) {
				mAnalyticsFile = new File(mPlugin.getDataFolder(), "warpportals-analytics.wpayml");
				if (!mAnalyticsFile.exists()) {
					mAnalyticsData = AnalyticsData.createNew();
					runFirstInstallReport();
					try {
						mAnalyticsFile.createNewFile();
						updateAnalyticsFile();
					} catch (IOException e) {
						// Silently fail
						return;
					}
				} else {
					try {
						String dataString = Utils.readFile(mAnalyticsFile.getAbsolutePath(), Charset.forName("UTF-8"));
						if (dataString != null) {
							try {
								Yaml yaml = new Yaml();
								mAnalyticsData = yaml.loadAs(dataString, AnalyticsData.class);
							} catch (Exception e) {
								// Silently fail
							}
						}
					} catch (IOException e) {
						// Silently fail
					}
				}

				if (mAnalyticsData.analyticsReportUsage == null)
					mAnalyticsData.analyticsReportUsage = new AnalyticsReportUsage();

				mHourlyTask = new TimerTask() {
					static final long ONE_DAY_IN_MS = 1000 * 60 * 60 * 24;

					@Override
					public void run() {
						if (System.currentTimeMillis() - mAnalyticsData.lastUsageReport.getTime() > ONE_DAY_IN_MS) {
							runUsageReport();
						}
						if (System.currentTimeMillis() - mAnalyticsData.lastInstallReport.getTime() > ONE_DAY_IN_MS * 7) {
							runInstallReport();
						}
					}
				};
				// schedule the task to run starting now and then every hour...
				new Timer().schedule(mHourlyTask, 1000 * 60, 1000 * 60 * 60);
			}
		} catch (Exception e) {
			// Silently fail
		}
	}

	public void shutdown() {
		updateAnalyticsFile();
	}

	public void incrementUseCounter() {
		mAnalyticsData.analyticsReportUsage.incrementThisHour();
	}

	boolean updateAnalyticsFile() {
		Yaml yaml = new Yaml();
		String dataString = yaml.dump(mAnalyticsData);
		if (!mAnalyticsFile.exists())
			try {
				mAnalyticsFile.createNewFile();
			} catch (IOException e) {
				// Silently fail
			}
		return Utils.writeToFile(dataString, mAnalyticsFile);
	}

	void runFirstInstallReport() {
		AnalyticsReportFirstInstall arFirstInstall = new AnalyticsReportFirstInstall();
		arFirstInstall.bukkitVersion = mPlugin.getServer().getBukkitVersion();
		arFirstInstall.maxPlayers = mPlugin.getServer().getMaxPlayers();
		arFirstInstall.numWorlds = mPlugin.getServer().getWorlds().size();
		arFirstInstall.installedPlugins = new ArrayList<String>();
		for (Plugin p : mPlugin.getServer().getPluginManager().getPlugins()) {
			arFirstInstall.installedPlugins.add(p.getName());
		}

		AnalyticsReport ar = genAnalyticsReport();
		ar.firstInstall = arFirstInstall;

		sendReport(ar);
	}

	void runUsageReport() {
		AnalyticsReport ar = genAnalyticsReport();
		ar.usage = mAnalyticsData.analyticsReportUsage;

		sendReport(ar);
		mAnalyticsData.lastUsageReport = new Date();
		mAnalyticsData.analyticsReportUsage = new AnalyticsReportUsage();
		updateAnalyticsFile();
	}

	void runInstallReport() {
		AnalyticsReportElapsedInstall aElapsedInstall = new AnalyticsReportElapsedInstall();
		aElapsedInstall.bukkitVersion = mPlugin.getServer().getBukkitVersion();
		aElapsedInstall.maxPlayers = mPlugin.getServer().getMaxPlayers();
		aElapsedInstall.numWorlds = mPlugin.getServer().getWorlds().size();
		aElapsedInstall.installedPlugins = new ArrayList<String>();
		for (Plugin p : mPlugin.getServer().getPluginManager().getPlugins()) {
			aElapsedInstall.installedPlugins.add(p.getName());
		}
		aElapsedInstall.portalCount = mPlugin.mPortalManager.mPortalDataManager.getPortalCount();
		Map<World, Integer> worldsToPortalCount = new HashMap<World, Integer>();
		Map<String, Integer> materialToPortalCount = new HashMap<String, Integer>();
		for (Entry<String, PortalInfo> entry : mPlugin.mPortalManager.mPortalDataManager.getPortalMap().entrySet()) {
			try {
				World world = entry.getValue().blockCoordArray.get(0).world;
				Integer worldCount = worldsToPortalCount.get(world);
				worldsToPortalCount.put(world, worldCount == null ? 1 : worldCount + 1);

				Location loc = new Location(world, 0, 0, 0);
				Utils.coordsToLoc(entry.getValue().blockCoordArray.get(0), loc);
				String material = loc.getBlock().getType().toString();
				Integer materialCount = materialToPortalCount.get(material);
				materialToPortalCount.put(material, materialCount == null ? 1 : materialCount + 1);
			} catch (Exception e) {
			}
		}
		aElapsedInstall.portalsPerWorld = new ArrayList<Integer>(worldsToPortalCount.values());
		aElapsedInstall.portalTypesCount = materialToPortalCount;
		aElapsedInstall.reportNum = mAnalyticsData.numReports;

		AnalyticsReport ar = genAnalyticsReport();
		ar.elapsedInstall = aElapsedInstall;

		sendReport(ar);
		mAnalyticsData.numReports++;
		mAnalyticsData.lastInstallReport = new Date();
		updateAnalyticsFile();
	}

	void sendReport(AnalyticsReport aReport) {
		try {
			Yaml yaml = new Yaml();
			final byte[] data = yaml.dump(aReport).getBytes("UTF-8");
			if (mPlugin.isEnabled()) {
				Bukkit.getScheduler().runTaskAsynchronously(mPlugin, new Runnable() {
					public void run() {
						try {
							Utils.urlPost(API.ANALYTICS_REPORT, "application/x-yaml", data);
						} catch (IOException e) {
							// Silently fail
						}
					}
				});
			}
		} catch (UnsupportedEncodingException e) {
			// Silently fail
		}
	}

	AnalyticsReport genAnalyticsReport() {
		AnalyticsReport ar = new AnalyticsReport();
		ar.serverId = mAnalyticsData.uuid;
		ar.timestamp = Utils.formatISO(new Date());
		return ar;
	}
}
