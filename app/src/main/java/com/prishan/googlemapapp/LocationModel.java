package com.prishan.googlemapapp;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = MyDatabase.TABLE_NAME_TODO)
public class LocationModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int location_id;
    public String name;
    public String address;
    public String image_url;
    public String description;


}
