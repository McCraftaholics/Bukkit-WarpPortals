package com.mccraftaholics.warpportals.objects;

public class NullWorldException extends Exception {

    String world;

    public NullWorldException() {
        super();
    }

    public NullWorldException(String world, String message) {
        super(message);
        this.world = world;
    }

    public static NullWorldException createForWorldName(String worldName) {
        NullWorldException e = new NullWorldException(worldName, "World \"" + worldName + "\" does not exist.");
        return e;
    }

    public static NullWorldException createForWorldUUID(String worldUUID) {
        NullWorldException e = new NullWorldException(worldUUID, "World with UUID=" + worldUUID + " does not exist.");
        return e;
    }

    public String getIdentifier() {
        return world;
    }
}
