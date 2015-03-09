package com.mccraftaholics.warpportals.objects;

import org.bukkit.Material;

public class PortalCreate {
    public Material toolType;
    public String portalName;
    public CoordsPY tpCoords;
    public Material blockType;

    public PortalCreate(String portalName, Material toolType, CoordsPY tpCoords, Material blockType) {
        this.portalName = portalName;
        this.toolType = toolType;
        this.tpCoords = tpCoords;
        this.blockType = blockType;
    }

    public PortalCreate() {

    }
}
