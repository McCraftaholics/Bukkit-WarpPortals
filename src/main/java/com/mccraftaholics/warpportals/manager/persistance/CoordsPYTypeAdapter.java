package com.mccraftaholics.warpportals.manager.persistance;

import com.google.gson.*;
import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;

import java.lang.reflect.Type;

public class CoordsPYTypeAdapter implements JsonSerializer<CoordsPY>, JsonDeserializer<CoordsPY> {
    @Override
    public CoordsPY deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return CoordsPY.deserialize(jsonElement.getAsString());
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(CoordsPY coordsPY, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(coordsPY.serialize());
    }
}
