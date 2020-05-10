package com.dr.fit.fitness.Model;

import java.util.List;

/**
 * Created by batuhanozkaya on 22.05.2017.
 */

public class SubcateModel {
    private String ProgramTitle;
    private String ProgramDescription;
    private String Image;
    private String TotalTime;
    private boolean isPremium;
    private int ProgramID;
    private int ExerciseCount;
    private int SubcateID;
    private String Place;
    private List<String> AllVideos;

    public SubcateModel(int mSubcateID, int mProgramID, String mProgramTitle, String mProgramDescription, String mImage, String mTotalTime, boolean mIsPremium, int mExerciseCount, String mPlace, List<String> mAllVideos){
        setSubcateID(mSubcateID);
        setProgramID(mProgramID);
        setProgramTitle(mProgramTitle);
        setProgramDescription(mProgramDescription);
        setImage(mImage);
        setPremium(mIsPremium);
        setTotalTime(mTotalTime);
        setExerciseCount(mExerciseCount);
        setPlace(mPlace);
        setAllVideos(mAllVideos);
    }

    public String getProgramTitle() {
        return ProgramTitle;
    }

    public void setProgramTitle(String programTitle) {
        ProgramTitle = programTitle;
    }

    public String getProgramDescription() {
        return ProgramDescription;
    }

    public void setProgramDescription(String programDescription) {
        ProgramDescription = programDescription;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public int getProgramID() {
        return ProgramID;
    }

    public void setProgramID(int programID) {
        ProgramID = programID;
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


    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public List<String> getAllVideos() {
        return AllVideos;
    }

    public void setAllVideos(List<String> allVideos) {
        AllVideos = allVideos;
    }

    public int getSubcateID() {
        return SubcateID;
    }

    public void setSubcateID(int subcateID) {
        SubcateID = subcateID;
    }
}
