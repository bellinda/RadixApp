package com.angelova.w510.radixapp.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.angelova.w510.radixapp.clients.OffersRestClient;
import com.angelova.w510.radixapp.OfferDetailsActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by W510 on 18.3.2018 г..
 */

public class GetOfferResponsesTask extends AsyncTask<Void, Void, Object> {

    private String url;
    private Activity context;
    private String accessToken;
    private String refId;

    public GetOfferResponsesTask(Activity activity, String url,  String refId, String accessToken) {
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

                    OffersRestClient.getOfferResponses(context, url, entity, "application/json", "x-access-token", accessToken,
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    ((OfferDetailsActivity) context).updateOfferDetails(response);
                                }

//                                @Override
//                                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                                    if(response.length() > 0) {
//                                        ((OfferDetailsActivity) context).updateOfferDetails(response);
//                                    } else {
//                                        ((OfferDetailsActivity)context).hideLoadingDialog();
//                                    }
//                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    //((ProfileActivity)context).showErrorMessage("Something went wrong - " + throwable.getMessage());
                                    ((OfferDetailsActivity)context).hideLoadingDialog();
                                    System.out.println("ERROR " + throwable.getMessage());
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    //((ProfileActivity)context).showErrorMessage("Something went wrong - " + throwable.getMessage());
                                    ((OfferDetailsActivity)context).hideLoadingDialog();
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
