package com.dr.fit.fitness.Retrofit.GetCategories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by batuhan on 11.05.2018.
 */

public class DayList {
    @SerializedName("replycount")
    @Expose
    private int ReplyCount;

    @SerializedName("isitduration")
    @Expose
    private boolean IsItDuration;

    @SerializedName("whichday")
    @Expose
    private int WhichDay;

    @SerializedName("altyazi")
    @Expose
    private List<String> Altyazilar = new ArrayList<>();

    @SerializedName("video")
    @Expose
    private String Video;

    @SerializedName("duration")
    @Expose
    private int Duration;

    @SerializedName("isim")
    @Expose
    private String Isim;

    @SerializedName("setcount")
    @Expose
    private int SetCount;


    public int getReplyCount() { return ReplyCount; }

    public void setReplyCount(int replyCount) {
        ReplyCount = replyCount;
    }

    public boolean isItDuration() {
        return IsItDuration;
    }

    public void setItDuration(boolean itDuration) {
        IsItDuration = itDuration;
    }

    public List<String> getAltyazilar() {
        return Altyazilar;
    }

    public void setAltyazilar(List<String> altyazilar) {
        Altyazilar = altyazilar;
    }

    public String getVideoURL() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getIsim() {
        return Isim;
    }

    public void setIsim(String isim) {
        Isim = isim;
    }

    public int getSetCount() {
        return SetCount;
    }

    public void setSetCount(int setCount) {
        SetCount = setCount;
    }

    public int getWhichDay() {
        return WhichDay;
    }

    public void setWhichDay(int whichDay) {
        WhichDay = whichDay;
    }
}
