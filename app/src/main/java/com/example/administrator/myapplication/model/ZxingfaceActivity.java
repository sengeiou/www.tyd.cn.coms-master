package com.example.administrator.myapplication.model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.ocr_discern.FaceLivenessExpActivity;
import com.example.administrator.myapplication.utilsx.L;
import com.example.administrator.myapplication.utilsx.SPUtils;

public class ZxingfaceActivity extends AppCompatActivity {
    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;
    private long exitTime;
    private ImageView mScanHorizontalLineImageView;

    private ImageView mPreviewView;

    private int brushface;
    private int booe = 1;
    private String uid;
    private String Avatar;
    private String Identity_card;
    private String Truename;
    private String Username;
    private String avatar;
    private String identity_card;
    private String truename;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxingface);
        DoPost();

        //登录


        mScanHorizontalLineImageView = (ImageView) findViewById(R.id.scanHorizontalLineImageView);

        mPreviewView = findViewById(R.id.previewView);
        mPreviewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(ZxingfaceActivity.this, FaceLivenessExpActivity.class);

                intent.putExtra("brushface", brushface);
                intent.putExtra("booe", booe);

                intent.putExtra("avatar",avatar);
                intent.putExtra("identity_card",identity_card);
                intent.putExtra("truename",truename);
                intent.putExtra("username",username);

                L.i("booe",avatar.toString());
                L.i("booe",identity_card.toString());
                L.i("booe",truename.toString());
                L.i("booe",username.toString());
                startActivity(intent);

            }
        });
        fullScreen(ZxingfaceActivity.this);
    }

    private void DoPost() {
        avatar = SPUtils.getString(ZxingfaceActivity.this, "avatar", Avatar);
        L.i("username",avatar.toString());
        identity_card = SPUtils.getString(ZxingfaceActivity.this, "identity_card", Identity_card);
        truename = SPUtils.getString(ZxingfaceActivity.this, "truename", Truename);
        username = SPUtils.getString(ZxingfaceActivity.this, "username", Username);
        L.i("username",username.toString());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int[] location = new int[2];

        // getLocationInWindow方法要在onWindowFocusChanged方法里面调用
        // 个人理解是onCreate时，View尚未被绘制，因此无法获得具体的坐标点
        mPreviewView.getLocationInWindow(location);

        // 模拟的mPreviewView的左右上下坐标坐标
        int left = mPreviewView.getLeft();
        int right = mPreviewView.getRight();
        int top = mPreviewView.getTop();
        int bottom = mPreviewView.getBottom();

        // 从上到下的平移动画
        Animation verticalAnimation = new TranslateAnimation(left, left, top, bottom);
        verticalAnimation.setDuration(3000); // 动画持续时间
        verticalAnimation.setRepeatCount(Animation.INFINITE); // 无限循环

        // 播放动画
        mScanHorizontalLineImageView.setAnimation(verticalAnimation);
        verticalAnimation.startNow();

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                brushface = data.getExtras().getInt("brushface");
                L.i("8888888888888882", String.valueOf(brushface).toString());
                // SharedPreferencesUtils.setParam(BrushfaceActivity.this,"brushface",brushface);

                SPUtils.putInt(ZxingfaceActivity.this, "brushface", brushface);
            }
        }

    }

    /**
     * 重写返回键，实现双击退出效果
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(ZxingfaceActivity.this, "再按一次返回开始页面", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ZxingfaceActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
