package com.touedian.com.facetyd.model;

import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.utilsx.ToastUtils;

public class H5Activity extends AppCompatActivity {
    private WebView webViewGame;
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);

        loadHtml();
    }

    //webView加载H5的链接
    private void loadHtml() {

        ImageView H5_back=findViewById(R.id.H5_back);
        H5_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        webViewGame=findViewById(R.id.H5webViewH5);
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



        if (webViewGame.getContentHeight() * webViewGame.getScale() == (webViewGame.getHeight() + webViewGame.getScrollY())) {
            ToastUtils.showLong(H5Activity.this,"请认真阅读完毕");
            //说明已经到底了
           /* checkBox = findViewById(R.id.H5checkboxs);
            //已经处于底端
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){

                      finish();
                    }
                }
            });*/
        }

    }
}
