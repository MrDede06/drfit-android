package com.dr.fit.fitness.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Helper.MyProgressDialog;
import com.dr.fit.fitness.Helper.Utility;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.Login.Login;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by batuhanozkaya on 20.05.2017.
 */

public class LoginActivity extends AppCompatActivity {
    TextView TVRegister;
    Button btnSignIn, btnFacebook, btnGoogle;
    RelativeLayout RLForgotPassword;
    AutoCompleteTextView ACTVEmail, ACTVPass;
    RestAdapter retrofit;
    RetrofitAPI retrofitAPI;
    String Email, Pass;
    ProgressDialog progressDialog;
    GSharedPreferences sharedPreferences;
    LoginButton btnFacebookButton;
    CallbackManager callbackManager;
    long FacebookID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        defineObjects();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = ACTVEmail.getText().toString();
                Pass = ACTVPass.getText().toString();

                attemptToLogin();
            }
        });

        btnFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        try {
                            FacebookID = Long.parseLong(user.getString("id"));

                            sendLoginWithFacebook(String.valueOf(FacebookID), user.getString("first_name"), user.getString("last_name"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }});
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("Kontrol", "Cancel'e girdi");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Kontrol", "Error'e girdi " + error.getLocalizedMessage());
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFacebookButton.performClick();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 5);
            }
        });
    }

    public  void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("Kontrol", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("Kontrol", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("Kontrol", "printHashKey()", e);
        }
    }

    public void defineObjects(){
        Toolbar toolbar = findViewById(R.id.myLoginToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /** Facebook Login **/
        btnFacebookButton = new LoginButton(this);
        btnFacebookButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        /** Define TextViews **/
        TVRegister =  findViewById(R.id.TVRegister);
        ACTVEmail =  findViewById(R.id.ACTVEmail);
        ACTVPass =  findViewById(R.id.ACTVPass);
        /** Define Buttons **/
        btnSignIn =  findViewById(R.id.btnSignIn);
        btnFacebook =  findViewById(R.id.btnFacebook);
        btnGoogle = findViewById(R.id.btnGoogle);
        RLForgotPassword =  findViewById(R.id.RLForgotPassword);
        /** Define APIs **/
        retrofit = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
        /** Define Others **/
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        sharedPreferences = new GSharedPreferences(this);

        /** Static Settings **/
        TVRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        RLForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });
    }

    private void attemptToLogin(){
        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        View focusView = null;

        if(!Utility.isEmailValid(Email) || Email.length() == 0) {
            ACTVEmail.setError(getString(R.string.please_fill_the_blank));
            focusView = ACTVEmail;
            focusView.requestFocus();
        }else if(TextUtils.isEmpty(Pass)){
            ACTVPass.setError(getString(R.string.please_fill_the_blank));
            focusView = ACTVPass;
            focusView.requestFocus();
        }else{
            sendLogin();
        }
    }

    private void sendLogin(){
        progressDialog.show();

        retrofitAPI.Login(Email, Pass, sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<Login>() {
            @Override
            public void success(Login login, Response response) {
                progressDialog.dismiss();

                Log.d("Kontrol", login.getResponse());

                if(login.getResponse().equals("nof")){
                    Utility.showAlertDialogOneButton(LoginActivity.this, login.getInfo());
                }else if(login.getResponse().equals("non")){
                    Utility.showAlertDialogOneButton(LoginActivity.this, login.getInfo());
                }else if(login.getResponse().equals("ok")){
                    setSharedWithUserInformations(login);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();

                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendLoginWithFacebook(String Facebook, final String Name, final String LastName){
        Log.d("Kontrol", "FacebookID: " + Facebook);
        progressDialog.show();

        retrofitAPI.LoginWithFacebook(Facebook, sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<Login>() {
            @Override
            public void success(Login login, Response response) {
                progressDialog.dismiss();

                Log.d("Kontrol", "Login: " + login.getResponse());

                if(login.getResponse().equals("non")){
                    Utility.showAlertDialogOneButton(LoginActivity.this, login.getInfo());
                }else if(login.getResponse().equals("nof")){
                    sendRegister(Name, LastName);
                }else if(login.getResponse().equals("ok")){
                    setSharedWithUserInformations(login);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();

                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendLoginWithGoogle(String GoogleID){
        progressDialog.show();

        retrofitAPI.LoginWithGoogle(GoogleID, sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<Login>() {
            @Override
            public void success(Login login, Response response) {
                progressDialog.dismiss();

                if(login.getResponse().equals("non")){
                    Utility.showAlertDialogOneButton(LoginActivity.this, login.getInfo());
                }else if(login.getResponse().equals("ok")){
                    setSharedWithUserInformations(login);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();

                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendRegister(String Name, String LastName){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra("Name", Name);
        intent.putExtra("LastName", LastName);
        startActivity(intent);
        finish();
    }

    private void setSharedWithUserInformations(Login login){
        sharedPreferences.SET_USERID(login.getLoginDetails().getUserID());
        sharedPreferences.SET_FACEBOOKID(login.getLoginDetails().getFacebookID());
        sharedPreferences.SET_PROFILE_IMAGE(login.getLoginDetails().getProfileImage());
        sharedPreferences.SET_USER_POINT(login.getLoginDetails().getUserPoint());
        sharedPreferences.SET_IS_PREMUIM(login.isPremium());
        sharedPreferences.SET_STEP(login.getStep());
        sharedPreferences.SET_NAME(login.getLoginDetails().getFullname());
        sharedPreferences.SET_SURNAME(login.getLoginDetails().getSurname());
        sharedPreferences.SET_EMAIL(login.getLoginDetails().getEmail());
        sharedPreferences.SET_BIRTHDAY(login.getLoginDetails().getBirthday());
        sharedPreferences.SET_GENDER(login.getLoginDetails().getGender());
        sharedPreferences.SET_PASSWORD(login.getLoginDetails().getPassword());
        sharedPreferences.SET_IS_PROGRAM_COMPLATE(login.isAllTestAreCompleted());

        startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            sendLoginWithGoogle(account.getId());
        } catch (ApiException e) {
            Log.d("Kontrol", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
}
