package com.dr.fit.fitness.Fragment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.dr.fit.fitness.Activity.HomePageActivity;
import com.dr.fit.fitness.Helper.AlarmHelper;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.CollectPersonalDataFromUser.CollectPersonalDataFromUser;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;


import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.view.View.GONE;

/**
 * Created by batuhan on 5.05.2018.
 */

public class FragmentGoalSelection8 extends Fragment implements View.OnClickListener {
    View rootView;
    GSharedPreferences sharedPreferences;
    View popupViewForPauseWorkout;
    PopupWindow popupWindow;
    FrameLayout FLPauseWorkout;
    VideoView VVWorkouts;
    ImageView IVPause;
    TextView TVExerciseCount, TVSubtitles, TVHowManyRepeat;
    LinearLayout LLHowManyRepeat, LLZeroToEight, LLEightToFifteen, LLFifteenPlus;
    Button btnYes, btnNo, btnNext;
    Chronometer trainingTime;
    private MediaPlayer mpSong;
    long timeWhenStopped = 0;
    boolean isFirstVideo = true;
    Animation fadeInOutAnimation;
    int subtitleSize = 4, whichSubTitle, fadeInOutRepeatCount = 0, howManyRepeat = 0;
    List<String> GeneralSubtitles = new ArrayList<String>();
    RetrofitAPI retrofitAPI;
    RestAdapter restAdapter;
    boolean isMPMustPlay = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goal_selection_8, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        mpSong = MediaPlayer.create(getContext(), R.raw.bgsong);
        mpSong.setLooping(true);
        mpSong.start();

        sharedPreferences = new GSharedPreferences(getContext());

        btnNext = rootView.findViewById(R.id.btnNext);
        /** Define VideoView **/
        VVWorkouts = rootView.findViewById(R.id.VVWorkouts);
        /** Define ImageView **/
        IVPause = rootView.findViewById(R.id.IVPause);
        /** Define LinearLayout **/
        LLHowManyRepeat = rootView.findViewById(R.id.LLHowManyRepeat);
        LLZeroToEight = rootView.findViewById(R.id.LLZeroToEight);
        LLEightToFifteen = rootView.findViewById(R.id.LLEightToFifteen);
        LLFifteenPlus = rootView.findViewById(R.id.LLFifteenPlus);
        LLZeroToEight.setOnClickListener(this);
        LLEightToFifteen.setOnClickListener(this);
        LLFifteenPlus.setOnClickListener(this);
        /** Define Chronometer **/
        trainingTime = rootView.findViewById(R.id.CMTrainingTime);
        trainingTime.setFormat("%s");
        TVExerciseCount = rootView.findViewById(R.id.TVExerciseCount);
        TVSubtitles = rootView.findViewById(R.id.TVSubtitles);
        TVHowManyRepeat = rootView.findViewById(R.id.TVHowManyRepeat);

        restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = restAdapter.create(RetrofitAPI.class);

        subtitleVars(0);

        IVPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoPlayPause();
            }
        });

        VVWorkouts.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSubtitles();
                LLHowManyRepeat.setVisibility(View.VISIBLE);
                TVHowManyRepeat.setVisibility(View.VISIBLE);
                btnNext.setVisibility(GONE);
            }
        });

        startVideo();
        startTimer();
        setSubtitles();
    }

    private void setSubtitles(){
        TVSubtitles.setVisibility(View.VISIBLE);
        fadeInOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_and_out);
        TVSubtitles.clearAnimation();
        TVSubtitles.startAnimation(fadeInOutAnimation);

        whichSubTitle = 1;

        fadeInOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                TVSubtitles.setText(GeneralSubtitles.get(0));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                fadeInOutRepeatCount++;

                if(fadeInOutRepeatCount % 2 == 0){
                    if(subtitleSize != whichSubTitle){
                        whichSubTitle++;
                    }else{
                        whichSubTitle = 1;
                    }

                    TVSubtitles.setText(GeneralSubtitles.get(whichSubTitle - 1));
                }
            }
        });
    }

    private void stopSubtitles(){
        TVSubtitles.clearAnimation();
        TVSubtitles.setVisibility(GONE);
    }

    private void videoPlayPause(){
        if(IVPause.getTag().equals("Pause")){
            if(mpSong.isPlaying()){
                mpSong.pause();
            }

            VVWorkouts.pause();
            IVPause.setTag("Play");
            showPausePopup();
        }else if(IVPause.getTag().equals("Play")){
            if(isMPMustPlay){
                if(!mpSong.isPlaying()){
                    mpSong.start();
                    Log.d("Kontrol", "Buraya girdi 3 - PLAY");
                }
            }

            startTimer();
            VVWorkouts.start();
            IVPause.setTag("Pause");
        }
    }

    private void startVideo(){
        IVPause.setTag("Pause");
        String path;

        Log.d("Kontrol", "Gender: " + String.valueOf(sharedPreferences.GET_GENDER()));

        if(isFirstVideo){
            path = "android.resource://" + getContext().getPackageName() + "/" + R.raw.another_test_workout;
        }else{
            if(sharedPreferences.GET_GENDER() == 0){
                Log.d("Kontrol", "Gender: Kadın burası: " + String.valueOf(sharedPreferences.GET_GENDER()));
                path = "android.resource://" + getContext().getPackageName() + "/" + R.raw.female_test_workout;
            }else{
                Log.d("Kontrol", "Gender: erkek burası: " + String.valueOf(sharedPreferences.GET_GENDER()));
                path = "android.resource://" + getContext().getPackageName() + "/" + R.raw.male_test_workout;
            }
        }

        VVWorkouts.setMediaController(null);
        VVWorkouts.setVideoURI(Uri.parse(path));
        VVWorkouts.requestFocus();

        VVWorkouts.start();

    }

    private void startTimer(){
        trainingTime.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        trainingTime.start();
    }

    private void pauseTimer(){
        timeWhenStopped = trainingTime.getBase() - SystemClock.elapsedRealtime();
        trainingTime.stop();
    }

    private void stopTimer(){
        trainingTime.setBase(SystemClock.elapsedRealtime());
        trainingTime.stop();
        timeWhenStopped = 0;
    }

    private void goToHomepage(){
        mpSong.release();

        startActivity(new Intent(getContext(), HomePageActivity.class));
    }

    private void showPausePopup(){
        pauseTimer();

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupViewForPauseWorkout = layoutInflater.inflate(R.layout.popup_pause_workout, null);
        FLPauseWorkout =  popupViewForPauseWorkout.findViewById(R.id.FLPauseWorkout);
        btnYes = popupViewForPauseWorkout.findViewById(R.id.btnYes);
        btnNo = popupViewForPauseWorkout.findViewById(R.id.btnNo);

        View decorView = popupViewForPauseWorkout;
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        popupWindow = new PopupWindow(popupViewForPauseWorkout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.showAtLocation(FLPauseWorkout, Gravity.NO_GRAVITY, 0, 0);

        Blurry.with(getContext()).radius(5).sampling(3).async().animate(500).onto((ViewGroup) rootView.findViewById(R.id.RLContent));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Blurry.delete((ViewGroup) rootView.findViewById(R.id.RLContent));
                popupWindow.dismiss();
                videoPlayPause();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMPMustPlay = false;
                stopTimer();
                Blurry.delete((ViewGroup) rootView.findViewById(R.id.RLContent));
                popupWindow.dismiss();
                goToHomepage();

            }
        });
    }

    private void subtitleVars(int whichSubTitle){
        GeneralSubtitles.clear();
        if(whichSubTitle == 0){
            GeneralSubtitles.add(getString(R.string.goal_screen_8_general_subtitle_1));
            GeneralSubtitles.add(getString(R.string.goal_screen_8_general_subtitle_2));
            GeneralSubtitles.add(getString(R.string.goal_screen_8_general_subtitle_3));
            GeneralSubtitles.add(getString(R.string.goal_screen_8_general_subtitle_4));
        }else if(whichSubTitle == 1){
            GeneralSubtitles.add(getString(R.string.goal_screen_8_male_subtitle_1));
            GeneralSubtitles.add(getString(R.string.goal_screen_8_male_subtitle_2));
            GeneralSubtitles.add(getString(R.string.goal_screen_8_male_subtitle_3));
            GeneralSubtitles.add(getString(R.string.goal_screen_8_male_subtitle_4));
        }else if(whichSubTitle == 2){
            GeneralSubtitles.add(getString(R.string.goal_screen_8_female_subtitle_1));
            GeneralSubtitles.add(getString(R.string.goal_screen_8_female_subtitle_2));
            GeneralSubtitles.add(getString(R.string.goal_screen_8_female_subtitle_3));
            GeneralSubtitles.add(getString(R.string.goal_screen_8_female_subtitle_4));
        }
    }

    private void sendAnswerToAPI(int howManyRepeat){
        if(!isFirstVideo){
            retrofitAPI.CollectPersonelDataFromUserForTestLastStep(6, sharedPreferences.GET_USERID(), this.howManyRepeat, sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<CollectPersonalDataFromUser>() {
                @Override
                public void success(CollectPersonalDataFromUser collectPersonalDataFromUser, Response response) {
                    sharedPreferences.SET_IS_PROGRAM_COMPLATE(true);
                    sharedPreferences.SET_IS_PROGRAM_PREPARING(true);
                    AlarmHelper alarmHelper = new AlarmHelper();
                    alarmHelper.setAlarm(getContext());

                    if(mpSong.isPlaying()){
                        mpSong.release();
                    }

                    goToHomepage();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("Kontrol", error.getMessage());
                }
            });
        }else{
            this.howManyRepeat = howManyRepeat;
            isFirstVideo = false;
            TVExerciseCount.setText("2 / 2");
            LLHowManyRepeat.setVisibility(GONE);
            TVHowManyRepeat.setVisibility(GONE);
            btnNext.setVisibility(View.VISIBLE);

            if(sharedPreferences.GET_GENDER() == 0){
                subtitleVars(2);
            }else{
                subtitleVars(1);
            }

            setSubtitles();
            startVideo();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LLZeroToEight:
                sendAnswerToAPI(1);
                break;
            case R.id.LLEightToFifteen:
                sendAnswerToAPI(2);
                break;
            case R.id.LLFifteenPlus:
                sendAnswerToAPI(3);
                break;
        }
    }
}
