package com.mccraftaholics.warpportals.objects;

import com.mccraftaholics.warpportals.common.model.SimpleCoords;
import com.mccraftaholics.warpportals.helpers.Regex;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class CoordsPY extends Coords implements Cloneable, Comparable<SimpleCoords> {

    public static final String SERIALIZED_COORDS_PY = "\\(" + Regex.IS_UUID + "(,-*[0-9]+\\.*[0-9]*){5}\\)";
    public static final String USER_COORDS_PY = "\\(.+(,-*[0-9]+\\.*[0-9]*){5}\\)";

    public float pitch, yaw;

    public CoordsPY(World world, double x, double y, double z, float pitch, float yaw) {
        super(world, x, y, z);
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public CoordsPY(Location loc) {
        this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
    }

    public CoordsPY(Coords crd) {
        this(crd.world, crd.x, crd.y, crd.z, 0, 0);
    }

    public static CoordsPY deserialize(String serialized) throws Exception {
        World world;
        double x, y, z;
        float pitch, yaw;
        String t = serialized.trim();
        if (t.matches(SERIALIZED_COORDS_PY)) {
            String n = serialized.substring(1, serialized.length() - 1);
            String[] s = n.split(",");

            world = Bukkit.getWorld(UUID.fromString(s[0]));
            if (world == null)
                throw NullWorldException.createForWorldUUID(s[0]);

            x = Double.parseDouble(s[1]);
            y = Double.parseDouble(s[2]);
            z = Double.parseDouble(s[3]);
            pitch = Float.parseFloat(s[4]);
            yaw = Float.parseFloat(s[5]);

            return new CoordsPY(world, x, y, z, pitch, yaw);
        } else {
            throw new Exception("Invalid Serialized Coordinates-PY");
        }
    }

    public static CoordsPY createFromUserInput(String coordsString) throws Exception {
        World world;
        double x, y, z;
        float pitch, yaw;
        String t = coordsString.trim();
        if (t.matches(USER_COORDS_PY)) {
            String n = coordsString.substring(1, coordsString.length() - 1);
            String[] s = n.split(",");

            world = Bukkit.getWorld(s[0]);
            if (world == null)
                throw NullWorldException.createForWorldName(s[0]);

            x = Double.parseDouble(s[1]);
            y = Double.parseDouble(s[2]);
            z = Double.parseDouble(s[3]);
            pitch = Float.parseFloat(s[4]);
            yaw = Float.parseFloat(s[5]);

            return new CoordsPY(world, x, y, z, pitch, yaw);
        } else {
            throw new Exception("Invalid Coordinate-PY String");
        }
    }

    public String getWorldName() {
        if (world != null)
            return world.getName();
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoordsPY)) return false;
        if (!super.equals(o)) return false;

        CoordsPY coordsPY = (CoordsPY) o;

        if (Float.compare(coordsPY.pitch, pitch) != 0) return false;
        if (Float.compare(coordsPY.yaw, yaw) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (pitch != +0.0f ? Float.floatToIntBits(pitch) : 0);
        result = 31 * result + (yaw != +0.0f ? Float.floatToIntBits(yaw) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "(" + getWorldName() + "," + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z) + "," + String.valueOf(pitch) + ","
                + String.valueOf(yaw) + ")";
    }

    public String toNiceString() {
        return "(" + getWorldName() + ", " + String.valueOf(Math.floor(x)) + ", " + String.valueOf(Math.floor(y)) + ", " + String.valueOf(Math.floor(z)) + ")";
    }

    @Override
    public String serialize() {
        return "(" + world.getUID() + "," + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z) + "," + String.valueOf(pitch) + "," + String.valueOf(yaw) + ")";
    }

    @Override
    public CoordsPY clone() {
        return new CoordsPY(this.world, this.x, this.y, this.z, this.pitch, this.yaw);
    }

    @Override
    public int compareTo(SimpleCoords that) {
        int i = super.compareTo(that);
        if (i != 0) return i;

        if (that instanceof SimpleCoords) {
            i = Float.compare(pitch, ((CoordsPY) that).pitch);
            if (i != 0) return i;

            return Float.compare(yaw, ((CoordsPY) that).yaw);
        } else {
            return 1;
        }
    }
}
