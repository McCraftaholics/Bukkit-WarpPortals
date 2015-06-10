package com.mccraftaholics.warpportals.manager.persistance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import org.bukkit.World;

public class PortalPersistanceManager implements BasePersistanceManager<WarpPortalPersistedData> {
    Gson gson;

    public PortalPersistanceManager() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Coords.class, new CoordsTypeAdapter());
        gb.registerTypeAdapter(CoordsPY.class, new CoordsPYTypeAdapter());
        gb.registerTypeAdapter(World.class, new WorldTypeAdapter());
        gson = gb.create();
    }

    @Override
    public void saveData(WarpPortalPersistedData data) {
        gson.toJson(data);
    }

    @Override
    public WarpPortalPersistedData loadData() {
        return null;
    }
}
