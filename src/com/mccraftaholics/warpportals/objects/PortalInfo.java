package com.mccraftaholics.warpportals.objects;

import java.util.ArrayList;
import java.util.Collection;

public class PortalInfo {

	public CoordsPY tpCoords;
	public ArrayList<Coords> blockCoordArray;
    public String name;
	
	public PortalInfo() {
		blockCoordArray = new ArrayList<Coords>();
	}

    public PortalInfo(PortalInfo oldPortal) {
        this.name = "" + oldPortal.name;
        this.tpCoords = new CoordsPY(oldPortal.tpCoords);
        this.blockCoordArray = new ArrayList<Coords>();
        for (Coords crds : oldPortal.blockCoordArray) {
            this.blockCoordArray.add(new Coords(crds));
        }
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
