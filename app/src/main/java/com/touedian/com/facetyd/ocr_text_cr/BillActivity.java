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
import com.touedian.com.facetyd.ocr_text_bean.BillBean;
import com.touedian.com.facetyd.ocr_text_bean.BusinessLicenseBean;
import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.PictureUtil;

import org.json.JSONObject;

import java.util.List;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.NONE;

public class BillActivity extends AppCompatActivity {
    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private JSONObject jsonObject;
    private static final int REQUEST_CODE_RECEIPT = 124;
    private String BillMessage;
    private TextView bill_text;
    private TextView date_of_issue;
    private TextView licenPlate_drawer;
    private TextView licenPlate_drawer_account;
    private TextView licenPlate_drawer_line;
    private TextView licenPlate_drawer_money;
    private TextView licenPlate_money_order_date;
    private String absolutePath;
    private ImageView receipt_button;
    private ImageView bill_bankcard_back;
    private String words_worddate;
    private String words_allname;
    private String words_nameid;
    private String words_name;
    private String words_moeny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(BillActivity.this);
        setContentView(R.layout.activity_bill);


        alertDialog = new AlertDialog.Builder(this);
        initAccessTokenWithAkSk();


        initdate();


        // 通用票据识别
        findViewById(R.id.receipt_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(BillActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_RECEIPT);
            }
        });

    }

    private void initdate() {



        bill_bankcard_back = findViewById(R.id.Bill_bankcard_back);
        bill_bankcard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        receipt_button = findViewById(R.id.receipt_button);


        //出票日期
        date_of_issue = findViewById(R.id.date_of_issue);

        //出票人全称
        licenPlate_drawer = findViewById(R.id.LicenPlate_drawer);

        //出票人账号
        licenPlate_drawer_account = findViewById(R.id.LicenPlate_drawer_account);

        //出票行全称
        licenPlate_drawer_line = findViewById(R.id.LicenPlate_drawer_line);

        //出票金额
        licenPlate_drawer_money = findViewById(R.id.LicenPlate_drawer_money);

        //汇票到期日
        licenPlate_money_order_date = findViewById(R.id.LicenPlate_money_order_date);

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


        BillMessage = message;

        L.LogUtil.e("BillMessage",BillMessage);
        try {
            jsonObject = new JSONObject(BillMessage);

            BillBean billBean = JsonUtil.parseJsonToBean(BillMessage,BillBean.class);


            List<BillBean.WordsResultBean> words_result = billBean.getWords_result();


            words_worddate = words_result.get(2).getWords();//出票日期

            //出票人全称
            words_allname = words_result.get(51).getWords();

            //开户行及账号
            words_nameid = words_result.get(45).getWords();

            //出票行全称
            words_name =words_result.get(34).getWords();

            //出票金额
            words_moeny = words_result.get(33).getWords();

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


        // 识别成功回调，通用票据识别
        if (requestCode == REQUEST_CODE_RECEIPT && resultCode == Activity.RESULT_OK) {
            RecognizeService.recReceipt(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                            L.i(result.toString());


                            absolutePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                            Glide
                                    .with(BillActivity.this)
                                    .load(absolutePath)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(NONE)
                                    .into(receipt_button);

                            //URL 转bitmap
                            Glide.with(BillActivity.this)
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

                            date_of_issue.setText(words_worddate);

                            licenPlate_drawer.setText(words_allname);

                            licenPlate_drawer_account.setText(words_nameid);

                            licenPlate_drawer_line.setText(words_name);

                            licenPlate_drawer_money.setText(words_moeny);
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
