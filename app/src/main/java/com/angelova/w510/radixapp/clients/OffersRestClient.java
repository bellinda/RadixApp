package com.angelova.w510.radixapp.clients;

import android.content.Context;

import com.angelova.w510.radixapp.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by W510 on 27.1.2018 г..
 */

public class OffersRestClient {

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

    public static void getOfferResponses(Context context, String url, HttpEntity entity, String contentType, String headerKey, String headerValue, AsyncHttpResponseHandler responseHandler) {
        System.out.println("Sending GET request for getting all offer responses to " + getAbsoluteUrl(url));
        client.addHeader(headerKey, headerValue);
        client.get(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void sendOfferResponse(Context context, String url, HttpEntity entity, String contentType, String headerKey, String headerValue, AsyncHttpResponseHandler responseHandler) {
        System.out.println("Sending POST request for uploading a user response to " + getAbsoluteUrl(url));
        client.addHeader(headerKey, headerValue);
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return Utils.URL + relativeUrl;
    }
}
