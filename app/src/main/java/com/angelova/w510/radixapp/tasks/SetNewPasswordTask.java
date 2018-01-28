package com.angelova.w510.radixapp.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.angelova.w510.radixapp.ForgotPassActivity;
import com.angelova.w510.radixapp.ResetPasswordActivity;
import com.angelova.w510.radixapp.clients.RegistrationRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by W510 on 28.1.2018 г..
 */

public class SetNewPasswordTask extends AsyncTask<Void, Void, Object> {

    private String oldPassword;
    private String newPassword;
    private String userId;
    private String url;
    private Activity context;

    public SetNewPasswordTask(Activity activity, String url, String oldPassword, String newPassword, String userId) {
        this.context = activity;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.userId = userId;
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
                    jsonParams.put("_id", userId);
                    jsonParams.put("oldPassword", oldPassword);
                    jsonParams.put("newPassword", newPassword);

                    StringEntity entity = new StringEntity(jsonParams.toString());
                    System.out.println("JSON " + jsonParams.toString());

                    RegistrationRestClient.setNewUserPassword(context, url, entity, "application/json",
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    ((ResetPasswordActivity) context).handleSuccessfulPasswordSet(response);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    ((ResetPasswordActivity)context).showErrorMessage("Something went wrong - " + throwable.getMessage());
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
