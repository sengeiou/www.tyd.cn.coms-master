package com.touedian.com.facetyd.ocr_text_cr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.bean.DrivingCardBean;
import com.touedian.com.facetyd.bean.NetWorkCardBean;
import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;

import org.json.JSONObject;

import java.util.List;

public class NetworksActivity extends AppCompatActivity {
    private boolean hasGotToken = false;
    private static final int REQUEST_CODE_GENERAL_WEBIMAGE = 110;
    private AlertDialog.Builder alertDialog;
    private String networksMessage;
    private JSONObject jsonObject;
    private NetWorkCardBean netWorkCardBean;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networks);

        alertDialog = new AlertDialog.Builder(this);
        initAccessTokenWithAkSk();

        InitDate();
        // 网络图片识别
        findViewById(R.id.general_webimage_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NetworksActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL_WEBIMAGE);
            }
        });
    }

    private void InitDate() {

        textView1 = findViewById(R.id.text1);

        textView2 = findViewById(R.id.text2);

        textView3 = findViewById(R.id.text3);
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


        networksMessage = message;
        L.i("11111DrivingCardMessage", "" + networksMessage);

        try {
            jsonObject = new JSONObject(networksMessage);

            netWorkCardBean = JsonUtil.parseJsonToBean(networksMessage,NetWorkCardBean.class);

            List<NetWorkCardBean.WordsResultBean> words_result = netWorkCardBean.getWords_result();


            // DrivingCardBean.WordsResultBean words_result = drivingCardBean.getWords_result();


            String words1 = words_result.get(0).getWords();

            String words2 = words_result.get(1).getWords();

            String words3 = words_result.get(2).getWords();
            //textView.setText(words1);
            //L.i(textView.toString());

            textView1.setText(words1);
            textView2.setText(words2);
            textView3.setText(words3);
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


        // 识别成功回调，网络图片文字识别
        if (requestCode == REQUEST_CODE_GENERAL_WEBIMAGE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recWebimage(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                            L.i(result);
                        }
                    });
        }



    }
}
