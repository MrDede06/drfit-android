package com.dr.fit.fitness.Retrofit.Gymlasium;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Batuhan Özkaya on 30.06.2017.
 */

public class GymlasiumList {

    @SerializedName("puan")
    @Expose
    private int Point;

    @SerializedName("isim")
    @Expose
    private String Name;

    @SerializedName("premium")
    @Expose
    private boolean isPremium;

    @SerializedName("resim")
    @Expose
    private String PhotoURL;


    public int getPoint() {
        return Point;
    }

    public void setPoint(int point) {
        Point = point;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
