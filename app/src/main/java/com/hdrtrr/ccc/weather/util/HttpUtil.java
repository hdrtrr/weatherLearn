package com.hdrtrr.ccc.weather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by hdrtrr on 2017/6/12.
 * http网络请求公共类
 */

public class HttpUtil {
    public static void sendOlHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }
}
