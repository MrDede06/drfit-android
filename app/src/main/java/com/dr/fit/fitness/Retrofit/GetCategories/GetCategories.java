package com.dr.fit.fitness.Retrofit.GetCategories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.dr.fit.fitness.Retrofit.GetCategories.PersonalProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Batuhan Özkaya on 1.07.2017.
 */

public class GetCategories {

    @SerializedName("response")
    @Expose
    private String Response;

    @SerializedName("category")
    @Expose
    private List<Category> categories = new ArrayList<>();

    @SerializedName("istestcomplated")
    @Expose
    private boolean IsTestCompleted;

    @SerializedName("isuserpremium")
    @Expose
    private boolean IsUserPremium;

    @SerializedName("isnextvideoavailable")
    @Expose
    private String IsNextVideoAvailable;

    @SerializedName("dayinweekforarray")
    @Expose
    private int DayInWeekForArray;

    @SerializedName("imagelist")
    @Expose
    private List<String> ImageList = new ArrayList<>();

   @SerializedName("personalprogram")
    @Expose
    private List<PersonalProgram> PersonalProgram = new ArrayList<>();

    @SerializedName("recheck")
    @Expose
    private boolean ReCheck;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public boolean isTestCompleted() {
        return IsTestCompleted;
    }

    public void setTestCompleted(boolean testCompleted) {
        IsTestCompleted = testCompleted;
    }

    public boolean isUserPremium() {
        return IsUserPremium;
    }

    public void setUserPremium(boolean userPremium) {
        IsUserPremium = userPremium;
    }

    public String getIsNextVideoAvailable() {
        return IsNextVideoAvailable;
    }

    public void setIsNextVideoAvailable(String isNextVideoAvailable) {
        IsNextVideoAvailable = isNextVideoAvailable;
    }

    public int getDayInWeekForArray() {
        return DayInWeekForArray;
    }

    public void setDayInWeekForArray(int dayInWeekForArray) {
        DayInWeekForArray = dayInWeekForArray;
    }

    public List<String> getImageList() {
        return ImageList;
    }

    public void setImageList(List<String> imageList) {
        ImageList = imageList;
    }

   public List<PersonalProgram> getPersonalProgram() {
        return PersonalProgram;
    }

    public void setPersonalProgram(List<PersonalProgram> personalProgram) {
        PersonalProgram = personalProgram;
    }

    public boolean isReCheck() {
        return ReCheck;
    }

    public void setReCheck(boolean reCheck) {
        ReCheck = reCheck;
    }
}
