package com.mccraftaholics.warpportals.manager.old;

import com.mccraftaholics.warpportals.common.model.SimpleCoords;
import com.mccraftaholics.warpportals.helpers.Regex;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.mccraftaholics.warpportals.objects.NullWorldException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class OldPersistanceCoordsPY {

    public static final String SERIALIZED_COORDS_PY = "\\(" + Regex.IS_UUID + "(,-*[0-9]+\\.*[0-9E\\-]*){5}\\)";
    public static final String USER_COORDS_PY = "\\(.+(,-*[0-9]+\\.*[0-9E\\-]*){5}\\)";

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
}
