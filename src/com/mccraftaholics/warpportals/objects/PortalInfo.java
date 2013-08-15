package com.mccraftaholics.warpportals.objects;

import java.util.ArrayList;

public class PortalInfo {

	public CoordsPY tpCoords;
	public ArrayList<Coords> blockCoordArray;
	
	public PortalInfo() {
		blockCoordArray = new ArrayList<Coords>();
	}

	public String blockCoordArrToString() {
		StringBuilder sb = new StringBuilder();
		for (Coords coord : blockCoordArray) {
			sb.append(coord.toString());
			sb.append(";");
		}
		return sb.substring(0, sb.length());
	}

	public void parseBlockCoordArr(String s) {
		blockCoordArray = new ArrayList<Coords>();
		String[] a = s.split(";");
		for (String i : a) {
			try {
				blockCoordArray.add(new Coords(i));
			} catch (Exception e) {
			}
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf(tpCoords) + "\n" + String.valueOf(blockCoordArray);
	}

}
