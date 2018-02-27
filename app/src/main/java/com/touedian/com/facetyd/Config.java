/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.touedian.com.facetyd;


public class Config {


    // 为了apiKey,secretKey为您调用百度人脸在线接口的，如注册，识别等。
    // 为了的安全，建议放在您的服务端，端把人脸传给服务器，在服务端端进行人脸注册、识别放在示例里面是为了您快速看到效果
    public static String apiKey = "G8Yb4pN89mMGRUgwi5DAWjde";
    public static String secretKey = "UW2Z6YEaCrdkXO14Mv0BlmD6o1jG23as";
    public static String licenseID = "facetyd-face-android";
    public static String licenseFileName = "idl-license.face-android";


    /**
     * groupId，标识一组用户（由数字、字母、下划线组成），长度限制128B，可以自行定义，只要注册和识别都是同一个组。
     * 详情见 http://ai.baidu.com/docs#/Face-API/top
     * <p>
     * 人脸识别 接口 https://aip.baidubce.com/rest/2.0/face/v2/identify
     * 人脸注册 接口 https://aip.baidubce.com/rest/2.0/face/v2/faceset/user/add
     */

    public static String groupID = "test1";


    //newcodeicon
    public static String TYD_Verification_Code = "http://face.touedian.com/index.php?m=face&f=index&v=sms";

    //登录
    public static String TYD_Phone_Login = "http://face.touedian.com/index.php?m=face&f=index&v=login";

    //身份证信息上传
    public static String TYD_IDcardMessage_up = "http://face.touedian.com/index.php?m=face&f=index&v=upload_card";

    //银行卡信息上传
    public static String TYD_BankcardMessage_up = "http://face.touedian.com/index.php?m=face&f=index&v=upload_mcard";


    //银行卡信息获取
    public static String TYD_BankcardMessage_get = "http://face.touedian.com/index.php?m=face&f=index&v=get_cardinfo";


    //交易所编码
    public static String TYD_ExchangeMessage_get = "http://face.touedian.com/index.php?m=face&f=index&v=money";

    //用户头像||更改头像
    public static String TYD_PersonIcon = " http://face.touedian.com/index.php?m=face&f=index&v=member_info";

    //表格文字识别 _ 提交请求接口
    public static String TYD_GridMessage = "https://aip.baidubce.com/rest/2.0/solution/v1/form_ocr/request";

    //OAuth2.0       获取Access Token
    public static String Access_Token = "https://aip.baidubce.com/oauth/2.0/token";

}
