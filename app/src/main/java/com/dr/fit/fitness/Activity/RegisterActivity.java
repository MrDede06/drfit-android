package com.dr.fit.fitness.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Helper.Utility;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.Login.Login;
import com.dr.fit.fitness.Retrofit.Register.Register;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by batuhanozkaya on 20.05.2017.
 */

public class RegisterActivity extends AppCompatActivity {
    TextView TVSignIn;
    AutoCompleteTextView ACTVName, ACTVSurname, ACTVEmail, ACTVPassword, ACTVBirthday, ACTVGender;
    String Name, Surname, Email, Password, Birthday;
    int Gender = 1;
    String FacebookID = "";
    BigInteger GoogleID;
    Button btnRegister, btnFacebook, btnGoogle;
    ImageView IVTOS;
    RestAdapter retrofit;
    RetrofitAPI retrofitAPI;
    GSharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    LoginButton btnFacebookButton;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        defineObjects();

        btnFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        try {
                            FacebookID = user.getString("id");
                            ACTVName.setText(user.getString("first_name"));
                            ACTVSurname.setText(user.getString("last_name"));
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
    }

    public void defineObjects(){
        Toolbar toolbar =  findViewById(R.id.myRegisterToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /** Facebook Login **/
        btnFacebookButton = new LoginButton(this);
        btnFacebookButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        /** Define APIs **/
        retrofit = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
        /** Define TextViews **/
        TVSignIn = findViewById(R.id.TVSignIn);
        /** Define AutoCompleteTextViews **/
        ACTVName =  findViewById(R.id.ACTVName);
        ACTVSurname = findViewById(R.id.ACTVSurname);
        ACTVEmail = findViewById(R.id.ACTVEmail);
        ACTVPassword =  findViewById(R.id.ACTVPass);
        ACTVBirthday =  findViewById(R.id.ACTVBirthday);
        ACTVGender =  findViewById(R.id.ACTVGender);
        /** Default Gender Value **/
        ACTVGender.setText(getString(R.string.male));

        /** Define Buttons **/
        btnRegister =  findViewById(R.id.btnRegister);
        btnFacebook =  findViewById(R.id.btnFacebook);
        btnGoogle = findViewById(R.id.btnGoogle);
        /** Define ImageViews **/
        IVTOS =  findViewById(R.id.IVTOS);
        IVTOS.setTag("UnCheck");
        /** Define Others **/
        sharedPreferences = new GSharedPreferences(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey("Name")){
                ACTVName.setText(getIntent().getExtras().get("Name").toString());
                ACTVSurname.setText(getIntent().getExtras().get("LastName").toString());
            }
        }

        IVTOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IVTOS.getTag().equals("UnCheck")){
                    IVTOS.setImageResource(R.drawable.check);
                    IVTOS.setTag("Check");
                }else if(IVTOS.getTag().equals("Check")){
                    IVTOS.setImageResource(R.drawable.uncheck);
                    IVTOS.setTag("UnCheck");
                }
            }
        });

        /** Static Settings **/
        TVSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSendToRegister();
            }
        });

        ACTVGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RegisterActivity.this);
                dialog.setContentView(R.layout.custom_alert_dialog_for_gender);
                dialog.setTitle(getString(R.string.select_your_gender));
                dialog.setCancelable(true);

                final RadioButton rbMale = dialog.findViewById(R.id.rbMale);
                final RadioButton rbFemale = dialog.findViewById(R.id.rbFemale);
                Button btnOK = dialog.findViewById(R.id.btnOK);

                rbMale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gender = 1;
                        ACTVGender.setText(getString(R.string.male));
                    }
                });

                rbFemale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gender = 0;
                        ACTVGender.setText(getString(R.string.female));
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });

        ACTVBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard(RegisterActivity.this);

                final DatePickerDialog datePicker;

                datePicker = new DatePickerDialog(RegisterActivity.this, R.style.MyOwnDatePicker, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, monthOfYear);

                        ACTVBirthday.setText(year + "-" + AppUtility.checkAndAddZero((monthOfYear + 1)) + "-" + AppUtility.checkAndAddZero(dayOfMonth));
                    }
                },1995,1,1);

                datePicker.setTitle(getString(R.string.select_your_birthday));
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.set), datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), datePicker);

                datePicker.show();
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
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(RegisterActivity.this, gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 5);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void attemptSendToRegister(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        View focusView = null;

        if(ACTVName.getText().toString().trim().length() == 0){
            ACTVName.setError(getString(R.string.please_fill_the_blank));
            focusView = ACTVName;
            focusView.requestFocus();
        }else if(ACTVSurname.getText().toString().trim().length() == 0){
            ACTVSurname.setError(getString(R.string.please_fill_the_blank));
            focusView = ACTVSurname;
            focusView.requestFocus();
        } else if(!Utility.isEmailValid(ACTVEmail.getText().toString())){
            ACTVEmail.setError(getString(R.string.email_address_format_incorrect));
            focusView = ACTVEmail;
            focusView.requestFocus();
        }else if(ACTVPassword.getText().toString().trim().length() == 0){
            ACTVPassword.setError(getString(R.string.please_fill_the_blank));
            focusView = ACTVPassword;
            focusView.requestFocus();
        }else if(ACTVGender.getText().toString().trim().length() == 0){
            ACTVGender.setError(getString(R.string.select_your_gender));
            focusView = ACTVGender;
            focusView.requestFocus();
        }else if(ACTVBirthday.getText().toString().trim().length() == 0){
            ACTVBirthday.setError(getString(R.string.please_fill_the_blank));
            focusView = ACTVBirthday;
            focusView.requestFocus();
        }else if(IVTOS.getTag().equals("UnCheck")){
            Utility.showAlertDialogOneButton(this, getString(R.string.check_tos));
        }else{
            Name = ACTVName.getText().toString();
            Surname = ACTVSurname.getText().toString();
            Email = ACTVEmail.getText().toString();
            Password = ACTVPassword.getText().toString();
            Birthday = ACTVBirthday.getText().toString();
            try {
                if(Utility.haveInternetConnection(RegisterActivity.this, true)){
                    Log.d("Kontrol", "Cinsiyet, Register: " + Gender);
                    progressDialog.show();
                    SendRegister();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void SendRegister(){
        retrofitAPI.Register(Name, Surname, Email, Birthday, Password, Gender, FacebookID, GoogleID, "android", sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<Register>() {
            @Override
            public void success(Register register, Response response) {
                progressDialog.dismiss();
                if(register.getResponse().equals("ok")){
                    sendLogin();
                    finish();
                }else if(register.getResponse().equals("non")){
                    Utility.showAlertDialogOneButton(RegisterActivity.this, register.getInfo());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                Utility.showAlertDialogOneButton(RegisterActivity.this, error.getMessage());
            }
        });
    }

    private void sendLogin(){
        progressDialog.show();

        retrofitAPI.Login(Email, Password, sharedPreferences.GET_PHONE_LANGUAGE(), new Callback<Login>() {
            @Override
            public void success(Login login, Response response) {
                progressDialog.dismiss();

                if(login.getResponse().equals("non")){
                    Utility.showAlertDialogOneButton(RegisterActivity.this, login.getInfo());
                }else if(login.getResponse().equals("ok")){
                    Log.d("Kontrol", "Ok PP: " + login.getLoginDetails().getProfileImage());
                    setSharedWithUserInformations(login);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();

                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        sharedPreferences.SET_GENDER(Gender);
        sharedPreferences.SET_PASSWORD(login.getLoginDetails().getPassword());
        sharedPreferences.SET_IS_PROGRAM_COMPLATE(login.isAllTestAreCompleted());

        startActivity(new Intent(RegisterActivity.this, HomePageActivity.class));
        finish();
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

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            ACTVEmail.setText(account.getEmail());
            ACTVName.setText(account.getGivenName());
            ACTVSurname.setText(account.getFamilyName());
            GoogleID = new BigInteger(account.getId());
        } catch (ApiException e) {
            Log.d("Kontrol", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
