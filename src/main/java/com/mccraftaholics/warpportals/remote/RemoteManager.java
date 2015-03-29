package com.mccraftaholics.warpportals.remote;

import com.google.gson.Gson;
import com.mccraftaholics.warpportals.bukkit.PortalPlugin;
import com.mccraftaholics.warpportals.remote.objects.GenericReportRunnable;
import com.mccraftaholics.warpportals.remote.objects.UsageReportRunnable;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class RemoteManager {
    PortalPlugin plugin;
    public final boolean isEnabled;

    public ReportManager reportManager;
    Gson gson;
    List<BukkitTask> asyncTasks;

    public RemoteManager(PortalPlugin plugin, boolean isEnabled) {
        this.plugin = plugin;
        this.isEnabled = isEnabled;

        this.reportManager = new ReportManager(plugin, isEnabled);
        this.asyncTasks = new ArrayList<BukkitTask>();
    }

    public void initialize() {
        gson = new Gson();
        reportManager.initialize(gson);

        if (reportManager.isEnabled) {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            // Run report submission hourly
            asyncTasks.add(
                    scheduler.runTaskTimerAsynchronously(
                            plugin,
                            new GenericReportRunnable(gson, reportManager),
                            RemoteConstants.TICKS_PER_HOUR / 60 * /*1*/0,
                            RemoteConstants.TICKS_PER_HOUR
                    )
            );
            asyncTasks.add(
                    scheduler.runTaskTimerAsynchronously(
                            plugin,
                            new UsageReportRunnable(gson, reportManager),
                            RemoteConstants.TICKS_PER_HOUR / 60 * /*1*/0,
                            RemoteConstants.TICKS_PER_HOUR
                    )
            );
        }
    }

    public void shutdown() {
        reportManager.shutdown();

        for (BukkitTask task : asyncTasks) {
            task.cancel();
        }
    }

}
