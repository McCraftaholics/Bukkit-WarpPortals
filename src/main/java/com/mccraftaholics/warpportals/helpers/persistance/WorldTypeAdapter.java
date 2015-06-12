package com.mccraftaholics.warpportals.helpers.persistance;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.UUID;

public class WorldTypeAdapter implements JsonSerializer<World>, JsonDeserializer<World> {
    @Override
    public World deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        /* Attempts to get the world from the static Bukkit object.
         * - Queries using the World's UUID
         * - Will return null if the world is non-existent
         */
        return Bukkit.getWorld(UUID.fromString(jsonElement.getAsString()));
    }

    @Override
    public JsonElement serialize(World world, Type type, JsonSerializationContext jsonSerializationContext) {
        /* Persists the world by saving it's UUID
         * This can be used to get a new reference to the world
         * on deserialization from the static Bukkit item
         */
        return new JsonPrimitive(world.getUID().toString());
    }
}
