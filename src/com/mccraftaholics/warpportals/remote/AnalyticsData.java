package com.mccraftaholics.warpportals.remote;

import java.util.Date;
import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import com.mccraftaholics.warpportalscommon.analytics.AnalyticsReportUsage;

public class AnalyticsData {

//	static final String DELIMITER = "\n--- ---\n";

	public String uuid;
	public Date lastFileUpdate;
	public Date lastUsageReport;
	public Date lastInstallReport;
	public int numReports;
	public AnalyticsReportUsage analyticsReportUsage;

	static AnalyticsData createNew() {
		AnalyticsData ad = new AnalyticsData();
		ad.uuid = UUID.randomUUID().toString();
		ad.lastInstallReport = new Date(0);
		ad.lastUsageReport = new Date(0);
		ad.lastFileUpdate = new Date(0);
		ad.numReports = 1;
		ad.analyticsReportUsage = new AnalyticsReportUsage();
		return ad;
	}

	/*String serialize() {
		StringBuilder sb = new StringBuilder();
		sb.append(uuid).append(DELIMITER);
		sb.append(Utils.formatISO(lastFileUpdate)).append(DELIMITER);
		sb.append(Utils.formatISO(lastUsageReport)).append(DELIMITER);
		sb.append(Utils.formatISO(lastInstallReport)).append(DELIMITER);
		sb.append(numReports).append(DELIMITER);
		sb.append(new Yaml().dump(analyticsReportUsage));
		return sb.toString();
	}*/
	String serialize() {
		return new Yaml().dump(this);
	}

	/*private static class Split {
		String[] pieces;
		int i = 0;

		Split(String string, String regex) {
			pieces = string.split(regex);
		}

		String next() {
			return pieces[i++];
		}
	}*/

	/*static AnalyticsData deserialize(String data) {
		AnalyticsData ad = new AnalyticsData();
		Split pieces = new Split(data, DELIMITER);
		ad.uuid = pieces.next();
		ad.lastFileUpdate = Utils.parseIsoTime(pieces.next());
		ad.lastUsageReport = Utils.parseIsoTime(pieces.next());
		ad.lastInstallReport = Utils.parseIsoTime(pieces.next());
		ad.numReports = Integer.parseInt(pieces.next());
		Yaml yaml = new Yaml();
		String tzt = pieces.next();
		ad.analyticsReportUsage = yaml.loadAs(tzt, AnalyticsReportUsage.class);
		return ad;
	}*/
	static AnalyticsData deserialize(String data) {
		return new Yaml().loadAs(data, AnalyticsData.class);
	}
}