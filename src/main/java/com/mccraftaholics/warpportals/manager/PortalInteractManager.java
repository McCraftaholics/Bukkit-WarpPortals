package com.mccraftaholics.warpportals.manager;

import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.PortalInfo;
import org.bukkit.Location;

import java.util.UUID;

public class PortalInteractManager {

    PortalManager mPortalManager;

    public PortalInteractManager(PortalManager portalManager) {
        mPortalManager = portalManager;
    }

    /**
     * Test whether or not the location is currently inside a portal.
     *
     * @param loc Location to test
     * @return PortalInfo portal containing the location OR null if the player is not in a portal.
     */
    public PortalInfo isLocationInsidePortal(Location loc) {
        Coords coords = Coords.createCourse(loc);
        return mPortalManager.getPortal(coords);
    }

}
