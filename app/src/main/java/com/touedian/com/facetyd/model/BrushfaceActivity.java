package com.touedian.com.facetyd.model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.SPUtils;

public class BrushfaceActivity extends AppCompatActivity {


    private boolean SwitchBool;//Switch开关状态
    private Switch switchBtn;
    private int brushface;
    private int brushfaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Color1SwitchStyle);
        fullScreen(BrushfaceActivity.this);
        setContentView(R.layout.activity_brushface);
        brushfaces = SPUtils.getInt(BrushfaceActivity.this, "brushface", brushface);
        ImageView imageView2 = findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        switchBtn = findViewById(R.id.switch_btn);
        if (brushfaces == 1) {
            switchBtn.setChecked(true);
        } else if (brushfaces == 0) {
            switchBtn.setChecked(false);
        }





        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked && BrushfaceActivity.this.brushface != 1) {
                    Intent intent = new Intent(BrushfaceActivity.this, ZxingfaceActivity.class);
                    intent.putExtra("brushface", BrushfaceActivity.this.brushface);
                    startActivityForResult(intent, BrushfaceActivity.this.brushface);
                } else if (!isChecked) {
                    switchBtn.setChecked(false);
                    SPUtils.putInt(BrushfaceActivity.this,"brushface",brushface);
                }
                L.i("888888888888881", String.valueOf(BrushfaceActivity.this.brushface).toString());


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                brushface = data.getExtras().getInt("brushface");
                L.i("8888888888888882", String.valueOf(brushface).toString());
                // SharedPreferencesUtils.setParam(BrushfaceActivity.this,"brushface",brushface);

                SPUtils.putInt(BrushfaceActivity.this, "brushface", brushface);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if(brushfaces==1){
            switchBtn.setChecked(true);
            L.i("22222222222222222",String.valueOf(BrushfaceActivity.this.brushface).toString());
        }*/

        if (SwitchBool == true) {
            brushface = 1;
        } else {
            brushface = 0;
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
