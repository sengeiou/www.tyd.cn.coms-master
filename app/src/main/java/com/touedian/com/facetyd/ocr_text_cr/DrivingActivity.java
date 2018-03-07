package com.touedian.com.facetyd.ocr_text_cr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;

import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.ocr_face.OcrFaceActivity;
import com.touedian.com.facetyd.ocr_text_bean.DrivingCardBean;
import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.CleanUtils;
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
    private TextView address;
    private TextView idnumber;
    private TextView driving_name;
    private TextView nationality;
    private TextView driving_birth;
    private TextView one_certificate;
    private TextView effective_date;
    private TextView card_type;
    private TextView effective_date_eid;
    private String address_words;
    private String idnumber_words;
    private String driving_name_words;
    private String nationality_words;
    private String driving_birth_words;
    private String one_certificate_words;
    private String effective_date_words;
    private String card_type_words;
    private String effective_date_eid_words;
    private TextView driving_sex;
    private String driving_sex_words;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(DrivingActivity.this);
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
        ImageView bankcard_back=findViewById(R.id.driving_bankcard_back);
        bankcard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //住址
        address = findViewById(R.id.zhuzhi);
        //证号
        idnumber = findViewById(R.id.idnumber);
        //姓名
        driving_name = findViewById(R.id.driving_name);

        driving_sex = findViewById(R.id.driving_sex);
        //国籍
        nationality = findViewById(R.id.nationality);
        //出生日期
        driving_birth = findViewById(R.id.driving_birth);
        //初次领证日期
        one_certificate = findViewById(R.id.one_certificate);
        //有效日期
        effective_date = findViewById(R.id.effective_date);
        //车架类型
        card_type = findViewById(R.id.card_type);
        //有效日期 至
        //effective_date_eid = findViewById(R.id.effective_date_eid);

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
       /* this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });*/


        DrivingCardMessage =message;
        L.i("11111DrivingCardMessage", "" + DrivingCardMessage);

        try {
            jsonObject = new JSONObject(DrivingCardMessage);

            drivingCardBean = JsonUtil.parseJsonToBean(DrivingCardMessage,DrivingCardBean.class);

            DrivingCardBean.WordsResultBean words_result = drivingCardBean.getWords_result();


            idnumber_words = words_result.get证号().getWords();

            address_words = words_result.get住址().getWords();

            driving_name_words = words_result.get姓名().getWords();

            driving_sex_words = words_result.get性别().getWords();

            nationality_words = words_result.get国籍().getWords();

            driving_birth_words = words_result.get出生日期().getWords();

            one_certificate_words = words_result.get初次领证日期().getWords();

            effective_date_words = words_result.get有效期限().getWords();

            card_type_words = words_result.get准驾车型().getWords();

            //effective_date_eid_words = words_result.get至().getWords();





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
                            L.i("55555"+result);
                            infoPopText(result);

                            address.setText(address_words);

                            idnumber.setText(idnumber_words);

                            driving_name.setText(driving_name_words);

                            driving_sex.setText(driving_sex_words);

                            nationality.setText(nationality_words);

                            driving_birth.setText(driving_birth_words);

                            one_certificate.setText(one_certificate_words);

                            effective_date.setText(effective_date_words);

                            card_type.setText(card_type_words);

                          //  effective_date_eid.setText(effective_date_eid_words);

                            String absolutePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();

                            L.i(absolutePath.toString());
                        }
                    });
        }



    }
    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

}
