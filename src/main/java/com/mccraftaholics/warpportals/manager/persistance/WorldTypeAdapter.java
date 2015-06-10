package com.mccraftaholics.warpportals.manager.persistance;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.UUID;

public class WorldTypeAdapter implements JsonSerializer<World>, JsonDeserializer<World> {
    @Override
    public World deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Bukkit.getWorld(UUID.fromString(jsonElement.getAsString()));
    }

    @Override
    public JsonElement serialize(World world, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(world.getUID().toString());
    }
}
