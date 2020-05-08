package com.dr.fit.fitness.Retrofit.GetCategories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Batuhan Özkaya on 1.07.2017.
 */

public class Category {

    @SerializedName("isim")
    @Expose
    private String Name;

    @SerializedName("resim")
    @Expose
    private String PhotoURL;

    @SerializedName("aciklama")
    @Expose
    private String Description;

    @SerializedName("subcate")
    @Expose
    private List<Subcate> subcates = new ArrayList<>();

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

    public List<Subcate> getSubcates() {
        return subcates;
    }

    public void setSubcates(List<Subcate> subcates) {
        this.subcates = subcates;
    }
}
