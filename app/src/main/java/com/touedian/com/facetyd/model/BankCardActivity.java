package com.touedian.com.facetyd.model;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.touedian.com.facetyd.Config;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.bean.CardviewBean;
import com.touedian.com.facetyd.ocr_text_cr.RecognizeService;
import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.HttpUtils;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.SPUtils;
import com.touedian.com.facetyd.utilsx.ToastUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.NONE;

public class BankCardActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_BANKCARD = 111;//银行卡标识

    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private String filePath;
    private ImageView idcard_button;
    private String number;
    private String cardType;
    private String bankname;
    private TextView bankcard_type;
    private TextView bankcard_number;
    private TextView bankcard_name;
    private HashMap<String, String> Bankcard_up = new HashMap<String, String>();
    private int uid;
    private HashMap<String, String> bankcard_zhuan;
    private Button bankcard_btn;
    private ImageView imageView2;


    private boolean Yhk;
    private CardviewBean cardviewBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(BankCardActivity.this);
        setContentView(R.layout.activity_bank_card);
        Intent intent=getIntent();
        if (intent != null) {
            //uid = intent.getStringExtra("uid");

            uid=   SPUtils.getInt(BankCardActivity.this,"uid",uid);

        }
        ButterKnife.bind(this);
        initAccessTokenWithAkSk();

        idcard_button = findViewById(R.id.bankcard_carme);
        idcard_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(BankCardActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_BANK_CARD);
                startActivityForResult(intent, REQUEST_CODE_BANKCARD);
            }
        });

        imageView2 = findViewById(R.id.bankcard_back);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bankcard_type = findViewById(R.id.Bankcard_type);

        bankcard_number = findViewById(R.id.Bankcard_number);

        bankcard_name = findViewById(R.id.Bankcard_name);

        bankcard_btn = findViewById(R.id.Bankcard_btn);
        bankcard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 finish();
            }
        });


    }


    public void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {

                L.i("Bankcard:Token" + result.toString().trim());
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                //alertText("AK，SK方式获取token失败", "请检查网络");
            }
        }, getApplicationContext(), "9evxCWG1MTN8k7u3XU0qVIqi", "5eesgiqRtSflHYOM5OUZLSsSeMPCC81n");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，downbankcard
        if (requestCode == REQUEST_CODE_BANKCARD && resultCode == Activity.RESULT_OK) {
            RecognizeService.recBankCard(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),

                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            filePath = FileUtil.getSaveFile(getApplication()).getAbsolutePath();
                            infoPopText(result);

// 银行卡识别参数设置
                            BankCardParams param = new BankCardParams();
                            param.setImageFile(new File(filePath));

// 调用银行卡识别服务
                            OCR.getInstance().recognizeBankCard(param, new OnResultListener<BankCardResult>() {
                                @Override
                                public void onResult(BankCardResult result) {

                                    L.i("222222222222", "result=" + result);
                                    // 调用成功，返回BankCardResult对象

                                    number = result.getBankCardNumber();
                                    L.i("number", "" + number.toString());

                                    cardType = String.valueOf(result.getBankCardType());
                                    L.i("cardType", "" + cardType.toString());

                                    bankname = result.getBankName();
                                    L.i("Bankname", "" + bankname.toString());

                                    if(number!=null&&cardType!=null&&bankname!=null){



                                    bankcard_number.setText(number);
                                    bankcard_type.setText(cardType);
                                    bankcard_name.setText(bankname);




                                    Glide
                                            .with(BankCardActivity.this)
                                            .load(filePath)
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(NONE)
                                            .into(idcard_button);



                                        PostBankcard();
                                    }else {
                                        ToastUtils.showLong(BankCardActivity.this,"请检查");
                                    }
                                }

                                @Override
                                public void onError(OCRError error) {


                                    ToastUtils.showShort(BankCardActivity.this, "请检查网络");
                                    // 调用失败，返回OCRError对象
                                }

                            });

                        }
                    });
        }

    }

    private void PostBankcard() {


        bankcard_zhuan = new HashMap<String, String>();

        Bankcard_up.put("uid", String.valueOf(uid));

        bankcard_zhuan.put("mcard_id",number);
        bankcard_zhuan.put("mcard_type",cardType);
        bankcard_zhuan.put("mcard_name",bankname);



        Bankcard_up.put("data", getGet(bankcard_zhuan));


        HttpUtils.doPost(Config.TYD_BankcardMessage_up, Bankcard_up, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200&&Bankcard_up!=null){
                    String s = response.body().string();

                    try {
                        JSONObject jsonObject=new JSONObject(s);

                        cardviewBean = JsonUtil.parseJsonToBean(s, CardviewBean.class);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Yhk=true;
                    SPUtils.putBoolean(BankCardActivity.this,"yhk",Yhk);
                }else {
                    ToastUtils.showLong(BankCardActivity.this,"请检查");
                }

                //关闭防止内存泄漏
                if (response.body() != null) {
                    response.body().close();

                    Bankcard_up.clear();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessTokenWithAkSk();
        } else {
            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "请稍等", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    private void infoPopText(final String result) {


        ToastUtils.showShort(BankCardActivity.this, result.toString());
        //alertText("", result);
    }

    private void alertText(final String title, final String message) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog
                        .setMessage("请稍等")
                        .setPositiveButton("exchangebutton", null)
                        .show();

            }
        });
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
    /**
     * 转换get请求参数   集合转换字符串工具
     *
     * @param map
     * @return
     */
    public static String getGet(HashMap<String, String> map) {
        StringBuffer sb = new StringBuffer();
        String key = null;
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();

        while (it.hasNext()) {
            key = it.next();
            sb.append(key + "=" + map.get(key) + "&");
        }
        String sbss=sb.substring(0,sb.length()-1).replaceAll(" ", "");//减去最后一个字符号&     replaceAll去掉中间空格

        L.i("sbss",sbss.toString().replaceAll(" ", ""));
        return sbss.toString();

    }


}
