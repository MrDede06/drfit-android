package com.dr.fit.fitness.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

public class FragmentGoalSelection3 extends Fragment implements View.OnClickListener {
    View rootView;
    Button btn3, btn4, btn5, btnBack, btnSkip;
    RestAdapter retrofit;
    RetrofitAPI retrofitAPI;
    GSharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goal_selection_3, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        AppUtility.FullScreencall(getActivity());

        /** Buttons **/
        btnBack = rootView.findViewById(R.id.btnBack);
        btnSkip = rootView.findViewById(R.id.btnSkip);
        btn3 =  rootView.findViewById(R.id.btn3);
        btn4 =  rootView.findViewById(R.id.btn4);
        btn5 =  rootView.findViewById(R.id.btn5);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSkip.setOnClickListener(this);

        /** Define APIs **/
        retrofit = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
        sharedPreferences = new GSharedPreferences(getContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn3:
                sendToAPI(3);
                break;
            case R.id.btn4:
                sendToAPI(4);
                break;
            case R.id.btn5:
                sendToAPI(5);
                break;
            case R.id.btnBack:
                ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection2());
                break;
            case R.id.btnSkip:
                startActivity(new Intent(getContext(), HomePageActivity.class));
                break;
        }
    }

    private void sendToAPI(int whichSelection){
        Log.d("Kontrol", "1");
        retrofitAPI.CollectPersonelDataFromUser(3, sharedPreferences.GET_USERID(), whichSelection, 0, 0, 0, sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<CollectPersonalDataFromUser>() {
            @Override
            public void success(CollectPersonalDataFromUser collectPersonalDataFromUser, Response response) {
                Log.d("Kontrol", "2");
                Log.d("Kontrol", "Response: " + collectPersonalDataFromUser.getResponse());
                if(collectPersonalDataFromUser.getResponse().equals("ok")){
                    Log.d("Kontrol", "3");
                    ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection4());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

