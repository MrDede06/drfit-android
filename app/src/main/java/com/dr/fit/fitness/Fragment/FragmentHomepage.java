package com.dr.fit.fitness.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.dr.fit.fitness.Activity.GoalSelectionActivity;
import com.dr.fit.fitness.Activity.NoInternetConnectionActivity;
import com.dr.fit.fitness.Adapter.AllWorkoutAdapter;
import com.dr.fit.fitness.Adapter.MyWorkoutAdapter;
import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Helper.Utility;
import com.dr.fit.fitness.Model.AllWorkoutModel;
import com.dr.fit.fitness.Model.MyWorkoutModel;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.GetCategories.GetCategories;
import com.dr.fit.fitness.Retrofit.GetPre.GetPre;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Batuhan Özkaya on 10.06.2017.
 */

public class FragmentHomepage extends Fragment implements BillingProcessor.IBillingHandler {
    View rootView;
    RecyclerView RVMyWorkouts;
    ListView LVAllWorkout;
    List<AllWorkoutModel> allWorkoutList = new ArrayList<>();
    ArrayList<MyWorkoutModel> myWorkoutModelList;
    MyWorkoutAdapter myWorkoutAdapter;
    RestAdapter retrofit;
    RetrofitAPI retrofitAPI;
    GSharedPreferences sharedPreferences;
    RoundedImageView RIVProfilePicture;
    TextView TVName;
    Context mContext;
    PermissionListener permissionListener;
    BillingProcessor bp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        defineObjects();
        getSpecialDeviceID.execute();

        return rootView;
    }

    private void defineObjects(){
        mContext = getContext();
        /** Define TextViews **/
        TVName = rootView.findViewById(R.id.TVName);
        /** Define ImageViews **/
        RIVProfilePicture =  rootView.findViewById(R.id.RIVProfilePicture);
        /** Define Other **/
        RVMyWorkouts =  rootView.findViewById(R.id.RVMyWorkouts);
        LVAllWorkout = rootView.findViewById(R.id.LVAllWorkout);
        /** Define APIs **/
        retrofit = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
        sharedPreferences = new GSharedPreferences(mContext);

        try {
            bp = new BillingProcessor(getActivity(), getString(R.string.google_play_license_key), this);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            if(!Utility.haveInternetConnection(getContext(), false)){
                startActivity(new Intent(getContext(), NoInternetConnectionActivity.class));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getCategories();
        getAndSetProfileInformations();
        checkReadStoragePermission();

    }

    private void checkReadStoragePermission(){
        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                AppUtility.isVideosFolderCreatedAndOpen();
                if(sharedPreferences.GET_IS_FIRST_TEST_GOAL()){
                    startActivity(new Intent(getContext(), GoalSelectionActivity.class));
                    sharedPreferences.SET_IS_FIRST_TEST_GOAL(false);
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.read_external_storage_permission_need))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Dexter.withActivity(getActivity()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(permissionListener).check();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };

        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(permissionListener).check();
    }

    private void isUserPremiumGoogle(){
        bp.loadOwnedPurchasesFromGoogle();

        if(bp.listOwnedSubscriptions().size() > 0){
            sharedPreferences.SET_IS_PREMUIM(true);
            getMyWorkouts();
        }else{
            sharedPreferences.SET_IS_PREMUIM(false);
            isUserPremiumFromAdminPanel();
        }
    }

    private void getMyWorkouts(){
        myWorkoutModelList = new ArrayList<>();

        if(sharedPreferences.GET_IS_PREMUIM() && sharedPreferences.GET_IS_PROGRAM_COMPLATE()){
            ifPremiumProcessStartForPremium();
        }else{
            if(sharedPreferences.GET_IS_PROGRAM_COMPLATE()){
                if(sharedPreferences.GET_IS_PREMUIM()){
                    ifPremiumProcessStartForPremium();
                }else{
                    myWorkoutModelList.add(new MyWorkoutModel(getString(R.string.purchase_your_workout), 0));
                    myWorkoutModelList.add(new MyWorkoutModel(getString(R.string.purchase_your_workout), 0));
                    myWorkoutModelList.add(new MyWorkoutModel(getString(R.string.purchase_your_workout), 0));
                }
            }else{
                if(sharedPreferences.GET_IS_PROGRAM_PREPARING()){
                    myWorkoutModelList.add(new MyWorkoutModel(getString(R.string.workout_preparing_title), 0));
                    myWorkoutModelList.add(new MyWorkoutModel(getString(R.string.workout_preparing_title), 0));
                    myWorkoutModelList.add(new MyWorkoutModel(getString(R.string.workout_preparing_title), 0));
                }else{
                    myWorkoutModelList.add(new MyWorkoutModel(getString(R.string.complete_your_test), 0));
                    myWorkoutModelList.add(new MyWorkoutModel(getString(R.string.complete_your_test), 0));
                    myWorkoutModelList.add(new MyWorkoutModel(getString(R.string.complete_your_test), 0));
                }
            }
        }

        myWorkoutAdapter = new MyWorkoutAdapter(mContext, myWorkoutModelList);
        final LinearLayoutManager myWorkoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        RVMyWorkouts.setLayoutManager(myWorkoutManager);
        RVMyWorkouts.setAdapter(myWorkoutAdapter);

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(RVMyWorkouts);
    }

    private void getCategories(){
        retrofitAPI.GetCategories(sharedPreferences.GET_USERID(), sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<GetCategories>() {
            @Override
            public void success(GetCategories getCategories, Response response) {
                if(getCategories.getResponse().equals("ok")){
                    isUserMustGoToTest(getCategories);

                    if(getCategories.getCategories().size() > 0){
                        for (int i = 0; i < getCategories.getCategories().size(); i++) {
                            allWorkoutList.add(new AllWorkoutModel(i, getCategories.getCategories().get(i).getName(), getCategories.getCategories().get(i).getDescription(), getCategories.getCategories().get(i).getPhotoURL()));
                        }

                        AllWorkoutAdapter allWorkoutAdapter = new AllWorkoutAdapter(mContext, allWorkoutList);
                        LVAllWorkout.setAdapter(allWorkoutAdapter);

                        AppUtility.setListViewHeightBasedOnChildren(LVAllWorkout);
                        isUserPremiumGoogle();
                    }else{
                        Toast.makeText(mContext, "Hiç kategori yokmuş.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "Serverda hata var.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                isUserPremiumGoogle();
                Log.d("Kontrol", "AllWorkout Error: " + error.getMessage());
            }
        });
    }

    private void ifPremiumProcessStartForPremium(){
        retrofitAPI.GetCategories(sharedPreferences.GET_USERID(), sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<GetCategories>() {
            @Override
            public void success(GetCategories getCategories, Response response) {
                if(getCategories.getResponse().equals("ok")){
                    isUserMustGoToTest(getCategories);
                    sharedPreferences.SET_DAY_IN_WEEK_FOR_ARRAY(getCategories.getDayInWeekForArray());
                    sharedPreferences.SET_IS_NEXT_VIDEO_AVAIABLE(getCategories.getIsNextVideoAvailable());

                    if(getCategories.getPersonalProgram().size() > 0){
                        for (int i = 0; i < getCategories.getPersonalProgram().size(); i++) {
                            myWorkoutModelList.add(new MyWorkoutModel("Deneme", i, getCategories.getPersonalProgram().get(i).getTotalTime(), getCategories.getPersonalProgram().get(i).getMoveCount() , getCategories.getPersonalProgram().get(i).getPlace()));
                        }

                        myWorkoutAdapter = new MyWorkoutAdapter(mContext, myWorkoutModelList);
                        LinearLayoutManager myWorkoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                        RVMyWorkouts.setLayoutManager(myWorkoutManager);
                        RVMyWorkouts.setAdapter(myWorkoutAdapter);
                    }else{
                        Toast.makeText(mContext, "Hiç kategori yokmuş.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "Serverda hata var.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAndSetProfileInformations(){
        AppUtility.checkProfilePictureAndLoad(mContext, RIVProfilePicture);

        TVName.setText(sharedPreferences.GET_NAME());
    }

    private void isUserPremiumFromAdminPanel(){
        retrofitAPI.getPremium(sharedPreferences.GET_USERID(), new Callback<GetPre>() {
            @Override
            public void success(GetPre getPre, Response response) {
                if(getPre.getResponse().equals("ok")){
                    sharedPreferences.SET_IS_PREMUIM(getPre.isPremium());
                }

                getMyWorkouts();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Kontrol", error.getMessage());
                getMyWorkouts();
            }
        });
    }

    private void isUserMustGoToTest(GetCategories getCategories){
        Log.d("Kontrol", "isReCheck: " + String.valueOf(getCategories.isReCheck()));
        if(getCategories.isReCheck()){
            sharedPreferences.SET_IS_MUST_GO_TO_RE_TEST(true);
            sharedPreferences.SET_IS_PROGRAM_COMPLATE(false);
            sharedPreferences.SET_IS_PROGRAM_PREPARING(false);
        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 200){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    AsyncTask<Void, Void, String> getSpecialDeviceID = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String advertId = null;
            try{
                advertId = idInfo.getId();
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            return advertId;
        }

        @Override
        protected void onPostExecute(String advertId) {
            Log.d("Kontrol", "onPostExecute: " + advertId);
        }
    };

}
