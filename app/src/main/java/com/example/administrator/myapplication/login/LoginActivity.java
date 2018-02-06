package com.example.administrator.myapplication.login;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.model.PhoneActivity;
import com.example.administrator.myapplication.model.ZxingfaceActivity;

import com.example.administrator.myapplication.utilsx.L;
import com.example.administrator.myapplication.utilsx.SPUtils;
import com.example.administrator.myapplication.utilsx.ToastUtils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by gcj on 2017/11/28
 */
public class LoginActivity extends AppCompatActivity {
    private long exitTime;
    private TextView tvRegister;
    private TextView tvLogin;
    private ImageView ivLogo;
    private int brushface ;
    private boolean hasGotToken = false;
    private int IeGrid = 1;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        brushface = SPUtils.getInt(LoginActivity.this,"brushface",brushface);
        L.i("brushface", String.valueOf(brushface));
        initViews();
        initAnims();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(brushface==1){
                    //刷脸登录页面
                    initAccessTokenWithAkSk();
                    Intent intents=new Intent(LoginActivity.this,ZxingfaceActivity.class);
                    intents.putExtra("identity_status",IeGrid);

                    L.i("WebIeGrid", String.valueOf(IeGrid));
                    startActivity(intents);


                }else {
                    //手机登录页面
                    gotoLogin();

                }




            }
        }, 1800);
    }

    private void gotoLogin() {
        Intent intent=new Intent(LoginActivity.this, PhoneActivity.class);
        startActivity(intent);
    }

    /**
     * 初始化View控件
     */
    private void initViews() {
        tvLogin=findViewById(R.id.tv_login);
        findViewById(R.id.tv_login);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        findViewById(R.id.tv_register);
        ivLogo = (ImageView) findViewById(R.id.iv_logo);

        //https://juejin.im/entry/59924afcf265da3e1c0f9d96         一键删除EditText

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
                ToastUtils.showLong(LoginActivity.this,"AK，SK方式获取token失败");
            }
        }, getApplicationContext(), "9evxCWG1MTN8k7u3XU0qVIqi", "5eesgiqRtSflHYOM5OUZLSsSeMPCC81n");
    }





    /**
     * 初始化logo图片以及底部注册、登录的按钮动画
     */
    private void initAnims() {
        //初始化底部注册、登录的按钮动画
        //以控件自身所在的位置为原点，从下方距离原点200像素的位置移动到原点
        ObjectAnimator tranLogin = ObjectAnimator.ofFloat(tvLogin, "translationY", 200, 0);
        ObjectAnimator tranRegister = ObjectAnimator.ofFloat(tvRegister, "translationY", 200, 0);
        //将注册、登录的控件alpha属性从0变到1
        ObjectAnimator alphaLogin = ObjectAnimator.ofFloat(tvLogin, "alpha", 0, 1);
        ObjectAnimator alphaRegister = ObjectAnimator.ofFloat(tvRegister, "alpha", 0, 1);
        final AnimatorSet bottomAnim = new AnimatorSet();
        bottomAnim.setDuration(1000);
        //同时执行控件平移和alpha渐变动画
        bottomAnim.play(tranLogin).with(tranRegister).with(alphaLogin).with(alphaRegister);

        //获取屏幕高度
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;

        //通过测量，获取ivLogo的高度
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ivLogo.measure(w, h);
        int logoHeight = ivLogo.getMeasuredHeight();

        //初始化ivLogo的移动和缩放动画
        float transY = (screenHeight - logoHeight) * 0.28f;
        //ivLogo向上移动 transY 的距离
        ObjectAnimator tranLogo = ObjectAnimator.ofFloat(ivLogo, "translationY", 0, -transY);
        //ivLogo在X轴和Y轴上都缩放0.75倍
        ObjectAnimator scaleXLogo = ObjectAnimator.ofFloat(ivLogo, "scaleX", 1f, 0.75f);
        ObjectAnimator scaleYLogo = ObjectAnimator.ofFloat(ivLogo, "scaleY", 1f, 0.75f);
        AnimatorSet logoAnim = new AnimatorSet();
        logoAnim.setDuration(1000);
        logoAnim.play(tranLogo).with(scaleXLogo).with(scaleYLogo);
        logoAnim.start();
        logoAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //待ivLogo的动画结束后,开始播放底部注册、登录按钮的动画
                bottomAnim.start();
            }
        });
    }


    /**
     * 重写返回键，实现双击退出效果
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(LoginActivity.this, "再按一次返回开始页面", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                LoginActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static String md5(String plainText) {

        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }


    public static String encodeUrlString(String str, String charset) {
        String strret = null;
        if (str == null)
            return str;
        try {
            strret = java.net.URLEncoder.encode(str, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strret;
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

