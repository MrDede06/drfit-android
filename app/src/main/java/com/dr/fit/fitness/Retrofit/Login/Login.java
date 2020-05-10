package com.dr.fit.fitness.Retrofit.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Batuhan Özkaya on 1.07.2017.
 */

public class Login {

    @SerializedName("info")
    @Expose
    private String Info;

    @SerializedName("response")
    @Expose
    private String Response;

    @SerializedName("premium")
    @Expose
    private boolean Premium;

    @SerializedName("step")
    @Expose
    private String Step;

    @SerializedName("alltestsarecomplated")
    @Expose
    private boolean allTestAreCompleted;

    @SerializedName("details")
    @Expose
    private LoginDetails loginDetails;

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

    public boolean isPremium() {
        return Premium;
    }

    public void setPremium(boolean premium) {
        Premium = premium;
    }

    public String getStep() {
        return Step;
    }

    public void setStep(String step) {
        Step = step;
    }

    public boolean isAllTestAreCompleted() {
        return allTestAreCompleted;
    }

    public void setAllTestAreCompleted(boolean allTestAreCompleted) {
        this.allTestAreCompleted = allTestAreCompleted;
    }

    public LoginDetails getLoginDetails() {
        return loginDetails;
    }

    public void setLoginDetails(LoginDetails loginDetails) {
        this.loginDetails = loginDetails;
    }
}
