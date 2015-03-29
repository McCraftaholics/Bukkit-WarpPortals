package com.mccraftaholics.warpportals.remote;

import com.google.gson.Gson;
import com.mccraftaholics.warpportals.bukkit.PortalPlugin;
import com.mccraftaholics.warpportals.common.model.SimpleCoords;
import com.mccraftaholics.warpportals.common.model.analytics.reports.AnalyticsReportServer;
import com.mccraftaholics.warpportals.common.model.analytics.reports.AnalyticsReportUsage;
import com.mccraftaholics.warpportals.helpers.BlockCrawler;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ReportManager {
    PortalPlugin plugin;
    final boolean isEnabled;

    public ReportPersistanceManager reportPersistanceManager;
    public AnalyticsReportUsage usageReport;

    Gson gson;

    public ReportManager(PortalPlugin plugin, boolean isEnabled) {
        this.plugin = plugin;
        this.isEnabled = isEnabled;

        reportPersistanceManager = new ReportPersistanceManager();
    }

    public void initialize(Gson gson) {
        this.gson = gson;
        reportPersistanceManager.initialize(plugin, gson);
        usageReport = reportPersistanceManager.load();
    }

    public void shutdown() {
        if (isEnabled) {
            reportPersistanceManager.save();
        }
    }

    public void incrementPortalUsageThisHour(UUID portalUuid) {
        usageReport.incrementPortalUsageThisHour(portalUuid);
    }

    public void addPortalCreatedThisHour(PortalInfo portal) {
        List<SimpleCoords> simpleBlocks = BlockCrawler.simplify(portal.blocks);
        usageReport.addPortalCreatedThisHour(portal.uuid, portal.name, portal.material, portal.message, simpleBlocks);
    }

    public void populateReport(AnalyticsReportServer report) {
        report.bukkitVersion = plugin.getServer().getBukkitVersion();
        report.installedPlugins = new ArrayList<AnalyticsReportServer.InstalledPlugin>();
        for (Plugin plugin : plugin.getServer().getPluginManager().getPlugins()) {
            AnalyticsReportServer.InstalledPlugin installedPlugin = new AnalyticsReportServer.InstalledPlugin(plugin.getName(), plugin.getDescription().getVersion());
            report.installedPlugins.add(installedPlugin);
        }
        report.maxPlayers = plugin.getServer().getMaxPlayers();
        report.numWorlds = plugin.getServer().getWorlds().size();
        report.serverId = plugin.getServer().getServerId();
        report.warpPortalsVersion = plugin.getDescription().getVersion();
        report.timestamp = Utils.formatISO(new Date());

        report.activePortals = new ArrayList<UUID>();
        for (PortalInfo portal : plugin.mPortalManager.getPortals()) {
            report.activePortals.add(portal.uuid);
        }
    }

}
