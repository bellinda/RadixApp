package com.angelova.w510.radixapp.clients;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by W510 on 27.1.2018 г..
 */

public class OffersRestClient {

    private static final String BASE_URL = "http://192.168.0.24:3000/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(Context context, String url, HttpEntity entity, String contentType, String headerKey, String headerValue, AsyncHttpResponseHandler responseHandler) {
        System.out.println("Sending GET request for getting all offers of the user to " + getAbsoluteUrl(url));
        client.addHeader(headerKey, headerValue);
        client.get(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void getOne(Context context, String url, HttpEntity entity, String contentType, String headerKey, String headerValue, AsyncHttpResponseHandler responseHandler) {
        System.out.println("Sending GET request for getting an offer by id to " + getAbsoluteUrl(url));
        client.addHeader(headerKey, headerValue);
        client.get(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
