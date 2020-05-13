package com.example.restaurantplus;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities =CartDb.class, exportSchema = false)
public abstract class MyAppDatabase extends RoomDatabase {
    public abstract CartDao cartDao();
}
