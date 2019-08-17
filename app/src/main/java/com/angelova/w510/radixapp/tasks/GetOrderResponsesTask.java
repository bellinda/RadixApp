package com.angelova.w510.radixapp.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.angelova.w510.radixapp.clients.OrdersRestClient;
import com.angelova.w510.radixapp.OrderDetailsActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by W510 on 6.4.2018 г..
 */

public class GetOrderResponsesTask extends AsyncTask<Void, Void, Object> {

    private String url;
    private Activity context;
    private String accessToken;
    private String refId;

    public GetOrderResponsesTask(Activity activity, String url,  String refId, String accessToken) {
        this.context = activity;
        this.url = url;
        this.refId = refId;
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
                    jsonParams.put("refID", refId);

                    StringEntity entity = new StringEntity(jsonParams.toString());
                    System.out.println("JSON " + jsonParams.toString());

                    OrdersRestClient.getOrderResponses(context, url, entity, "application/json", "x-access-token", accessToken,
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    ((OrderDetailsActivity) context).updateOrderDetails(response);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    //((ProfileActivity)context).showErrorMessage("Something went wrong - " + throwable.getMessage());
                                    ((OrderDetailsActivity)context).hideLoadingDialog();
                                    System.out.println("ERROR " + throwable.getMessage());
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    //((ProfileActivity)context).showErrorMessage("Something went wrong - " + throwable.getMessage());
                                    ((OrderDetailsActivity)context).hideLoadingDialog();
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
