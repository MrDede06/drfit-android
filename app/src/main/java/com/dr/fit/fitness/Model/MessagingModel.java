package com.dr.fit.fitness.Model;

/**
 * Created by Batuhan Özkaya on 30.06.2017.
 */

public class MessagingModel {
    private String id;
    private String message;
    private String image;
    private String type;
    private String userid;
    private boolean SystemOrUser;

    public MessagingModel(String mMessage, String mType, String mUserID, String mImage, String mAdvertisingID, boolean mSystemOrUser){
        setMessage(mMessage);
        setSystemOrUser(mSystemOrUser);
        setType(mType);
        setUserID(mUserID);
        setImage(mImage);
        setAdvertisingID(mAdvertisingID);
    }

    public MessagingModel(String mMessage, String mType, String mUserID, String mImage, String mAdvertisingID){
        setMessage(mMessage);
        setType(mType);
        setUserID(mUserID);
        setImage(mImage);
        setAdvertisingID(mAdvertisingID);
    }

    public MessagingModel(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSystemOrUser() {
        return SystemOrUser;
    }

    public void setSystemOrUser(boolean systemOrUser) {
        SystemOrUser = systemOrUser;
    }

    public String getAdvertisingID() {
        return id;
    }

    public void setAdvertisingID(String advertisingID) {
        this.id = advertisingID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserID() {
        return userid;
    }

    public void setUserID(String userID) {
        this.userid = userID;
    }
}
