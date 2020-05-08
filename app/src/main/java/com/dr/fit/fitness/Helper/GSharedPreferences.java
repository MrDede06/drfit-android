package com.dr.fit.fitness.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Batuhan Özkaya on 1.07.2017.
 */

public class GSharedPreferences {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    /** Defines **/
    private static final String PREF_NAME = "DrFit";

    Context context;
    public GSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        this.context = context;
        editor = sharedPreferences.edit();
        editor.apply();
    }

    /** User Datas**/
    private static final String NAME = "Name";
    private static final String SURNAME = "Surname";
    private static final String BIRTHDAY = "Birthday";
    private static final String PROFILE_IMAGE = "ProfileImage";
    private static final String PASSWORD = "Password";
    private static final String USERID = "UserID";
    private static final String FACEBOOKID = "FacebookID";
    private static final String GENDER = "Gender";
    private static final String EMAIL = "Email";
    private static final String USERPOINT = "UserPoint";
    private static final String IS_PREMIUM = "IsPremium";
    private static final String STEP = "Step";
    private static final String ISPROGRAMCOMPLATE = "IsProgramComplate";
    private static final String ISPROGRAMPREPARING = "IsProgramPreparing";
    private static final String ISFIRSTTESTGOAL = "IsFirstTestGoal";
    private static final String ISNEXTVIDEOAVAILABLE = "IsNextVideoAvailable";
    private static final String DAYINWEEKFORARRAY = "DayInWeekForArray";
    private static final String GOALSELECTION = "GoalSelection";
    private static final String GOAL_SELECTION_1 = "GoalSelection1";
    private static final String GOAL_SELECTION_2 = "GoalSelection2";
    private static final String IS_MUST_GO_TO_RE_TEST = "IsMustgoToReTest";
    /** General **/
    private static final String PHONE_LANGUAGE = "phone_language";

    public String GET_NAME(){ return sharedPreferences.getString(NAME, ""); }

    public void SET_NAME(String Name){
        editor.putString(NAME, Name);
        editor.commit();
    }

    public String GET_SURNAME(){ return sharedPreferences.getString(SURNAME, ""); }

    public void SET_SURNAME(String Surname){
        editor.putString(SURNAME, Surname);
        editor.commit();
    }

    public String GET_BIRTHDAY(){ return sharedPreferences.getString(BIRTHDAY, ""); }

    public void SET_BIRTHDAY(String Birthday){
        editor.putString(BIRTHDAY, Birthday);
        editor.commit();
    }

    public String GET_PROFILE_IMAGE(){ return sharedPreferences.getString(PROFILE_IMAGE, ""); }

    public void SET_PROFILE_IMAGE(String ProfileImage){
        editor.putString(PROFILE_IMAGE, ProfileImage);
        editor.commit();
    }

    public String GET_PASSWORD(){ return sharedPreferences.getString(PASSWORD, ""); }

    public void SET_PASSWORD(String Password){
        editor.putString(PASSWORD, Password);
        editor.commit();
    }

    public int GET_USERID(){ return sharedPreferences.getInt(USERID, 0); }

    public void SET_USERID(int UserID){
        editor.putInt(USERID, UserID);
        editor.commit();
    }

    public String GET_FACEBOOKID(){ return sharedPreferences.getString(FACEBOOKID, ""); }

    public void SET_FACEBOOKID(String FacebookID){
        editor.putString(FACEBOOKID, FacebookID);
        editor.commit();
    }

    public int GET_GENDER(){ return sharedPreferences.getInt(GENDER, 0); }

    public void SET_GENDER(int Gender){
        editor.putInt(GENDER, Gender);
        editor.commit();
    }

    public String GET_EMAIL(){ return sharedPreferences.getString(EMAIL, ""); }

    public void SET_EMAIL(String Email){
        editor.putString(EMAIL, Email);
        editor.commit();
    }

    public int GET_USER_POINT(){ return sharedPreferences.getInt(USERPOINT, 0); }

    public void SET_USER_POINT(int UserPoint){
        editor.putInt(USERPOINT, UserPoint);
        editor.commit();
    }

    public boolean GET_IS_PREMUIM(){ return sharedPreferences.getBoolean(IS_PREMIUM, false); }

    public void SET_IS_PREMUIM(boolean IsPremium){
        editor.putBoolean(IS_PREMIUM, IsPremium);
        editor.commit();
    }

    public String GET_STEP(){ return sharedPreferences.getString(STEP, ""); }

    public void SET_STEP(String Step){
        editor.putString(STEP, Step);
        editor.commit();
    }

    public boolean GET_IS_PROGRAM_COMPLATE(){ return sharedPreferences.getBoolean(ISPROGRAMCOMPLATE, false); }

    public void SET_IS_PROGRAM_COMPLATE(boolean IsProgramComplate){
        editor.putBoolean(ISPROGRAMCOMPLATE, IsProgramComplate);
        editor.commit();
    }

    public boolean GET_IS_PROGRAM_PREPARING(){ return sharedPreferences.getBoolean(ISPROGRAMPREPARING, false); }

    public void SET_IS_PROGRAM_PREPARING(boolean IsProgramPreparing){
        editor.putBoolean(ISPROGRAMPREPARING, IsProgramPreparing);
        editor.commit();
    }

    public String GET_PHONE_LANGUAGE(){ return sharedPreferences.getString(PHONE_LANGUAGE, "en"); }

    public void SET_PHONE_LANGUAGE(String PhoneLanguage){
        editor.putString(PHONE_LANGUAGE, PhoneLanguage);
        editor.commit();
    }

    public boolean GET_IS_FIRST_TEST_GOAL(){ return sharedPreferences.getBoolean(ISFIRSTTESTGOAL, true); }

    public void SET_IS_FIRST_TEST_GOAL(boolean isFirstTest){
        editor.putBoolean(ISFIRSTTESTGOAL, isFirstTest);
        editor.commit();
    }

    public String GET_IS_NEXT_VIDEO_AVAILABLE(){ return sharedPreferences.getString(ISNEXTVIDEOAVAILABLE, "programisready"); }

    public void SET_IS_NEXT_VIDEO_AVAIABLE(String isNextVideoAvailable){
        editor.putString(ISNEXTVIDEOAVAILABLE, isNextVideoAvailable);
        editor.commit();
    }

    public int GET_DAY_IN_WEEK_FOR_ARRAY(){ return sharedPreferences.getInt(DAYINWEEKFORARRAY, 0); }

    public void SET_DAY_IN_WEEK_FOR_ARRAY(int DayInWeekForArray){
        editor.putInt(DAYINWEEKFORARRAY, DayInWeekForArray);
        editor.commit();
    }

    public boolean GET_GOAL_SELECTION(){ return sharedPreferences.getBoolean(GOALSELECTION, false); }

    public void SET_GOAL_SELECTION(boolean GoalSelection){
        editor.putBoolean(GOALSELECTION, GoalSelection);
        editor.commit();
    }

    public int GET_GOAL_SELECTION_1(){ return sharedPreferences.getInt(GOAL_SELECTION_1, 0); }

    public void SET_GOAL_SELECTION_1(int GoalSelection){
        editor.putInt(GOAL_SELECTION_1, GoalSelection);
        editor.commit();
    }

    public int GET_GOAL_SELECTION_2(){ return sharedPreferences.getInt(GOAL_SELECTION_2, 0); }

    public void SET_GOAL_SELECTION_2(int GoalSelection){
        editor.putInt(GOAL_SELECTION_2, GoalSelection);
        editor.commit();
    }

    public boolean GET_IS_MUST_GO_TO_RE_TEST(){ return sharedPreferences.getBoolean(IS_MUST_GO_TO_RE_TEST, false); }

    public void SET_IS_MUST_GO_TO_RE_TEST(boolean GoalSelection){
        editor.putBoolean(IS_MUST_GO_TO_RE_TEST, GoalSelection);
        editor.commit();
    }



}
