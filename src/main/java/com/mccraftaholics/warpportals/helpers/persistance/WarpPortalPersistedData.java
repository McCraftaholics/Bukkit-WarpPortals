package com.mccraftaholics.warpportals.helpers.persistance;

import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.DestinationInfo;
import com.mccraftaholics.warpportals.objects.PortalInfo;

import javax.sound.sampled.Port;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WarpPortalPersistedData {
    public Collection<PortalInfo> portals = new LinkedList<PortalInfo>();
    public Collection<DestinationInfo> destinations = new LinkedList<DestinationInfo>();

    public Collection<PortalInfo> getPortals() {
        return portals;
    }

    public WarpPortalPersistedData setPortals(Collection<PortalInfo> portals) {
        this.portals = portals;
        return this;
    }

    public Collection<DestinationInfo> getDestinations() {
        return destinations;
    }

    public WarpPortalPersistedData setDestinations(Collection<DestinationInfo> destinations) {
        this.destinations = destinations;
        return this;
    }

    public DeadWarpPortalPersistedData cleanupDeadEntries() {
        DeadWarpPortalPersistedData dead = new DeadWarpPortalPersistedData();
        // Iterate over portals and add invalid ones to dead list
        // also remove them from this data structure
        for (Iterator<PortalInfo> portalIterator = this.portals.iterator(); portalIterator.hasNext(); ) {
            PortalInfo portal = portalIterator.next();
            Coords firstBlock = portal.blocks.get(0);
            if (firstBlock == null || firstBlock.world == null) {
                dead.portalsInNullWorlds.add(portal);
                portalIterator.remove();
            } else if (portal.tpCoords == null || portal.tpCoords.world == null) {
                dead.portalsWithNullTPCoords.add(portal);
                portalIterator.remove();
            }
        }
        // Iterate over destinations and add invalid ones to dead list
        // also remove them from this data structure
        for (Iterator<DestinationInfo> destIterator = this.destinations.iterator(); destIterator.hasNext(); ) {
            DestinationInfo dest = destIterator.next();
            if (dest.coordspy == null || dest.coordspy.world == null) {
                dead.destinationsInNullWorlds.add(dest);
                destIterator.remove();
            }
        }
        // Return dead data for processing
        return dead;
    }
}
