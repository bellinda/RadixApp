package com.angelova.w510.radixapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.angelova.w510.radixapp.tasks.ResetPasswordTask;

import org.json.JSONObject;

public class ForgotPassActivity extends AppCompatActivity {

    private EditText mEmailInput;
    private Button mForgotPassBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        initializeActivity();
    }

    private void initializeActivity() {
        mEmailInput = (EditText) findViewById(R.id.email_input);
        mForgotPassBtn = (Button) findViewById(R.id.forgot_pass_btn);

        mForgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEmailInput.getText() == null || mEmailInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter your email", "Warning");
                } else {
                    new ResetPasswordTask(ForgotPassActivity.this, "users/resetPassword", mEmailInput.getText().toString()).execute();
                }
            }
        });
    }

    private void showAlertDialogNow(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassActivity.this);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void handleSuccessfulPasswordReset(JSONObject receivedData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassActivity.this);
        builder.setMessage("Please check your email for your new password").setTitle("Reset password");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                Intent i = new Intent(ForgotPassActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showErrorMessage(String errorMsg) {
        showAlertDialogNow(errorMsg, "Warning");
    }
}
