package com.dr.fit.fitness.Retrofit.GetCategories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Batuhan Özkaya on 3.12.2017.
 */

public class PersonalProgram {
    @SerializedName("totaltime")
    @Expose
    private String TotalTime;

    @SerializedName("place")
    @Expose
    private int Place;

    @SerializedName("daylist")
    @Expose
    private List<DayList> DayList = new ArrayList<>();

    @SerializedName("dailyvideolist")
    @Expose
    private List<String> DailyVideoList = new ArrayList<>();

    @SerializedName("movecount")
    @Expose
    private int MoveCount;

    public String getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
    }

    public int getPlace() {
        return Place;
    }

    public void setPlace(int place) {
        Place = place;
    }

    public int getMoveCount() {
        return MoveCount;
    }

    public void setMoveCount(int moveCount) {
        MoveCount = moveCount;
    }

    public List<com.dr.fit.fitness.Retrofit.GetCategories.DayList> getDayList() {
        return DayList;
    }

    public void setDayList(List<com.dr.fit.fitness.Retrofit.GetCategories.DayList> dayList) {
        DayList = dayList;
    }

    public List<String> getDailyVideoList() {
        return DailyVideoList;
    }

    public void setDailyVideoList(List<String> dailyVideoList) {
        DailyVideoList = dailyVideoList;
    }
}
