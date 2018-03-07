package com.touedian.com.facetyd.ocr_text_cr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.ocr_text_bean.DrivingCardBean;

import com.touedian.com.facetyd.ocr_text_bean.LawyerCardBean;

import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.Base64Uti;

import com.touedian.com.facetyd.utilsx.FileUti;

import com.touedian.com.facetyd.utilsx.HttpU;
import com.touedian.com.facetyd.utilsx.HttpUtils;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;


import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;


public class LawyerCardActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_GENERAL = 105;
    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private String LawyerCardMessage;
    private JSONObject jsonObject;
    private DrivingCardBean drivingCardBean;

    private String absolutePath;
    private TextView textViewss;
    private LawyerCardBean lawyerCardBean;
    private TextView textViews1;
    private TextView textViews2;
    private TextView textViews3;
    private TextView textViews4;
    private TextView textViews5;
    private TextView textViews6;
    private TextView textViews7;
    private TextView textViews8;
    private TextView textViews9;
    private TextView textViews10;
    private TextView textViews11;
    private TextView textViews12;
    private TextView lawyer_organization;
    private String words执业机构;
    private TextView lawyer_type;
    private TextView lawyer_number;
    private TextView lawyer_certificate_number;
    private TextView lawyer_holder;
    private TextView lawyer_sex;
    private TextView lawyer_idcode;
    private String words律师;
    private String words律师类别;
    private String words执业证号;
    private String words执业证号xx;
    private String words律师职业资格证;
    private String words律师职业资格证号码;
    private String words律师7;
    private String words律师11;
    private String words律师8;
    private String words律师10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullScreen(LawyerCardActivity.this);
        setContentView(R.layout.activity_lawyer_card);
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
        textViewss = findViewById(R.id.textsss);

        textViews1 = findViewById(R.id.textss1);
        textViews2 = findViewById(R.id.textss2);
        textViews3 = findViewById(R.id.textss3);
        textViews4 = findViewById(R.id.textss4);
        textViews5 = findViewById(R.id.textss5);
        textViews6 = findViewById(R.id.textss6);
        textViews7 = findViewById(R.id.textss7);
        textViews8 = findViewById(R.id.textss8);
        textViews9 = findViewById(R.id.textss9);
        textViews10 = findViewById(R.id.textss10);
        textViews11 = findViewById(R.id.textss11);
        textViews12 = findViewById(R.id.textss12);

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


        LawyerCardMessage = message;
        L.i("11111DrivingCardMessage", "" + LawyerCardMessage);

        try {
            jsonObject = new JSONObject(LawyerCardMessage);

            lawyerCardBean = JsonUtil.parseJsonToBean(LawyerCardMessage, LawyerCardBean.class);

            List<LawyerCardBean.WordsResultBean> words_result = lawyerCardBean.getWords_result();


            words执业机构 = words_result.get(0).getWords();

            words律师 = words_result.get(1).getWords();

            words律师类别 = words_result.get(2).getWords();

            words执业证号 = words_result.get(3).getWords();

            words执业证号xx = words_result.get(4).getWords();

            words律师职业资格证 = words_result.get(5).getWords();

            words律师职业资格证号码 = words_result.get(6).getWords();

            words律师7 = words_result.get(7).getWords();

            words律师8 = words_result.get(8).getWords();

            String words律师9 = words_result.get(9).getWords();

            words律师10 = words_result.get(10).getWords();

            words律师11 = words_result.get(11).getWords();

            //String words律师12 = words_result.get(12).getWords();




            //String words1 = words_result.get住址().getWords();
            textViewss.setText(words执业机构);
            textViews1.setText(words律师);
            textViews2.setText(words律师类别);
            textViews3.setText(words执业证号);
            textViews4.setText(words执业证号xx);
            textViews5.setText(words律师职业资格证);
            textViews6.setText(words律师职业资格证号码);
            textViews7.setText(words律师7);
            textViews8.setText(words律师8);
            textViews9.setText(words律师9);
            textViews10.setText(words律师10);
            textViews11.setText(words律师11);
            //textViews12.setText(words律师12);



        } catch (Exception e) {
            e.printStackTrace();
        }


       // notify();
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
                            infoPopText(result);
                            //拍摄的照片
                            absolutePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();

                            LogUtil.e("aaa", absolutePath.toString());

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Tone();
                                }
                            }).start();
                            lawyer_organization.setText(words执业机构);

                            lawyer_type.setText(words律师);

                            lawyer_number.setText(words执业证号);

                            lawyer_certificate_number.setText(words律师10);

                            lawyer_holder.setText(words执业证号xx);

                            lawyer_sex.setText(words律师8);

                            lawyer_idcode.setText(words律师11);




                        }
                    });
        }


    }

    private void Tone(){

        // 通用识别url
        String otherHost = "https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise";
        // 本地图片路径
        String filePath = absolutePath;
        L.i("*********************"+filePath.toString());
        String templateSign = "94bb3a1d85352a9c8246a19eedc2c275";

        try {
            byte[] imgData = FileUti.readFileByBytes(filePath);

            String imgStr = Base64Uti.encode(imgData);
            L.i("*********************"+imgStr.toString());
            String params = "templateSign=94bb3a1d85352a9c8246a19eedc2c275&image=" + URLEncoder.encode(imgStr, "UTF-8");
            //String params = templateSign + URLEncoder.encode(imgStr, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            String accessToken ="24.e6ebae8cf6c5879c422dc45c58fa5b39.2592000.1523002683.282335-10615594";
            String result = HttpU.post(otherHost, accessToken, params);

            LogUtil.e("----------------------",result);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();


        }
    }
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
}
