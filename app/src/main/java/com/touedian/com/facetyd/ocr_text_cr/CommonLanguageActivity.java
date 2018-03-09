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
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.ocr_text_bean.CommonLanguageBean;
import com.touedian.com.facetyd.ocr_text_bean.LawyerCardBean;
import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.PictureUtil;

import org.json.JSONObject;

import java.util.List;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.NONE;

public class CommonLanguageActivity extends AppCompatActivity {
    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private JSONObject jsonObject;
    private static final int REQUEST_CODE_GENERAL = 105;
    private String CommonLanguageMessage;
    private TextView common_text;
    private ImageView common_general_button;
    private ImageView common_bankcard_back;
    private String absolutePath;
    private String words0;
    private String words1;
    private String words2;
    private String words3;
    private String words4;
    private TextView common_text1;
    private TextView common_text2;
    private TextView common_text3;
    private TextView common_text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(CommonLanguageActivity.this);
        setContentView(R.layout.activity_common_language);



        alertDialog = new AlertDialog.Builder(this);
        initAccessTokenWithAkSk();


        initdate();

        // 通用文字识别（含位置信息版）
        findViewById(R.id.Common_general_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(CommonLanguageActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL);
            }
        });
    }

    private void initdate() {


        common_bankcard_back = findViewById(R.id.Common_bankcard_back);
        common_bankcard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        common_general_button = findViewById(R.id.Common_general_button);

        common_text = findViewById(R.id.Common_text0);
        common_text1 = findViewById(R.id.Common_text1);
        common_text2 = findViewById(R.id.Common_text2);
        common_text3 = findViewById(R.id.Common_text3);
        common_text4 = findViewById(R.id.Common_text4);
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


        CommonLanguageMessage = message;
        L.i("CommonLanguageMessage", "" + CommonLanguageMessage);

        try {
            jsonObject = new JSONObject(CommonLanguageMessage);

            LawyerCardBean lawyerCardBean = JsonUtil.parseJsonToBean(CommonLanguageMessage,LawyerCardBean.class);

            List<LawyerCardBean.WordsResultBean> words_result = lawyerCardBean.getWords_result();

            words0 = words_result.get(0).getWords();
            words1 = words_result.get(1).getWords();
            words2 = words_result.get(2).getWords();
            words3 = words_result.get(3).getWords();
            words4 = words_result.get(4).getWords();


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


        // 识别成功回调，通用文字识别（含位置信息）
        if (requestCode == REQUEST_CODE_GENERAL && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneral(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                            L.i(result.toString());


                            absolutePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                            Glide
                                    .with(CommonLanguageActivity.this)
                                    .load(absolutePath)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(NONE)
                                    .into(common_general_button);

                            //URL 转bitmap
                            Glide.with(CommonLanguageActivity.this)
                                    .load(absolutePath)
                                    .asBitmap()
                                    .diskCacheStrategy(NONE)
                                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {


                                        @Override
                                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                            //得到bitmap
                                            L.i(bitmap.toString());
                                            String front_stringBase64 = PictureUtil.bitmapToString(bitmap);

                                            L.i("88888" + front_stringBase64.toString());
                                        }

                                    });

                            common_text.setText(words0);
                            common_text1.setText(words1);
                            common_text2.setText(words2);
                            common_text3.setText(words3);
                            common_text4.setText(words4);
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

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity)   {
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
