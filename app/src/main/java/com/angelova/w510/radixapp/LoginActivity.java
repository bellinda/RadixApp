package com.angelova.w510.radixapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.LoginTask;
import com.angelova.w510.radixapp.tasks.LogoutTask;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
                    mLoginBtn.setVisibility(View.GONE);
                    mLoginLoader.setVisibility(View.VISIBLE);
                    new LogoutTask(LoginActivity.this, "users/logout", mEmailInput.getText().toString()).execute();
                }
            }
        });

        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void handleSuccessfulLogout(JSONObject receivedData) {
        new LoginTask(LoginActivity.this, "users/login", mEmailInput.getText().toString(), mPasswordInput.getText().toString()).execute();
    }

    public void handleSuccessfulLogin(JSONObject receivedData) {
        Profile profile = new Profile();
        profile.setEmail(mEmailInput.getText().toString());
        try {
            profile.setToken(receivedData.getString("token"));
            profile.setName(receivedData.getString("fullName"));
            profile.setUserId(receivedData.getString("userID"));
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
        saveProfile(profile);

        mLoginLoader.setVisibility(View.GONE);
        mLoginBtn.setVisibility(View.VISIBLE);

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        // close this activity
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

    public void showErrorMessage(String errorMsg) {
        showAlertDialogNow(errorMsg, "Warning");
    }
}
