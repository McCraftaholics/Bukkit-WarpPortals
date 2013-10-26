package com.mccraftaholics.test;

import com.mccraftaholics.test.PortalTestSetup.PortalTest;

public class Main {

	public static void main(String[] args) {
		
		int numberOfPlayersInPortals = 10;
		int numberOfPlayersNotInPortals = 1000;
		int numberOfPortals = 100;
		int numberOfBlocksPerPortal = 1000;
		int numberOfIterations = 1000;
		
		PortalTest pt = PortalTestSetup.setup(numberOfPlayersInPortals,numberOfBlocksPerPortal,numberOfPortals,numberOfBlocksPerPortal, numberOfIterations);

		System.out.println("Testing " + String.valueOf(numberOfPlayersInPortals + numberOfPlayersNotInPortals) + " players, " + "\n" + 
				"\t\t" + numberOfPlayersInPortals + " of which would be teleported & " + numberOfPlayersNotInPortals + " that would not," + "\n" +
				"\tagainst " + numberOfPortals + " portals containing a total of " + String.valueOf(numberOfPortals * numberOfBlocksPerPortal) + " blocks." + "\n" + 
				"\tTest will run " + numberOfIterations + " times." + "\n");
		/* These commented methods take a ridiculously long time with the provided numbers and are therefore ruled out as possible algorithms */
		//System.out.println("Looping: " + String.valueOf(PortalTests.run1(pt)) + "ms"); // Try using 1 iteration, takes over 1 second
		//System.out.println("ReverseMap: " + String.valueOf(PortalTests.run2(pt)) + "ms"); // Try using 1 iteration, takes over 1 second
		System.out.println("ReverseMap.contains(): " + String.valueOf(PortalTests.run3(pt)) + "ms"); // Finishes in under 200ms for 1000 iterations
	}
}
