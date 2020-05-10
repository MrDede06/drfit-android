package com.dr.fit.fitness.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Helper.Utility;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.FreeSubCompleted.FreeSubCompleted;
import com.dr.fit.fitness.Retrofit.GetCategories.Subcate;
import com.dr.fit.fitness.Retrofit.PersonalProgramCompleted.PersonalProgramCompleted;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CongratulationsActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnPoint, btnGoHome;
    GSharedPreferences sharedPreferences;
    RetrofitAPI retrofitAPI;
    RestAdapter restAdapter;
    ImageView IVFacebook, IVGoogle, IVWhatsapp;
    int SubcateID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_congratulations);

        defineObjects();
    }

    public void defineObjects(){
        sharedPreferences = new GSharedPreferences(this);

        SubcateID = Integer.parseInt(getIntent().getExtras().getString("SubcateID"));

        btnGoHome = findViewById(R.id.btnGoHome);
        btnPoint = findViewById(R.id.btnPoint);

        IVFacebook = findViewById(R.id.IVFacebook);
        IVGoogle = findViewById(R.id.IVGoogle);
        IVWhatsapp = findViewById(R.id.IVWhatsapp);
        IVFacebook.setOnClickListener(this);
        IVGoogle.setOnClickListener(this);
        IVWhatsapp.setOnClickListener(this);

        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CongratulationsActivity.this, HomePageActivity.class));
                finish();
            }
        });

        Log.d("Kontrol", "UserPoint" + String.valueOf(sharedPreferences.GET_USER_POINT()));

        restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = restAdapter.create(RetrofitAPI.class);

        if(getIntent().getExtras().getBoolean("isPremiumExercise")){
            sendFinishForPremium();
        }else{
            sendFinishForFree();
        }

    }

    private void sendFinishForFree(){
        retrofitAPI.FreeSubComplated(sharedPreferences.GET_USERID(), SubcateID, new Callback<FreeSubCompleted>() {
            @Override
            public void success(FreeSubCompleted freeSubCompleted, Response response) {
                if(freeSubCompleted.getResponse().equals("ok")){
                    btnPoint.setVisibility(View.VISIBLE);
                    btnPoint.setText("+100 " + getString(R.string.point));
                    Utility.showAlertDialogOneButton(CongratulationsActivity.this, getString(R.string.success_message));
                    sharedPreferences.SET_USER_POINT((freeSubCompleted.getPoint()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Kontrol", error.getMessage());
            }
        });
    }

    private void sendFinishForPremium(){
        retrofitAPI.PersonalProgramCompleted(sharedPreferences.GET_USERID(), new Callback<PersonalProgramCompleted>() {
            @Override
            public void success(PersonalProgramCompleted personalProgramCompleted, Response response) {
                if(personalProgramCompleted.getResponse().equals("ok")){
                    btnPoint.setVisibility(View.VISIBLE);
                    btnPoint.setText("+100 " + getString(R.string.point));
                    Utility.showAlertDialogOneButton(CongratulationsActivity.this, getString(R.string.success_message));
                    sharedPreferences.SET_USER_POINT((personalProgramCompleted.getPoint()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Kontrol", error.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.IVFacebook:
                share();
                break;
            case R.id.IVGoogle:
                share();
                break;
            case R.id.IVWhatsapp:
                share();
                break;
        }
    }

    private void share(){
        ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle("Training")
                .setText("http://dededevops.com")
                .startChooser();

    }

    private void makeFullScreen(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
