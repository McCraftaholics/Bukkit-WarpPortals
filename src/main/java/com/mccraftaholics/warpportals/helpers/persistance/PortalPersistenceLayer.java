package com.mccraftaholics.warpportals.helpers.persistance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;
import org.bukkit.World;

public class PortalPersistenceLayer implements BasePersistenceLayer<WarpPortalPersistedData> {
    Gson gson;

    public PortalPersistenceLayer() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Coords.class, new CoordsTypeAdapter());
        gb.registerTypeAdapter(CoordsPY.class, new CoordsPYTypeAdapter());
        gb.registerTypeAdapter(World.class, new WorldTypeAdapter());
        gson = gb.create();
    }

    @Override
    public String serialize(WarpPortalPersistedData data) {
        return gson.toJson(data);
    }

    @Override
    public WarpPortalPersistedData deserialize(String stringData) throws JsonSyntaxException {
        return gson.fromJson(stringData, WarpPortalPersistedData.class);
    }
}
