package com.dr.fit.fitness.Model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by batuhan on 28.04.2018.
 */

public class ProgramDetailModel implements Serializable {
    private int ReplyCount;
    private int Duration;
    private int SetCount;
    private int MoveCount;
    private int WhichDay;
    private String VideoUrl;
    private String VideoName;
    private List<String> Altyazilar;
    private boolean IsItDuration;

    public ProgramDetailModel(int mReplyCount, boolean mIsItDuration, List<String> mAltyazilar, String mVideoUrl, int mDuration, String mVideoName, int mSetcount, int mMoveCount){
        setReplyCount(mReplyCount);
        setItDuration(mIsItDuration);
        setAltyazilar(mAltyazilar);
        setVideoUrl(mVideoUrl);
        setDuration(mDuration);
        setVideoName(mVideoName);
        setSetCount(mSetcount);
        setMoveCount(mMoveCount);
    }

    public ProgramDetailModel(int mReplyCount, int mWhichDay, String mVideoUrl, int mDuration, String mVideoName, int mSetcount, List<String> mAltyazilar){
        setReplyCount(mReplyCount);
        setWhichDay(mWhichDay);
        setAltyazilar(mAltyazilar);
        setVideoUrl(mVideoUrl);
        setDuration(mDuration);
        setVideoName(mVideoName);
        setSetCount(mSetcount);
    }

    public int getReplyCount() {
        return ReplyCount;
    }

    public void setReplyCount(int replyCount) {
        ReplyCount = replyCount;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public int getSetCount() {
        return SetCount;
    }

    public void setSetCount(int setCount) {
        SetCount = setCount;
    }

    public int getMoveCount() {
        return MoveCount;
    }

    public void setMoveCount(int moveCount) {
        MoveCount = moveCount;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public List<String> getAltyazilar() {
        return Altyazilar;
    }

    public void setAltyazilar(List<String> altyazilar) {
        Altyazilar = altyazilar;
    }

    public boolean isItDuration() {
        return IsItDuration;
    }

    public void setItDuration(boolean itDuration) {
        IsItDuration = itDuration;
    }

    public int getWhichDay() {
        return WhichDay;
    }

    public void setWhichDay(int whichDay) {
        WhichDay = whichDay;
    }
}
