package com.mccraftaholics.warpportals.remote.reports;

import com.google.gson.Gson;
import com.mccraftaholics.warpportals.helpers.Utils;

public abstract class AbstractReportRunnable<T> implements Runnable {
    Gson gson;
    ReportManager reportManager;

    public AbstractReportRunnable(Gson gson, ReportManager reportManager) {
        this.gson = gson;
        this.reportManager = reportManager;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - getLastSuccessTimestamp() > getFrequency()) {
            try {
                if (shouldSubmitReport()) {
                    Utils.urlPost(getApiEndpoint(), "application/json", gson.toJson(getReport()).getBytes("UTF-8"));
                    success();
                }
            } catch (Exception e) {
                error(e);
            }
        }
    }

    public abstract String getApiEndpoint();

    public abstract long getFrequency();

    public abstract long getLastSuccessTimestamp();

    public abstract boolean shouldSubmitReport();

    public abstract T getReport();

    public abstract void success();

    public abstract void error(Exception e);
}
