package com.mccraftaholics.warpportals.manager;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import com.mccraftaholics.warpportals.objects.CoordsPY;

public class PortalDestManager {

	Logger mLogger;
	
	private PortalManager mPM;

	public HashMap<String, CoordsPY> mPortalDestMap = new HashMap<String, CoordsPY>();

	public PortalDestManager(PortalManager pm, Logger logger) {
		mPM = pm;
		mLogger = logger;
	}

	public void addDestination(String destname, CoordsPY coords) {
		mPortalDestMap.put(destname, coords);
		mPM.saveDataFile();
	}

	public void removeDestination(String destName) {
		mPortalDestMap.remove(destName);
		mPM.saveDataFile();
	}

	public CoordsPY getDestCoords(String destname) {
		return mPortalDestMap.get(destname);
	}

	public Set<String> getDestinations() {
		return mPortalDestMap.keySet();
	}

}
