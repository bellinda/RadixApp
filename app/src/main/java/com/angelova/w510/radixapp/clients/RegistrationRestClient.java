package com.angelova.w510.radixapp.clients;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by W510 on 27.1.2018 г..
 */

public class RegistrationRestClient {

    private static final String BASE_URL = "http://192.168.0.24:3000/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void registerUser(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        System.out.println("Sending POST request for registering a new user to " + getAbsoluteUrl(url));
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void resetUserPassword(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        System.out.println("Sending POST request for resetting user password to " + getAbsoluteUrl(url));
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void setNewUserPassword(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        System.out.println("Sending POST request for setting new user password to " + getAbsoluteUrl(url));
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}