package com.example.app;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Person {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String hairColor;
    @ColumnInfo
    private String gender;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hairColor='" + hairColor + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
