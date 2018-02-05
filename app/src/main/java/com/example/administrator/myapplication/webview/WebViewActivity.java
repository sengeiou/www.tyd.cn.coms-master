package com.example.administrator.myapplication.webview;

import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;


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

public class WebViewActivity extends AppCompatActivity {
    private WebView webViewGame;
    private CheckBox checkBox;
    private int brushface ;
    private boolean hasGotToken = false;
    private int IeGrid = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);

        brushface = SPUtils.getInt(WebViewActivity.this,"brushface",brushface);

        L.i("sse", String.valueOf(brushface).toString());


        ImageView WebViwe_back=findViewById(R.id.WebViwe_back);
        WebViwe_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


       // webViewGame.setHorizontalScrollBarEnabled(false);
      //  webViewGame.setVerticalScrollBarEnabled(false);
    }

    //webView加载H5的链接
    private void loadHtml() {

        webViewGame=findViewById(R.id.webViewH5);
        webViewGame.loadUrl("http://face.touedian.com/tk.html");
        //声明WebSettings子类
        WebSettings webSettings = webViewGame.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小



//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式


        webViewGame.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }

        });



      /*  if (webViewGame.getContentHeight() * webViewGame.getScale() == (webViewGame.getHeight() + webViewGame.getScrollY())) {
            ToastUtils.showLong(WebViewActivity.this,"请认真阅读完毕");
            //说明已经到底了
            checkBox = findViewById(R.id.checkboxs);
            //已经处于底端
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){

                        //Intent intent=new Intent(WebViewActivity.this,LoginActivity.class);
                        Intent intent=new Intent(WebViewActivity.this,PhoneActivity.class);
                        startActivity(intent);
                    }else{
                        ToastUtils.showShort(WebViewActivity.this,"未选择");

                    }
                }
            });
        }*/


    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webViewGame.canGoBack()) {
            webViewGame.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // presenter.unregisterEventBus();
        if (null != webViewGame){
            webViewGame.destroy();
        }
    }

    @Override
    protected void onResume() {
        if(brushface==1){
            initAccessTokenWithAkSk();
            Intent intents=new Intent(WebViewActivity.this,ZxingfaceActivity.class);
            intents.putExtra("identity_status",IeGrid);

            L.i("WebIeGrid", String.valueOf(IeGrid));
            startActivity(intents);


        }else {
            //加载H5
            loadHtml();

        }

        super.onResume();


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
                ToastUtils.showLong(WebViewActivity.this,"AK，SK方式获取token失败");
            }
        }, getApplicationContext(), "9evxCWG1MTN8k7u3XU0qVIqi", "5eesgiqRtSflHYOM5OUZLSsSeMPCC81n");
    }


}
