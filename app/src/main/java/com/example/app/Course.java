package com.example.app;

public class Course {

    private boolean objectifAtteint;
    private int tempsDeCourse, calorieBurlee;
    private String tvTempsCourse, tvCalorieBrulee, tvObjectifAttein;

    public String getTvObjectifAttein() {
        return tvObjectifAttein; }

    public String gettVtempsCourse() {
        return tvTempsCourse;
    }

    public String getTvCalorieBrulee() {
        return tvCalorieBrulee;
    }

    public int getTempsDeCourse() {
        return tempsDeCourse;
    }

    public int getCalorieBurlee() {
        return calorieBurlee;
    }

    public boolean isObjectifAtteint() {
        return objectifAtteint;
    }

    public Course(boolean objectifAtteint, int tempsDeCourse, int calorieBurlee, String tvTempsCourse, String tvCalorieBrulee, String tvObjectifAttein) {
        this.objectifAtteint = objectifAtteint;
        this.tempsDeCourse = tempsDeCourse;
        this.calorieBurlee = calorieBurlee;
        this.tvTempsCourse = tvTempsCourse;
        this.tvCalorieBrulee = tvCalorieBrulee;
        this.tvObjectifAttein = tvObjectifAttein;
    }
}
