/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.example.administrator.myapplication.utils;


import com.example.administrator.myapplication.exception.FaceException;

public interface OnResultListener<T> {
    void onResult(T result);

    void onError(FaceException error);


}
