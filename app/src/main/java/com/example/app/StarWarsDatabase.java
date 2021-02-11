package com.example.app;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Person.class}, version = 1)
public abstract class StarWarsDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
}
