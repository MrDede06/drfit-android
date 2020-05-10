package com.dr.fit.fitness.Model;

/**
 * Created by batuhanozkaya on 22.05.2017.
 */

public class GymnasiumModel {
    private String Name, Image;
    private int Point;

    public GymnasiumModel(String mName, int mPoint, String mImage){
        setName(mName);
        setPoint(mPoint);
        setImage(mImage);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPoint() {
        return Point;
    }

    public void setPoint(int point) {
        Point = point;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
