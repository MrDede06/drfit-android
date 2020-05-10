package com.dr.fit.fitness.Activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.ProgressBarAnimation;
import com.dr.fit.fitness.Model.ProgramDetailModel;
import com.dr.fit.fitness.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;


/**
 * Created by Batuhan Özkaya on 15.06.2017.
 */

public class WorkoutActivity extends AppCompatActivity implements View.OnClickListener {
    View popupViewForPauseWorkout, popupVieweForRestWorkout;
    PopupWindow popupPause, popupRest;
    FrameLayout FLPauseWorkout, FLRest;
    VideoView VVWorkouts;
    ImageView IVPause;
    TextView TVExerciseCount, TVReplyCount, TVExerciseName, TVSetAndRepeat, TVNext, TVSubtitles;
    RelativeLayout RLRep1, RLRep2, RLRep3, RLRep4, RLNextSetOrExercise;
    LinearLayout LLVideoInformation;
    Button btnYes, btnNo, btnNext;
    Chronometer trainingTime;
    private MediaPlayer mpSong;
    long timeWhenStopped = 0;
    List<ProgramDetailModel> programDetailModel = new ArrayList<>();
    int whichIDVideoPlaying = 0, totalSetCount, totalReplyCount, whichSetPlaying = 1, subtitleSize = 0, whichSubTitle = 1, fadeInOutRepeatCount = 0;
    Animation fadeInOutAnimation;
    boolean isMPMustPlay = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_workout);

        defineObjects();
    }

    private void defineObjects(){
        mpSong = MediaPlayer.create(this, R.raw.bgsong);
        mpSong.setLooping(true);
        mpSong.start();

        btnNext = findViewById(R.id.btnNext);
        /** Define VideoView **/
        VVWorkouts = findViewById(R.id.VVWorkouts);
        /** Define ImageView **/
        IVPause = findViewById(R.id.IVPause);
        /** Define Chronometer **/
        trainingTime = findViewById(R.id.CMTrainingTime);
        trainingTime.setFormat("%s");
        /** TextViews **/
        TVExerciseCount = findViewById(R.id.TVExerciseCount);
        TVReplyCount = findViewById(R.id.TVReplyCount);
        TVSetAndRepeat = findViewById(R.id.TVSetAndRepeat);
        TVExerciseName = findViewById(R.id.TVExerciseName);
        TVNext = findViewById(R.id.TVNext);
        TVSubtitles = findViewById(R.id.TVSubtitles);
        /** RelativeLayouts **/
        RLRep1 = findViewById(R.id.RLRep1);
        RLRep2 = findViewById(R.id.RLRep2);
        RLRep3 = findViewById(R.id.RLRep3);
        RLRep4 = findViewById(R.id.RLRep4);
        RLNextSetOrExercise = findViewById(R.id.RLNextSetOrExercise);
        LLVideoInformation = findViewById(R.id.LLVideoInformation);

        VVWorkouts.setOnClickListener(this);
        IVPause.setOnClickListener(this);

        programDetailModel = (List<ProgramDetailModel>) getIntent().getExtras().getSerializable("AllVideos");

        VVWorkouts.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        RLNextSetOrExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSetAndNextVideoConfigurations();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSetAndNextVideoConfigurations();
            }
        });

        Log.d("Kontrol", "whichIDVideo: " + String.valueOf(whichIDVideoPlaying));
        Log.d("Kontrol", "programDetailModeSize: " + String.valueOf(programDetailModel.size()));
        if(AppUtility.isVideoExistInVideosFolder(programDetailModel.get(whichIDVideoPlaying).getVideoUrl())){
            startTimer();
            setDatasForNextVideo();
            setVideoInformationTexts();
            preConfigurationBeforeStartExercise();
            setSubtitles();
            configurationForSetCountsAndDesigns();
        }
    }

    private void nextSetAndNextVideoConfigurations(){
        if(whichSetPlaying == totalSetCount){
            setNextVideo();
        }else{
            whichSetPlaying++;
            configurationSetDesigns();
            setVideoInformationTexts();
        }

        videoInformationFadeOut();

    }

    private void setNextVideo(){
        if((whichIDVideoPlaying + 1) == programDetailModel.size()){
            isMPMustPlay = false;

            if(mpSong.isPlaying()){
                mpSong.release();
            }

            Intent intent = new Intent(this, CongratulationsActivity.class);
            intent.putExtra("SubcateID", getIntent().getExtras().getString("SubcateID"));
            intent.putExtra("isPremiumExercise", getIntent().getExtras().getBoolean("isPremiumExercise"));
            startActivity(intent);
        }else{
            whichSetPlaying = 1;
            whichIDVideoPlaying++;
            setDatasForNextVideo();
            setVideoInformationTexts();
            configurationSetDesigns();
            setSubtitles();
            configurationForSetCountsAndDesigns();
            preConfigurationBeforeStartExercise();
        }

    }

    private void setSubtitles(){
        fadeInOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_and_out);
        TVSubtitles.clearAnimation();
        TVSubtitles.startAnimation(fadeInOutAnimation);
        subtitleSize = programDetailModel.get(whichIDVideoPlaying).getAltyazilar().size();

        whichSubTitle = 1;

        fadeInOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                TVSubtitles.setText(programDetailModel.get(whichIDVideoPlaying).getAltyazilar().get(0));
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

                    TVSubtitles.setText(programDetailModel.get(whichIDVideoPlaying).getAltyazilar().get(whichSubTitle - 1));
                }
            }
        });
    }

    private void setDatasForNextVideo(){
        totalSetCount = programDetailModel.get(whichIDVideoPlaying).getSetCount();
        totalReplyCount = programDetailModel.get(whichIDVideoPlaying).getReplyCount();
        Log.d("Kontrol", "Setcount: " + String.valueOf(programDetailModel.get(whichIDVideoPlaying).getSetCount()));
        Log.d("Kontrol", "Replycount: " + String.valueOf(programDetailModel.get(whichIDVideoPlaying).getReplyCount()));
    }

    private void videoInformationFadeOut(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 0f);
        valueAnimator.setDuration(4000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                TVExerciseName.setAlpha(alpha);
                TVSetAndRepeat.setAlpha(alpha);
                TVNext.setAlpha(alpha);
            }
        });
        valueAnimator.start();
    }

    private void setVideoInformationTexts(){
        TVExerciseName.setText(programDetailModel.get(whichIDVideoPlaying).getVideoName());
        TVSetAndRepeat.setText(String.valueOf(whichSetPlaying)+". Set " + totalReplyCount + " " + getString(R.string.repeat));
    }

    private void preConfigurationBeforeStartExercise(){
        TVExerciseCount.setText(String.valueOf(whichIDVideoPlaying + 1) + " / " + String.valueOf(programDetailModel.size()));
        TVReplyCount.setText(String.valueOf(programDetailModel.get(whichIDVideoPlaying).getReplyCount()) + " " + getString(R.string.repeat));
        videoInformationFadeOut();

        if(programDetailModel.get(whichIDVideoPlaying).getVideoName().equals("rest")){
            TVSubtitles.clearAnimation();
            showRestPopup();
        }else{
            startVideo(Environment.getExternalStorageDirectory() + "/DrFitVideos/" + programDetailModel.get(whichIDVideoPlaying).getVideoUrl());
        }

    }

    private void configurationSetDesigns(){
        if(whichSetPlaying == 2){
            RLRep1.setBackgroundResource(R.drawable.workout_info_circle);
            RLRep2.setBackgroundResource(R.drawable.workout_info_circle_active);
        }else if(whichSetPlaying == 3){
            RLRep2.setBackgroundResource(R.drawable.workout_info_circle);
            RLRep3.setBackgroundResource(R.drawable.workout_info_circle_active);
        }else if(whichSetPlaying == 4){
            RLRep3.setBackgroundResource(R.drawable.workout_info_circle);
            RLRep4.setBackgroundResource(R.drawable.workout_info_circle_active);
        }else{
            RLRep1.setBackgroundResource(R.drawable.workout_info_circle_active);
            RLRep2.setBackgroundResource(R.drawable.workout_info_circle);
            RLRep3.setBackgroundResource(R.drawable.workout_info_circle);
            RLRep4.setBackgroundResource(R.drawable.workout_info_circle);
        }
    }

    private void configurationForSetCountsAndDesigns(){
        RLRep2.setVisibility(View.GONE);
        RLRep3.setVisibility(View.GONE);
        RLRep4.setVisibility(View.GONE);

        if(totalSetCount > 1){RLRep2.setVisibility(View.VISIBLE);}
        if(totalSetCount > 2){RLRep3.setVisibility(View.VISIBLE);}
        if(totalSetCount > 3){RLRep4.setVisibility(View.VISIBLE);}
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.IVPause:
                videoPlayPause();
                break;
        }
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

    private void startVideo(String VideoURL){
        IVPause.setTag("Pause");

        VVWorkouts.setMediaController(null);
        VVWorkouts.setVideoPath(VideoURL);
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
        if(mpSong.isPlaying()){

        }

        mpSong.pause();
        finish();
        startActivity(new Intent(WorkoutActivity.this, HomePageActivity.class));
    }

    private void showPausePopup(){
        pauseTimer();

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupViewForPauseWorkout = layoutInflater.inflate(R.layout.popup_pause_workout, null);
        FLPauseWorkout =  popupViewForPauseWorkout.findViewById(R.id.FLPauseWorkout);
        btnYes = popupViewForPauseWorkout.findViewById(R.id.btnYes);
        btnNo = popupViewForPauseWorkout.findViewById(R.id.btnNo);

        View decorView = popupViewForPauseWorkout;
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        popupPause = new PopupWindow(popupViewForPauseWorkout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupPause.setAnimationStyle(R.style.Animation);
        popupPause.showAtLocation(FLPauseWorkout, Gravity.NO_GRAVITY, 0, 0);

        Blurry.with(WorkoutActivity.this).radius(5).sampling(3).async().animate(500).onto((ViewGroup) findViewById(R.id.RLContent));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Blurry.delete((ViewGroup) findViewById(R.id.RLContent));
                popupPause.dismiss();
                videoPlayPause();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMPMustPlay = false;
                stopTimer();
                Log.d("Kontrol", "Buraya girdi 1");
                Blurry.delete((ViewGroup) findViewById(R.id.RLContent));
                popupPause.dismiss();
                goToHomepage();

            }
        });
    }

    private void showRestPopup(){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupVieweForRestWorkout = layoutInflater.inflate(R.layout.popup_workout_rest, null);
        FLRest = popupVieweForRestWorkout.findViewById(R.id.FLRest);
        final ProgressBar PBLoadingPercentage = popupVieweForRestWorkout.findViewById(R.id.PBLoadingPercentage);
        final TextView TVCountTimer = popupVieweForRestWorkout.findViewById(R.id.TVCountTimer);

        View decorView = popupVieweForRestWorkout;
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        popupRest = new PopupWindow(popupVieweForRestWorkout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupRest.setAnimationStyle(R.style.Animation);
        popupRest.showAtLocation(FLRest, Gravity.NO_GRAVITY, 0, 0);

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                TVCountTimer.setText(String.valueOf(millisUntilFinished / 1000));

                Log.d("Kontrol", "Millis: " + String.valueOf(millisUntilFinished));

                if(millisUntilFinished / 1000 <= 1){
                    setNextVideo();
                    popupRest.dismiss();
                }
            }

            public void onFinish() {

            }

        }.start();

        ProgressBarAnimation anim = new ProgressBarAnimation(PBLoadingPercentage, 100, 0);
        anim.setDuration(30000);
        PBLoadingPercentage.startAnimation(anim);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Kontrol", "onPause giriyor da ondan");
        if(isMPMustPlay){
            videoPlayPause();
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}