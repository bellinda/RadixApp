package com.angelova.w510.radixapp.clients;

import android.content.Context;

import com.angelova.w510.radixapp.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.security.KeyStore;

public class InvoicesRestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getPdfFile(Context context, String url, String headerKey, String headerValue, AsyncHttpResponseHandler responseHandler) {
        System.out.println("Sending GET request for getting an invoice file to " + getAbsoluteUrl(url));
        client.addHeader(headerKey, headerValue);
        client.get(context, getAbsoluteUrl(url), null, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return Utils.URL + relativeUrl;
    }
}
