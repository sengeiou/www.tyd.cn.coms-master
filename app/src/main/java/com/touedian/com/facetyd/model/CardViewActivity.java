package com.touedian.com.facetyd.model;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.touedian.com.facetyd.Config;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.alladapter.CardViewAdapter;
import com.touedian.com.facetyd.bean.CardviewBean;
import com.touedian.com.facetyd.utilsx.HttpUtils;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.SPUtils;


import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *
 * Author:hwr
 * Date:2018/newhomepageone/22
 * 佛祖保佑       永无BUG     永不修改
 *
 * 银行卡 层叠 页面
 */

public class CardViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> datas;
    private HashMap<String,String> BankcardGet=new HashMap<>();
    private  int uid;
    private  int usid;//拿到本地保存的uid

    private JSONObject jsonObject;
    private CardviewBean cardviewBean;
    private CardViewAdapter adapter;
    private List<CardviewBean> cardviewBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(CardViewActivity.this);
        setContentView(R.layout.activity_card_view);

        usid = SPUtils.getInt(CardViewActivity.this,"uid",uid);

        ImageView cardview_back=findViewById(R.id.cardview_back);
        cardview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageView BankcardAdd=findViewById(R.id.BankcardAdd);
        BankcardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CardViewActivity.this,BankCardActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.recycler_view);


        //RecyclerView.HORIZONTAL,一个竖着滑一个横着滑
        int orientation = RecyclerView.VERTICAL;
        //列表显示,最后一个参数如果为true,列表将倒叙显示
        mLayoutManager = new LinearLayoutManager(this, orientation, false);
        //网格显示
        // mLayoutManager = new GridLayoutManager(this, mSpanCount, orientation, false);
        //分散对齐网格(瀑布流),如果每个item的宽高都固定一致,则和网格显示无差别
        //mLayoutManager = new StaggeredGridLayoutManager(mSpanCount, orientation);
        //为 RecyclerView 指定布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new CardViewAdapter();
        PostCardMessage();

    }

    private void PostCardMessage() {

        BankcardGet.put("uid", String.valueOf(usid));

        HttpUtils.doPost(Config.TYD_BankcardMessage_get, BankcardGet, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.code()==200){


                    String s = response.body().string();
                    L.i("sssssssssssss",s.toString());
                    try {
                        jsonObject = new JSONObject(s);

                        cardviewBean = JsonUtil.parseJsonToBean(s, CardviewBean.class);

                        adapter.setDatas(cardviewBean);
                        adapter.notifyDataSetChanged();

                        //notify();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        mRecyclerView.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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
