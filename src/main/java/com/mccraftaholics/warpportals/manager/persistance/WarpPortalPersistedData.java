package com.mccraftaholics.warpportals.manager.persistance;

import com.mccraftaholics.warpportals.objects.DestinationInfo;
import com.mccraftaholics.warpportals.objects.PortalInfo;

import java.util.LinkedList;
import java.util.List;

public class WarpPortalPersistedData {
    public List<PortalInfo> portals = new LinkedList<PortalInfo>();
    public List<DestinationInfo> destinations = new LinkedList<DestinationInfo>();

    public List<PortalInfo> getPortals() {
        return portals;
    }

    public void setPortals(List<PortalInfo> portals) {
        this.portals = portals;
    }

    public List<DestinationInfo> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<DestinationInfo> destinations) {
        this.destinations = destinations;
    }
}
