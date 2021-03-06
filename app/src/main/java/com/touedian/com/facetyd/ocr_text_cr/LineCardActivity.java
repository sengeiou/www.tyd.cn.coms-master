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
import com.touedian.com.facetyd.ocr_text_bean.LineCardBean;
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

// 行驶证识别
public class LineCardActivity extends AppCompatActivity {
    private boolean hasGotToken = false;
    private AlertDialog.Builder alertDialog;
    private static final int REQUEST_CODE_VEHICLE_LICENSE = 120;
    private String LineCardMessage;
    private JSONObject jsonObject;
    private TextView lineText1;
    private TextView lineText2;
    private TextView lineText3;
    private TextView lineText4;
    private TextView lineCard_idnumber;
    private TextView linecard_type;
    private TextView linecard_all;
    private TextView linecard_address;
    private TextView linecard_nature;
    private TextView linecard_pinpaitype;
    private TextView linecard_code;
    private TextView linecard_enginecode;
    private TextView linecard_signdate;
    private TextView linecard_lssuedate;
    private String lineCard_idnumber_words;
    private String linecard_type_words;
    private String linecard_all_words;
    private String linecard_address_words;
    private String linecard_nature_words;
    private String linecard_pinpaitype_words;
    private String linecard_code_words;
    private String linecard_enginecode_words;
    private String linecard_signdate_words;
    private String linecard_lssuedate_words;
    private String absolutePath;
    private ImageView vehicle_license_button;
    private HashMap<String, String> Picture_params;
    private HashMap<String, String> LineCard_Message_params;
    private HashMap<String, String> LineCard_Message_data;
    private String picture_stringBase64;
    private int uid;
    private int usid;
    private Button line_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(LineCardActivity.this);
        setContentView(R.layout.activity_line_card);
        alertDialog = new AlertDialog.Builder(this);
        initAccessTokenWithAkSk();

        usid = SPUtils.getInt(LineCardActivity.this, "uid", uid);
        initdate();

        // 行驶证识别
        findViewById(R.id.vehicle_license_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LineCardActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_VEHICLE_LICENSE);
            }
        });

    }

    private void initdate() {


        ImageView bankcard_back = findViewById(R.id.linecard_bankcard_back);
        bankcard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        line_btn = findViewById(R.id.Line_btn);
        line_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(getApplication(),"成功",0);
                finish();
            }
        });

        vehicle_license_button = findViewById(R.id.vehicle_license_button);

        //号牌号码
        lineCard_idnumber = findViewById(R.id.lineCard_idnumber);
        //车辆类型
        linecard_type = findViewById(R.id.linecard_type);
        //所有人
        linecard_all = findViewById(R.id.linecard_all);
        //住址
        linecard_address = findViewById(R.id.linecard_address);
        //使用性质
        linecard_nature = findViewById(R.id.linecard_nature);
        //品牌型号
        linecard_pinpaitype = findViewById(R.id.linecard_pinpaitype);
        //车辆识别代号
        linecard_code = findViewById(R.id.linecard_code);
        //发动机号码
        linecard_enginecode = findViewById(R.id.linecard_enginecode);
        //注册日期
        linecard_signdate = findViewById(R.id.linecard_signdate);
        //发证日期
        linecard_lssuedate = findViewById(R.id.linecard_lssuedate);


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


        LineCardMessage = message;
        L.i("LineCardMessage", "" + LineCardMessage);

        try {
            jsonObject = new JSONObject(LineCardMessage);

            LineCardBean lineCardBean = JsonUtil.parseJsonToBean(LineCardMessage, LineCardBean.class);
            LineCardBean.WordsResultBean words_result = lineCardBean.getWords_result();


            lineCard_idnumber_words = words_result.get号牌号码().getWords();

            linecard_type_words = words_result.get车辆类型().getWords();

            linecard_all_words = words_result.get所有人().getWords();

            linecard_address_words = words_result.get住址().getWords();

            linecard_nature_words = words_result.get使用性质().getWords();

            linecard_pinpaitype_words = words_result.get品牌型号().getWords();

            linecard_code_words = words_result.get车辆识别代号().getWords();

            linecard_enginecode_words = words_result.get发动机号码().getWords();

            linecard_signdate_words = words_result.get注册日期().getWords();

            linecard_lssuedate_words = words_result.get发证日期().getWords();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // 识别成功回调，行驶证识别
        if (requestCode == REQUEST_CODE_VEHICLE_LICENSE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recVehicleLicense(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                            L.i(result);
                            absolutePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();

                            Glide
                                    .with(LineCardActivity.this)
                                    .load(absolutePath)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(NONE)
                                    .into(vehicle_license_button);

                            //URL 转bitmap
                            Glide.with(LineCardActivity.this)
                                    .load(absolutePath)
                                    .asBitmap()
                                    .diskCacheStrategy(NONE)
                                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {


                                        @Override
                                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                            //得到bitmap
                                            L.i(bitmap.toString());
                                            picture_stringBase64 = PictureUtil.bitmapToString(bitmap);

                                            L.i("88888" + picture_stringBase64.toString());
                                            LineCardPicture();
                                        }


                                    });


                            lineCard_idnumber.setText(lineCard_idnumber_words);

                            linecard_type.setText(linecard_type_words);

                            linecard_all.setText(linecard_all_words);

                            linecard_address.setText(linecard_address_words);

                            linecard_nature.setText(linecard_nature_words);

                            linecard_pinpaitype.setText(linecard_pinpaitype_words);

                            linecard_code.setText(linecard_code_words);

                            linecard_enginecode.setText(linecard_enginecode_words);

                            linecard_signdate.setText(linecard_signdate_words);

                            linecard_lssuedate.setText(linecard_lssuedate_words);
                        }
                    });
        }

    }

    private void LineCardPicture() {
        Picture_params = new HashMap<>();

        Picture_params.put("uid", String.valueOf(usid));
        Picture_params.put("photo", picture_stringBase64);

        HttpUtils.doPost(Config.TYD_LinePicture, Picture_params, new Callback() {


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
                    PostLineMessage();

                }


                L.d("Picture_params33333333333", response.body().string());
                L.d("Picture_params33333333333", "成功");


            }
        });
    }

    private void PostLineMessage() {

        LineCard_Message_params = new HashMap<>();

        LineCard_Message_data = new HashMap<>();


        LineCard_Message_params.put("uid", String.valueOf(usid));

        LineCard_Message_data.put("number", lineCard_idnumber_words);

        LineCard_Message_data.put("cartype", linecard_type_words);

        LineCard_Message_data.put("every", linecard_all_words);

        LineCard_Message_data.put("address", linecard_address_words);

        LineCard_Message_data.put("useproperty", linecard_nature_words);

        LineCard_Message_data.put("brandmodel", linecard_pinpaitype_words);

        LineCard_Message_data.put("code", linecard_code_words);

        LineCard_Message_data.put("engine_number", linecard_enginecode_words);

        LineCard_Message_data.put("registration", linecard_signdate_words);

        LineCard_Message_data.put("issuing", linecard_lssuedate_words);


        LineCard_Message_params.put("data", FileUti.getGet(LineCard_Message_data));

        HttpUtils.doPost(Config.TYD_LineMessage, LineCard_Message_params, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                L.d("11111111111", "### fileName : " + e.toString());
                L.d("11111111111", "失败");


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.code() == 200) {


                    L.i("LineCard_Message_params" + response.body().string());

                    try {


                        L.i("LineCard_Message_params", "成功");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


                L.d("LineCard_Message_params333333333", response.body().string());
                L.d("LineCard_Message_params333333333", "成功");


            }
        });
    }

    private void infoPopText(final String result) {
        alertText("", result);
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
