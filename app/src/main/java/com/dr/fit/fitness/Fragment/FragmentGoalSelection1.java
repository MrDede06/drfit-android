package com.dr.fit.fitness.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dr.fit.fitness.Activity.GoalSelectionActivity;
import com.dr.fit.fitness.Activity.HomePageActivity;
import com.dr.fit.fitness.Activity.LoginActivity;
import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.CollectPersonalDataFromUser.CollectPersonalDataFromUser;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Batuhan Özkaya on 11.06.2017.
 */

public class FragmentGoalSelection1 extends Fragment implements View.OnClickListener{
    View rootView;
    Button btnEsthetic, btnPerformance, btnHypertrophy, btnSkip;
    RestAdapter retrofit;
    RetrofitAPI retrofitAPI;
    GSharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goal_selection_1, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        AppUtility.FullScreencall(getActivity());

        /** Buttons **/
        btnEsthetic = rootView.findViewById(R.id.btnEsthetic);
        btnPerformance = rootView.findViewById(R.id.btnPerformance);
        btnHypertrophy = rootView.findViewById(R.id.btnHypertrophy);
        btnSkip = rootView.findViewById(R.id.btnSkip);
        btnEsthetic.setOnClickListener(this);
        btnPerformance.setOnClickListener(this);
        btnHypertrophy.setOnClickListener(this);
        btnSkip.setOnClickListener(this);

        /** Define APIs **/
        retrofit = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
        sharedPreferences = new GSharedPreferences(getContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEsthetic:
                sendToAPI(1);
                break;
            case R.id.btnPerformance:
                sendToAPI(2);
                break;
            case R.id.btnHypertrophy:
                sharedPreferences.SET_GOAL_SELECTION(true);
                sendToAPI(3);
                break;
            case R.id.btnSkip:
                startActivity(new Intent(getContext(), HomePageActivity.class));
                break;

        }
    }

    private void sendToAPI(final int whichSelection){
        retrofitAPI.CollectPersonelDataFromUser(1, sharedPreferences.GET_USERID(), whichSelection, 0, 0, 0, sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<CollectPersonalDataFromUser>() {
            @Override
            public void success(CollectPersonalDataFromUser collectPersonalDataFromUser, Response response) {
                if(collectPersonalDataFromUser.getResponse().equals("ok")){
                    sharedPreferences.SET_GOAL_SELECTION_1(whichSelection);
                    ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection2());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
