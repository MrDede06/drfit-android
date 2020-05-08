package com.dr.fit.fitness.Retrofit.ForgotPassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Batuhan Özkaya on 3.03.2018.
 */

public class ForgotPassword {
    @SerializedName("info")
    @Expose
    private String Info;

    @SerializedName("response")
    @Expose
    private String Response;

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
}
