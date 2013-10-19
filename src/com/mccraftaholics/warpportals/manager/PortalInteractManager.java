package com.mccraftaholics.warpportals.manager;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Location;

import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.PortalInfo;

public class PortalInteractManager {

	Logger mLogger;
	PortalManager mPM;

	private HashMap<String, PortalInfo> mPortalMap = new HashMap<String, PortalInfo>();

	public PortalInteractManager(PortalManager pm, Logger logger) {
		mPM = pm;
		mLogger = logger;
	}

	public PortalInfo checkPlayer(Location loc) {
		return checkPlayer(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public PortalInfo checkPlayer(double x, double y, double z) {
		// Loop through each portal
		for (String portalName : mPortalMap.keySet()) {
			// Coordinates making up this portal
			PortalInfo portal = mPortalMap.get(portalName);
			for (Coords crd : portal.blockCoordArray) {
				if (Math.floor(x) == crd.x && Math.floor(y) == crd.y && Math.floor(z) == crd.z)
					return portal;
			}
		}
		return null;
	}

	public PortalInfo checkPlayerLoose(Location loc) {
		return checkPlayerLoose(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public PortalInfo checkPlayerLoose(double x, double y, double z) {
		// Loop through each portal
		for (String portalName : mPortalMap.keySet()) {
			// Coordinates making up this portal
			PortalInfo portal = mPortalMap.get(portalName);
			for (Coords crd : portal.blockCoordArray) {
				x = Math.floor(x);
				y = Math.floor(y);
				z = Math.floor(z);
				if ((x <= crd.x + 1 && x >= crd.x - 1) && (y <= crd.y + 1 && y >= crd.y - 1) && (z <= crd.z + 1 && z >= crd.z - 1))
					return portal;
			}
		}
		return null;
	}
	
	public void addPortal(String portalName, PortalInfo portalInfo) {
		mPortalMap.put(portalName, portalInfo);
		mPM.saveDataFile();
	}
	
	public HashMap<String, PortalInfo> getPortalMap() {
		return mPortalMap;
	}
	
	public void clearPortalMap() {
		mPortalMap.clear();
	}

	public Set<String> getPortalNames() {
		return mPortalMap.keySet();
	}

	public PortalInfo getPortalInfo(String portalName) {
		return mPortalMap.get(portalName);
	}

	public PortalInfo removePortal(String portalName) {
		PortalInfo rtn = mPortalMap.remove(portalName);
		mPM.saveDataFile();
		return rtn;
	}

}
