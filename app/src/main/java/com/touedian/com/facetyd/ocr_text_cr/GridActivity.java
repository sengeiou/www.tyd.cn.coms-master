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
import com.touedian.com.facetyd.Config;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.ocr_text_bean.BillBean;
import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.HttpUtils;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GridActivity extends AppCompatActivity {
    private HashMap<String, String> Grid_down = new HashMap<String, String>();
    private HashMap<String, String> Access = new HashMap<String, String>();
    private String client_credentials;
    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private JSONObject jsonObject;
    private static final int REQUEST_CODE_RECEIPT = 124;
    private String GridMessage;
    private TextView grid_text1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);



        alertDialog = new AlertDialog.Builder(this);
        initAccessTokenWithAkSk();


        initdate();

        // 通用票据识别   and  表格识别
        findViewById(R.id.grid_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(GridActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_RECEIPT);
            }
        });
       // PostAccess_Token();
      //  PostGrid_down();
    }

    private void initdate() {
        grid_text1 = findViewById(R.id.grid_text1);
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

        GridMessage = message;
        L.i("BillMessage", "" + GridMessage);

        try {
            jsonObject = new JSONObject(GridMessage);

            //共用一个Bean

            BillBean billBean = JsonUtil.parseJsonToBean(GridMessage,BillBean.class);


            List<BillBean.WordsResultBean> words_result = billBean.getWords_result();


            String words_wordname = words_result.get(2).getWords();


            grid_text1.setText(words_wordname);



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


        // 识别成功回调，通用票据识别  and  表格识别
        if (requestCode == REQUEST_CODE_RECEIPT && resultCode == Activity.RESULT_OK) {
            RecognizeService.recReceipt(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
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
    private void PostAccess_Token() {
        String clientId = "9evxCWG1MTN8k7u3XU0qVIqi";
        String clientSecret = "5eesgiqRtSflHYOM5OUZLSsSeMPCC81n";
        Access.put("grant_type",client_credentials);
        Access.put("client_id",clientId);
        Access.put("client_secret",clientSecret);

        HttpUtils.doPost(Config.Access_Token, Access, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private void PostGrid_down() {

       // Grid_down.put()


        HttpUtils.doPost(Config.TYD_GridMessage, Grid_down, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}
