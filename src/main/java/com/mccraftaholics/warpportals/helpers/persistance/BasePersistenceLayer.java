package com.mccraftaholics.warpportals.helpers.persistance;


public interface BasePersistenceLayer<T> {
    String serialize(T data);

    T deserialize(String input);
}
