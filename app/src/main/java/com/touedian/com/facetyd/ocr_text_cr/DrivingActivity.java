package com.touedian.com.facetyd.ocr_text_cr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;

import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.ocr_text_bean.DrivingCardBean;
import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;

import org.json.JSONObject;


//驾驶证识别
public class DrivingActivity extends AppCompatActivity {
    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private static final int REQUEST_CODE_DRIVING_LICENSE = 121;
    private String DrivingCardMessage;
    private JSONObject jsonObject;
    private DrivingCardBean drivingCardBean;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);
        alertDialog = new AlertDialog.Builder(this);
        initAccessTokenWithAkSk();

        InitDate();
        // 驾驶证识别
        findViewById(R.id.driving_license_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DrivingActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_DRIVING_LICENSE);
            }
        });
    }

    private void InitDate() {
        textView = findViewById(R.id.zhuzhi);

    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {

                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(), "9evxCWG1MTN8k7u3XU0qVIqi", "5eesgiqRtSflHYOM5OUZLSsSeMPCC81n");
    }
    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });


        DrivingCardMessage =message;
        L.i("11111DrivingCardMessage", "" + DrivingCardMessage);

        try {
            jsonObject = new JSONObject(DrivingCardMessage);

            drivingCardBean = JsonUtil.parseJsonToBean(DrivingCardMessage,DrivingCardBean.class);

            DrivingCardBean.WordsResultBean words_result = drivingCardBean.getWords_result();


            String words = words_result.get证号().getWords();

            String words1 = words_result.get住址().getWords();
            textView.setText(words1);
            L.i(textView.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void infoPopText(final String result) {
        alertText("", result);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // 识别成功回调，驾驶证识别
        if (requestCode == REQUEST_CODE_DRIVING_LICENSE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recDrivingLicense(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                            L.i(result);
                            String absolutePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                        }
                    });
        }



    }


}
