package com.mccraftaholics.warpportals.manager.persistance;


public interface BasePersistanceManager<T> {
    void saveData(T data);
    T loadData();
}
