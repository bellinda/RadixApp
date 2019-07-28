package com.angelova.w510.radixapp.clients;

import android.content.Context;

import com.angelova.w510.radixapp.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by W510 on 27.1.2018 г..
 */

public class LoginRestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void loginUser(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        System.out.println("Sending POST request for login to " + getAbsoluteUrl(url));
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return Utils.URL + relativeUrl;
    }
}
