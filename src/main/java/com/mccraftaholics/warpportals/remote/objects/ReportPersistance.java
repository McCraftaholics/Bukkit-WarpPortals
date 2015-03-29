package com.mccraftaholics.warpportals.remote.objects;

import com.mccraftaholics.warpportals.common.model.analytics.reports.AnalyticsReportUsage;

public class ReportPersistance {
    public long timestampGenericReport;
    public long timestampUsageReport;
    public AnalyticsReportUsage usageReport;

    public ReportPersistance() {
    }

    public static ReportPersistance createNew() {
        ReportPersistance rpd = new ReportPersistance();
        rpd.usageReport = new AnalyticsReportUsage();
        return rpd;
    }
}
