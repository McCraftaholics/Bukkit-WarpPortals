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

	public HashMap<String, PortalInfo> mPortalMap = new HashMap<String, PortalInfo>();

	public PortalInteractManager(PortalManager pm, Logger logger) {
		mPM = pm;
		mLogger = logger;
	}

	public CoordsPY checkPlayer(Location loc) {
		return checkPlayer(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public CoordsPY checkPlayer(double x, double y, double z) {
		// Loop through each portal
		for (String portalName : mPortalMap.keySet()) {
			// Coordinates making up this portal
			PortalInfo portal = mPortalMap.get(portalName);
			for (Coords crd : portal.blockCoordArray) {
				if (Math.floor(x) == crd.x && Math.floor(y) == crd.y && Math.floor(z) == crd.z)
					return portal.tpCoords;
			}
		}
		return null;
	}

	public CoordsPY checkPlayerLoose(Location loc) {
		return checkPlayerLoose(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public CoordsPY checkPlayerLoose(double x, double y, double z) {
		// Loop through each portal
		for (String portalName : mPortalMap.keySet()) {
			// Coordinates making up this portal
			PortalInfo portal = mPortalMap.get(portalName);
			for (Coords crd : portal.blockCoordArray) {
				x = Math.floor(x);
				y = Math.floor(y);
				z = Math.floor(z);
				if ((x <= crd.x + 1 && x >= crd.x - 1) && (y <= crd.y + 1 && y >= crd.y - 1) && (z <= crd.z + 1 && z >= crd.z - 1))
					return portal.tpCoords;
			}
		}
		return null;
	}
	
	public void addPortal(String portalName, PortalInfo portalInfo) {
		mPortalMap.put(portalName, portalInfo);
		mPM.saveDataFile();
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
