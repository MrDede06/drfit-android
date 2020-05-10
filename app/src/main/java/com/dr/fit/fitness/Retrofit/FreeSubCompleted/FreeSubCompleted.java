package com.dr.fit.fitness.Retrofit.FreeSubCompleted;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by batuhan on 2.05.2018.
 */

public class FreeSubCompleted {
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
