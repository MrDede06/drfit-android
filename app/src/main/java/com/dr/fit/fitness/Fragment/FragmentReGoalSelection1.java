package com.dr.fit.fitness.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

public class FragmentReGoalSelection1 extends Fragment implements View.OnClickListener{
    View rootView;
    Button btnYes, btnNo;
    RestAdapter retrofit;
    RetrofitAPI retrofitAPI;
    GSharedPreferences sharedPreferences;
    TextView TVWhatIsYourGoal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_re_goal_selection_1, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        AppUtility.FullScreencall(getActivity());

        /** Buttons **/
        btnYes = rootView.findViewById(R.id.btnYes);
        btnNo = rootView.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

        TVWhatIsYourGoal = rootView.findViewById(R.id.TVWhatIsYourGoal);

        /** Define APIs **/
        retrofit = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
        sharedPreferences = new GSharedPreferences(getContext());

        if(sharedPreferences.GET_GOAL_SELECTION_1() == 1){
            TVWhatIsYourGoal.setText(getString(R.string.selection_1) + " " + getString(R.string.title_re_goal));
        }else if(sharedPreferences.GET_GOAL_SELECTION_1() == 2){
            TVWhatIsYourGoal.setText(getString(R.string.selection_2) + " " + getString(R.string.title_re_goal));
        }else if(sharedPreferences.GET_GOAL_SELECTION_1() == 3){
            TVWhatIsYourGoal.setText(getString(R.string.selection_3) + " " + getString(R.string.title_re_goal));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnYes:
                ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentReGoalSelection2());
                break;
            case R.id.btnNo:
                ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection1());
                break;
        }
    }
}
