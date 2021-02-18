package com.example.app;

public class Course {

    private boolean objectifAtteint;
    private int tempsDeCourse, calorieBurlee;


    public Course(boolean objectifAtteint, int tempsDeCourse, int calorieBurlee) {
        this.objectifAtteint = objectifAtteint;
        this.tempsDeCourse = tempsDeCourse;
        this.calorieBurlee = calorieBurlee;
    }

    public boolean isObjectifAtteint() {
        return objectifAtteint;
    }

    public int getTempsDeCourse() {
        return tempsDeCourse;
    }

    public int getCalorieBurlee() {
        return calorieBurlee;
    }

    public void setObjectifAtteint(boolean objectifAtteint) {
        this.objectifAtteint = objectifAtteint;
    }

    public void setTempsDeCourse(int tempsDeCourse) {
        this.tempsDeCourse = tempsDeCourse;
    }

    public void setCalorieBurlee(int calorieBurlee) {
        this.calorieBurlee = calorieBurlee;
    }
}
