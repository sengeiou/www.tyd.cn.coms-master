/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.touedian.com.facetyd.parser;

import android.text.TextUtils;


import com.touedian.com.facetyd.exception.FaceException;
import com.touedian.com.facetyd.ocr_model.FaceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VerifyParser implements Parser<FaceModel> {
    @Override
    public FaceModel parse(String json) throws FaceException {

        FaceModel faceModel = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            int errorCode = jsonObject.optInt("error_code");
            if (errorCode > 0) {
                String errorMsg = jsonObject.optString("error_msg");
                FaceException faceError = new FaceException(errorCode, errorMsg);
                throw faceError;
            }
            JSONArray resultArray = jsonObject.optJSONArray("result");
            faceModel = new FaceModel();
            if (resultArray != null) {
                faceModel.setScore(resultArray.getDouble(0));
            }
            JSONObject extInfo = jsonObject.optJSONObject("ext_info");
            if (extInfo != null) {
                String faceliveness = extInfo.optString("faceliveness");
                if (!TextUtils.isEmpty(faceliveness)) {
                    faceModel.setFaceliveness(faceliveness);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return faceModel;
    }
}
