package com.angelova.w510.radixapp.tasks;

import android.app.Activity;
import android.net.nsd.NsdManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.angelova.w510.radixapp.RegisterActivity;
import com.angelova.w510.radixapp.clients.RegistrationRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by W510 on 27.1.2018 г..
 */

public class RegisterTask extends AsyncTask<Void, Void, Object> {

    private String password;
    private String fullName;
    private String email;
    private String url;
    private Activity context;

    public RegisterTask(Activity activity, String url, String fullName, String password, String email) {
        this.fullName = fullName;
        this.context = activity;
        this.password = password;
        this.email = email;
        this.url = url;
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
                    jsonParams.put("email", email);
                    jsonParams.put("firstname", fullName);

                    StringEntity entity = new StringEntity(jsonParams.toString());
                    System.out.println("JSON " + jsonParams.toString());

                    RegistrationRestClient.registerUser(context, url, entity, "application/json",
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                    try {
                                        if (response.has("status")) {
                                            ((RegisterActivity) context).showRegistrationStatus(response.getString("status"));
                                        }
                                    } catch (JSONException jse) {
                                        jse.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    ((RegisterActivity)context).showErrorMessage("Something went wrong - " + throwable.getMessage());
                                    System.out.println("ERROR " + throwable.getMessage());
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
