package com.example.app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

//CRUD : Create Read Update Delete
@Dao
public interface PersonDao {

    //Read
    @Query("select * from Person")
    List<Person> getAll();

    //Insert or update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Person> personList);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Person person);

    //Delete
    @Delete
    void delete(Person person);
}
