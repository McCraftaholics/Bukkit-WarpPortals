package com.mccraftaholics.warpportals.objects;

import com.mccraftaholics.warpportals.common.model.SimpleCoords;
import com.mccraftaholics.warpportals.helpers.Regex;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.UUID;

public class Coords extends SimpleCoords implements Cloneable, Comparable<SimpleCoords> {

    public static final String SERIALIZED_COORDS = "\\(" + Regex.IS_UUID + "(,-*[0-9]+\\.*[0-9]*){3}\\)";
    public static final String USER_COORDS = "\\(.+(,-*[0-9]+\\.*[0-9]*){3}\\)";

    public World world;

    protected Coords() {

    }

    public Coords(World world, double x, double y, double z) {
        super(x, y, z);
        this.world = world;
    }

    public Coords(Block b) {
        this(b.getWorld(), b.getX(), b.getY(), b.getZ());
    }

    public Coords(Location loc) {
        this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
    }

    public static Coords createCourse(Location loc) {
        return new Coords(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static Coords deserialize(String serialized) throws Exception {
        World world;
        double x, y, z;
        String t = serialized.trim();
        if (t.matches(SERIALIZED_COORDS)) {
            String n = serialized.substring(1, serialized.length() - 1);
            String[] s = n.split(",");

            world = Bukkit.getWorld(UUID.fromString(s[0]));
            if (world == null)
                throw NullWorldException.createForWorldUUID(s[0]);

            x = Double.parseDouble(s[1]);
            y = Double.parseDouble(s[2]);
            z = Double.parseDouble(s[3]);

            return new Coords(world, x, y, z);
        } else {
            throw new Exception("Invalid Serialized Coordinates");
        }
    }

    public static Coords createFromUserInput(String coordsString) throws Exception {
        World world;
        double x, y, z;
        String t = coordsString.trim();
        if (t.matches(USER_COORDS)) {
            String n = coordsString.substring(1, coordsString.length() - 1);
            String[] s = n.split(",");

            world = Bukkit.getWorld(s[0]);
            if (world == null)
                throw NullWorldException.createForWorldName(s[0]);

            x = Double.parseDouble(s[1]);
            y = Double.parseDouble(s[2]);
            z = Double.parseDouble(s[3]);

            return new Coords(world, x, y, z);
        } else {
            throw new Exception("Invalid Coordinate String");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coords)) return false;
        if (!super.equals(o)) return false;

        Coords coords = (Coords) o;

        if (!world.getUID().equals(coords.world.getUID())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + world.hashCode();
        return result;
    }

    public String toString() {
        return "(" + world.getName() + "," + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z) + ")";
    }

    public String serialize() {
        return "(" + world.getUID() + "," + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z) + ")";
    }

    @Override
    public Coords clone() {
        return new Coords(this.world, this.x, this.y, this.z);
    }

    @Override
    public int compareTo(SimpleCoords that) {
        int i = super.compareTo(that);
        if (i != 0) return i;

        if (that instanceof Coords) {
            return world.getUID().compareTo(((Coords) that).world.getUID());
        } else {
            return 1;
        }
    }
}
