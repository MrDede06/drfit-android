package com.dr.fit.fitness.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.R;

/**
 * Created by batuhanozkaya on 20.05.2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        defineObjects();
    }

    public void defineObjects(){
        AppUtility.setTransparentStatusBar(this);

        Handler handler=  new Handler();
        Runnable myRunnable = new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, FirstScreenActivity.class));
                finish();
            }
        };
        handler.postDelayed(myRunnable,2000);
    }
}
