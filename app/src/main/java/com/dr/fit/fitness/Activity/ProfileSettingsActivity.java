package com.dr.fit.fitness.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Helper.Utility;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;
import com.dr.fit.fitness.Retrofit.UpdateProfile.UpdateProfile;
import com.dr.fit.fitness.Retrofit.UpdateProfile.UpdateProfilePhoto;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import static java.security.AccessController.getContext;

/**
 * Created by batuhanozkaya on 21.05.2017.
 */

public class ProfileSettingsActivity extends AppCompatActivity {
    AutoCompleteTextView ACTVName, ACTVSurname, ACTVEmail, ACTVPass, ACTVBirthday, ACTVGender;
    int Gender, columnIndex;
    String imagefilePath, imagePhoto = "";
    Button btnSave;
    ImageView IVLogout;
    RelativeLayout RLImage, RLLogout;
    RoundedImageView RIVProfilePicture;
    GSharedPreferences sharedPreferences;
    RestAdapter retrofit;
    RetrofitAPI retrofitAPI;
    Context context;
    /** ACTIVITY RESULT CODES **/
    public static final int SELECT_FROM_GALLERY = 701;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile_settings);
        defineObjects();
    }

    public void defineObjects(){
        Toolbar toolbar =  findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        context = this;

        /** Define APIs **/
        retrofit = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);

        /** Defining Others **/
        sharedPreferences = new GSharedPreferences(this);
        RIVProfilePicture =  findViewById(R.id.RIVProfilePicture);
        RLLogout = findViewById(R.id.RLLogout);
        btnSave =  findViewById(R.id.btnSave);
        IVLogout = findViewById(R.id.IVLogout);

        /** Defining TextViews **/
        ACTVName = findViewById(R.id.ACTVName);
        ACTVSurname = findViewById(R.id.ACTVSurname);
        ACTVEmail =  findViewById(R.id.ACTVEmail);
        ACTVPass =  findViewById(R.id.ACTVPass);
        ACTVBirthday =  findViewById(R.id.ACTVBirthday);
        ACTVGender =  findViewById(R.id.ACTVGender);

        RLImage = findViewById(R.id.RLImage);
        RLImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.choosePhoto(context, ProfileSettingsActivity.this);
            }
        });

        IVLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.logoutProcesses(ProfileSettingsActivity.this);
                finishAffinity();
            }
        });

        RLLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.logoutProcesses(ProfileSettingsActivity.this);
                finishAffinity();
            }
        });

        ACTVGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ProfileSettingsActivity.this);
                dialog.setContentView(R.layout.custom_alert_dialog_for_gender);
                dialog.setTitle(getString(R.string.select_your_gender));
                dialog.setCancelable(true);

                final RadioButton rbMale =  dialog.findViewById(R.id.rbMale);
                final RadioButton rbFemale =  dialog.findViewById(R.id.rbFemale);
                Button btnOK =  dialog.findViewById(R.id.btnOK);

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rbMale.isChecked()){
                            Gender = 1;
                            ACTVGender.setText(getString(R.string.male));
                            dialog.dismiss();
                        }else if(rbFemale. isChecked()){
                            Gender = 0;
                            ACTVGender.setText(getString(R.string.female));
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });

        ACTVBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePicker;

                datePicker = new DatePickerDialog(ProfileSettingsActivity.this, R.style.MyOwnDatePicker, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, monthOfYear);

                        ACTVBirthday.setText(year + "-" + AppUtility.checkAndAddZero((monthOfYear + 1)) + "-" + AppUtility.checkAndAddZero(dayOfMonth));
                    }
                },1995,1,1);

                datePicker.setTitle(getString(R.string.select_your_birthday));
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.set), datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), datePicker);

                datePicker.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(ACTVName.getText().toString()) || TextUtils.isEmpty(ACTVSurname.getText().toString()) ||TextUtils.isEmpty(ACTVEmail.getText().toString()) || TextUtils.isEmpty(ACTVPass.getText().toString()) || TextUtils.isEmpty(ACTVBirthday.getText().toString())){
                    Utility.showAlertDialogOneButton(ProfileSettingsActivity.this, getString(R.string.dont_leave_empty_field));
                }else{
                    retrofitAPI.UpdateProfile(sharedPreferences.GET_USERID(), ACTVName.getText().toString(), ACTVSurname.getText().toString(), ACTVEmail.getText().toString(), ACTVPass.getText().toString(), Gender, ACTVBirthday.getText().toString(), sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<UpdateProfile>() {
                        @Override
                        public void success(UpdateProfile updateProfile, Response response) {
                            if(updateProfile.getResponse().equals("ok")){
                                Utility.showAlertDialogOneButton(ProfileSettingsActivity.this, updateProfile.getInfo());
                                setSharedDatas(ACTVName.getText().toString(), ACTVSurname.getText().toString(), ACTVEmail.getText().toString(), ACTVPass.getText().toString(), Gender, ACTVBirthday.getText().toString());
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Utility.showAlertDialogOneButton(ProfileSettingsActivity.this, error.getMessage());
                        }
                    });
                }

                if(!imagePhoto.equals("")){
                    TypedFile typedFile = new TypedFile("multipart/form-data", new File(imagePhoto));

                    retrofitAPI.UpdateProfilePhoto(sharedPreferences.GET_USERID(), typedFile, sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<UpdateProfilePhoto>() {
                        @Override
                        public void success(UpdateProfilePhoto updateProfilePhoto, Response response) {
                            Log.d("Kontrol", "Profile: " + updateProfilePhoto.getProfileImg());
                            sharedPreferences.SET_PROFILE_IMAGE(updateProfilePhoto.getProfileImg());
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("Kontrol", error.getMessage());
                        }
                    });
                }
            }
        });

        fillContentWithSharedInformations();
    }

    private void fillContentWithSharedInformations(){
        ACTVName.setText(sharedPreferences.GET_NAME());
        ACTVSurname.setText(sharedPreferences.GET_SURNAME());
        ACTVEmail.setText(sharedPreferences.GET_EMAIL());
        ACTVPass.setText(sharedPreferences.GET_PASSWORD());
        ACTVBirthday.setText(sharedPreferences.GET_BIRTHDAY());
        if(sharedPreferences.GET_GENDER() == 0){
            ACTVGender.setText(getString(R.string.female));
        }else{
            ACTVGender.setText(getString(R.string.male));
        }

        if (!sharedPreferences.GET_PROFILE_IMAGE().equals("http://dededevops.com/media/")){
            Picasso.with(this).load(sharedPreferences.GET_PROFILE_IMAGE()).into(RIVProfilePicture);
        }

    }

    private void setSharedDatas(String Name, String Surname, String Email, String Password, int Gender, String Birthday){
        sharedPreferences.SET_NAME(Name);
        sharedPreferences.SET_SURNAME(Surname);
        sharedPreferences.SET_EMAIL(Email);
        sharedPreferences.SET_PASSWORD(Password);
        sharedPreferences.SET_BIRTHDAY(Birthday);
        sharedPreferences.SET_GENDER(Gender);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void imageReady(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        try {
            getContentResolver().openInputStream(data.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        }

        imagefilePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap myBitmap = BitmapFactory.decodeFile(Utility.compressImage(this, imagefilePath));

        imagePhoto = Utility.compressImage(this, imagefilePath);
        RIVProfilePicture.setImageBitmap(myBitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Kontrol", "girdi, Request: " + String.valueOf(requestCode));
        if(requestCode == SELECT_FROM_GALLERY){
            if(data != null){
                imageReady(data);
            }
        }
    }
}
