package com.dr.fit.fitness.Retrofit.UpdateProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by batuhan on 29.06.2018.
 */

public class UpdateProfilePhoto {

    @SerializedName("info")
    @Expose
    private String Info;

    @SerializedName("response")
    @Expose
    private String Response;

    @SerializedName("profileimg")
    @Expose
    private String ProfileImg;

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getProfileImg() {
        return ProfileImg;
    }

    public void setProfileImg(String profileImg) {
        ProfileImg = profileImg;
    }
}
