/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.touedian.com.facetyd.ocr_face;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.touedian.com.facetyd.APIService;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.exception.FaceException;
import com.touedian.com.facetyd.ocr_model.AccessToken;
import com.touedian.com.facetyd.ocr_model.LivenessVsIdcardResult;
import com.touedian.com.facetyd.utils.OnResultListener;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.SPUtils;

import java.io.File;

/**
 * 在线检测活体和公安核实
 */

public class     FaceOnlineVerifyActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int OFFLINE_FACE_LIVENESS_REQUEST = 100;

    //private String username="韩文瑞";
   // private String idnumber="140202199601112039";
    private String username;
    private String idnumber;
    private TextView resultTipTV;
    private TextView onlineFacelivenessTipTV;
    private TextView scoreTV;
    private ImageView avatarIv;
    private Button retBtn;
    private String filePath;
    private boolean policeVerifyFinish = false;
    private boolean waitAccesstoken = true;

    private String idnumbers;
    private String usernames;
    public boolean IdcardCode  ;
    private Button retryfinal_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_online_check);


        //Intent intent = getIntent();
       /* if (intent != null && username.isEmpty() && idnumber.isEmpty()) {
            username = intent.getStringExtra("username");
            idnumber = intent.getStringExtra("idnumber");
        }*/

        idnumber = SPUtils.getString(FaceOnlineVerifyActivity.this, "idNumber", this.idnumbers);
        L.i(idnumber);
        username = SPUtils.getString(FaceOnlineVerifyActivity.this, "name", this.usernames);
        L.i(username);

        resultTipTV = (TextView) findViewById(R.id.result_tip_tv);
        onlineFacelivenessTipTV = (TextView) findViewById(R.id.online_faceliveness_tip_tv);
        scoreTV = (TextView) findViewById(R.id.score_tv);
        avatarIv = (ImageView) findViewById(R.id.avatar_iv);
        retBtn = (Button) findViewById(R.id.retry_btn);
        retBtn.setOnClickListener(this);
        retryfinal_btn =(Button) findViewById(R.id.retryfinal_btn);
        retryfinal_btn.setOnClickListener(this);


        initAccessToken();
        // 打开离线活体检测
        Intent faceLivenessintent = new Intent(this, OfflineFaceLivenessActivity.class);
        startActivityForResult(faceLivenessintent, OFFLINE_FACE_LIVENESS_REQUEST);
    }

    @Override
    public void onClick(View v) {
        if (v == retBtn) {
            if (TextUtils.isEmpty(filePath)) {
                finish();
                return;
            }
            if (TextUtils.isEmpty(APIService.getInstance().getAccessToken())) {

                initAccessToken();

            } else {
                policeVerify(filePath);
            }
        }if (v == retryfinal_btn) {
            finish();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 检测完成后 开始在线活体检测和公安核实
        if (requestCode == OFFLINE_FACE_LIVENESS_REQUEST && data != null) {
            filePath = data.getStringExtra("bestimage_path");
            if (TextUtils.isEmpty(filePath)) {
                Toast.makeText(this, "离线活体图片没找到", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//            avatarIv.setImageBitmap(bitmap);
            policeVerify(filePath);
        } else {
            finish();
        }
    }

    // 在线活体检测和公安核实需要使用该token，为了防止ak、sk泄露，建议在线活体检测和公安接口在您的服务端请求
    private void initAccessToken() {

        displayTip(resultTipTV, "加载中");

        APIService.getInstance().init(getApplicationContext());
        APIService.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                L.i(result.toString());
                if (result != null && !TextUtils.isEmpty(result.getAccessToken())) {
                    Log.e("111111","AccessToken result1="+result.getAccessToken());
                    waitAccesstoken = false;
                    policeVerify(filePath);
                }else if (result != null) {
                    Log.e("111111","AccessToken result2="+result.toString());
                    displayTip(resultTipTV, "在线活体token获取失败1");
                    retBtn.setVisibility(View.VISIBLE);
                } else {
                    Log.e("111111","AccessToken  ="+result.toString());
                    displayTip(resultTipTV, "在线活体token获取失败2");
                    retBtn.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onError(FaceException error) {
                // TODO 错误处理
                displayTip(resultTipTV, "在线活体token获取失败3");
                retBtn.setVisibility(View.VISIBLE);
                L.i(error.toString());
            }
        }, Config.apiKey, Config.secretKey);
    }

    /**
     * 公安接口合并在线活体，调用公安验证接口进行最后的核身比对；公安权限需要在官网控制台提交工单开启
     * 接口地址：https://aip.baidubce.com/rest/2.0/face/v2/person/verify
     * 入参为「姓名」「身份证号」「bestimage」
     *  ext_fields 扩展功能。如 faceliveness 表示返回活体值, qualities 表示返回质检测结果
     *  quality string 判断质 是否达标。“use” 表示做质 控制,质  好的照 会 直接拒绝
     *  faceliveness string 判断活体值是否达标。 use 表示做活体控制,低于活体阈值的 照 会直接拒绝
     * quality_conf和faceliveness_conf 用于指定阈值，超过此分数才调用公安验证，
     *
     * @param filePath
     */
    private void policeVerify(String filePath) {
        if (TextUtils.isEmpty(filePath) || waitAccesstoken) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists() ) {
            return;
        }

        displayTip(resultTipTV, "公安身份核实中...");
        APIService.getInstance().policeVerify(username, idnumber, filePath, new
                OnResultListener<LivenessVsIdcardResult>() {


            @Override
            public void onResult(LivenessVsIdcardResult result) {
                delete();
                if (result != null && result.getScore() > 0.8) {
                    displayTip(resultTipTV, "核身成功");
                    displayTip(onlineFacelivenessTipTV, "在线活体分数：" + result.getFaceliveness());
                    displayTip(scoreTV, "公安验证分数：" + result.getScore());
                    IdcardCode=true;
                    SPUtils.putInt(getApplication(),"Score", (int) result.getScore());
                    SPUtils.putBoolean(getApplication(),"IdcardCode",IdcardCode);
                    L.i(String.valueOf(IdcardCode));

                } else {
                    displayTip(resultTipTV, "核身失败");
                    displayTip(scoreTV, "公安验证分数过低：" + result.getScore());
                    retBtn.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onError(FaceException error) {
                L.e(username.toString());
                L.e(idnumber.toString());
                delete();
                displayTip(resultTipTV, "核身失败：" + error.getErrorMessage());
                // TODO 错误处理
                // 如返回错误码为：216600，则核身失败，提示信息为：身份证号码错误
                // 如返回错误码为：216601，则核身失败，提示信息为：身份证号码与姓名不匹配
                Toast.makeText(FaceOnlineVerifyActivity.this, "公安身份核实失败:" + error.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                retBtn.setVisibility(View.VISIBLE);

            }
        });
    }

    private void delete() {
        File file = new File(filePath);
        if (file.exists() ) {
            file.delete();
        }
    }

    private void displayTip(final TextView textView, final String tip) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textView != null) {
                    textView.setText(tip);
                }
            }
        });
    }
}

