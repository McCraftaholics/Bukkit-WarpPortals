package com.mccraftaholics.warpportals.remote;

import com.google.gson.Gson;
import com.mccraftaholics.warpportals.common.model.analytics.reports.AnalyticsReportUsage;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.remote.objects.ReportPersistance;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ReportPersistanceManager {
    Gson gson;
    File analyticsFile;

    ReportPersistance persistedReport;

    public void initialize(Plugin plugin, Gson gson) {
        this.gson = gson;
        analyticsFile = new File(plugin.getDataFolder(), "analytics.json");
    }

    public AnalyticsReportUsage load() {
        if (!analyticsFile.exists()) {
            // First run
            try {
                analyticsFile.createNewFile();
            } catch (IOException e) {
                // Well that's unfortunate that we can't create a new file
            }
            persistedReport = ReportPersistance.createNew();
        } else {
            // Subsequent run
            try {
                persistedReport = gson.fromJson(Utils.readFile(analyticsFile.getAbsolutePath(), "UTF-8"), ReportPersistance.class);
                if (persistedReport == null) {
                    throw new NullPointerException();
                }
            } catch (Exception e) {
                persistedReport = ReportPersistance.createNew();
            }
        }
        return persistedReport.usageReport;
    }

    public long getLastUsageReportTimestamp() {
        return persistedReport.timestampUsageReport;
    }

    public long getTimeSinceUsageReport() {
        return System.currentTimeMillis() - getLastUsageReportTimestamp();
    }

    public long getLastGenericReportTimestamp() {
        return persistedReport.timestampGenericReport;
    }

    public long getTimeSinceGenericReport() {
        return System.currentTimeMillis() - getLastGenericReportTimestamp();
    }

    public void updateLastUsageReportTimestamp() {
        persistedReport.timestampUsageReport = System.currentTimeMillis();
    }

    public void updateLastGenericReportTimestamp() {
        persistedReport.timestampGenericReport = System.currentTimeMillis();
    }

    public void save() {
        boolean writeSucceeded = Utils.writeToFile(gson.toJson(persistedReport), analyticsFile);
        if (!writeSucceeded) {
            // That's too bad that we can't persist report data :(
        }
    }
}
