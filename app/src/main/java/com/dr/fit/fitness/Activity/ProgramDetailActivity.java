package com.dr.fit.fitness.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dr.fit.fitness.Adapter.SubcateAdapter;
import com.dr.fit.fitness.Helper.GSharedPreferences;
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
 * Created by batuhanozkaya on 22.05.2017.
 */

public class ProgramDetailActivity extends AppCompatActivity {
    TextView TVProgramTitle;
    List<SubcateModel> subcateList = new ArrayList<>();
    ListView LVProgramDetail;
    RestAdapter restAdapter;
    RetrofitAPI retrofitAPI;
    Bundle extras;
    GSharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_program_details);
        defineObjects();

    }

    public void defineObjects(){
        sharedPreferences = new GSharedPreferences(this);
        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        extras = getIntent().getExtras();

        TVProgramTitle = findViewById(R.id.TVProgramTitle);
        TVProgramTitle.setText(extras.getString("WorkoutTitle") + " :");

        LVProgramDetail =  findViewById(R.id.LVProgramDetails);

        /** Define APIs **/
        restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = restAdapter.create(RetrofitAPI.class);

        getSubcates();
    }

    private void getSubcates(){
        retrofitAPI.GetCategories(sharedPreferences.GET_USERID(), sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<GetCategories>() {
            @Override
            public void success(GetCategories getCategories, Response response) {
                if(getCategories.getResponse().equals("ok")){
                    if(getCategories.getCategories().size() > 0){
                        for (int i = 0; i < getCategories.getCategories().get(Integer.parseInt(extras.getString("ID"))).getSubcates().size(); i++) {
                            subcateList.add(new SubcateModel(
                                    getCategories.getCategories().get(Integer.parseInt(extras.getString("ID"))).getSubcates().get(i).getSubcateID(),
                                    Integer.parseInt(extras.getString("ID")),
                                    getCategories.getCategories().get(Integer.parseInt(extras.getString("ID"))).getSubcates().get(i).getName(),
                                    getCategories.getCategories().get(Integer.parseInt(extras.getString("ID"))).getSubcates().get(i).getDescription(),
                                    getCategories.getCategories().get(Integer.parseInt(extras.getString("ID"))).getSubcates().get(i).getPhotoURL(),
                                    getCategories.getCategories().get(Integer.parseInt(extras.getString("ID"))).getSubcates().get(i).getTotalTime(),
                                    getCategories.getCategories().get(Integer.parseInt(extras.getString("ID"))).getSubcates().get(i).isPremium(),
                                    getCategories.getCategories().get(Integer.parseInt(extras.getString("ID"))).getSubcates().get(i).getAllVideos().size(),
                                    getCategories.getCategories().get(Integer.parseInt(extras.getString("ID"))).getSubcates().get(i).getPlace(),
                                    getCategories.getCategories().get(Integer.parseInt(extras.getString("ID"))).getSubcates().get(i).getAllVideos()));

                        }

                        SubcateAdapter subcateAdapter = new SubcateAdapter(ProgramDetailActivity.this, subcateList);
                        LVProgramDetail.setAdapter(subcateAdapter);

                    }else{
                        Log.d("Kontrol", "Kategori yokmus. Dikkat!!");
                    }
                }else{
                    Toast.makeText(ProgramDetailActivity.this, "Serverda hata var.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
