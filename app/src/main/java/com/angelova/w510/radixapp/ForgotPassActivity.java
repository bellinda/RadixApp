package com.angelova.w510.radixapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.angelova.w510.radixapp.dialogs.WarnDialog;
import com.angelova.w510.radixapp.tasks.ResetPasswordTask;

import org.json.JSONObject;

public class ForgotPassActivity extends AppCompatActivity {

    private EditText mEmailInput;
    private Button mForgotPassBtn;
    private ProgressBar mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        initializeActivity();
    }

    private void initializeActivity() {
        mEmailInput = (EditText) findViewById(R.id.email_input);
        mForgotPassBtn = (Button) findViewById(R.id.forgot_pass_btn);
        mLoader = (ProgressBar) findViewById(R.id.forgot_pass_btn_loader);

        mForgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEmailInput.getText() == null || mEmailInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter your email", "Warning");
                } else {
                    mForgotPassBtn.setVisibility(View.GONE);
                    mLoader.setVisibility(View.VISIBLE);
                    new ResetPasswordTask(ForgotPassActivity.this, "users/resetPassword", mEmailInput.getText().toString()).execute();
                }
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

    public void handleSuccessfulPasswordReset(JSONObject receivedData) {
        final WarnDialog warning = new WarnDialog(this, "Reset password", "Please check your email for your new password", new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
                Intent i = new Intent(ForgotPassActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        warning.show();
    }

    public void showErrorMessage(String errorMsg) {
        mLoader.setVisibility(View.GONE);
        mForgotPassBtn.setVisibility(View.VISIBLE);
        showAlertDialogNow(errorMsg, "Warning");
    }
}
