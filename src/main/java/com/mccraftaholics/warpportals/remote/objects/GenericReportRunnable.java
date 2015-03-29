package com.mccraftaholics.warpportals.remote.objects;

import com.google.gson.Gson;
import com.mccraftaholics.warpportals.common.model.analytics.reports.AnalyticsReportServer;
import com.mccraftaholics.warpportals.remote.RemoteConstants;
import com.mccraftaholics.warpportals.remote.ReportManager;

public class GenericReportRunnable extends AbstractReportRunnable<AnalyticsReportServer> {

    public GenericReportRunnable(Gson gson, ReportManager reportManager) {
        super(gson, reportManager);
    }

    @Override
    public String getApiEndpoint() {
        return RemoteConstants.BASE_API_URL() + "/report/generic";
    }

    @Override
    public long getFrequency() {
        return RemoteConstants.MILLIS_PER_DAY * 7;
    }

    @Override
    public long getLastSuccessTimestamp() {
        return reportManager.reportPersistanceManager.getLastGenericReportTimestamp();
    }

    @Override
    public boolean shouldSubmitReport() {
        return true;
    }

    @Override
    public AnalyticsReportServer getReport() {
        AnalyticsReportServer report = new AnalyticsReportServer();
        reportManager.populateReport(report);
        return report;
    }

    @Override
    public void success() {
        reportManager.reportPersistanceManager.updateLastGenericReportTimestamp();
    }

    @Override
    public void error(Exception e) {

    }
}
