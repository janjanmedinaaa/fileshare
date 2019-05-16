package com.medina.juanantonio.fileshare;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FileShareRequestClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
//        client.addHeader("Accept", "application/vnd.github.mercy-preview+json");
//        client.addHeader("User-Agent", "PostmanRuntime/7.11.0");

        client.get(url, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }
}
