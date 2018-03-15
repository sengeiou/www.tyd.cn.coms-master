package com.touedian.com.facetyd.ocr_text_cr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.touedian.com.facetyd.ocr_text_bean.DrivingCardBean;

import com.touedian.com.facetyd.ocr_text_bean.LawyerCardBean;

import com.touedian.com.facetyd.ocr_text_bean.LawyerCardBeandate;
import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.Base64Uti;

import com.touedian.com.facetyd.utilsx.FileUti;

import com.touedian.com.facetyd.utilsx.HttpU;
import com.touedian.com.facetyd.utilsx.HttpUtils;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.PictureUtil;
import com.touedian.com.facetyd.utilsx.SPUtils;
import com.touedian.com.facetyd.utilsx.ToastUtils;


import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.NONE;


public class LawyerCardActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_GENERAL = 105;
    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private String LawyerCardMessage;
    private JSONObject jsonObject;


    private String absolutePath;

    private LawyerCardBeandate lawyerCardBeandate;

    private TextView lawyer_organization;

    private TextView lawyer_type;
    private TextView lawyer_number;
    private TextView lawyer_certificate_number;
    private TextView lawyer_holder;
    private TextView lawyer_sex;
    private TextView lawyer_idcode;

    private String accessToken;
    private String result;
    private String word0;
    private String word1;
    private String word2;
    private String word3;
    private String word4;
    private String word5;
    private String word6;

    private Handler handler = null;
    private ImageView lawyer_bankcard_back;
    private ImageView general_button;
    private String lawyercard_stringBase64;
    private HashMap<String, String> Picture_params;
    private HashMap<String, String> Laywer_Message_params;
    private HashMap<String, String> Laywer_Message_data;
    private int uid;
    private int usid;
    private Button lawyer_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullScreen(LawyerCardActivity.this);
        setContentView(R.layout.activity_lawyer_card);
        usid = SPUtils.getInt(LawyerCardActivity.this, "uid", uid);
        L.i("Lawyer_usid", String.valueOf(usid));
        //创建属于主线程的handler
        handler = new Handler();
        initAccessTokenWithAkSk();
        alertDialog = new AlertDialog.Builder(this);


        InitDate();
        // 通用文字识别（含位置信息版）
        findViewById(R.id.general_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LawyerCardActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL);
            }
        });


    }

    private void InitDate() {

        lawyer_bankcard_back = findViewById(R.id.lawyer_bankcard_back);

        lawyer_bankcard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        lawyer_btn = findViewById(R.id.Lawyer_btn);

        lawyer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ToastUtils.show(getApplication(),"成功",0);
                finish();
            }
        });

        general_button = findViewById(R.id.general_button);
        //执业机构
        lawyer_organization = findViewById(R.id.lawyer_organization);

        //执业证类型
        lawyer_type = findViewById(R.id.lawyer_type);

        //执业证号
        lawyer_number = findViewById(R.id.lawyer_number);

        //法律职业资格\n或律师资格证号
        lawyer_certificate_number = findViewById(R.id.lawyer_certificate_number);

        //持证人
        lawyer_holder = findViewById(R.id.lawyer_holder);

        //性别
        lawyer_sex = findViewById(R.id.lawyer_sex);

        //身份证号
        lawyer_idcode = findViewById(R.id.lawyer_idcode);


    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {

                hasGotToken = true;
                accessToken = result.getAccessToken();

                L.i(result.getAccessToken());
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(), "9evxCWG1MTN8k7u3XU0qVIqi", "5eesgiqRtSflHYOM5OUZLSsSeMPCC81n");
    }

    private void alertText(final String title, final String message) {

        ToastUtils.show(getApplication(), "请稍后,等待时间大概2~3秒", 0);
        new Thread(new Runnable() {
            @Override
            public void run() {

                Tone();

                handler.post(runnableUi);
            }
        }).start();

        LawyerCardMessage = message;
        L.i("11111DrivingCardMessage", "" + LawyerCardMessage);


    }

    private void infoPopText(final String result) {
        alertText("", result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // 识别成功回调，通用文字识别（含位置信息）
        if (requestCode == REQUEST_CODE_GENERAL && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneral(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {

                            //拍摄的照片
                            absolutePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();

                            Glide
                                    .with(LawyerCardActivity.this)
                                    .load(absolutePath)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(NONE)
                                    .into(general_button);

                            //URL 转bitmap
                            Glide.with(LawyerCardActivity.this)
                                    .load(absolutePath)
                                    .asBitmap()
                                    .diskCacheStrategy(NONE)
                                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {


                                        @Override
                                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                            //得到bitmap
                                            L.i(bitmap.toString());
                                            lawyercard_stringBase64 = PictureUtil.bitmapToString(bitmap);

                                            L.i("88888" + lawyercard_stringBase64.toString());
                                            LawyerCardPicture();

                                        }

                                    });

                            LogUtil.e("aaa", absolutePath.toString());


                            infoPopText(result);


                        }
                    });
        }


    }

    private void LawyerCardPicture() {
        Picture_params = new HashMap<>();

        Picture_params.put("uid", String.valueOf(usid));

        Picture_params.put("photo", lawyercard_stringBase64);

        HttpUtils.doPost(Config.TYD_LawyerPicture, Picture_params, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                L.d("11111111111", "### fileName : " + e.toString());
                L.d("11111111111", "失败");


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.code() == 200) {


                    L.i("Picture_params"+response.body().string());

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

    private void PostLawerMess() {
        Laywer_Message_params = new HashMap<>();
        Laywer_Message_data = new HashMap<>();

        Laywer_Message_params.put("uid", String.valueOf(usid));

        Laywer_Message_data.put("cardholder", word4);

        Laywer_Message_data.put("practising", word0);

        Laywer_Message_data.put("license", word2);

        Laywer_Message_data.put("number", word3);

        Laywer_Message_data.put("practice", word1);

        Laywer_Message_data.put("idnumber", word6);

        Laywer_Message_data.put("sex", word5);



        Laywer_Message_params.put("data", FileUti.getGet(Laywer_Message_data));



        HttpUtils.doPost(Config.TYD_LawyerMessage, Laywer_Message_params, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                L.d("11111111111", "### fileName : " + e.toString());
                L.d("11111111111", "失败");


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.code() == 200) {


                    L.i("Laywer_Message_params"+response.body().string());

                    try {


                        L.i("Laywer_Message_params", "成功");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


                L.d("Laywer_Message_params33333333333", response.body().string());
                L.d("Laywer_Message_params33333333333", "成功");


            }
        });
    }

    private void Tone() {

        // 通用识别url
        String otherHost = "https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise";
        // 本地图片路径
        String filePath = absolutePath;
        L.i("*********************" + filePath.toString());
        String templateSign = "94bb3a1d85352a9c8246a19eedc2c275";

        try {
            byte[] imgData = FileUti.readFileByBytes(filePath);

            String imgStr = Base64Uti.encode(imgData);
            L.i("*********************" + imgStr.toString());

            String params = "templateSign=94bb3a1d85352a9c8246a19eedc2c275&image=" + URLEncoder.encode(imgStr, "UTF-8");
            //String params = templateSign + URLEncoder.encode(imgStr, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            // String accessToken ="24.e6ebae8cf6c5879c422dc45c58fa5b39.2592000.1523002683.282335-10615594";
            result = HttpU.post(otherHost, accessToken, params);


            LogUtil.e("----------------------", result);
            System.out.println(result);


            jsonObject = new JSONObject(result);

            lawyerCardBeandate = JsonUtil.parseJsonToBean(result, LawyerCardBeandate.class);

            List<LawyerCardBeandate.DataBean.RetBean> ret = lawyerCardBeandate.getData().getRet();

            word0 = ret.get(0).getWord();

            L.i("word0" + word0.toString());
            word1 = ret.get(1).getWord();

            word2 = ret.get(2).getWord();

            word3 = ret.get(3).getWord();

            word4 = ret.get(4).getWord();

            word5 = ret.get(5).getWord();

            word6 = ret.get(6).getWord();
            L.i("word6" + word6.toString());


        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            lawyer_organization.setText(word0);

            lawyer_type.setText(word3);

            lawyer_number.setText(word2);

            lawyer_certificate_number.setText(word1);

            lawyer_holder.setText(word5);

            lawyer_sex.setText(word6);

            lawyer_idcode.setText(word4);
            PostLawerMess();
        }

    };

    public static class LogUtil {
        /**
         * 截断输出日志
         *
         * @param msg
         */
        public static void e(String tag, String msg) {
            if (tag == null || tag.length() == 0
                    || msg == null || msg.length() == 0)
                return;

            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.e(tag, msg);
            } else {
                while (msg.length() > segmentSize) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize);
                    msg = msg.replace(logContent, "");
                    Log.e(tag, logContent);
                }
                Log.e(tag, msg);// 打印剩余日志
            }
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

    @Override
    protected void onDestroy() {
        //将线程销毁掉
        handler.removeCallbacks(runnableUi);
        Log.d("Thread", "Activity --onDestroy中线程是否活着:" + handler.toString());
        super.onDestroy();

    }
}
