package com.angelova.w510.radixapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.angelova.w510.radixapp.menuItems.OfferActivity;
import com.angelova.w510.radixapp.models.Profile;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameInput;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private EditText mConfirmPassword;
    private Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeActivity();
    }

    private void initializeActivity() {
        mNameInput = (EditText) findViewById(R.id.name_input);
        mEmailInput = (EditText) findViewById(R.id.email_input);
        mPasswordInput = (EditText) findViewById(R.id.password_input);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password_input);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNameInput.getText() == null || mNameInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter your name before submitting the data", "Warning");
                } else if (mEmailInput.getText() == null || mEmailInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter your email before submitting the data", "Warning");
                } else if (mEmailInput.getText() != null && !mEmailInput.getText().toString().isEmpty() && !isValidEmail(mEmailInput.getText().toString())) {
                    showAlertDialogNow("Please enter a valid email address", "Warning");
                } else if (mPasswordInput.getText() == null || mPasswordInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter a password for your profile", "Warning");
                } else if (mConfirmPassword.getText() == null || mConfirmPassword.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please confirm your password", "Warning");
                } else if (!mPasswordInput.getText().toString().equals(mConfirmPassword.getText().toString())) {
                    showAlertDialogNow("Your password doesn't match the confirmation one. Please check your password", "Warning");
                } else {
                    Profile profile = new Profile();
                    profile.setName(mNameInput.getText().toString());
                    profile.setEmail(mEmailInput.getText().toString());
                    profile.setPassword(mPasswordInput.getText().toString());
                    //TODO: send the profile for registration to the server
                }
            }
        });
    }

    private void showAlertDialogNow(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
