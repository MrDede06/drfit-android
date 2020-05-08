package com.dr.fit.fitness.Retrofit.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Batuhan Özkaya on 2.07.2017.
 */

public class LoginDetails {

    @SerializedName("userpoint")
    @Expose
    private int UserPoint;

    @SerializedName("birthday")
    @Expose
    private String Birthday;

    @SerializedName("facebookid")
    @Expose
    private String FacebookID;

    @SerializedName("password")
    @Expose
    private String Password;

    @SerializedName("id")
    @Expose
    private int UserID;

    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("surname")
    @Expose
    private String Surname;

    @SerializedName("profileimg")
    @Expose
    private String ProfileImage;

    @SerializedName("gender")
    @Expose
    private int Gender;

    @SerializedName("email")
    @Expose
    private String Email;

    public int getUserPoint() {
        return UserPoint;
    }

    public void setUserPoint(int userPoint) {
        UserPoint = userPoint;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getFacebookID() {
        return FacebookID;
    }

    public void setFacebookID(String facebookID) {
        FacebookID = facebookID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getFullname() {
        return Name;
    }

    public void setFullname(String fullname) {
        Name = fullname;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }
}
