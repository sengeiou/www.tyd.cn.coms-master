/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.touedian.tyd.com.utils;


import com.touedian.tyd.com.exception.FaceException;

public interface OnResultListener<T> {
    void onResult(T result);

    void onError(FaceException error);


}
