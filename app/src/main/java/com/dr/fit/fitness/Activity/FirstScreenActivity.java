package com.dr.fit.fitness.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.R;

import java.util.Locale;


/**
 * Created by batuhanozkaya on 20.05.2017.
 */

public class FirstScreenActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    GSharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        sharedPreferences = new GSharedPreferences(this);
        sharedPreferences.SET_PHONE_LANGUAGE(Locale.getDefault().getLanguage());
        AppUtility.checkIfLogedIn(FirstScreenActivity.this);

        setContentView(R.layout.activity_firstscreen);
        defineObjects();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreenActivity.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstScreenActivity.this, RegisterActivity.class));
            }
        });
    }

    public void defineObjects(){

        /** Button Defines **/
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister =  findViewById(R.id.btnRegister);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
