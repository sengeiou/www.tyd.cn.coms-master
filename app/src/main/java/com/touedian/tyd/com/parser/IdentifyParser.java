/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.touedian.tyd.com.parser;




import com.touedian.tyd.com.exception.FaceException;
import com.touedian.tyd.com.ocr_model.FaceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IdentifyParser implements Parser<FaceModel> {
    @Override
    public FaceModel parse(String json) throws FaceException {

        FaceModel faceModel = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray resultArray = jsonObject.optJSONArray("result");
            if (resultArray != null) {
                faceModel = new FaceModel();
                JSONObject faceObject = resultArray.getJSONObject(0);
                faceModel.setUid(faceObject.getString("uid"));
                JSONArray scroeArray = faceObject.optJSONArray("scores");
                if (scroeArray != null) {
                    faceModel.setScore(scroeArray.getDouble(0));
                }
                faceModel.setGroupID(faceObject.getString("group_id"));
                faceModel.setUserInfo(faceObject.getString("user_info"));

            }
            JSONObject extInfoObject = jsonObject.optJSONObject("ext_info");
            if (extInfoObject != null) {
                double faceliveness = extInfoObject.optDouble("faceliveness");
                faceModel.setFaceliveness(faceliveness);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return faceModel;
    }
}
