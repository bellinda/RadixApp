package com.angelova.w510.radixapp.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.angelova.w510.radixapp.LoginActivity;
import com.angelova.w510.radixapp.RegisterActivity;
import com.angelova.w510.radixapp.clients.LoginRestClient;
import com.angelova.w510.radixapp.clients.RegistrationRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by W510 on 27.1.2018 г..
 */

public class LoginTask extends AsyncTask<Void, Void, Object> {

    private String password;
    private String email;
    private String url;
    private String iv;
    private Activity context;

    public LoginTask(Activity activity, String url, String email, String password, String iv) {
        this.context = activity;
        this.password = password;
        this.email = email;
        this.url = url;
        this.iv = iv;
    }

    @Override
    protected Object doInBackground(Void... params) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("username", email);
                    jsonParams.put("password", password);
                    jsonParams.put("iv", iv);
                    jsonParams.put("isMobile", true);

                    StringEntity entity = new StringEntity(jsonParams.toString());
                    System.out.println("JSON " + jsonParams.toString());

                    LoginRestClient.loginUser(context, url, entity, "application/json",
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                    try {
                                        if (response.has("status")) {
                                            System.out.println(">>>>>>>>>>>>>>>>> " + response.getString("status"));
                                            ((LoginActivity) context).handleSuccessfulLogin(response);
                                        }
                                    } catch (JSONException jse) {
                                        jse.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    try {
                                        if (errorResponse.has("status")) {
                                            ((LoginActivity) context).showErrorMessage(errorResponse.getString("status"));
                                        } else {
                                            ((LoginActivity) context).showErrorMessage("Something went wrong - " + throwable.getMessage());
                                        }
                                        System.out.println("ERROR " + throwable.getMessage());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        mainHandler.post(myRunnable);
        return null;
    }
}
