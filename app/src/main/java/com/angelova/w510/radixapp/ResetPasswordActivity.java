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

import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.ResetPasswordTask;
import com.angelova.w510.radixapp.tasks.SetNewPasswordTask;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText mOldPassInput;
    private EditText mNewPassInput;
    private EditText mConfirmPassInput;
    private Button mSendBtn;

    public static final String SHARED_PROFILE_KEY = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initializeActivity();
    }

    private void initializeActivity() {
        mOldPassInput = (EditText) findViewById(R.id.old_pass_input);
        mNewPassInput = (EditText) findViewById(R.id.new_password_input);
        mConfirmPassInput = (EditText) findViewById(R.id.confirm_password_input);
        mSendBtn = (Button) findViewById(R.id.send_btn);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOldPassInput.getText() == null || mOldPassInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter your new password your password", "Warning");
                } else if (mNewPassInput.getText() == null || mNewPassInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter your new password your password", "Warning");
                } else if (mConfirmPassInput.getText() == null || mConfirmPassInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please confirm your password", "Warning");
                } else if (!mNewPassInput.getText().toString().equals(mConfirmPassInput.getText().toString())) {
                    showAlertDialogNow("Your password doesn't match the confirmation one. Please check your password", "Warning");
                } else {
                    new SetNewPasswordTask(ResetPasswordActivity.this, "users/setNewPassword", mOldPassInput.getText().toString(),
                            mNewPassInput.getText().toString(), getProfile().getUserId()).execute();
                }
            }
        });
    }

    private void showAlertDialogNow(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void handleSuccessfulPasswordSet(JSONObject receivedData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
        builder.setMessage("Your password is set successfully. Please login in your profile with it.").setTitle("Password set");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                Intent i = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                // close this activity
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showErrorMessage(String errorMsg) {
        showAlertDialogNow(errorMsg, "Registration");
    }

    private Profile getProfile() {
        //get current profile from shared preferences (is available)
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        Profile profile = new Profile();
        String json = appPreferences.getString(SHARED_PROFILE_KEY, "");
        if(!json.isEmpty()) {
            profile = gson.fromJson(json, Profile.class);
        }
        return profile;
    }
}
