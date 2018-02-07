package com.touedian.com.facetyd.ocr_discern;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.utils.PreferencesUtil;

public class FaceRecognitionActivity extends AppCompatActivity implements View.OnClickListener {
    private Button regBtn;
    private Button login1Btn;
    private Button login2Btn;
    private Button detectedBtn;
    private Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        findView();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void findView() {
        regBtn = (Button) findViewById(R.id.reg_btn);
        login1Btn = (Button) findViewById(R.id.login1_btn);
        login2Btn = (Button) findViewById(R.id.login2_btn);
        detectedBtn = (Button) findViewById(R.id.detected_btn);
        resetBtn = (Button) findViewById(R.id.reset_btn);
    }

    private void addListener() {
        regBtn.setOnClickListener(this);
        login1Btn.setOnClickListener(this);
        login2Btn.setOnClickListener(this);
        detectedBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            return;
        }

        if (regBtn == v) {
            Intent intent = new Intent(FaceRecognitionActivity.this, RegActivity.class);
            startActivity(intent);
        } else if (login1Btn == v) {
            // TODO 实时人脸检测
            Intent intent = new Intent(FaceRecognitionActivity.this, DetectLoginActivity.class);
            startActivity(intent);
        } else if (login2Btn == v) {
            Intent intent = new Intent(FaceRecognitionActivity.this, IdentifyLoginActivity.class);
            startActivity(intent);
        } else if (detectedBtn == v) {
            Intent intent = new Intent(FaceRecognitionActivity.this, VerifyLoginActivity.class);
            startActivity(intent);
        } else if (resetBtn == v) {
            PreferencesUtil.initPrefs(getApplicationContext());
            PreferencesUtil.remove("username");
        }
    }
}
