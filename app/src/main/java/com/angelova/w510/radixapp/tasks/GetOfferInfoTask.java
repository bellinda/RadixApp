package com.angelova.w510.radixapp.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.angelova.w510.radixapp.ForgotPassActivity;
import com.angelova.w510.radixapp.RegisterActivity;
import com.angelova.w510.radixapp.clients.OffersRestClient;
import com.angelova.w510.radixapp.clients.RegistrationRestClient;
import com.angelova.w510.radixapp.menuItems.OrderActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by W510 on 3.2.2018 г..
 */

public class GetOfferInfoTask extends AsyncTask<Void, Void, Object> {

    private String url;
    private Activity context;
    private String offerId;
    private String userId;
    private String accessToken;

    public GetOfferInfoTask(Activity activity, String url, String offerId, String userId, String accessToken) {
        this.context = activity;
        this.url = url;
        this.offerId = offerId;
        this.userId = userId;
        this.accessToken = accessToken;
    }

    @Override
    protected Object doInBackground(Void... params) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("offerId", offerId);
                    jsonParams.put("userId", userId);

                    StringEntity entity = new StringEntity(jsonParams.toString());
                    System.out.println("JSON " + jsonParams.toString());

                    OffersRestClient.getOne(context, url, entity, "application/json", "x-access-token", accessToken,
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    ((OrderActivity) context).showOfferDetails(response);
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                    try {
                                        if(response.length() > 0) {
                                            ((OrderActivity) context).showOfferDetails((JSONObject) response.get(0));
                                        } else {
                                            ((OrderActivity) context).showErrorMessage("No data found for this offer ID");
                                        }
                                    } catch (JSONException jse) {
                                        jse.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    ((OrderActivity)context).showErrorMessage("Something went wrong - " + throwable.getMessage());
                                    System.out.println("ERROR " + throwable.getMessage());
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    ((OrderActivity)context).showErrorMessage("Something went wrong - " + throwable.getMessage());
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
