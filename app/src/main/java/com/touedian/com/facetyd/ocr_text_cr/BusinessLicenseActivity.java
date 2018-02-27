package com.touedian.com.facetyd.ocr_text_cr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.ocr_text_bean.BusinessLicenseBean;
import com.touedian.com.facetyd.ocr_text_bean.LicencePlateBean;
import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;

import org.json.JSONObject;

public class BusinessLicenseActivity extends AppCompatActivity {
    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private JSONObject jsonObject;

    private static final int REQUEST_CODE_BUSINESS_LICENSE = 123;
    private String BusinessLicenseMessage;
    private TextView business_text1;
    private TextView business_text2;
    private TextView business_text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_license);


        alertDialog = new AlertDialog.Builder(this);
        initAccessTokenWithAkSk();


        initdate();
        // 营业执照识别
        findViewById(R.id.business_license_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(BusinessLicenseActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_BUSINESS_LICENSE);
            }
        });
    }

    private void initdate() {
        business_text1 = findViewById(R.id.Business_license_text1);
        business_text2 = findViewById(R.id.Business_license_text2);
        business_text3 = findViewById(R.id.Business_license_text3);
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

        BusinessLicenseMessage = message;
        L.i("BusinessLicenseMessage", "" + BusinessLicenseMessage);

        try {
            jsonObject = new JSONObject(BusinessLicenseMessage);

            BusinessLicenseBean businessLicenseBean = JsonUtil.parseJsonToBean(BusinessLicenseMessage,BusinessLicenseBean.class);


            BusinessLicenseBean.WordsResultBean words_result = businessLicenseBean.getWords_result();


            String words_wordname = words_result.get单位名称().getWords();
            String words_address = words_result.get地址().getWords();
            String words_data = words_result.get成立日期().getWords();

            business_text1.setText(words_wordname);
            business_text2.setText(words_address);
            business_text3.setText(words_data);


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


        // 识别成功回调，营业执照识别
        if (requestCode == REQUEST_CODE_BUSINESS_LICENSE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recBusinessLicense(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                            L.i(result.toString());
                        }
                    });
        }


    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }
}
