package com.example.app;

import static java.lang.Math.abs;

public class Algo {

    protected int burnedCal(int weight,int distance, int speed){   // unite => weight en kg, distance en km, speed en kh/h
        int costPerKmPerKg = 1028; // average
        int energyPerKm = costPerKmPerKg * weight;
        int costTotal = distance * energyPerKm;
        return costTotal;
    }

    protected int objectifCal(String gender,int objWeight,int weight, int height, int age, String lvlActivity){ // weight en kg, height en cm, age en année

        float met;
        float metObj;
        float coef;

        switch (gender){
            case "Male":
                met = (float) ((13.7516 * weight) + (500.33 * height/100) - (6.7550 * age + 66.473));
                break;
            case "Female":
                met = (float) ((9.5634 * weight) + (184.96 * height/100) - (4.6756 * age + 655.0955));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + gender);
        }

        switch (gender){
            case "Male":
                metObj = (float) ((13.7516 * objWeight) + (500.33 * height/100) - (6.7550 * age + 66.473));
                break;
            case "Female":
                metObj = (float) ((9.5634 * objWeight) + (184.96 * height/100) - (4.6756 * age + 655.0955));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + gender);
        }

        switch(lvlActivity){
            case "Sedentaire":
                coef = (float) 1.3;
                break;
            case "Activite physique legere":
                coef = (float) 1.375;
            case "Activite physique moderee":
                coef = (float) 1.55;
            case "Activite physique intense":
                coef = (float) 1.725;
            case "Activite physique tres intense":
                coef = (float) 1.9;
            default:
                throw new IllegalStateException("Unexpected value: " + lvlActivity);
        }

        met = met * coef;
        metObj = metObj * coef;
        return (int) abs(met-metObj);
    }
}
