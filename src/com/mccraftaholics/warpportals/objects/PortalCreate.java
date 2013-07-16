package com.mccraftaholics.warpportals.objects;

import org.bukkit.Material;

public class PortalCreate {
	public Material blockType;
	public String portalName;
	public CoordsPY tpCoords;

	public PortalCreate(String portalName, Material blockType, CoordsPY tpCoords) {
		this.portalName = portalName;
		this.blockType = blockType;
		this.tpCoords = tpCoords;
	}

	public PortalCreate() {

	}
}
