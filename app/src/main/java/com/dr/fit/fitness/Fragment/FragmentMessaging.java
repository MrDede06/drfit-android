package com.dr.fit.fitness.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.dr.fit.fitness.Activity.NoInternetConnectionActivity;
import com.dr.fit.fitness.Adapter.MessagingAdapter;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Helper.Utility;
import com.dr.fit.fitness.Model.AddMessageModel;
import com.dr.fit.fitness.Model.MessagingModel;
import com.dr.fit.fitness.R;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by batuhan on 22.06.2018.
 */

public class FragmentMessaging extends Fragment implements View.OnClickListener,  BillingProcessor.IBillingHandler {
    View rootView;
    ListView LVMessaging;
    List<MessagingModel> messagingModel = new ArrayList<>();
    MessagingAdapter messagingAdapter;
    CardView CVSendMessage;
    EditText ETMessage;
    DatabaseReference myRef;
    GSharedPreferences sharedPreferences;
    String AdvertisingID = "default_id";
    BillingProcessor bp;
    boolean isReadyToPurchase = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_messasing, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        LVMessaging = rootView.findViewById(R.id.LVMessaging);
        CVSendMessage =  rootView.findViewById(R.id.CVSendMessage);
        CVSendMessage.setOnClickListener(this);
        ETMessage = rootView.findViewById(R.id.ETMessage);
        sharedPreferences = new GSharedPreferences(getActivity());
        task.execute();

        try {
            if(!Utility.haveInternetConnection(getContext(), false)){
                startActivity(new Intent(getContext(), NoInternetConnectionActivity.class));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bp = new BillingProcessor(getContext(), getString(R.string.google_play_license_key), this);
        }catch (Exception e){
            e.printStackTrace();
        }

        FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        getMessages();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.CVSendMessage:
                addToMessages();
                break;
        }
    }

    private void getMessages(){
        myRef.child("msgv2").child(String.valueOf(sharedPreferences.GET_USERID())).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messagingModel.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    boolean isAdmin = true;

                    if(ds.getValue(MessagingModel.class).getType().equals("user")){
                        isAdmin = false;
                    }

                    messagingModel.add(new MessagingModel(
                            ds.getValue(MessagingModel.class).getMessage(),
                            ds.getValue(MessagingModel.class).getType(),
                            ds.getValue(MessagingModel.class).getUserID(),
                            ds.getValue(MessagingModel.class).getImage(),
                            ds.getValue(MessagingModel.class).getAdvertisingID(),
                            isAdmin));
                }

                messagingAdapter = new MessagingAdapter(getContext(), messagingModel);
                LVMessaging.setAdapter(messagingAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Kontrol", "Failed to read value.", error.toException());
            }
        });
    }

    private void addToMessages(){
        if(ETMessage.getText().toString().length() > 20){
            bp.purchase(getActivity(), "message_one_time");
        }else{
            Utility.showAlertDialogOneButton(getActivity(), getString(R.string.type_minimum_20));
        }
    }

    private void addNewMessageToDatabase(String message) {
        AddMessageModel messageModel = new AddMessageModel(AdvertisingID, "", message, "user", sharedPreferences.GET_USERID());

        myRef.child("msgv2").child(String.valueOf(sharedPreferences.GET_USERID())).child("messages").push().setValue(messageModel);
        myRef.child("msgv2").child(String.valueOf(sharedPreferences.GET_USERID())).child("status").setValue("open");
    }

    AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String advertId = null;
            try{
                advertId = idInfo.getId();
            }catch (Exception e){
                e.printStackTrace();
            }
            return advertId;
        }
        @Override
        protected void onPostExecute(String advertId) {
            AdvertisingID = advertId;
        }
    };


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Log.d("Kontrol", "FragmentMessaging Product Purchased");
        addNewMessageToDatabase(ETMessage.getText().toString());

        ETMessage.setText("");
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        bp.consumePurchase("message_one_time");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.d("Kontrol", "onPurchaseHistoryRestored");
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Log.d("Kontrol", "onBillingError");
        Log.d("Kontrol", "ErrorCode: " + String.valueOf(errorCode));

        try{
            Log.d("Kontrol", "Error" + error.getMessage());
        }catch (NullPointerException npe){
        }

        if(errorCode == 105){ //Promosyon kodu ile alınırsa..

        }
    }

    @Override
    public void onBillingInitialized() {
        Log.d("Kontrol", "onBillingInitialized");
        isReadyToPurchase = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Kontrol", "FragmentMessaginActivityResult");
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
