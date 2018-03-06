package com.touedian.com.facetyd.model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class IDLookActivity extends AppCompatActivity {

    private String toIDcardMassageshow;
    private TextView idcard_look_name;
    private String name;
    private String idNumber;
    private String issueAuthority;
    private TextView idcard_look_idcard;
    private String signDate;
    private String expiryDate;
    private TextView idcard_idSignDate;
    private TextView idcard_idExpiryDate;
    private JSONObject jsonObjectback;
    private String id_name;
    private String id_expiryDate;
    private String nameMassageAmendShow;
    private String nameMessage;
    private String idNumberMassageAmendShow;
    private String idNumberMassage;
    private String signDateMassageAmendShow;
    private String signDateMassage;
    private String expiryDateMassageAmendShow;
    private String expiryDateMassage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(IDLookActivity.this);
        setContentView(R.layout.activity_idlook);
        Intent intent = getIntent();
        if (intent != null) {
            toIDcardMassageshow = intent.getStringExtra("IDcardMassageshow");
            ObjetIDcardMassage();

           // id_expiryDate = (String) SharedPreferencesUtils.getParam(IDLookActivity.this, "expiryDate", expiryDate);
        }
        nameMessage = SPUtils.getString(IDLookActivity.this,"name",nameMassageAmendShow);

        idNumberMassage = SPUtils.getString(IDLookActivity.this,"idNumber",idNumberMassageAmendShow);

        signDateMassage = SPUtils.getString(IDLookActivity.this,"signDate",signDateMassageAmendShow);

        expiryDateMassage = SPUtils.getString(IDLookActivity.this,"expiryDate",expiryDateMassageAmendShow);

        ImageView LookBankback=findViewById(R.id.LookBankback);
        LookBankback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //姓名
        idcard_look_name = findViewById(R.id.Idcard_look_name);
        idcard_look_name.setText(nameMessage);


        //身份证号
        idcard_look_idcard = findViewById(R.id.Idcard_look_idcard_show);


        idcard_look_idcard.setText(idNumberMassage);



        //身份证有效期限
        idcard_idSignDate = findViewById(R.id.Idcard_IdSignDate);


        idcard_idSignDate.setText(signDateMassage);


        //身份证到期时间
        idcard_idExpiryDate = findViewById(R.id.Idcard_IdExpiryDate);


        idcard_idExpiryDate.setText(expiryDateMassage);

    }

    private void ObjetIDcardMassage() {
        if (toIDcardMassageshow != null) {


            if (toIDcardMassageshow.contains("IDCardResult front")) {

                L.d("2222222", "身份证正面");
                String IDCardResultFront = toIDcardMassageshow.replace("IDCardResult front", "");

                //转换成功的集合
                String resultfront = IDCardResultFront.replaceAll("=", ":");

                try {
                    JSONObject jsonObjectfront = new JSONObject(resultfront);

                    toIDcardMassageshow = jsonObjectfront.optString("address");


                    idNumber = jsonObjectfront.optString("idNumber");
                    SPUtils.putString(IDLookActivity.this,"idNumber",idNumber);


                    name = jsonObjectfront.optString("name");
                    SPUtils.putString(IDLookActivity.this,"name",name);

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } else if (toIDcardMassageshow.contains("IDCardResult back")) {


                String IDCardResultBack = toIDcardMassageshow.replace("IDCardResult back", "");
                String IDCardResultBacks = "{" + IDCardResultBack.substring(2);


                //转换成功的集合
                String resultback = IDCardResultBacks.replaceAll("=", ":");

                try {
                    jsonObjectback = new JSONObject(resultback);


                    L.d("2222222", "身份证反面" + jsonObjectback.toString());

                    issueAuthority = jsonObjectback.optString("issueAuthority");

                    //有效期限
                    signDate = jsonObjectback.optString("signDate");
                    SPUtils.putString(IDLookActivity.this,"signDate",signDate);
                    //到期时间
                    expiryDate = jsonObjectback.optString("expiryDate");
                    SPUtils.putString(IDLookActivity.this,"expiryDate",expiryDate);


                } catch (JSONException e) {
                    e.printStackTrace();

                }
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
