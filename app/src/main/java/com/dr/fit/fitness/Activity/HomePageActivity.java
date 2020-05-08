package com.dr.fit.fitness.Activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dr.fit.fitness.Fragment.FragmentGymnasium;
import com.dr.fit.fitness.Fragment.FragmentHomepage;
import com.dr.fit.fitness.Fragment.FragmentMessaging;
import com.dr.fit.fitness.Fragment.FragmentProfile;
import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GlobalValues;
import com.dr.fit.fitness.R;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by batuhanozkaya on 21.05.2017.
 */

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout LLGymnasium, LLHome, LLMessaging, LLProfile;
    ImageView IVHome, IVMessaging, IVGymnasium, IVProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_homepage);

        Toolbar toolbar = findViewById(R.id.myHomepageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        defineObjects();
    }

    public void defineObjects(){
        /** Define ImageViews **/
        IVHome = findViewById(R.id.IVHome);
        IVMessaging = findViewById(R.id.IVMessaging);
        IVGymnasium = findViewById(R.id.IVGymnasium);
        IVProfile = findViewById(R.id.IVProfile);
        /** Define Linear Layouts **/
        LLGymnasium =  findViewById(R.id.LLGymnasium);
        LLHome =  findViewById(R.id.LLHome);
        LLMessaging = findViewById(R.id.LLMessaging);
        LLProfile = findViewById(R.id.LLProfile);

        LLGymnasium.setOnClickListener(this);
        LLHome.setOnClickListener(this);
        LLMessaging.setOnClickListener(this);
        LLProfile.setOnClickListener(this);

        goToFragment(new FragmentHomepage(), GlobalValues.HOMEPAGE);
    }


    public void goToFragment(android.support.v4.app.Fragment fragment, String tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.FLHome, fragment).addToBackStack(null).commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LLHome:
                goToFragment(new FragmentHomepage(), GlobalValues.HOMEPAGE);
                setBottomBarActiveButtons(1);
                break;
            case R.id.LLMessaging:
                goToFragment(new FragmentMessaging(), GlobalValues.MESSAGING);
                setBottomBarActiveButtons(2);
                break;
            case R.id.LLGymnasium:
                goToFragment(new FragmentGymnasium(), GlobalValues.GYMNASIUM);
                setBottomBarActiveButtons(3);
                break;
            case R.id.LLProfile:
                goToFragment(new FragmentProfile(), GlobalValues.PROFILE);
                setBottomBarActiveButtons(4);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void setBottomBarActiveButtons(int whichButton){
        if(whichButton == 1){
            IVHome.setImageResource(R.drawable.b1);
            IVMessaging.setImageResource(R.drawable.a2);
            IVGymnasium.setImageResource(R.drawable.a3);
            IVProfile.setImageResource(R.drawable.a4);
        }else if(whichButton == 2){
            IVHome.setImageResource(R.drawable.a1);
            IVMessaging.setImageResource(R.drawable.b2);
            IVGymnasium.setImageResource(R.drawable.a3);
            IVProfile.setImageResource(R.drawable.a4);
        }else if(whichButton == 3){
            IVHome.setImageResource(R.drawable.a1);
            IVMessaging.setImageResource(R.drawable.a2);
            IVGymnasium.setImageResource(R.drawable.b3);
            IVProfile.setImageResource(R.drawable.a4);
        }else if(whichButton == 4){
            IVHome.setImageResource(R.drawable.a1);
            IVMessaging.setImageResource(R.drawable.a2);
            IVGymnasium.setImageResource(R.drawable.a3);
            IVProfile.setImageResource(R.drawable.b4);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
