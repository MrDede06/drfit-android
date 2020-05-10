package com.dr.fit.fitness.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Model.ProgramDetailModel;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.GetCategories.GetCategories;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by batuhan on 4.06.2018.
 */

public class MyWorkoutPreviewActivity extends AppCompatActivity {
    Button btnExerciseCount, btnExerciseTime, btnBack;
    TextView TVFreeWeight, TVBodyWeight, TVDownload;
    RelativeLayout RLFreeWeight, RLBodyWeight;
    LinearLayout LLDownload;
    int videoID, position;
    List<ProgramDetailModel> programDetailList = new ArrayList<>();
    ArrayList<String> allVideos = new ArrayList<>();
    boolean isVideosExist = false;
    int howManyVideosWillDownload = 0;
    int downloadedVideoCount = 0;
    ProgressDialog progress;
    RetrofitAPI retrofitAPI;
    RestAdapter restAdapter;
    GSharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_workout_preview);
        defineObjects();
    }

    private void defineObjects(){
        btnExerciseCount = findViewById(R.id.btnExerciseCount);
        btnExerciseTime = findViewById(R.id.btnExerciseTime);
        btnBack = findViewById(R.id.btnBack);

        TVFreeWeight = findViewById(R.id.TVFreeWeight);
        TVBodyWeight = findViewById(R.id.TVBodyWeight);
        TVDownload = findViewById(R.id.TVDownload);

        RLFreeWeight = findViewById(R.id.RLFreeWeight);
        RLBodyWeight = findViewById(R.id.RLBodyWeight);

        LLDownload = findViewById(R.id.LLDownload);

        btnExerciseCount.setText(getIntent().getExtras().getInt("ExerciseCount") + " " + getString(R.string.exercise));
        btnExerciseTime.setText(getIntent().getExtras().getString("TotalTime"));

        sharedPreferences = new GSharedPreferences(this);

        videoID = Integer.parseInt(getIntent().getExtras().getString("ID"));
        position = getIntent().getExtras().getInt("Position");

        /** Define APIs **/
        restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = restAdapter.create(RetrofitAPI.class);

        if(getIntent().getExtras().getString("Place").equals("1")){
            RLFreeWeight.setBackgroundResource(R.drawable.background_button_category_30_percent);
            TVFreeWeight.setTextColor(getResources().getColor(R.color.transparent_30_percent));
        }else{
            RLBodyWeight.setBackgroundResource(R.drawable.background_button_category_30_percent);
            TVBodyWeight.setTextColor(getResources().getColor(R.color.transparent_30_percent));
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LLDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isVideosExist){
                    AppUtility.downloadVideos(MyWorkoutPreviewActivity.this, allVideos, onComplete);
                    progress = ProgressDialog.show(MyWorkoutPreviewActivity.this, getString(R.string.exercises_downloading), getString(R.string.please_wait_until_videos_are_downloaded), true);
                }else{
                    Intent intent = new Intent(MyWorkoutPreviewActivity.this, WorkoutActivity.class);
                    intent.putExtra("ExerciseCount", getIntent().getExtras().getInt("ExerciseCount"));
                    intent.putExtra("AllVideos", (ArrayList<ProgramDetailModel>) programDetailList);
                    intent.putExtra("SubcateID", getIntent().getExtras().getString("ID"));
                    intent.putExtra("isPremiumExercise", getIntent().getExtras().getBoolean("isPremiumExercise"));
                    startActivity(intent);
                }
            }
        });


        getProgramDetails();
    }

    private void checkVideosExistAndPerformProcess(){
        howManyVideosWillDownload = 0;
        if(allVideos.size() > 0){
            for (int i = 0; i < allVideos.size(); i++) {
                if(!AppUtility.isVideoExistInVideosFolder(allVideos.get(i))){
                    howManyVideosWillDownload++;
                }
            }

            for (int i = 0; i < allVideos.size(); i++) {
                if(AppUtility.isVideoExistInVideosFolder(allVideos.get(i))){
                    isVideosExist = true;
                }else{
                    isVideosExist = false;
                    break;
                }
            }

            if(isVideosExist){
                TVDownload.setText(R.string.start);
            }else{
                TVDownload.setText(R.string.download);
            }
        }else{
            Toast.makeText(MyWorkoutPreviewActivity.this, "Video yok, ne yapacağız?", Toast.LENGTH_SHORT).show();
        }
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            downloadedVideoCount++;
            if(downloadedVideoCount == howManyVideosWillDownload){
                downloadedVideoCount = 0;
                TVDownload.setText(getString(R.string.start));
                isVideosExist = true;
                progress.dismiss();
            }
        }
    };

    private void getProgramDetails(){
        progress = ProgressDialog.show(MyWorkoutPreviewActivity.this, getString(R.string.loading), getString(R.string.loading_please_wait), true);

        retrofitAPI.GetCategories(sharedPreferences.GET_USERID(), sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<GetCategories>() {
            @Override
            public void success(GetCategories getCategories, Response response) {
                if(getCategories.getResponse().equals("ok")){
                    if(getCategories.getPersonalProgram().get(videoID).getDayList().size() > 0){
                        for (int i = 0; i < getCategories.getPersonalProgram().get(videoID).getDayList().size(); i++) {
                            programDetailList.add(new ProgramDetailModel(
                                    getCategories.getPersonalProgram().get(videoID).getDayList().get(i).getReplyCount(),
                                    getCategories.getPersonalProgram().get(videoID).getDayList().get(i).getWhichDay(),
                                    getCategories.getPersonalProgram().get(videoID).getDayList().get(i).getVideoURL(),
                                    getCategories.getPersonalProgram().get(videoID).getDayList().get(i).getDuration(),
                                    getCategories.getPersonalProgram().get(videoID).getDayList().get(i).getIsim(),
                                    getCategories.getPersonalProgram().get(videoID).getDayList().get(i).getSetCount(),
                                    getCategories.getPersonalProgram().get(videoID).getDayList().get(i).getAltyazilar()
                            ));
                        }

                        for (int i = 0; i < getCategories.getPersonalProgram().get(videoID).getDailyVideoList().size(); i++) {
                            allVideos.add(getCategories.getPersonalProgram().get(videoID).getDailyVideoList().get(i));
                        }

                        checkVideosExistAndPerformProcess();

                        progress.dismiss();
                    }else{
                        progress.dismiss();
                        Log.d("Kontrol", "Program YOK!");
                    }
                }else{
                    progress.dismiss();
                    Toast.makeText(MyWorkoutPreviewActivity.this, "Serverda hata var.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                Log.d("Kontrol", error.getMessage());
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
