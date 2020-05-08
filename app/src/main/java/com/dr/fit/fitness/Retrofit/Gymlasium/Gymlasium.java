package com.dr.fit.fitness.Retrofit.Gymlasium;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Batuhan Özkaya on 30.06.2017.
 */

public class Gymlasium {

    @SerializedName("response")
    @Expose
    private String Response;

    @SerializedName("gymlasium")
    @Expose
    private List<GymlasiumList> gymlasiumListList = new ArrayList<>();

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public List<GymlasiumList> getGymlasiumListList() {
        return gymlasiumListList;
    }

    public void setGymlasiumListList(List<GymlasiumList> gymlasiumListList) {
        this.gymlasiumListList = gymlasiumListList;
    }
}
