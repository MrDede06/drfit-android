package com.dr.fit.fitness.Model;

/**
 * Created by batuhanozkaya on 21.05.2017.
 */

public class MyWorkoutModel {
    String Name;
    int ID;
    private String TotalTime;
    private int ExerciseCount;
    private int Place;

    public MyWorkoutModel(String mName, int mID) {
        this.Name = mName;
        this.ID = mID;
    }

    public MyWorkoutModel(String mName, int mID, String mTotalTime, int mExerciseCount, int mPlace) {
        this.Name = mName;
        this.ID = mID;
        this.TotalTime = mTotalTime;
        this.ExerciseCount = mExerciseCount;
        this.Place = mPlace;
    }

    public String getName() {
        return Name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
    }

    public int getExerciseCount() {
        return ExerciseCount;
    }

    public void setExerciseCount(int exerciseCount) {
        ExerciseCount = exerciseCount;
    }

    public int getPlace() {
        return Place;
    }

    public void setPlace(int place) {
        Place = place;
    }
}
