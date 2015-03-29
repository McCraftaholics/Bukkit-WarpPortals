package com.mccraftaholics.warpportals.remote;

import com.google.gson.Gson;
import com.mccraftaholics.warpportals.common.model.SimpleCoords;
import com.mccraftaholics.warpportals.common.model.SimplePortal;
import com.mccraftaholics.warpportals.common.model.analytics.reports.AnalyticsReportUsage;
import com.mccraftaholics.warpportals.helpers.BlockCrawler;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import com.mccraftaholics.warpportals.remote.reports.ReportPersistance;
import junit.framework.Assert;
import org.bukkit.Material;
import org.bukkit.World;
import org.mockito.Mockito;

import java.util.*;

public class ReportManagerPersistanceTest {

    String EXPECTED_JSON = "{\"timestampGenericReport\":0,\"timestampUsageReport\":0,\"usageReport\":{\"perHour\":{\"396551\":{\"portalUses\":{\"28286ad9-a6ec-465a-9397-a294c4c18208\":1},\"portalsCreated\":{\"28286ad9-a6ec-465a-9397-a294c4c18208\":{\"uuid\":\"28286ad9-a6ec-465a-9397-a294c4c18208\",\"name\":\"test\",\"material\":\"PORTAL\",\"message\":\"Hello!\",\"blocks\":[{\"x\":-1.0,\"y\":0.0,\"z\":0.0},{\"x\":0.0,\"y\":0.0,\"z\":0.0},{\"x\":1.0,\"y\":0.0,\"z\":0.0}]}}}}}}";

    public PortalInfo createPortalInfo() {
        UUID portalId = UUID.fromString("28286ad9-a6ec-465a-9397-a294c4c18208");
        World world = Mockito.mock(World.class);
        List<Coords> blocks = new ArrayList<Coords>();
        blocks.add(new Coords(world, 0, 0, 0));
        blocks.add(new Coords(world, -1, 0, 0));
        blocks.add(new Coords(world, 1, 0, 0));
        PortalInfo portal = new PortalInfo(portalId, "test", Material.getMaterial("PORTAL"), "Hello!", blocks, new CoordsPY(world, 0, 0, 0, 0f, 0f));
        return portal;
    }

    @org.junit.Test
    public void testGsonForReportPersistance() throws Exception {
        Gson gson = new Gson();
        ReportPersistance reportPersistance = ReportPersistance.createNew();
        SimplePortal portal = createPortalInfo();
        reportPersistance.usageReport.incrementPortalUsageThisHour(portal.uuid);
        List<SimpleCoords> simpleBlocks = BlockCrawler.simplify(portal.blocks);
        reportPersistance.usageReport.addPortalCreatedThisHour(portal.uuid, portal.name, portal.material, portal.message, simpleBlocks);
        String json = gson.toJson(reportPersistance);
        String timeStampedExpectedJson = EXPECTED_JSON.replace("396551", Long.toString(AnalyticsReportUsage.getCurrentHour()));
        Assert.assertEquals(timeStampedExpectedJson, json);
    }
}
