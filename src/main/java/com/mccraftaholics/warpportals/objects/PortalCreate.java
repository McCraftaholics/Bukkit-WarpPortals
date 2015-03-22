package com.mccraftaholics.warpportals.objects;

import org.bukkit.Material;

public class PortalCreate {
    public Material toolType;
    public String portalName;
    public CoordsPY tpCoords;
    public Material blockType;
    public Byte blockData;
    public String teleportMessage;

    public PortalCreate(Material toolType, String portalName, CoordsPY tpCoords, Material blockType, Byte blockData, String teleportMessage) {
        this.portalName = portalName;
        this.toolType = toolType;
        this.tpCoords = tpCoords;
        this.blockType = blockType;
        this.blockData = blockData;
        this.teleportMessage = teleportMessage;
    }
}
