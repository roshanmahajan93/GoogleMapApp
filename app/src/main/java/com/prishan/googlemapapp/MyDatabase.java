package com.prishan.googlemapapp;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LocationModel.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    public static final String DB_NAME = "location_db";
    public static final String TABLE_NAME_TODO = "location";

    public abstract DaoAccess daoAccess();

}
