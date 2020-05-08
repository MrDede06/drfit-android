package com.dr.fit.fitness.Model;

/**
 * Created by batuhanozkaya on 21.05.2017.
 */

public class AllWorkoutModel {
    private String WorkoutTitle, WorkoutDescription, ImageURL;
    private int ID;

    public AllWorkoutModel(int mID, String mWorkoutTitle, String mWorkoutDescription, String mImageURL){
        this.setID(mID);
        this.setWorkoutTitle(mWorkoutTitle);
        this.setWorkoutDescription(mWorkoutDescription);
        this.setImageURL(mImageURL);
    }

    public String getWorkoutTitle() {
        return WorkoutTitle;
    }

    public void setWorkoutTitle(String workoutTitle) {
        WorkoutTitle = workoutTitle;
    }

    public String getWorkoutDescription() {
        return WorkoutDescription;
    }

    public void setWorkoutDescription(String workoutDescription) {
        WorkoutDescription = workoutDescription;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
