/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.touedian.com.facetyd.ocr_discern;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;


import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.touedian.com.facetyd.ocr_face.OcrFaceActivity;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.wigdet.DefaultDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FaceLivenessExpActivity extends FaceLivenessActivity {

    private DefaultDialog mDefaultDialog;
    private String bestImagePath;
    private int brushface;//判断刷脸标识
    private int booe;
    private String avatar;
    private String identity_card;
    private String truename;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            brushface = intent.getExtras().getInt("brushface");
            booe = intent.getExtras().getInt("booe");
            avatar = intent.getStringExtra("avatar");
            identity_card = intent.getStringExtra("identity_card");
            truename = intent.getStringExtra("truename");
            username = intent.getStringExtra("username");
        }
        L.i("booe",avatar.toString());
        L.i("booe",identity_card.toString());
        L.i("booe",truename.toString());
        L.i("booe",username.toString());
    }

    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onLivenessCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            showMessageDialog("downdetection", "检测成功");
            saveImage(base64ImageMap);
            brushface = 1;
            Intent intent = new Intent();
            intent.putExtra("file_path", bestImagePath);
            intent.putExtra("brushface", brushface);

            L.i("99999999999999", String.valueOf(brushface).toString());

            setResult(Activity.RESULT_OK, intent);
            if(booe==1){
                Intent intents=new Intent(FaceLivenessExpActivity.this, OcrFaceActivity.class);
                intents.putExtra("avatar",avatar);
                intents.putExtra("identity_card",identity_card);
                intents.putExtra("truename",truename);
                intents.putExtra("username",username);

                L.i("88888888888888",avatar.toString());
                L.i("88888888888888",identity_card.toString());
                L.i("88888888888888",truename.toString());
                L.i("88888888888888",username.toString());
                startActivity(intents);
            }else {
                finish();
            }

        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            showMessageDialog("downdetection", "采集超时");
        }
    }

    private void saveImage(HashMap<String, String> base64ImageMap) {

        String bestimageBase64 = base64ImageMap.get("bestImage0");
        Bitmap bmp = base64ToBitmap(bestimageBase64);

        // 如果觉的在线校验慢，可以压缩图片的分辨率，目前没有压缩分辨率，压缩质量80%-100%，在neuxs5上大概30k
        try {
            File file = File.createTempFile(UUID.randomUUID().toString(), ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.close();

            bestImagePath = file.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMessageDialog(String title, String message) {
        if (mDefaultDialog == null) {
            DefaultDialog.Builder builder = new DefaultDialog.Builder(this);
            builder.setTitle(title).
                    setMessage(message).
                    setNegativeButton("cardamendbtn",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDefaultDialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.putExtra("file_path", bestImagePath);
                                    setResult(Activity.RESULT_OK, intent);
                                   // finish();
                                }
                            });
            mDefaultDialog = builder.create();
            mDefaultDialog.setCancelable(true);
        }
        mDefaultDialog.dismiss();
        mDefaultDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
    }

}
