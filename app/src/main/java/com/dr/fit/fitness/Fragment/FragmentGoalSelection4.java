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

import com.afollestad.materialdialogs.MaterialDialog;
import com.dr.fit.fitness.Activity.GoalSelectionActivity;
import com.dr.fit.fitness.Activity.HomePageActivity;
import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Helper.Utility;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.CollectPersonalDataFromUser.CollectPersonalDataFromUser;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by batuhan on 4.05.2018.
 */

public class FragmentGoalSelection4 extends Fragment implements View.OnClickListener {
    View rootView;
    Button btnHeight, btnWeight, btnContinue, btnBack, btnSkip;
    RestAdapter retrofit;
    RetrofitAPI retrofitAPI;
    GSharedPreferences sharedPreferences;
    int Height = 0, Weight = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goal_selection_4, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        AppUtility.FullScreencall(getActivity());

        /** Buttons **/
        btnBack = rootView.findViewById(R.id.btnBack);
        btnSkip = rootView.findViewById(R.id.btnSkip);
        btnHeight = rootView.findViewById(R.id.btnHeight);
        btnWeight = rootView.findViewById(R.id.btnWeight);
        btnContinue = rootView.findViewById(R.id.btnContinue);
        btnHeight.setOnClickListener(this);
        btnWeight.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
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
            case R.id.btnHeight:
                openDialog(true);
                break;
            case R.id.btnWeight:
                openDialog(false);
                break;
            case R.id.btnContinue:
                lastProcess();
                break;
            case R.id.btnBack:
                ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection3());
                break;
            case R.id.btnSkip:
                startActivity(new Intent(getContext(), HomePageActivity.class));
                break;
        }
    }

    private void sendToAPI(){
        retrofitAPI.CollectPersonelDataFromUserForHeightWeight(4, sharedPreferences.GET_USERID(), Weight, Height, sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<CollectPersonalDataFromUser>() {
            @Override
            public void success(CollectPersonalDataFromUser collectPersonalDataFromUser, Response response) {
                if(collectPersonalDataFromUser.getResponse().equals("ok")){
                    ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection5());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialog(final boolean isItHeight){
        List<String> listItems = new ArrayList<String>();
        String titleSelection;

        if(isItHeight){
            titleSelection = getString(R.string.height_screen_4);
            for (int i = 100; i <= 230; i++) {listItems.add(String.valueOf(i));}
        }else{
            titleSelection = getString(R.string.weight_screen_4);
            for (int i = 35; i <= 200; i++) {listItems.add(String.valueOf(i));}
        }

        final CharSequence[] charSequenceItems = listItems.toArray(new CharSequence[listItems.size()]);

        new MaterialDialog.Builder(getContext()).title(titleSelection).items(charSequenceItems).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                if(isItHeight){
                    Height = Integer.parseInt(text.toString());
                    btnHeight.setText(text.toString() + " CM");
                }else{
                    Weight = Integer.parseInt(text.toString());
                    btnWeight.setText(text.toString() + " KG");
                }

                Log.d("Kontrol", text.toString());
            }
        }).show();
    }

    private void lastProcess(){
        if(Height != 0 && Weight != 0){
           sendToAPI();
        }else{
            Utility.showAlertDialogOneButton(getContext(), getString(R.string.error_100));
        }
    }
}