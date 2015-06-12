package com.mccraftaholics.warpportals.manager;

import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.DestinationInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

public class PortalDestManager {

    public HashMap<String, DestinationInfo> mPortalDestMap = new HashMap<String, DestinationInfo>();
    Logger mLogger;
    private PortalManager mPM;

    public PortalDestManager(PortalManager pm, Logger logger) {
        mPM = pm;
        mLogger = logger;
    }

    public void addDestination(boolean doSave, String destname, CoordsPY coords) {
        addDestination(doSave, new DestinationInfo(destname, coords));
    }

    public void addDestination(boolean doSave, DestinationInfo dest) {
        mPortalDestMap.put(dest.name, dest);
        mPM.saveDataFile();
    }

    public void addDestinations(boolean doSave, DestinationInfo... dests) {
        for (DestinationInfo dest : dests) {
            mPortalDestMap.put(dest.name, dest);
        }
        if (doSave) {
            mPM.saveDataFile();
        }
    }

    public void removeDestination(String destName) {
        mPortalDestMap.remove(destName);
        mPM.saveDataFile();
    }

    public CoordsPY getDestCoords(String destname) {
        if (mPortalDestMap.containsKey(destname)) {
            return mPortalDestMap.get(destname).coordspy;
        }
        return null;
    }

    public Set<String> getDestinationNames() {
        return mPortalDestMap.keySet();
    }

    public String getDestinationName(CoordsPY coords) {
        for (DestinationInfo dest : mPortalDestMap.values()) {
            if (dest.coordspy.equals(coords)) {
                return dest.name;
            }
        }
        return null;
    }

    public Collection<DestinationInfo> getDestinations() {
        return mPortalDestMap.values();
    }
}
