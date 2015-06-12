package com.mccraftaholics.warpportals.manager;

import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.helpers.Utils;
import com.mccraftaholics.warpportals.manager.old.OldPersistenceManager;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import junit.framework.Assert;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, UUID.class})
public class OldPersistanceManagerTest {
    Logger mockedLogger;
    UUID mockedUuid;
    String portalDataLatest;
    String portalDataLatestZeroed;
    OldPersistenceManager.PersistedData exampleData;
    Map<String, UUID> nameToUUID;

    @Before
    public void setup() {
        try {
            // Setup mocks
            mockedLogger = mock(Logger.class);
            PowerMockito.mockStatic(Bukkit.class);
            World world = mock(World.class);
            mockedUuid = PowerMockito.mock(UUID.class);
            Mockito.when(world.getUID()).thenReturn(mockedUuid);
            Mockito.when(world.getName()).thenReturn("world");
            Block mockedBlock = Mockito.mock(Block.class);
            Mockito.when(mockedBlock.getType()).thenReturn(Material.PORTAL);
            Mockito.when(world.getBlockAt(Mockito.any(Location.class))).thenReturn(mockedBlock);
            Mockito.when(Bukkit.getWorld(Mockito.any(UUID.class))).thenReturn(world);
            Mockito.when(Bukkit.getWorld("world")).thenReturn(world);

            // Prepare latest string for comparison
            portalDataLatest = Utils.readStream(getClass().getClassLoader().getResourceAsStream("portals.v0601.yml"), "UTF-8");
            portalDataLatestZeroed = portalDataLatest.replaceAll(Regex.IS_UUID, "00000000-0000-0000-0000-000000000000");

            // Example portal data
            exampleData = new OldPersistenceManager.PersistedData();
            exampleData.portals.add(
                    new PortalInfo(
                            UUID.fromString("11111111-1111-1111-1111-111111111111"),
                            "Test1",
                            Material.getMaterial("PORTAL"),
                            "$default",
                            Arrays.asList(new Coords[]{new Coords(Bukkit.getWorld("world"), 173.0, 65.0, 250.0),
                                    new Coords(Bukkit.getWorld("world"), 173.0, 64.0, 250.0)}),
                            new CoordsPY(Bukkit.getWorld("world"), 168.5, 66.0, 264.9, 7.9f, -1.3f)
                    ));
            exampleData.portals.add(
                    new PortalInfo(
                            UUID.fromString("22222222-2222-2222-2222-222222222222"),
                            "Test2",
                            Material.getMaterial("PORTAL"),
                            "$default",
                            Arrays.asList(new Coords[]{new Coords(Bukkit.getWorld("world"), 168.0, 67.0, 268.0),
                                    new Coords(Bukkit.getWorld("world"), 167.0, 67.0, 268.0)}),
                            new CoordsPY(Bukkit.getWorld("world"), 174.4, 64.0, 254.5, 2.7f, -178.5f)
                    ));
            exampleData.destinations.put("Dest1", new CoordsPY(Bukkit.getWorld("world"), 174.4, 64.0, 254.5, 2.7f, -178.5f));
            exampleData.destinations.put("Dest2", new CoordsPY(Bukkit.getWorld("world"), 168.5, 66.0, 264.9, 7.9f, -1.3f));

            nameToUUID = new HashMap<String, UUID>();
            nameToUUID.put("Test1", UUID.fromString("11111111-1111-1111-1111-111111111111"));
            nameToUUID.put("Test2", UUID.fromString("22222222-2222-2222-2222-222222222222"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean testPersistedDataEquals(OldPersistenceManager.PersistedData a, OldPersistenceManager.PersistedData b) {
        if (a == b) return true;

        if (a.needToBackup != b.needToBackup) return false;
        if (a.blocksToRevert != null ? !a.blocksToRevert.equals(b.blocksToRevert) : b.blocksToRevert != null)
            return false;
        if (a.destinations != null ? !a.destinations.equals(b.destinations) : b.destinations != null)
            return false;
        if (a.portals != null && b.portals != null) {
            Collections.sort(a.portals);
            Collections.sort(b.portals);
            if (!a.portals.equals(b.portals)) return false;
        } else {
            return false;
        }

        return true;
    }

    @Test
    public void testLatestParseMethod() {
        OldPersistenceManager.PersistedData parsedData = OldPersistenceManager.parseDataFile(portalDataLatest, mockedLogger);

        Assert.assertTrue(testPersistedDataEquals(parsedData, exampleData));
    }

    @Test
    public void testMigrateFrom0000ToLatest() {
        String portalData0000;
        try {
            portalData0000 = Utils.readStream(getClass().getClassLoader().getResourceAsStream("portals.v0.yml"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        OldPersistenceManager.PersistedData oldData = OldPersistenceManager.parseDataFile(portalData0000, mockedLogger);
        for (PortalInfo portal : oldData.portals) {
            portal.uuid = nameToUUID.get(portal.name);
        }

        Assert.assertTrue(testPersistedDataEquals(oldData, exampleData));
    }

    @Test
    public void testMigrateFrom0413AndLatest() {
        String portalData0413;
        try {
            portalData0413 = Utils.readStream(getClass().getClassLoader().getResourceAsStream("portals.v0413.yml"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        OldPersistenceManager.PersistedData oldData = OldPersistenceManager.parseDataFile(portalData0413, mockedLogger);
        for (PortalInfo portal : oldData.portals) {
            portal.uuid = nameToUUID.get(portal.name);
        }

        Assert.assertTrue(testPersistedDataEquals(oldData, exampleData));
    }
}
