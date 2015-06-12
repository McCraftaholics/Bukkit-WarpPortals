package com.mccraftaholics.warpportals.helpers.persistance;


import com.mccraftaholics.warpportals.objects.DestinationInfo;
import com.mccraftaholics.warpportals.objects.PortalInfo;

import java.util.LinkedList;
import java.util.List;

public class DeadWarpPortalPersistedData {

    public List<PortalInfo> portalsWithNullTPCoords = new LinkedList<PortalInfo>();
    public List<PortalInfo> portalsInNullWorlds = new LinkedList<PortalInfo>();
    public List<DestinationInfo> destinationsInNullWorlds = new LinkedList<DestinationInfo>();
}
