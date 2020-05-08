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

import com.dr.fit.fitness.Adapter.SubcateAdapter;
import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Model.ProgramDetailModel;
import com.dr.fit.fitness.Model.SubcateModel;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.GetCategories.GetCategories;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Batuhan Özkaya on 3.12.2017.
 */

public class WorkoutPreviewActivity extends AppCompatActivity {
    Button btnExerciseCount, btnExerciseTime, btnBack;
    TextView TVFreeWeight, TVBodyWeight, TVDownload;
    RelativeLayout RLFreeWeight, RLBodyWeight;
    LinearLayout LLDownload;
    int videoID, position;
    List<ProgramDetailModel> programDetailList = new ArrayList<>();
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
                    AppUtility.downloadVideos(WorkoutPreviewActivity.this, getIntent().getStringArrayListExtra("AllVideos"), onComplete);
                    progress = ProgressDialog.show(WorkoutPreviewActivity.this, getString(R.string.exercises_downloading), getString(R.string.please_wait_until_videos_are_downloaded), true);
                }else{
                    Intent intent = new Intent(WorkoutPreviewActivity.this, WorkoutActivity.class);
                    intent.putStringArrayListExtra("AllVideos", getIntent().getStringArrayListExtra("AllVideos"));
                    intent.putExtra("ExerciseCount", getIntent().getExtras().getInt("ExerciseCount"));
                    intent.putExtra("AllVideos", (ArrayList<ProgramDetailModel>) programDetailList);
                    intent.putExtra("SubcateID", getIntent().getExtras().getString("SubcateID"));
                    intent.putExtra("isPremiumExercise", false);
                    startActivity(intent);
                }
            }
        });

        checkVideosExistAndPerformProcess();
        getProgramDetails();
    }

    private void getProgramDetails(){
        retrofitAPI.GetCategories(sharedPreferences.GET_USERID(), sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<GetCategories>() {
            @Override
            public void success(GetCategories getCategories, Response response) {
                if(getCategories.getResponse().equals("ok")){
                    if(getCategories.getCategories().get(videoID).getSubcates().get(position).getProgramDetails().size() > 0){
                        for (int i = 0; i < getCategories.getCategories().get(videoID).getSubcates().get(position).getProgramDetails().size(); i++) {
                            programDetailList.add(new ProgramDetailModel(
                                    getCategories.getCategories().get(videoID).getSubcates().get(position).getProgramDetails().get(i).getReplyCount(),
                                    getCategories.getCategories().get(videoID).getSubcates().get(position).getProgramDetails().get(i).isItDuration(),
                                    getCategories.getCategories().get(videoID).getSubcates().get(position).getProgramDetails().get(i).getAltyazilar(),
                                    getCategories.getCategories().get(videoID).getSubcates().get(position).getProgramDetails().get(i).getVideoURL(),
                                    getCategories.getCategories().get(videoID).getSubcates().get(position).getProgramDetails().get(i).getDuration(),
                                    getCategories.getCategories().get(videoID).getSubcates().get(position).getProgramDetails().get(i).getIsim(),
                                    getCategories.getCategories().get(videoID).getSubcates().get(position).getProgramDetails().get(i).getSetCount(),
                                    getCategories.getCategories().get(videoID).getSubcates().get(position).getProgramDetails().get(i).getMoveCount()));
                        }
                    }else{
                        Log.d("Kontrol", "Program YOK!");
                    }
                }else{
                    Toast.makeText(WorkoutPreviewActivity.this, "Serverda hata var.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Kontrol", error.getMessage());
            }
        });
    }

    private void checkVideosExistAndPerformProcess(){
        howManyVideosWillDownload = 0;
        if(getIntent().getStringArrayListExtra("AllVideos").size() > 0){
            for (int i = 0; i < getIntent().getStringArrayListExtra("AllVideos").size(); i++) {
                if(!AppUtility.isVideoExistInVideosFolder(getIntent().getStringArrayListExtra("AllVideos").get(i))){
                    howManyVideosWillDownload++;
                }
            }

            for (int i = 0; i < getIntent().getStringArrayListExtra("AllVideos").size(); i++) {
                if(AppUtility.isVideoExistInVideosFolder(getIntent().getStringArrayListExtra("AllVideos").get(i))){
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
            Toast.makeText(WorkoutPreviewActivity.this, "Video yok, ne yapacağız?", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
