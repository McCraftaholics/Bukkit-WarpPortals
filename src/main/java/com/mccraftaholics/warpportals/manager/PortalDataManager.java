package com.mccraftaholics.warpportals.manager;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class PortalDataManager {

	Logger mLogger;
	PortalManager mPM;

	private Map<UUID, PortalInfo> mPortalMap = new HashMap<UUID, PortalInfo>();
	private Map<Coords, UUID> mCoordsToPortalsMap = new HashMap<Coords, UUID>();

	public PortalDataManager(PortalManager pm, Logger logger) {
		mPM = pm;
		mLogger = logger;
	}
	
	public Map<UUID, PortalInfo> getPortalMap() {
		return mPortalMap;
	}
	
	public void clearPortalMap() {
		mPortalMap.clear();
	}
	
	public int getPortalCount() {
		return mPortalMap.size();
	}

	public Collection<PortalInfo> getPortals() {
		return mPortalMap.values();
	}

	public PortalInfo getPortal(UUID portalUuid) {
		return mPortalMap.get(portalUuid);
	}

    public PortalInfo getPortal(Coords coord) {
        UUID portalUuid = mCoordsToPortalsMap.get(coord);
        if (portalUuid != null) {
            return getPortal(portalUuid);
        }
        return null;
    }

    public PortalInfo getPortal(String name) {
        for (PortalInfo portal : mPortalMap.values()) {
            if (portal.name.equals(name)) {
                return portal;
            }
        }
        return null;
    }

    public boolean isNameUsed(String name) {
        return getPortal(name) != null;
    }
	
	public void addPortalNoSave(PortalInfo portalInfo) {
		mPortalMap.put(portalInfo.uuid, portalInfo);
		rebuildCoordsToPortalsMap();
	}
	
	public void addPortal(PortalInfo portalInfo) {
		addPortalNoSave(portalInfo);
		mPM.saveDataFile();
		mPM.backupDataFile();
	}
	
	public PortalInfo removePortalNoSave(UUID portalUuid) {
		PortalInfo rtn = mPortalMap.remove(portalUuid);
		rebuildCoordsToPortalsMap();
		return rtn;
	}

	public PortalInfo removePortal(UUID portalUuid) {
		PortalInfo rtn = removePortalNoSave(portalUuid);
		mPM.saveDataFile();
		return rtn;
	}
	
	private void rebuildCoordsToPortalsMap() {
		mCoordsToPortalsMap.clear();
		for (PortalInfo portal : mPortalMap.values()) {
			for (Coords coord : portal.blocks) {
				mCoordsToPortalsMap.put(coord, portal.uuid);
			}
		}
	}

}
