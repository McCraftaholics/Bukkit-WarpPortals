package com.mccraftaholics.warpportals.manager;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class PortalDataManager {

	Logger mLogger;
	PortalManager mPM;

	private HashMap<String, PortalInfo> mPortalMap = new HashMap<String, PortalInfo>();
	private HashMap<Coords, String> mCoordsToPortalsMap = new HashMap<Coords, String>();

	public PortalDataManager(PortalManager pm, Logger logger) {
		mPM = pm;
		mLogger = logger;
	}
	
	public HashMap<String, PortalInfo> getPortalMap() {
		return mPortalMap;
	}
	
	public void clearPortalMap() {
		mPortalMap.clear();
	}
	
	public int getPortalCount() {
		return mPortalMap.size();
	}

	public Set<String> getPortalNames() {
		return mPortalMap.keySet();
	}

	public PortalInfo getPortalInfo(String portalName) {
		return mPortalMap.get(portalName);
	}
	
	public void addPortalNoSave(String portalName, PortalInfo portalInfo) {
		mPortalMap.put(portalName, portalInfo);
		rebuildCoordsToPortalsMap();
	}
	
	public void addPortal(String portalName, PortalInfo portalInfo) {
		addPortalNoSave(portalName, portalInfo);
		mPM.saveDataFile();
	}
	
	public PortalInfo removePortalNoSave(String portalName) {
		PortalInfo rtn = mPortalMap.remove(portalName);
		rebuildCoordsToPortalsMap();
		return rtn;
	}

	public PortalInfo removePortal(String portalName) {
		PortalInfo rtn = removePortalNoSave(portalName);
		mPM.saveDataFile();
		return rtn;
	}
	
	private void rebuildCoordsToPortalsMap() {
		mCoordsToPortalsMap.clear();
		for (Entry<String, PortalInfo> portalEntry : mPortalMap.entrySet()) {
			PortalInfo portal = portalEntry.getValue();
			for (Coords coord : portal.blockCoordArray) {
				mCoordsToPortalsMap.put(coord, portal.name);
			}
		}
	}
	
	public String getPortalName(Coords coord) {
		return mCoordsToPortalsMap.get(coord);
	}

}
