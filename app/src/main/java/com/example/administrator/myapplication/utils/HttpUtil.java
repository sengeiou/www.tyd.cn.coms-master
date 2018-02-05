/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.example.administrator.myapplication.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


import com.example.administrator.myapplication.exception.FaceException;
import com.example.administrator.myapplication.ocr_model.AccessToken;
import com.example.administrator.myapplication.ocr_model.RequestParams;
import com.example.administrator.myapplication.APIService;
import com.example.administrator.myapplication.parser.AccessTokenParser;
import com.example.administrator.myapplication.parser.Parser;

import java.io.IOException;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

/**
 * 使用okhttp请求tokeh和调用服务
 */
public class HttpUtil {

    private OkHttpClient client;
    private Handler handler;
    private static volatile HttpUtil instance;

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (instance == null) {
            synchronized (HttpUtil.class) {
                if (instance == null) {
                    instance = new HttpUtil();
                }
            }

        }

        return instance;
    }

    public void init() {
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    public <T> void post(String path, RequestParams params, final Parser<T> parser, final OnResultListener<T>
            listener) {
        post(path, "images", params, parser, listener);
    }

    public <T> void post(String path, String key, RequestParams params,
                         final Parser<T> parser, final OnResultListener<T> listener) {
        Base64RequestBody body = new Base64RequestBody();

        body.setKey(key);
        body.setFileParams(params.getFileParams());
        body.setStringParams(params.getStringParams());
        final Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();
        // liujinhui 经常client为空指针 ？
        if (client == null) {

            HttpUtil.getInstance().release();
            HttpUtil.getInstance().init();
            if (client == null) {
                throwError(listener, -999, "okhttp inner error");
                return;
            }
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                throwError(listener, FaceException.ErrorCode.NETWORK_REQUEST_ERROR, "network request error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                try {
                    final T result = parser.parse(responseString);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onResult(result);
                        }
                    });
                } catch (final FaceException error) {
                    error.printStackTrace();
                    throwError(listener, -1, error.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError(error);
                        }
                    });
                }
            }
        });
    }

    public void getAccessToken(final OnResultListener<AccessToken> listener, String url, String param) {

        final Parser<AccessToken> accessTokenParser = new AccessTokenParser();
        RequestBody body = RequestBody.create(MediaType.parse("text/html"), param);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                throwError(listener, FaceException.ErrorCode.NETWORK_REQUEST_ERROR, "network request error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null || response.body() == null || TextUtils.isEmpty(response.toString())) {
                    throwError(listener, FaceException.ErrorCode.ACCESS_TOKEN_PARSE_ERROR,
                            "token is parse error, please rerequest token");
                }
                try {
                    final AccessToken accessToken = accessTokenParser.parse(response.body().string());

                    if (accessToken != null) {
                        APIService.getInstance().setAccessToken(accessToken.getAccessToken());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onResult(accessToken);
                            }
                        });
                    } else {
                        throwError(listener, FaceException.ErrorCode.ACCESS_TOKEN_PARSE_ERROR,
                                "token is parse error, please rerequest token");
                    }
                } catch (FaceException error) {
                    error.printStackTrace();
                    throwError(listener, FaceException.ErrorCode.ACCESS_TOKEN_PARSE_ERROR,
                            "token is parse error, please rerequest token");
                }
            }
        });

    }

    /**
     * throw error
     *
     * @param errorCode
     * @param msg
     * @param listener
     */
    private void throwError(final OnResultListener listener, int errorCode, String msg) {
        final FaceException error = new FaceException(errorCode, msg);
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onError(error);
            }
        });
    }
    ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_1)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
            .build();

    //OkHttpClient.connectionSpecs(Collections.singletonList(spec))
    /**
     * 释放资源
     */
    public void release() {
        client = null;
        handler = null;
    }

}
