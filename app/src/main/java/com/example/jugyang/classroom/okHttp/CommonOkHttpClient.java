package com.example.jugyang.classroom.okHttp;

import com.example.jugyang.classroom.okHttp.Response.CommonJsonCallback;
import com.example.jugyang.classroom.okHttp.https.HttpsUtils;
import com.example.jugyang.classroom.okHttp.listener.DisposeDataHandle;
import com.example.jugyang.classroom.utils.StaticClass;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.http.StatusLine;

/**
 * Project Name:     Classroom_Toy_v1.0
 * Package Name:     com.example.jugyang.classroom.okHttp
 * File Name:        CommonOkHttpClient
 * Description:      Created by jugyang on 4/12/17.
 *                   请求的发送，请求参数的配置，https支持
 */

public class CommonOkHttpClient {

    private static OkHttpClient mOkHttpClient;
    public static Object get;

    //为我们的client去配置参数
    static {
        //创建我们client对象的构建者
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        //为构建者填充超时时间
        okHttpBuilder.connectTimeout(StaticClass.TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(StaticClass.TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(StaticClass.TIME_OUT, TimeUnit.SECONDS);

        okHttpBuilder.followRedirects(true);

        //https支持
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        okHttpBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());

        //生成我们的client对象
        mOkHttpClient = okHttpBuilder.build();
    }

    /**
     * 发送具体的http/https请求
     * @param request
     * @param commCallback
     * @return Call
     */
    public static Call sendRequest(Request request, CommonJsonCallback commCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commCallback);
        return call;
    }

    /**
     * 通过构造好的Request,Callback去发送请求
     * @param request
     * @param handle
     * @return
     */
    public static Call get (Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    public static Call post (Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }
}
