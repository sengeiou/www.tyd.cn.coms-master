/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.touedian.com.facetyd.ocr_face;


public class Config {



//     百度AI官网ai.baidu.com人脸模块创建应用，选择相应模块，然后查看ak,sk(公安权限需要在官网控制台提交工单开启)
//     apiKey,secretKey为您调用百度人脸在线接口时使用，如注册，比对等。
//     license 是为了使用sdk进行人脸检测。人脸识别 = 人脸检测 + downfacecontrast
//     为了的安全，建议放在您的服务端，端把人脸传给服务器，在服务端端进行人脸注册、识别放在示例里面是为了您快速看到效果
    public static String apiKey = "G8Yb4pN89mMGRUgwi5DAWjde";
    public static String secretKey = "UW2Z6YEaCrdkXO14Mv0BlmD6o1jG23as";
    public static String licenseID = "facestyd-face-android";
    public static String licenseFileName = "idl-license.face-android";

    /**
     * groupId，标识一组用户（由数字、字母、下划线组成），长度限制128B，可以自行定义，只要注册和识别都是同一个组。
     * 详情见 http://ai.baidu.com/docs#/Face-API/top
     * <p>
     * 人脸识别 接口 https://aip.baidubce.com/rest/2.0/face/v2/identify
     * 人脸注册 接口 https://aip.baidubce.com/rest/2.0/face/v2/faceset/user/add
     */

    public static String groupID = "test1";

}
