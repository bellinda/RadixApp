package com.angelova.w510.radixapp.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.angelova.w510.radixapp.MainActivity;
import com.angelova.w510.radixapp.clients.OrdersRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class GetAllInvoicesTask extends AsyncTask<Void, Void, Object> {

    private String url;
    private Activity context;
    private String accessToken;

    public GetAllInvoicesTask(Activity activity, String url, String accessToken) {
        this.context = activity;
        this.url = url;
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
                    StringEntity entity = new StringEntity(jsonParams.toString());
                    System.out.println("JSON " + jsonParams.toString());

                    OrdersRestClient.getAllInvoices(context, url, entity, "application/json", "x-access-token", accessToken,
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    if (context instanceof MainActivity) {
                                        ((MainActivity)context).handleSuccessfulInvoicesDownload(response);
                                    }
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                    System.out.println("GET all invoices - resposne is array");
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    System.out.println("ERROR " + throwable.getMessage());
                                    ((MainActivity)context).handleErrorOnInvoicesGet("Something went wrong - " + throwable.getMessage());
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    System.out.println("ERROR " + throwable.getMessage());
                                    ((MainActivity)context).handleErrorOnInvoicesGet("Something went wrong - " + throwable.getMessage());
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
