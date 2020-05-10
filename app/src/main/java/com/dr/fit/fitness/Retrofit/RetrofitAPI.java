package com.dr.fit.fitness.Retrofit;

import com.dr.fit.fitness.Helper.GlobalValues;
import com.dr.fit.fitness.Retrofit.CollectPersonalDataFromUser.CollectPersonalDataFromUser;
import com.dr.fit.fitness.Retrofit.ForgotPassword.ForgotPassword;
import com.dr.fit.fitness.Retrofit.FreeSubCompleted.FreeSubCompleted;
import com.dr.fit.fitness.Retrofit.GetCategories.GetCategories;
import com.dr.fit.fitness.Retrofit.GetPre.GetPre;
import com.dr.fit.fitness.Retrofit.Gymlasium.Gymlasium;
import com.dr.fit.fitness.Retrofit.Login.Login;
import com.dr.fit.fitness.Retrofit.PersonalProgramCompleted.PersonalProgramCompleted;
import com.dr.fit.fitness.Retrofit.Register.Register;
import com.dr.fit.fitness.Retrofit.UpdateProfile.UpdateProfile;
import com.dr.fit.fitness.Retrofit.UpdateProfile.UpdateProfilePhoto;

import java.math.BigInteger;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

/**
 * Created by Batuhan Özkaya on 30.06.2017.
 */

public interface RetrofitAPI {
    @GET("/gymlasium")
    void Gymlasium(Callback<Gymlasium> callback);

    @GET("/getcategories/{Lang}/{UserID}")
    void GetCategories(@Path("UserID") Integer UserID, @Path("Lang") String Language, Callback<GetCategories> callback);

    @FormUrlEncoded
    @POST("/regmein/")
    void Register(
            @Field("name") String Name,
            @Field("surname") String Surname,
            @Field("email") String Email,
            @Field("birthday") String Birthday,
            @Field("password") String Password,
            @Field("gender") int Gender,
            @Field("facebookid") String FacebookID,
            @Field("googleid") BigInteger GoogleID,
            @Field("phonetype") String PhoneType,
            @Field("lang") String Lang,
            Callback<Register> callback);

    @FormUrlEncoded
    @POST("/logmein/")
    void Login(
            @Field("email") String Email,
            @Field("password") String Password,
            @Field("lang") String Lang,
            Callback<Login> callback);

    @FormUrlEncoded
    @POST("/logmein/")
    void LoginWithFacebook(
            @Field("facebookid") String FacebookID,
            @Field("lang") String Lang,
            Callback<Login> callback);

    @FormUrlEncoded
    @POST("/logmein/")
    void LoginWithGoogle(
            @Field("googleid") String GoogleID,
            @Field("lang") String Lang,
            Callback<Login> callback);

    @FormUrlEncoded
    @POST("/forgotpassword/")
    void ForgotPassword(
                    @Field("email") String Email,
                    @Field("lang") String Lang,
                    Callback<ForgotPassword> callback);

    @FormUrlEncoded
    @POST("/editdetails/")
    void UpdateProfile(
            @Field("userid") int UserID,
            @Field("name") String Name,
            @Field("surname") String Lastname,
            @Field("email") String Email,
            @Field("password") String Password,
            @Field("gender") int Gender,
            @Field("birthday") String Birthday,
            @Field("lang") String Lang,
            Callback<UpdateProfile> callback);

    @Multipart
    @POST("/updateprofilephoto/")
    void UpdateProfilePhoto(
            @Part("userid") int UserID,
            @Part("file") TypedFile file,
            @Part("lang") String Lang,
            Callback<UpdateProfilePhoto> callback);

    @FormUrlEncoded
    @POST("/freesubcomplated/")
    void FreeSubComplated(
            @Field("userid") int UserID,
            @Field("subcateid") int SubcateID,
            Callback<FreeSubCompleted> callback);

    @FormUrlEncoded
    @POST("/personalprogramcomplated/")
    void PersonalProgramCompleted(
            @Field("userid") int UserID,
            Callback<PersonalProgramCompleted> callback);


    @FormUrlEncoded
    @POST("/collectpersonaldatafromuser/")
    void CollectPersonelDataFromUser(
            @Field("step") int Step,
            @Field("userid") int UserID,
            @Field("type") int Type,
            @Field("userwidth") int UserWidth,
            @Field("userheight") int UserHeight,
            @Field("userpower") int UserPower,
            @Field("lang") String Lang,
            Callback<CollectPersonalDataFromUser> callback);

    @FormUrlEncoded
    @POST("/collectpersonaldatafromuser/")
    void CollectPersonelDataFromUserForHeightWeight(
            @Field("step") int Step,
            @Field("userid") int UserID,
            @Field("userwidth") int UserWidth,
            @Field("userheight") int UserHeight,
            @Field("lang") String Lang,
            Callback<CollectPersonalDataFromUser> callback);

    @FormUrlEncoded
    @POST("/collectpersonaldatafromuser/")
    void CollectPersonelDataFromUserForTestLastStep(
            @Field("step") int Step,
            @Field("userid") int UserID,
            @Field("userpower") int UserPower,
            @Field("lang") String Lang,
            Callback<CollectPersonalDataFromUser> callback);

    @FormUrlEncoded
    @POST("/getpre/")
    void getPremium(
            @Field("userid") int UserID,
            Callback<GetPre> callback);




}
