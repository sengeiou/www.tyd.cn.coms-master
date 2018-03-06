package com.touedian.com.facetyd.model;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.touedian.com.facetyd.Config;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.ocr_text_bean.CxchangeBean;
import com.touedian.com.facetyd.ocr_text_cr.DrivingActivity;
import com.touedian.com.facetyd.utilsx.HttpUtils;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.SPUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CxchangeActivity extends AppCompatActivity {


    private int uid;

    private HashMap<String, String> map = new HashMap<String, String>();
    private EditText exchangeEditText;
    private String sexchangeEditText;
    private int uId;
    private Button cxchangBtn;
    private TextView exchangeTextText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(CxchangeActivity.this);
        setContentView(R.layout.activity_cxchange);
        ButterKnife.bind(this);

        uId = SPUtils.getInt(CxchangeActivity.this,"uid",uid);
        L.i("uId", String.valueOf(uId));

        exchangeEditText = findViewById(R.id.ExchangeEditText);
        exchangeTextText = findViewById(R.id.ExchangeTextText);

        PostCx();
        ImageView imageView2=findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cxchangBtn = findViewById(R.id.CxchangBtn);
        exchangeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cxchangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void PostCx() {

        sexchangeEditText = exchangeEditText.getText().toString();
        map.put("uid", String.valueOf(uId));
        L.i("uid",String.valueOf(uId));
        map.put("minfo",sexchangeEditText);
        L.i("minfo",sexchangeEditText);
        HttpUtils.doPost(Config.TYD_ExchangeMessage_get, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if(response.code()==200){

                    String s=response.body().string();
                    try {
                        JSONObject jsonObject=new JSONObject(s);

                     CxchangeBean cxchangeBean=   JsonUtil.parseJsonToBean(s,CxchangeBean.class);

                        View view=new View(CxchangeActivity.this);
                        if (cxchangeBean.getStatus()==1){


                            exchangeEditText.setVisibility(view.GONE);
                            exchangeTextText.setVisibility(view.VISIBLE);
                        }else {
                            exchangeEditText.setVisibility(view.VISIBLE);
                            exchangeTextText.setVisibility(view.GONE);
                        }

                    } catch ( Exception e) {
                        e.printStackTrace();
                    }

                   // ToastUtils.showLong(CxchangeActivity.this,"成功");
                }

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
}
