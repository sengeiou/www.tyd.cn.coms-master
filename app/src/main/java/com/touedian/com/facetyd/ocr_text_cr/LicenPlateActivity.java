package com.touedian.com.facetyd.ocr_text_cr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
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
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.touedian.com.facetyd.Config;
import com.touedian.com.facetyd.R;

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


public class LicenPlateActivity extends AppCompatActivity {
    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private static final int REQUEST_CODE_LICENSE_PLATE = 122;
    private String LicenPlateMessage;
    private JSONObject jsonObject;

    private TextView licence_plate;
    private TextView license_plate_color;

    private ImageView licenPlate_bankcard_back;
    private ImageView license_plate_button;
    private String absolutePath;
    private String licence_plate_number;
    private String licence_plate_color;
    private String licenPlatestringBase64;
    private HashMap<String, String> Picture_params;
    private HashMap<String, String> LicenPlate_Message_params;
    private HashMap<String, String> LicenPlate_Message_data;
    private int uid;
    private int usid;
    private Button licenPlate_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(LicenPlateActivity.this);
        setContentView(R.layout.activity_licen_plate);


        alertDialog = new AlertDialog.Builder(this);
        initAccessTokenWithAkSk();

        usid = SPUtils.getInt(LicenPlateActivity.this, "uid", uid);

        initdate();
        // 车牌识别
        findViewById(R.id.license_plate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(LicenPlateActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_LICENSE_PLATE);
            }
        });
    }

    private void initdate() {


        licenPlate_bankcard_back = findViewById(R.id.LicenPlate_bankcard_back);
        licenPlate_bankcard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        licenPlate_btn = findViewById(R.id.LicenPlate_btn);
        licenPlate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(getApplication(),"成功",0);
                finish();
            }
        });


        license_plate_button = findViewById(R.id.license_plate_button);


        //车牌号
        licence_plate = findViewById(R.id.licence_plate);

        //牌照颜色
        license_plate_color = findViewById(R.id.license_plate_color);


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


        LicenPlateMessage = message;
        L.i("LicenPlateMessage", "" + LicenPlateMessage);

        try {
            jsonObject = new JSONObject(LicenPlateMessage);

            LicencePlateBean licencePlateBean = JsonUtil.parseJsonToBean(LicenPlateMessage, LicencePlateBean.class);

            LicencePlateBean.WordsResultBean words_result = licencePlateBean.getWords_result();

            licence_plate_number = words_result.getNumber();

            licence_plate_color = words_result.getColor();


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

        // 识别成功回调，车牌识别
        if (requestCode == REQUEST_CODE_LICENSE_PLATE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recLicensePlate(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                            L.i(result.toString());

                            absolutePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                            Glide
                                    .with(LicenPlateActivity.this)
                                    .load(absolutePath)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(NONE)
                                    .centerCrop()
                                   // .transform(new RotateTransformation(getApplication(), 90f))
                                    .into(license_plate_button);

                            //URL 转bitmap
                            Glide.with(LicenPlateActivity.this)
                                    .load(absolutePath)
                                    .asBitmap()
                                    .diskCacheStrategy(NONE)
                                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {


                                        @Override
                                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                            //得到bitmap
                                            L.i(bitmap.toString());
                                            licenPlatestringBase64 = PictureUtil.bitmapToString(bitmap);

                                            L.i("88888" + licenPlatestringBase64.toString());

                                            LicenPlatePictuer();
                                        }

                                    });


                            licence_plate.setText(licence_plate_number);

                            license_plate_color.setText(licence_plate_color);
                        }
                    });
        }


    }

    private void LicenPlatePictuer() {

        Picture_params=new HashMap<>();

        Picture_params.put("uid", String.valueOf(usid));
        Picture_params.put("photo",licenPlatestringBase64);

        HttpUtils.doPost(Config.TYD_LicenPlatePicture, Picture_params, new Callback() {


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
                    PostLicenPlateMessage();

                }


                L.d("Picture_params33333333333", response.body().string());
                L.d("Picture_params33333333333", "成功");


            }
        });
    }

    private void PostLicenPlateMessage() {

        LicenPlate_Message_params =new HashMap<>();
        LicenPlate_Message_data =new HashMap<>();


        LicenPlate_Message_params.put("uid", String.valueOf(usid));

        LicenPlate_Message_data.put("car_num", licence_plate_number);

        LicenPlate_Message_data.put("car_color", licence_plate_color);




        LicenPlate_Message_params.put("data", FileUti.getGet(LicenPlate_Message_data));

        HttpUtils.doPost(Config.TYD_LicenPlateMessage, LicenPlate_Message_params, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                L.d("11111111111", "### fileName : " + e.toString());
                L.d("11111111111", "失败");


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.code() == 200) {


                    L.i("LicenPlate_Message_params" + response.body().string());

                    try {


                        L.i("LicenPlate_Message_params", "成功");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


                L.d("LicenPlate_Message_params333333333", response.body().string());
                L.d("LicenPlate_Message_params333333333", "成功");


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
