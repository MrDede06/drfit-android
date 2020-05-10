package com.dr.fit.fitness.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

public class FragmentGoalSelection5 extends Fragment implements View.OnClickListener {
    View rootView;
    ViewPager mViewPager;
    Button btnItsMyType, btnBack, btnSkip;
    RestAdapter retrofit;
    RetrofitAPI retrofitAPI;
    GSharedPreferences sharedPreferences;
    int whichSelection = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goal_selection_5, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        AppUtility.FullScreencall(getActivity());

        /** Buttons **/
        btnItsMyType = rootView.findViewById(R.id.btnItsMyType);
        btnBack = rootView.findViewById(R.id.btnBack);
        btnSkip = rootView.findViewById(R.id.btnSkip);
        btnBack.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
        btnItsMyType.setOnClickListener(this);

        /** Define APIs **/
        retrofit = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
        sharedPreferences = new GSharedPreferences(getContext());

        mViewPager = rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(new SamplePagerAdapter(getChildFragmentManager()));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                whichSelection = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnItsMyType:
                sendToAPI();
                break;
            case R.id.btnBack:
                ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection4());
                break;
            case R.id.btnSkip:
                startActivity(new Intent(getContext(), HomePageActivity.class));
                break;
        }
    }

    private void sendToAPI(){
        retrofitAPI.CollectPersonelDataFromUser(5, sharedPreferences.GET_USERID(), whichSelection + 1, 0, 0, 0, sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<CollectPersonalDataFromUser>() {
            @Override
            public void success(CollectPersonalDataFromUser collectPersonalDataFromUser, Response response) {
                if(collectPersonalDataFromUser.getResponse().equals("ok")){
                    ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection6());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class SamplePagerAdapter extends FragmentPagerAdapter {

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("Kontrol", "POS: " + String.valueOf(position));
            if (position == 0) {
                return new BodyType1Fragment();
            }else if(position == 1){
                return new BodyType2Fragment();
            }else if(position == 2){
                return new BodyType3Fragment();
            }else{
                return new BodyType1Fragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
