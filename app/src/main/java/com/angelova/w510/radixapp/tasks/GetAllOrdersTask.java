package com.angelova.w510.radixapp.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.angelova.w510.radixapp.MainActivity;
import com.angelova.w510.radixapp.clients.OffersRestClient;
import com.angelova.w510.radixapp.OrderActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by W510 on 11.2.2018 г..
 */

public class GetAllOrdersTask extends AsyncTask<Void, Void, Object> {

    private String url;
    private Activity context;
    private String userId;
    private String accessToken;

    public GetAllOrdersTask(Activity activity, String url,  String userId, String accessToken) {
        this.context = activity;
        this.url = url;
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
                    jsonParams.put("allOrders", true);
                    jsonParams.put("_id", userId);

                    StringEntity entity = new StringEntity(jsonParams.toString());
                    System.out.println("JSON " + jsonParams.toString());

                    OffersRestClient.get(context, url, entity, "application/json", "x-access-token", accessToken,
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    ((OrderActivity) context).showOfferDetails(response);
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                    if (context instanceof MainActivity) {
                                        ((MainActivity)context).handleSuccessfulOrdersDownload(response);
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    //((ProfileActivity)context).showErrorMessage("Something went wrong - " + throwable.getMessage());
                                    System.out.println("ERROR " + throwable.getMessage());
                                    ((MainActivity)context).handleErrorOnOrdersGet("Something went wrong - " + throwable.getMessage());
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    //((ProfileActivity)context).showErrorMessage("Something went wrong - " + throwable.getMessage());
                                    System.out.println("ERROR " + throwable.getMessage());
                                    ((MainActivity)context).handleErrorOnOrdersGet("Something went wrong - " + throwable.getMessage());
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
