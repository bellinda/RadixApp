package com.angelova.w510.radixapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.angelova.w510.radixapp.dialogs.WarnDialog;
import com.angelova.w510.radixapp.list_fragments.AllOrdersFragment;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.LoginTask;
import com.angelova.w510.radixapp.tasks.LogoutTask;
import com.angelova.w510.radixapp.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailInput;
    private EditText mPasswordInput;
    private Button mLoginBtn;
    private TextView mForgotPass;
    private TextView mSignUpNew;
    private ProgressBar mLoginLoader;

    public static final String SHARED_PROFILE_KEY = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeActivity();
    }

    private void initializeActivity() {
        mEmailInput = (EditText) findViewById(R.id.email_input);
        mPasswordInput = (EditText) findViewById(R.id.password_input);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mForgotPass = (TextView) findViewById(R.id.forgot_pass);
        mSignUpNew = (TextView) findViewById(R.id.sign_up);
        mLoginLoader = (ProgressBar) findViewById(R.id.login_btn_loader);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEmailInput.getText() == null || mEmailInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter your email before trying to login", "Warning");
                } else if (mPasswordInput.getText() == null || mPasswordInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter your password before trying to login", "Warning");
                } else {
                    if(isNetworkAvailable()) {
                        mLoginBtn.setVisibility(View.GONE);
                        mLoginLoader.setVisibility(View.VISIBLE);
                        Profile profile = getProfile();
                        if(profile != null && profile.getEmail() != null) {
                            System.out.println("LOGIN OUT " + profile.getEmail());
                            new LogoutTask(LoginActivity.this, "users/logout", profile.getEmail()).execute();
                        } else {
                            new LogoutTask(LoginActivity.this, "users/logout", mEmailInput.getText().toString()).execute();
                        }
                    } else {
                        showAlertDialogNow("No internet connection. Please turn on your Wi-Fi ir mobile data", "Warning");
                    }
                }
            }
        });

        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPassActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        mSignUpNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                // close this activity
                //finish();
            }
        });
    }

    private void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
            }
        });
        warning.show();
    }

    public void handleSuccessfulLogout(JSONObject receivedData) {
        try {
            byte[] salt = Utils.ENCRYPTION_SALT.getBytes("Utf8");
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(Utils.ENCRYPTION_PASSWORD.toCharArray(), salt, Utils.ENCRYPTION_ITERATION_COUNT, Utils.ENCRYPTION_KEY_STRENGTH);
            SecretKey tmp = factory.generateSecret(spec);

            Log.d("encryptString Key: ", new String(Base64.encode(tmp.getEncoded(), Base64.DEFAULT)));

            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, tmp);
            byte[] encryptedEmail = c.doFinal(mEmailInput.getText().toString().getBytes());
            byte[] encryptedPass = c.doFinal(mPasswordInput.getText().toString().getBytes());
            encryptedEmail = Base64.encode(encryptedEmail,Base64.DEFAULT);
            encryptedPass = Base64.encode(encryptedPass, Base64.DEFAULT);
            byte[] iv = c.getIV();

            String encEmail = new String(encryptedEmail);
            String encPass = new String(encryptedPass);
            String encIv = new String(Base64.encode(iv, Base64.DEFAULT));
            Log.d("encryptString: ", encEmail);
            Log.d("encryptString iv:", encIv);

            new LoginTask(LoginActivity.this, "users/login", encEmail, encPass, encIv).execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleSuccessfulLogin(JSONObject receivedData) {
        try {
            Profile profile = new Profile();
            profile.setEmail(mEmailInput.getText().toString());
            profile.setToken(receivedData.getString("token"));
            profile.setName(receivedData.getString("fullName"));
            profile.setUserId(receivedData.getString("userID"));
            saveProfile(profile);

            mLoginLoader.setVisibility(View.GONE);
            mLoginBtn.setVisibility(View.VISIBLE);

            boolean shouldResetPass = receivedData.getBoolean("needsNewPassSet");
            if(shouldResetPass) {
                showSetNewPasswordLayout();
            } else {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                // close this activity
                finish();
            }
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
    }

    private void showSetNewPasswordLayout() {
        Intent i = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();
    }

    private void saveProfile(Profile profile) {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = appPreferences.edit();
        Gson gson = new Gson();

        String updatedJson = gson.toJson(profile);
        prefsEditor.putString(SHARED_PROFILE_KEY, updatedJson);
        prefsEditor.apply();
    }

    private Profile getProfile() {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        Profile profile = new Profile();
        String json = appPreferences.getString(SHARED_PROFILE_KEY, "");
        if(!json.isEmpty()) {
            profile = gson.fromJson(json, Profile.class);
        }
        return profile;
    }

    public void showErrorMessage(String errorMsg) {
        mLoginLoader.setVisibility(View.GONE);
        mLoginBtn.setVisibility(View.VISIBLE);

        showAlertDialogNow(errorMsg, "Warning");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
