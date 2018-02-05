/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.example.administrator.myapplication.parser;

import android.util.Log;


import com.example.administrator.myapplication.exception.FaceException;
import com.example.administrator.myapplication.ocr_model.RegResult;

import org.json.JSONException;
import org.json.JSONObject;

public class RegResultParser implements Parser<RegResult> {


    @Override
    public RegResult parse(String json) throws FaceException {
        Log.e("xx", "oarse:" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has("error_code")) {
                FaceException error = new FaceException(jsonObject.optInt("error_code"), jsonObject.optString("error_msg"));
                throw error;
            }

            RegResult result = new RegResult();
            result.setLogId(jsonObject.optLong("log_id"));
            result.setJsonRes(json);

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            FaceException error = new FaceException(FaceException.ErrorCode.JSON_PARSE_ERROR, "Json parse error:" + json, e);
            throw error;
        }
    }
}
