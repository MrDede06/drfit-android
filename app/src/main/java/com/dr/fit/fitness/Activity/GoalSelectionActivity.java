package com.dr.fit.fitness.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.dr.fit.fitness.Fragment.FragmentGoalSelection1;
import com.dr.fit.fitness.Fragment.FragmentReGoalSelection1;
import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.R;

/**
 * Created by Batuhan Özkaya on 11.06.2017.
 */

public class GoalSelectionActivity extends AppCompatActivity {
    GSharedPreferences sharedPreferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goal_selection);
        defineObjects();
    }

    private void defineObjects(){
        AppUtility.setTransparentStatusBar(this);
        sharedPreferences = new GSharedPreferences(this);

        if(sharedPreferences.GET_IS_MUST_GO_TO_RE_TEST()){
            sharedPreferences.SET_IS_MUST_GO_TO_RE_TEST(false);

            goToFragment(new FragmentReGoalSelection1());
        }else{
            goToFragment(new FragmentGoalSelection1());
        }
    }

    public void goToFragment(android.support.v4.app.Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        transaction.replace(R.id.FLHome, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
