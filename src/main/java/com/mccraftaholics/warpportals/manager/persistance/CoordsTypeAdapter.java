package com.mccraftaholics.warpportals.manager.persistance;

import com.google.gson.*;
import com.mccraftaholics.warpportals.objects.Coords;

import java.lang.reflect.Type;

public class CoordsTypeAdapter implements JsonSerializer<Coords>, JsonDeserializer<Coords> {
    @Override
    public Coords deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return Coords.deserialize(jsonElement.getAsString());
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Coords coords, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(coords.serialize());
    }
}
