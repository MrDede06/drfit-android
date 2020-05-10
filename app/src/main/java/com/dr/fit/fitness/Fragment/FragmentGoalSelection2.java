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

public class FragmentGoalSelection2 extends Fragment implements View.OnClickListener {
    View rootView;
    Button btnBodyWeight, btnFreeWeight, btnBack, btnSkip;
    RestAdapter retrofit;
    RetrofitAPI retrofitAPI;
    GSharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goal_selection_2, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        AppUtility.FullScreencall(getActivity());

        /** Buttons **/
        btnBack = rootView.findViewById(R.id.btnBack);
        btnSkip = rootView.findViewById(R.id.btnSkip);
        btnBodyWeight = rootView.findViewById(R.id.btnBodyWeight);
        btnFreeWeight = rootView.findViewById(R.id.btnFreeWeight);
        btnBodyWeight.setOnClickListener(this);
        btnFreeWeight.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSkip.setOnClickListener(this);

        /** Define APIs **/
        retrofit = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
        sharedPreferences = new GSharedPreferences(getContext());

        if(sharedPreferences.GET_GOAL_SELECTION()){
            btnBodyWeight.setVisibility(View.GONE);
            sharedPreferences.SET_GOAL_SELECTION(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBodyWeight:
                sendToAPI(1);
                break;
            case R.id.btnFreeWeight:
                sendToAPI(2);
                break;
            case R.id.btnBack:
                ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection1());
                break;
            case R.id.btnSkip:
                startActivity(new Intent(getContext(), HomePageActivity.class));
                break;
        }
    }

    private void sendToAPI(final int whichSelection){
        retrofitAPI.CollectPersonelDataFromUser(2, sharedPreferences.GET_USERID(), whichSelection, 0, 0, 0, sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<CollectPersonalDataFromUser>() {
            @Override
            public void success(CollectPersonalDataFromUser collectPersonalDataFromUser, Response response) {
                if(collectPersonalDataFromUser.getResponse().equals("ok")){
                    sharedPreferences.SET_GOAL_SELECTION_2(whichSelection);
                    ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection3());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
