package com.mccraftaholics.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PortalTestSetup {

	static class Coords {
		int x;
		int y;
		int z;
		String world;

		Coords() {
			x = (int) (Math.random() * 1000);
			y = (int) (Math.random() * 1000);
			z = (int) (Math.random() * 1000);
			world = "w" + getText();
		}

		boolean compare(int _x, int _y, int _z, String world) {
			return x == _x && y == _y && z == _z && world.equals(world);
		}

		boolean equals(Coords c) {
			return compare(c.x, c.y, c.z, c.world);
		}
	}

	static String getText() {
		return Integer.toHexString((int) (Math.random() * 1e6));
	}

	public static PortalTest setup(int numPlayersSuccess, int numPlayersFail, int numPortals, int numBlocksPerPortal, int numTicks) {
		PortalTest portalTest = new PortalTest();
		
		portalTest.numTicks = numTicks;
		
		portalTest.portalsMap = new HashMap<String, List<Coords>>();

		portalTest.testCoordsList = new ArrayList<Coords>();

		for (int i = 0; i < numPortals; i++) {
			String name = "p" + getText();
			List<Coords> cList = new ArrayList<Coords>();
			int randomStop = (int) (Math.random() * numBlocksPerPortal);
			for (int j = 0; j < numBlocksPerPortal; j++) {
				Coords crd = new Coords();
				if (j == randomStop && portalTest.testCoordsList.size() <= numPlayersSuccess)
					portalTest.testCoordsList.add(crd);
				cList.add(crd);
			}
			portalTest.portalsMap.put(name, cList);
		}
		
		for (int i = 0; i < numPlayersFail; i++) {
			portalTest.testCoordsList.add(new Coords());
		}

		portalTest.reversePortalMap = new HashMap<Coords, String>();
		for (Entry<String, List<Coords>> portal : portalTest.portalsMap.entrySet()) { // Loop
																						// through
																						// portals
			for (Coords coord : portal.getValue()) { // Loop through portal's
														// coords
				portalTest.reversePortalMap.put(coord, portal.getKey());
			}
		}

		return portalTest;
	}

	static class PortalTest {
		Map<String, List<Coords>> portalsMap;
		List<Coords> testCoordsList;
		Map<Coords, String> reversePortalMap;
		int numTicks;
	}

}
