package com.alexwendland.warpportals.manager;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import com.alexwendland.warpportals.objects.CoordsPY;

public class PortalDestManager {

	Logger mLogger;

	public HashMap<String, CoordsPY> mPortalDestMap = new HashMap<String, CoordsPY>();

	public PortalDestManager(Logger logger) {
		mLogger = logger;
	}

	public void addDestination(String destname, CoordsPY coords) {
		mPortalDestMap.put(destname, coords);
	}

	public void removeDestination(String destName) {
		mPortalDestMap.remove(destName);
	}

	public CoordsPY getDestCoords(String destname) {
		return mPortalDestMap.get(destname);
	}

	public Set<String> getDestinations() {
		return mPortalDestMap.keySet();
	}

}
