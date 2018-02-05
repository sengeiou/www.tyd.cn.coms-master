/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.example.administrator.myapplication;

import android.content.Context;


import com.example.administrator.myapplication.ocr_model.AccessToken;
import com.example.administrator.myapplication.ocr_model.DynamicParams;
import com.example.administrator.myapplication.ocr_model.FaceModel;
import com.example.administrator.myapplication.ocr_model.LivenessVsIdcardResult;
import com.example.administrator.myapplication.ocr_model.RegParams;
import com.example.administrator.myapplication.parser.IdentifyParser;
import com.example.administrator.myapplication.parser.RegResultParser;
import com.example.administrator.myapplication.parser.UploadParser;
import com.example.administrator.myapplication.parser.UserListParser;
import com.example.administrator.myapplication.parser.VerifyParser;
import com.example.administrator.myapplication.utils.DeviceUuidFactory;
import com.example.administrator.myapplication.utils.HttpUtil;
import com.example.administrator.myapplication.utils.OnResultListener;
import com.example.administrator.myapplication.utils.OnlineLivenessResultParser;
import com.example.administrator.myapplication.utils.PoliceCheckResultParser;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by wangtianfei01 on 17/7/13.
 */

public class APIService {


    // 获取在线活体检测和公安接口使用的token， client_id为ak， client_secret为sk，为了安全起见，请把ak，sk放在自己的服务端去获取token
    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/newhomepagetwo.0/token";

    // 公安接口
    private static final String LIVENESS_VS_IDCARD_URL = "https://aip.baidubce.com/rest/newhomepagetwo.0/face/v2/person/verify";


    private static final String BASE_URL = "https://aip.baidubce.com";

    private static final String ACCESS_TOEKN_URL = BASE_URL + "/oauth/newhomepagetwo.0/token?";

    private static final String REG_URL = BASE_URL + "/rest/newhomepagetwo.0/face/v2/faceset/user/add";
    // 在线活体接口
    private static final String ONLINE_LIVENESS_URL = BASE_URL + "/rest/newhomepagetwo.0/face/v2/faceverify";

    private static final String DELETE_GROUP_USERS = BASE_URL + "/rest/newhomepagetwo.0/face/v2/faceset/group/deleteuser";

    private static final String IDENTIFY_URL = BASE_URL + "/rest/newhomepagetwo.0/face/v2/identify";
    private static final String VERIFY_URL = BASE_URL + "/rest/newhomepagetwo.0/face/v2/verify";
    private static final String FIND_USERS_IN_GROUP_URL = BASE_URL + "/rest/newhomepagetwo.0/face/v2/faceset/group/getusers";
    private static final String DEL_USER_URL = BASE_URL + "/rest/newhomepagetwo.0/face/v2/faceset/user/delete";


    private String accessToken;

    private String groupId;

    private APIService() {

    }

    private static volatile APIService instance;

    public static APIService getInstance() {
        if (instance == null) {
            synchronized (APIService.class) {
                if (instance == null) {
                    instance = new APIService();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        HttpUtil.getInstance().init();
        DeviceUuidFactory.init(context);
    }



    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    /**
     * 明文aksk获取token
     *
     * @param listener 回调
     * @param ak       apiKey
     * @param sk       secretKey
     */
    public void initAccessTokenWithAkSk(final OnResultListener<AccessToken> listener, String ak,
                                        String sk) {

        StringBuilder sb = new StringBuilder();
        sb.append("client_id=").append("G8Yb4pN89mMGRUgwi5DAWjde");
        sb.append("&client_secret=").append("UW2Z6YEaCrdkXO14Mv0BlmD6o1jG23as");
        sb.append("&grant_type=client_credentials");
        HttpUtil.getInstance().getAccessToken(listener, ACCESS_TOEKN_URL, sb.toString());

    }


    /**
     * 明文aksk获取token
     *
     * @param listener
     * @param context
     * @param ak
     * @param sk
     */
    public void initAccessTokenWithAkSkApplication(final OnResultListener<AccessToken> listener, Context context, String ak,
                                        String sk) {

        StringBuilder sb = new StringBuilder();
        sb.append("client_id=").append("G8Yb4pN89mMGRUgwi5DAWjde");
        sb.append("&client_secret=").append("UW2Z6YEaCrdkXO14Mv0BlmD6o1jG23as");
        sb.append("&grant_type=client_credentials");
        HttpUtil.getInstance().getAccessToken(listener, ACCESS_TOEKN_URL, sb.toString());

    }
    /**
     * 设置accessToken 如何获取 accessToken 详情见:
     *
     * @param accessToken accessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {


        return accessToken;

    }


    public void reg(OnResultListener listener, File file, String uid, String username) {
        RegParams params = new RegParams();
        params.setGroupId(groupId);

        params.setUid(uid);
        params.setUserInfo(username);
        params.setImageFile(file);

        RegResultParser parser = new RegResultParser();
        String url = urlAppendCommonParams(REG_URL);
        HttpUtil.getInstance().post(url, params, parser, listener);
    }
    /**
     * 在线活体检测接口，放在视频伪造，传活体得的图片，
     * @param listener
     * @param file
     */
    public void onlineLiveness(OnResultListener listener, File file) {
        DynamicParams params = new DynamicParams();
        params.putParam("face_fields", "qualities,faceliveness");
        params.putFile("image", file);
        OnlineLivenessResultParser parser = new OnlineLivenessResultParser();
        HttpUtil.getInstance().post(urlAppendCommonParams(ONLINE_LIVENESS_URL), "image", params, parser, listener);
    }
    /**
     * @param listener
     * @param file
     */

    public void identify(OnResultListener<FaceModel> listener, File file) {
        DynamicParams params = new DynamicParams();
        params.putParam("group_id", groupId);
        params.putFile("image", file);
        params.putParam("ext_fields", "faceliveness");

        IdentifyParser parser = new IdentifyParser();
        String url = urlAppendCommonParams(IDENTIFY_URL);
        HttpUtil.getInstance().post(url, params, parser, listener);
    }

    public void verify(OnResultListener<FaceModel> listener, File file, String uid) {
        DynamicParams params = new DynamicParams();
        params.putParam("group_id", groupId);
        params.putFile("image", file);
        params.putParam("uid", uid);
        params.putParam("ext_fields", "faceliveness");


        VerifyParser parser = new VerifyParser();
        String url = urlAppendCommonParams(VERIFY_URL);
        HttpUtil.getInstance().post(url, params, parser, listener);
    }

    public void upload(OnResultListener<Integer> listener, File file, HashMap<String, String> extraParams) {
        UUID uuid = DeviceUuidFactory.getUuid();
        DynamicParams params = new DynamicParams();
//        params.putParam("deviceId", "DS-2CD5A32EFWD-IZ20170412AACH734237291");
        params.putParam("deviceId", "DS-2CD5A32EFWD20170317AACH734025525");
        // 20170317AACH734025525
        params.putParam("tabId", uuid.toString());
        params.putParam("createdTime", String.valueOf(System.currentTimeMillis()));
        if (extraParams != null) {
            for (String p : extraParams.keySet()) {
                params.putParam(p, extraParams.get(p));
            }
        }

        params.putFile(file.getName(), file);

        UploadParser parser = new UploadParser();
        String url = "http://api.sit.ffan.com/faceperception/v1/user/identify";
        HttpUtil.getInstance().post(url, "imgStr", params, parser, listener);
    }
    public void findGroupUsers(OnResultListener<List<FaceModel>> listener) {
        DynamicParams params = new DynamicParams();
        params.putParam("group_id", groupId);
        UserListParser parser = new UserListParser();
        String url = urlAppendCommonParams(FIND_USERS_IN_GROUP_URL);
        HttpUtil.getInstance().post(url, params, parser, listener);
    }
    public void delUidInGroup(String uid, OnResultListener listener) {
        DynamicParams params = new DynamicParams();
        // params.putParam("group_id", groupId);
        params.putParam("uid", uid);
        //        params.putParam("group_id", groupId);


        RegResultParser parser = new RegResultParser();
        String url = urlAppendCommonParams(DEL_USER_URL);
        HttpUtil.getInstance().post(url, params, parser, listener);
    }


    public void policeVerify(String name, String idnumber, String file, OnResultListener<LivenessVsIdcardResult>
            listener) {
        DynamicParams params = new DynamicParams();
        params.putParam("name", name);
        params.putParam("id_card_number", idnumber);
        params.putParam("ext_fields", "qualities,faceliveness");
        params.putParam("quality", "use");
        params.putParam("faceliveness", "use");
        params.putFile("image", new File(file));


        PoliceCheckResultParser parser = new PoliceCheckResultParser();

        HttpUtil.getInstance().post(urlAppendCommonParams(LIVENESS_VS_IDCARD_URL), "image", params, parser, listener);
    }

    /**
     * URL append access token，sdkversion，aipdevid
     *
     * @param url
     * @return
     */
    private String urlAppendCommonParams(String url) {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?access_token=").append(accessToken);

        return sb.toString();
    }

}
