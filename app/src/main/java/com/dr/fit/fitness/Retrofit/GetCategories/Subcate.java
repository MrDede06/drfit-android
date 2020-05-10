package com.dr.fit.fitness.Retrofit.GetCategories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Batuhan Özkaya on 1.07.2017.
 */

public class Subcate {

    @SerializedName("id")
    @Expose
    private int SubcateID;

    @SerializedName("isim")
    @Expose
    private String Name;

    @SerializedName("resim")
    @Expose
    private String PhotoURL;

    @SerializedName("aciklama")
    @Expose
    private String Description;

    @SerializedName("premium")
    @Expose
    private boolean Premium;

    @SerializedName("totaltime")
    @Expose
    private String TotalTime;

    @SerializedName("place")
    @Expose
    private String Place;

    @SerializedName("allvideos")
    @Expose
    private List<String> AllVideos = new ArrayList<>();

    @SerializedName("programdetails")
    @Expose
    private List<ProgramDetails> programDetails = new ArrayList<>();

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isPremium() {
        return Premium;
    }

    public void setPremium(boolean premium) {
        Premium = premium;
    }

    public String getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
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

    public List<ProgramDetails> getProgramDetails() {
        return programDetails;
    }

    public void setProgramDetails(List<ProgramDetails> programDetails) {
        this.programDetails = programDetails;
    }

    public int getSubcateID() {
        return SubcateID;
    }

    public void setSubcateID(int subcateID) {
        SubcateID = subcateID;
    }
}
