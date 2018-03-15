package com.touedian.com.facetyd.ocr_text_cr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.touedian.com.facetyd.Config;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.ocr_text_bean.BusinessLicenseBean;
import com.touedian.com.facetyd.ocr_text_bean.LicencePlateBean;
import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.FileUti;
import com.touedian.com.facetyd.utilsx.HttpUtils;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.PictureUtil;
import com.touedian.com.facetyd.utilsx.SPUtils;
import com.touedian.com.facetyd.utilsx.ToastUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.NONE;

public class BusinessLicenseActivity extends AppCompatActivity {
    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private JSONObject jsonObject;

    private static final int REQUEST_CODE_BUSINESS_LICENSE = 123;
    private String BusinessLicenseMessage;
    private TextView business_text1;
    private TextView business_text2;
    private TextView business_text3;
    private TextView business_login_number;
    private TextView business_socialCredit_code;
    private TextView business_company_name;
    private TextView business_address;
    private TextView business_legal_representative;
    private TextView business_effective_date;
    private String words_login_number;
    private String words_code;
    private String words_company_name;
    private String words_address;
    private String words_legal_representative;
    private String words_effective_date;
    private String words_data;
    private ImageView business_bankcard_back;
    private ImageView business_license_button;
    private String absolutePath;
    private int uid;
    private int usid;
    private HashMap<String, String> Picture_params;
    private HashMap<String, String> Business_Message_params;
    private HashMap<String, String> Business_Message_data;
    private String businessStringBase64;
    private Button business_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(BusinessLicenseActivity.this);
        setContentView(R.layout.activity_business_license);


        alertDialog = new AlertDialog.Builder(this);
        initAccessTokenWithAkSk();
        usid = SPUtils.getInt(BusinessLicenseActivity.this, "uid", uid);

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


        business_bankcard_back = findViewById(R.id.Business_bankcard_back);
        business_bankcard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        business_btn = findViewById(R.id.Business_btn);
        business_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(getApplication(),"成功",0);
                finish();
            }
        });

        business_license_button = findViewById(R.id.business_license_button);

        //注册号
        business_login_number = findViewById(R.id.Business_login_number);

        //社会信用代码
        business_socialCredit_code = findViewById(R.id.Business_SocialCredit_code);

        //企业名称
        business_company_name = findViewById(R.id.Business_company_name);

        //企业住址
        business_address = findViewById(R.id.Business_address);

        //法定代表人
        business_legal_representative = findViewById(R.id.Business_legal_representative);

        //有效日期至
        business_effective_date = findViewById(R.id.Business_effective_date);


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


        BusinessLicenseMessage = message;
        L.i("BusinessLicenseMessage", "" + BusinessLicenseMessage);

        try {
            jsonObject = new JSONObject(BusinessLicenseMessage);

            BusinessLicenseBean businessLicenseBean = JsonUtil.parseJsonToBean(BusinessLicenseMessage,BusinessLicenseBean.class);


            BusinessLicenseBean.WordsResultBean words_result = businessLicenseBean.getWords_result();




            words_data = words_result.get成立日期().getWords();
            words_login_number = words_result.get证件编号().getWords();
            words_code = words_result.get社会信用代码().getWords();
            words_company_name = words_result.get单位名称().getWords();
            words_address = words_result.get地址().getWords();
            words_legal_representative = words_result.get法人().getWords();
            words_effective_date = words_result.get有效期().getWords();



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

                            absolutePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                            Glide
                                    .with(BusinessLicenseActivity.this)
                                    .load(absolutePath)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(NONE)
                                    .into(business_license_button);

                            //URL 转bitmap
                            Glide.with(BusinessLicenseActivity.this)
                                    .load(absolutePath)
                                    .asBitmap()
                                    .diskCacheStrategy(NONE)
                                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {


                                        @Override
                                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                            //得到bitmap
                                            L.i(bitmap.toString());
                                            businessStringBase64 = PictureUtil.bitmapToString(bitmap);

                                            L.i("88888" + businessStringBase64.toString());

                                            BusinessPicture();
                                        }

                                    });
                            business_login_number.setText(words_login_number);
                            business_socialCredit_code.setText(words_code);
                            business_company_name.setText(words_company_name);
                            business_address.setText(words_address);
                            business_legal_representative.setText(words_legal_representative);
                            business_effective_date.setText(words_data);

                            PostBusinessMessage();
                        }
                    });
        }


    }

    private void BusinessPicture() {

        Picture_params =new HashMap<>();
        Picture_params.put("uid", String.valueOf(usid));
        Picture_params.put("photo",businessStringBase64);

        HttpUtils.doPost(Config.TYD_BusinessLicensePicture, Picture_params, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                L.d("11111111111", "### fileName : " + e.toString());
                L.d("11111111111", "失败");


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.code() == 200) {


                    L.i("Picture_params" + response.body().string());

                    try {


                        L.i("Picture_params", "成功");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


                L.d("Picture_params33333333333", response.body().string());
                L.d("Picture_params33333333333", "成功");


            }
        });

    }

    private void PostBusinessMessage() {
        Business_Message_params=new HashMap<>();
        Business_Message_data =new HashMap<>();


        Business_Message_params.put("uid", String.valueOf(usid));

        Business_Message_data.put("number", words_login_number);

        Business_Message_data.put("code", words_code);

        Business_Message_data.put("unitname", words_company_name);

        Business_Message_data.put("address", words_address);

        Business_Message_data.put("legal", words_legal_representative);

        Business_Message_data.put("day_to", words_data);



        Business_Message_params.put("data", FileUti.getGet(Business_Message_data));


        HttpUtils.doPost(Config.TYD_BusinessMessage, Business_Message_params, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                L.d("11111111111", "### fileName : " + e.toString());
                L.d("11111111111", "失败");


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.code() == 200) {


                    L.i("Business_Message_params" + response.body().string());

                    try {


                        L.i("Business_Message_params", "成功");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


                L.d("Business_Message_params333333333", response.body().string());
                L.d("Business_Message_params333333333", "成功");


            }
        });
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
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
