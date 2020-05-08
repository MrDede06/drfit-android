package com.dr.fit.fitness.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Helper.Utility;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.ForgotPassword.ForgotPassword;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Batuhan Özkaya on 23.06.2017.
 */

public class ForgotPasswordActivity extends AppCompatActivity {
    AutoCompleteTextView ACTVEmail;
    TextView TVRegister;
    Button btnSendPassword;
    RestAdapter retrofit;
    RetrofitAPI retrofitAPI;
    GSharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        defineObjects();
    }

    private void defineObjects(){
        ACTVEmail = findViewById(R.id.ACTVEmail);
        btnSendPassword = findViewById(R.id.btnSendPassword);
        TVRegister =  findViewById(R.id.TVRegister);

        sharedPreferences = new GSharedPreferences(this);

        /** Define APIs **/
        retrofit = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);

        btnSendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attempToSendPassword();
            }
        });

        /** Static Settings **/
        TVRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    private void attempToSendPassword(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        View focusView = null;

        if(ACTVEmail.getText().toString().trim().length() == 0){
            ACTVEmail.setError(getString(R.string.please_fill_the_blank));
            focusView = ACTVEmail;
            focusView.requestFocus();
        }else{
            retrofitAPI.ForgotPassword(ACTVEmail.getText().toString().trim(), sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<ForgotPassword>() {
                @Override
                public void success(ForgotPassword forgotPassword, Response response) {
                    Utility.showAlertDialogOneButton(ForgotPasswordActivity.this, forgotPassword.getInfo());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
