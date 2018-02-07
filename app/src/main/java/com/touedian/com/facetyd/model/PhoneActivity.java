package com.touedian.com.facetyd.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.touedian.com.facetyd.Config;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.bean.PhoneLogin;
import com.touedian.com.facetyd.ocr_face.OcrFaceActivity;
import com.touedian.com.facetyd.utilsx.HttpUtils;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.NetUtils;
import com.touedian.com.facetyd.utilsx.SPUtils;
import com.touedian.com.facetyd.utilsx.ToastUtils;
import com.weavey.loading.lib.LoadingLayout;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Author:hwr
 * Date:2018/newhomepageone/22
 * 佛祖保佑       永无BUG     永不修改
 * <p>
 * 手机号码验证 登录
 */

public class PhoneActivity extends AppCompatActivity {
    private Button timeButton;
    private EditText editText_yzm;
    private LoadingLayout loading;
    private String jsonStr; // 需要解析json格式的字符串
    private Button request_buttons;
    private String url;
    private EditText edit_passwords;
    private RadioButton radiobutton;
    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, String> lodingcode = new HashMap<String, String>();

    private String phonenumber;
    private String phonenumbers;
    private String phoneMessCode;
    private PhoneLogin phoneLogin;
    private JSONObject jsonObject;
    private  String uid;
    private int identity_status;
    private int uisd;
    private int IeGrid;
    private int iG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        //从本地获取到IeGrid判断实名认证的标识
        iG = SPUtils.getInt(PhoneActivity.this,"identity_status",IeGrid);

        View view=new View(this);

        LinearLayout Phone_Liner=findViewById(R.id.Phone_Liner);//判断H5Text文字和RadioButton是否展示
        final GlobalValue globalValue = new GlobalValue();
        radiobutton = findViewById(R.id.radiobutton);
        if(iG==1){
            Phone_Liner.setVisibility(view.GONE);
            radiobutton.setChecked(true);
        }else if(iG!=1){
            Phone_Liner.setVisibility(view.VISIBLE);
            radiobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isCheck = globalValue.isCheck();
                    if (isCheck) {
                        if (view == radiobutton) radiobutton.setChecked(false);
                        Toast.makeText(getApplicationContext(), "请选择", Toast.LENGTH_LONG).show();
                    } else {
                        if (view == radiobutton) radiobutton.setChecked(true);

                        Toast.makeText(getApplicationContext(), "阅读完毕!", Toast.LENGTH_SHORT).show();
                    }
                    globalValue.setCheck(!isCheck);
                }
            });
        }
        editText_yzm = findViewById(R.id.phone_name);//手机号码


        timeButton = (Button) findViewById(R.id.timebutton);



        edit_passwords = findViewById(R.id.Edit_password);//newcodeicon

        request_buttons = findViewById(R.id.request_button);//登录btn


        TextView Phone_h5text=findViewById(R.id.Phone_h5text);
        Phone_h5text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PhoneActivity.this,H5Activity.class);
                startActivity(intent);
            }
        });


        request_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (editText_yzm.length() == 11 && edit_passwords.length() == 4 && radiobutton.isChecked()) {

                    DoPost();


                    SPUtils.putString(PhoneActivity.this,"phonenumber",phonenumber);

                    if (radiobutton.isChecked()&&iG!=1) {

                        DoPost();


                        SPUtils.putString(PhoneActivity.this,"phonenumber",phonenumber);

                    } else if(iG!=1){
                        ToastUtils.showShort(PhoneActivity.this, "请阅读后勾选");
                    }

                } else if (editText_yzm.length() == 11 && edit_passwords.length() < 4) {
                    ToastUtils.showShort(PhoneActivity.this, "请输入正确的手机号和验证码");
                } else if (editText_yzm.length() < 11 && edit_passwords.length() == 4) {
                    ToastUtils.showShort(PhoneActivity.this, "请输入正确的手机号和验证码");
                } else if (editText_yzm.length() < 11 && edit_passwords.length() < 4) {
                    ToastUtils.showShort(PhoneActivity.this, "请输入正确的手机号和验证码");
                }else if (editText_yzm.length() == 11 && edit_passwords.length() == 4 && !radiobutton.isChecked() ) {
                    ToastUtils.showShort(PhoneActivity.this, "请请阅读后勾选");
                }




            }

        });
        editText_yzm.addTextChangedListener(new TextWatcher() {
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

        //new倒计时对象,总共的时间,每隔多少秒更新一次时间
        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);


        //设置Button点击事件触发倒计时
        timeButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                //判断网络是否连接
                if (NetUtils.isConnected(PhoneActivity.this)) {
                    myCountDownTimer.start();
                    phonenumber = editText_yzm.getText().toString();
                    map.put("mobile", phonenumber);
                    HttpUtils.doPost(Config.TYD_Verification_Code, map, new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {

                          //  ToastUtils.showShort(PhoneActivity.this, "验证码发送失败");
                            L.i("newcodeicon", "失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            if (response.code() == 200) {
                                if (phonenumber.length() == 11) {
                                    L.i("手机号码", phonenumber);
                                   // ToastUtils.showShort(PhoneActivity.this, "验证码已发送" + response);
                                    L.i("newcodeicon", "成功" + response);


                                } else if (phonenumber.length() < 11) {
                                    L.i("newcodeicon", "手机号码错误");
                                   // Toast.makeText(PhoneActivity.this, "手机号码错误", Toast.LENGTH_SHORT).show();
                                   // ToastUtils.showLong(getApplicationContext(), "验证码已发送" + response);
                                }

                            } else {

                            }
                            //关闭防止内存泄漏
                            if (response.body() != null) {
                                response.body().close();

                                map.clear();
                            }


                        }
                    });

                }

            }


        });


    }

    //登录
    private void DoPost() {

        if (NetUtils.isConnected(PhoneActivity.this)) {
            phonenumber = editText_yzm.getText().toString();
            phoneMessCode = edit_passwords.getText().toString();
            lodingcode.put("mobile", phonenumber);
            lodingcode.put("code", phoneMessCode);
            HttpUtils.doPost(Config.TYD_Phone_Login, lodingcode, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {



                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        L.i("手机号码2", phoneMessCode);
                        L.i("手机号码2", phonenumber);
                        L.i("手机号码2", "登录接口" + response.toString());

                        String s = response.body().string();
                        try {

                            //TODO

                                jsonObject = new JSONObject(s);


                                phoneLogin = JsonUtil.parseJsonToBean(s, PhoneLogin.class);

                            if (phoneLogin.getStatus()==1){
                                Intent intent=new Intent(PhoneActivity.this,OcrFaceActivity.class);
                                intent.putExtra("identity_status",phoneLogin.getIdentity_status());
                                intent.putExtra("uid",phoneLogin.getUid());
                                //
                                intent.putExtra("avatar",phoneLogin.getAvatar());
                                Log.d("url",phoneLogin.getAvatar());

                                intent.putExtra("identity_card",phoneLogin.getIdentity_card());
                                intent.putExtra("truename",phoneLogin.getTruename());
                                intent.putExtra("username",phoneLogin.getUsername());
                                SPUtils.putInt(PhoneActivity.this,"uid", Integer.parseInt(phoneLogin.getUid()));
                                SPUtils.putString(PhoneActivity.this,"avatar", phoneLogin.getAvatar());
                                SPUtils.putString(PhoneActivity.this,"identity_card", phoneLogin.getIdentity_card());
                                SPUtils.putString(PhoneActivity.this,"truename", phoneLogin.getTruename());
                                SPUtils.putString(PhoneActivity.this,"username", phoneLogin.getUsername());
                                startActivity(intent);
                              //  ToastUtils.showShort(PhoneActivity.this, phoneLogin.getMsg());
                            }else {
                               // ToastUtils.showShort(PhoneActivity.this, phoneLogin.getMsg());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            });

        }
    }


    //倒计时函数
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            timeButton.setClickable(false);
            timeButton.setText(l / 1000 + "秒");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {

            //重新给Button设置文字
            timeButton.setText("重新获取");
            //设置可点击
            timeButton.setClickable(true);

        }
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public class GlobalValue {
        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        private boolean isCheck;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //沉浸式
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }
}
