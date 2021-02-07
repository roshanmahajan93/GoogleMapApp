package com.prishan.googlemapapp;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    long insertLocation(LocationModel locationModel);

    @Insert
    void insertLocationList(List<LocationModel> locationModelList);

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_LOCATION)
    List<LocationModel> fetchAllLocationModel();

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_LOCATION + " WHERE location_id = :locationId")
    LocationModel fetchLocationModelListById(int locationId);

    @Update
    int updateLocationModel(LocationModel locationModel);

    @Delete
    int deleteLocationModel(LocationModel locationModel);
}
