package com.mccraftaholics.test;

import java.util.List;
import java.util.Map.Entry;

import com.mccraftaholics.test.PortalTestSetup.Coords;
import com.mccraftaholics.test.PortalTestSetup.PortalTest;

public class PortalTests {

	/** Loops through every portal block and compares it to player location
	 * @param pt
	 * @return
	 */
	public static long run1(PortalTest pt) {		
		long startTime = System.currentTimeMillis();
		
		int t = 0;
		while (t < pt.numTicks) {
			for (Coords playerCoords : pt.testCoordsList) { // Run test for each "player"
				// Find in Portals map
				for (Entry<String, List<Coords>> portal : pt.portalsMap.entrySet()) { // Loop through portals
					for (Coords coord : portal.getValue()) { // Loop through portal's coords
						if (playerCoords.equals(coord))
							break;
					}
				}
			}
			
			t++;
		}
		long endTime = System.currentTimeMillis();
		
		return endTime - startTime;
	}
	
	/** Uses a coords -> Portal map, loops though map values and compares key to player coords
	 * @param pt
	 * @return
	 */
	public static long run2(PortalTest pt) {		
		long startTime = System.currentTimeMillis();
		
		int t = 0;
		while (t < pt.numTicks) {
			for (Coords playerCoords : pt.testCoordsList) { // Run test for each "player"
				// Find in Portals map
				for (Entry<Coords, String> portal : pt.reversePortalMap.entrySet()) { // Loop through portals
					if (portal.getKey() == playerCoords)
						break;
				}
			}
			
			t++;
		}
		long endTime = System.currentTimeMillis();
		
		return endTime - startTime;
	}
	
	/** Uses a coords -> Portal map, get(playerCoords) to test if value is good
	 * @param pt
	 * @return
	 */
	public static long run3(PortalTest pt) {		
		long startTime = System.currentTimeMillis();
		
		int t = 0;
		while (t < pt.numTicks) {
			for (Coords playerCoords : pt.testCoordsList) { // Run test for each "player"
				// Find in Portals map
				pt.reversePortalMap.get(playerCoords);
			}
			
			t++;
		}
		long endTime = System.currentTimeMillis();
		
		return endTime - startTime;
	}

}
