package com.mccraftaholics.warpportals.objects;

import com.mccraftaholics.warpportals.common.model.SimplePortal;

import java.util.List;
import java.util.UUID;

public class PortalInfo extends SimplePortal<Coords> implements Comparable<SimplePortal> {

    public CoordsPY tpCoords;

    public PortalInfo(UUID uuid, String name, List<Coords> blocks, CoordsPY tpCoords) {
        super(uuid, name, blocks);
        this.tpCoords = tpCoords;
    }

    public PortalInfo(UUID uuid, String name, CoordsPY tpCoords) {
        super(uuid, name);
        this.tpCoords = tpCoords;
    }

    public PortalInfo clone() {
        PortalInfo portal = new PortalInfo(this.uuid, this.name, this.tpCoords.clone());
        for (Coords crds : this.blocks) {
            portal.blocks.add(crds.clone());
        }
        return portal;
    }

    @Override
    public String toString() {
        return String.valueOf(tpCoords) + "\n" + String.valueOf(blocks);
    }

    @Override
    public int compareTo(SimplePortal that) {
        int i = super.compareTo(that);
        if (i != 0) return i;

        if (that instanceof PortalInfo) {
            return tpCoords.compareTo(((PortalInfo) that).tpCoords);
        } else {
            return 1;
        }
    }
}
