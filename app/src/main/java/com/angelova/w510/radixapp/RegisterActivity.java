package com.angelova.w510.radixapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.angelova.w510.radixapp.dialogs.WarnDialog;
import com.angelova.w510.radixapp.tasks.RegisterTask;
import com.angelova.w510.radixapp.utils.Utils;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameInput;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private EditText mConfirmPassword;
    private Button mRegisterBtn;
    private LinearLayout mRegisterBtnLayout;
    private ProgressBar mLoader;

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
        mRegisterBtnLayout = (LinearLayout) findViewById(R.id.register_btn_layout);
        mLoader = (ProgressBar) findViewById(R.id.register_btn_loader);

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
                    try {
                        mRegisterBtn.setVisibility(View.GONE);
                        mLoader.setVisibility(View.VISIBLE);
                        String fullName = mNameInput.getText().toString();
                        String email = mEmailInput.getText().toString();

                        byte[] salt = Utils.ENCRYPTION_SALT.getBytes("Utf8");
                        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                        KeySpec spec = new PBEKeySpec(Utils.ENCRYPTION_PASSWORD.toCharArray(), salt, Utils.ENCRYPTION_ITERATION_COUNT, Utils.ENCRYPTION_KEY_STRENGTH);
                        SecretKey tmp = factory.generateSecret(spec);

                        Log.d("encryptString Key: ", new String(Base64.encode(tmp.getEncoded(), Base64.DEFAULT)));

                        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
                        c.init(Cipher.ENCRYPT_MODE, tmp);
                        byte[] encryptedEmail = c.doFinal(email.getBytes());
                        byte[] encryptedPass = c.doFinal(mPasswordInput.getText().toString().getBytes());
                        encryptedEmail = Base64.encode(encryptedEmail,Base64.DEFAULT);
                        encryptedPass = Base64.encode(encryptedPass, Base64.DEFAULT);
                        byte[] iv = c.getIV();

                        String encEmail = new String(encryptedEmail);
                        String encPass = new String(encryptedPass);
                        String encIv = new String(Base64.encode(iv, Base64.DEFAULT));
                        Log.d("encryptString: ", encEmail);
                        Log.d("encryptString iv:", encIv);


                       // String encEmail = Utils.encrypt(email);
//                        String encData = Utils.encrypt(mPasswordInput.getText().toString());
//                        String pass = encData;
                        new RegisterTask(RegisterActivity.this, "users/register", fullName, encPass, encEmail, encIv).execute();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void showRegistrationStatus(String status) {
        WarnDialog warning = new WarnDialog(this, "Registration", status, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                finish();
            }
        });
        warning.show();
    }

    public void showErrorMessage(String errorMsg) {
        mLoader.setVisibility(View.GONE);
        mRegisterBtn.setVisibility(View.VISIBLE);
        showAlertDialogNow(errorMsg, "Registration");
    }
}
