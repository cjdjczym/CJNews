package com.example.cjnews.netClient;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Client {
    private static OkHttpClient mClient;

    private Client() {

    }

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        if (mClient == null) {
            mClient = new OkHttpClient.Builder().build();
        }
        final Request request = new Request.Builder()
                .url(address)
                .get()
                .build();
        mClient.newCall(request).enqueue(callback);
    }
}
