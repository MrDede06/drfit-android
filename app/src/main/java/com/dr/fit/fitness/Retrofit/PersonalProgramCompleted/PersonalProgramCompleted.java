package com.dr.fit.fitness.Retrofit.PersonalProgramCompleted;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by batuhan on 23.07.2018.
 */

public class PersonalProgramCompleted {
    @SerializedName("response")
    @Expose
    private String Response;

    @SerializedName("point")
    @Expose
    private int Point;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public int getPoint() {
        return Point;
    }

    public void setPoint(int point) {
        Point = point;
    }
}
