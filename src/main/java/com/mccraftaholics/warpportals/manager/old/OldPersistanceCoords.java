package com.mccraftaholics.warpportals.manager.old;

import com.mccraftaholics.warpportals.common.model.SimpleCoords;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.NullWorldException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.UUID;

public class OldPersistanceCoords {

    public static final String SERIALIZED_COORDS = "\\(" + Regex.IS_UUID + "(,-*[0-9]+\\.*[0-9E\\-]*){3}\\)";
    public static final String USER_COORDS = "\\(.+(,-*[0-9]+\\.*[0-9E\\-]*){3}\\)";

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
}
