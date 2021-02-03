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
    long insertTodo(LocationModel todo);

    @Insert
    void insertTodoList(List<LocationModel> todoList);

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_TODO)
    List<LocationModel> fetchAllTodos();

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_TODO + " WHERE location_id = :locationId")
    LocationModel fetchLocationModelListById(int locationId);

    @Update
    int updateTodo(LocationModel todo);

    @Delete
    int deleteTodo(LocationModel todo);
}
