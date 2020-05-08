package com.dr.fit.fitness.Retrofit.GetPre;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by batuhan on 1.07.2018.
 */

public class GetPre {

    @SerializedName("response")
    @Expose
    private String Response;

    @SerializedName("info")
    @Expose
    private String Info;

    @SerializedName("premium")
    @Expose
    private boolean Premium;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public boolean isPremium() {
        return Premium;
    }

    public void setPremium(boolean premium) {
        Premium = premium;
    }
}
