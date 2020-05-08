package com.dr.fit.fitness.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dr.fit.fitness.Fragment.FragmentHomepage;
import com.dr.fit.fitness.R;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;


/**
 * Created by batuhan on 2.05.2018.
 */

public class BuyPremiumActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler, View.OnClickListener {
    RelativeLayout RL1Year, RL3Months, RL1Month;
    BillingProcessor bp;
    boolean isReadyToPurchase = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_buy_premium);

        defineObjects();
    }

    private void defineObjects(){
        Toolbar toolbar = findViewById(R.id.myHomepageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        try {
            bp = new BillingProcessor(this, getString(R.string.google_play_license_key), this);
        }catch (Exception e){
            e.printStackTrace();
        }

        RL1Year = findViewById(R.id.RL1Year);
        RL3Months = findViewById(R.id.RL3Months);
        RL1Month = findViewById(R.id.RL1Month);
        RL1Year.setOnClickListener(this);
        RL3Months.setOnClickListener(this);
        RL1Month.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Log.d("Kontrol", "onProductPurchased");
        setResult(200);
        finish();
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


    }

    @Override
    public void onBillingInitialized() {
        Log.d("Kontrol", "onBillingInitialized");
        isReadyToPurchase = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.RL1Year:
                goToSubscribeProduct("twelve_month");
                break;
            case R.id.RL3Months:
                goToSubscribeProduct("three_month");
                break;
            case R.id.RL1Month:
                goToSubscribeProduct("one_month");
                break;
        }
    }

    private void goToSubscribeProduct(String productName){
        if(isReadyToPurchase){
            bp.subscribe(BuyPremiumActivity.this, productName);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
