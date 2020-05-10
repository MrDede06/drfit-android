package com.dr.fit.fitness.Model;

/**
 * Created by batuhan on 9.06.2018.
 */

public class AddMessageModel {
    private String id;
    private String image;
    private String message;
    private String type;
    private int userid;

    public AddMessageModel(String mId, String mImage, String mMessage, String mType, int mUserId){
        setId(mId);
        setImage(mImage);
        setMessage(mMessage);
        setType(mType);
        setUserid(mUserId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
