package com.mccraftaholics.warpportals.manager;

import org.bukkit.Location;
import com.mccraftaholics.warpportals.objects.Coords;

public class PortalInteractManager {

	PortalManager mPortalManager;
	
	public PortalInteractManager(PortalManager portalManager) {
		mPortalManager = portalManager;
	}
	
	/** Test whether or not the location is currently inside a portal.
	 * @param loc Location to test
	 * @return String of the portal's name OR null if the player is not in a portal.
	 */
	public String isLocationInsidePortal(Location loc) {
		Coords coords = Coords.createCourse(loc);
		return mPortalManager.getPortalName(coords);
	}

}
