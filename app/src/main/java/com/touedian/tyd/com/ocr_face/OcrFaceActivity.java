package com.touedian.tyd.com.ocr_face;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.touedian.tyd.com.R;
import com.touedian.tyd.com.alladapter.GridViewAdapter;
import com.touedian.tyd.com.bean.GridviewBean;
import com.touedian.tyd.com.model.BrushfaceActivity;
import com.touedian.tyd.com.model.CardViewActivity;
import com.touedian.tyd.com.model.CxchangeActivity;
import com.touedian.tyd.com.model.H5Activity;
import com.touedian.tyd.com.model.IDCardProveActivity;
import com.touedian.tyd.com.model.IDLookActivity;
import com.touedian.tyd.com.model.OcrDetectionActivity;
import com.touedian.tyd.com.model.PayingFaceActivity;
import com.touedian.tyd.com.model.PersonalActivity;
import com.touedian.tyd.com.ocr_text_cr.IDCardActivity;
import com.touedian.tyd.com.utilsx.L;
import com.touedian.tyd.com.utilsx.SPUtils;
import com.touedian.tyd.com.utilsx.ToastUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class OcrFaceActivity extends AppCompatActivity {
    // IeGridOne 是已经认证完成 ,IeGridTwo 是要实名认证
    public int IeGridOne = 1;
    public int IeGridTwo = 0;

    //认证标示
    private int IeGrid;
    private Banner banner;
    private String[] images;
    private GridView gridView;
    private boolean hasGotToken = false;
    private static final int REQUEST_CODE_BANKCARD = 111;//银行卡标识
    private AlertDialog.Builder alertDialog;

    //初始图片
    private String[] imgURLHs = {
            String.valueOf(R.drawable.launch_logo),
            String.valueOf(R.drawable.launch_logo),
            String.valueOf(R.drawable.launch_logo),
            String.valueOf(R.drawable.launch_logo),
            String.valueOf(R.drawable.launch_logo),
            String.valueOf(R.drawable.launch_logo),
            String.valueOf(R.drawable.launch_logo),
            String.valueOf(R.drawable.launch_logo),
            String.valueOf(R.drawable.launch_logo)
    };
    private ArrayList<GridviewBean> mGridData = null;
    private GridViewAdapter mGridViewAdapter1;


    private int uisd;
    private List<Integer> integerList;
    private String avatar;
    private String identity_card;
    private String truename;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(OcrFaceActivity.this);
        setContentView(R.layout.activity_ocr_face);

        Intent intent = getIntent();
        if (intent != null) {

            if (isChild()) {

                IeGrid = intent.getExtras().getInt("identity_status");

            } else {
                IeGrid = SPUtils.getInt(OcrFaceActivity.this, "identity_status", IeGrid);
            }

            //uid = intent.getStringExtra("uid");

            avatar = intent.getStringExtra("avatar");
            Log.d("url",avatar+".............");

            identity_card = intent.getStringExtra("identity_card");
            truename = intent.getStringExtra("truename");
            username = intent.getStringExtra("username");




            uisd = SPUtils.getInt(OcrFaceActivity.this, "uid", uisd);
            L.i("IeGrid", "" + uisd);
            L.i("IeGrid", "" + avatar);
            L.i("IeGrid", "" + identity_card);
            L.i("IeGrid", "" + truename);
            L.i("IeGrid", "" + username);

        }


        //Banner
        //设置图片资源:url或本地资源
        // images = new String[]{String.valueOf(R.drawable.assd), String.valueOf(R.drawable.avatar)};

        integerList = new ArrayList<>();
        integerList.add(R.mipmap.groundo);
        integerList.add(R.mipmap.groundt);


        //设置图片标题:自动对应
        //String[] title = new String[]{"投易点face++"};

        banner = findViewById(R.id.banner);
        gridView = findViewById(R.id.gridview);
        gridView = (GridView) findViewById(R.id.gridview);
        mGridData = new ArrayList<GridviewBean>();
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        for (int i = 0; i < imgURLHs.length; i++) {
            GridviewBean item = new GridviewBean();
            item.setImage(imgURLHs[i]);
            mGridData.add(item);

        }
        mGridViewAdapter1 = new GridViewAdapter(this, R.layout.gridview_ui, mGridData, IeGrid);
        gridView.setAdapter(mGridViewAdapter1);

        if (IeGrid == IeGridTwo) {//要实名认证
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    if (position != 4) {
                        ToastUtils.showShort(OcrFaceActivity.this, "请先认证");
                    }
                    if (position == 4) {

                        Intent intent = new Intent(getApplicationContext(), IDCardActivity.class);
                        intent.putExtra("uid", uisd);
                        startActivity(intent);

                    }


                    mGridViewAdapter1.notifyDataSetChanged();


                }
            });
        } else if (IeGrid == IeGridOne) {//认证完成
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    if (position == 0) {
                        ToastUtils.showShort(OcrFaceActivity.this, "进入成功");
                        Intent intent = new Intent(getApplicationContext(), OcrDetectionActivity.class);
                        startActivity(intent);
                    }

                    if (position == 1) {
                        ToastUtils.showShort(OcrFaceActivity.this, "进入成功");
                        Intent intent = new Intent(getApplicationContext(), IDCardProveActivity.class);
                        startActivity(intent);
                    }
                    if (position == 2) {
                        ToastUtils.showShort(OcrFaceActivity.this, "进入成功");
                        Intent intent = new Intent(getApplicationContext(), BrushfaceActivity.class);//静态人脸
                        startActivity(intent);

                    }

                    if (position == 3) {

                        ToastUtils.showShort(OcrFaceActivity.this, "进入成功");
                        Intent intent = new Intent(getApplicationContext(), CardViewActivity.class);
                        intent.putExtra("uid", uisd);
                        startActivity(intent);


                    }

                    if (position == 4) {
                        ToastUtils.showShort(OcrFaceActivity.this, "进入成功");
                        Intent intent = new Intent(getApplicationContext(), IDLookActivity.class);
                        intent.putExtra("uid", uisd);
                        startActivity(intent);

                    }

                    if (position == 5) {
                        ToastUtils.showShort(OcrFaceActivity.this, "进入成功");
                        Intent intent = new Intent(getApplicationContext(), PayingFaceActivity.class);
                        startActivity(intent);
                    }

                    if (position == 6) {
                        ToastUtils.showShort(OcrFaceActivity.this, "进入成功");
                        Intent intent = new Intent(getApplicationContext(), CxchangeActivity.class);
                        startActivity(intent);
                    }

                    if (position == 7) {
                        ToastUtils.showShort(OcrFaceActivity.this, "进入成功");
                        Intent intent = new Intent(getApplicationContext(), H5Activity.class);
                        startActivity(intent);
                    }

                    if (position == 8) {
                        ToastUtils.showShort(OcrFaceActivity.this, "进入成功");
                        Intent intent = new Intent(getApplicationContext(), PersonalActivity.class);
                        intent.putExtra("avatar", avatar);
                        L.d("url",avatar+"++++++++++++");

                        intent.putExtra("identity_card", identity_card);
                        intent.putExtra("truename", truename);
                        intent.putExtra("username", username);
                        L.d("url",username+"+++++++------");
                        startActivity(intent);
                    }
                    mGridViewAdapter1.notifyDataSetChanged();
                }
            });


        }


        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        //可选样式如下:
        //newhomepageone. Banner.CIRCLE_INDICATOR    显示圆形指示器
        //newhomepagetwo. Banner.NUM_INDICATOR   显示数字指示器
        //3. Banner.NUM_INDICATOR_TITLE 显示数字指示器和标题
        //4. Banner.CIRCLE_INDICATOR_TITLE  显示圆形指示器和标题


        banner.setBannerStyle(Banner.CIRCLE_INDICATOR);

        //设置轮播样式（没有标题默认为右边,有标题时默认左边）
        //可选样式:
        //Banner.LEFT   指示器居左
        //Banner.CENTER 指示器居中
        //Banner.RIGHT  指示器居右
        banner.setIndicatorGravity(Banner.CENTER);

        //设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
        //banner.setBannerTitle(title);

        //设置是否自动轮播（不设置则默认自动）
        banner.isAutoPlay(true);

        //设置轮播图片间隔时间（不设置默认为2000）
        banner.setDelayTime(4000);
        //设置图片资源:可选图片网址/资源文件，默认用Glide加载,也可自定义图片的加载框架
        //所有设置参数方法都放在此方法之前执行
        // banner.setImages(images);

        banner.setImages(integerList, new Banner.OnLoadImageListener() {
            @Override
            public void OnLoadImage(ImageView view, Object url) {

                System.out.println("加载中");
                Glide.with(getApplicationContext()).load(url).into(view);
                System.out.println("加载完");

            }
        });
        //设置点击事件，下标是从1开始
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "你点击了：" + position, Toast.LENGTH_LONG).show();
            }
        });

/*        findViewById(R.id.paly_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OcrFaceActivity.this, Ocr_Text_Activity.class);
                startActivity(intent);
                // 调转到活体识别界面
                Intent faceIntent = new Intent(OcrFaceActivity.this, FaceOnlineVerifyActivity.class);
                startActivity(faceIntent);
            }
        });*/

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

