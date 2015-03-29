package com.mccraftaholics.warpportals.remote.objects;

import com.google.gson.Gson;
import com.mccraftaholics.warpportals.common.model.analytics.reports.AnalyticsReportUsage;
import com.mccraftaholics.warpportals.remote.RemoteConstants;
import com.mccraftaholics.warpportals.remote.ReportManager;

public class UsageReportRunnable extends AbstractReportRunnable<AnalyticsReportUsage> {
    public UsageReportRunnable(Gson gson, ReportManager reportManager) {
        super(gson, reportManager);
    }

    @Override
    public String getApiEndpoint() {
        return RemoteConstants.BASE_API_URL + "/report/usage";
    }

    @Override
    public long getFrequency() {
        return RemoteConstants.MILLIS_PER_DAY;
    }

    @Override
    public long getLastSuccessTimestamp() {
        return reportManager.reportPersistanceManager.getLastUsageReportTimestamp();
    }

    @Override
    public boolean shouldSubmitReport() {
        return !reportManager.usageReport.getPerHour().isEmpty();
    }

    @Override
    public AnalyticsReportUsage getReport() {
        return reportManager.usageReport;
    }

    @Override
    public void success() {
        reportManager.reportPersistanceManager.updateLastUsageReportTimestamp();
        reportManager.usageReport.getPerHour().clear();
    }

    @Override
    public void error(Exception e) {

    }
}
