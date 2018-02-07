package com.touedian.com.facetyd.qohoosdk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.touedian.com.facetyd.R;
import com.qihoo.appstore.common.updatesdk.lib.UpdateHelper;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity {
    private EditText mEditText;
    private Button mButton;

    private List<TestItem> mTestList = new ArrayList<>();
    private ListView mListView;
    private ListItemAdapter mListItemAdapter;
    private String mTestPackageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        mEditText = (EditText) findViewById(R.id.input_package_edit);
        mTestPackageName="com.example.administrator.myapplication";
        mEditText.setText(mTestPackageName);
        mButton = (Button) findViewById(R.id.input_package_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdateSilent();
                Toast.makeText(TestActivity.this, "请修改Demo源码重新编译：1.修改此处包名 2.修改AndroidManifest中的authorities包名", Toast.LENGTH_SHORT).show();
            }
        });
        initList();
    }

    private void initList() {
        mTestList.add(new TestItem(0, "手动查询升级"));
        mTestList.add(new TestItem(1, "后台非强制升级"));
        mTestList.add(new TestItem(2, "后台强制升级"));
        mListItemAdapter = new ListItemAdapter(TestActivity.this, R.layout.list_item_func, mTestList);

        mListView = (ListView) findViewById(R.id.func_list);
        mListView.setAdapter(mListItemAdapter);
    }

    private void onClickItem(int position) {
        switch (position) {
            case 0 :
                checkUpdateManual();
                break;
            case 1 :
                checkUpdateSilent();
                break;
            case 2 :
                checkForceUpdateSilent();
                break;
            default:
                break;
        }
    }

    private void checkUpdateManual() {
        String packageName = mEditText.getText().toString();
        if (!TextUtils.isEmpty(packageName)) {
            UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#0A93DB"));
            UpdateHelper.getInstance().setDebugMode(false);
            UpdateHelper.getInstance().manualUpdate(packageName);
        }
    }

    private void checkUpdateSilent() {
        String packageName = mEditText.getText().toString();
        if (!TextUtils.isEmpty(packageName)) {
            UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#0A93DB"));
            UpdateHelper.getInstance().setDebugMode(true);
            long intervalMillis = 10 * 1000L;           //第一次调用startUpdateSilent出现弹窗后，如果10秒内进行第二次调用不会查询更新
            UpdateHelper.getInstance().autoUpdate(packageName, false, intervalMillis);
        }
    }

    private void checkForceUpdateSilent() {
        String packageName = mEditText.getText().toString();
        if (!TextUtils.isEmpty(packageName)) {
            UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#0A93DB"));
            UpdateHelper.getInstance().setDebugMode(true);
            long intervalMillis = 0 * 1000L;           //第一次调用startUpdateSilent出现弹窗后，如果10秒内进行第二次调用不会查询更新
            UpdateHelper.getInstance().autoUpdate(packageName, true, intervalMillis);
        }
    }

    private class ListItemAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private int mItemResourceId;
        private List<TestItem> mTestItemList;

        public ListItemAdapter(Context context, int itemResourceId, List<TestItem> testItemList) {
            mInflater = LayoutInflater.from(context);
            mItemResourceId = itemResourceId;
            mTestItemList = testItemList;
        }

        @Override
        public int getCount() {
            return mTestItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return  mTestItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewCache viewCache;
            if (convertView == null) {
                convertView = mInflater.inflate(mItemResourceId, null);
                viewCache = new ViewCache();
                viewCache.titleText = (TextView) convertView.findViewById(R.id.func_desc_title);
                viewCache.descText = (TextView) convertView.findViewById(R.id.func_desc_text);
                viewCache.okBtn = (Button) convertView.findViewById(R.id.func_pop_dialog_button);
                convertView.setTag(viewCache);
            } else {
                viewCache = (ViewCache) convertView.getTag();
            }

            TestItem item = (TestItem) getItem(position);
            viewCache.titleText.setText("状态" + (item.mIndex + 1));
            viewCache.descText.setText(item.mDesc);

            viewCache.okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem(position);
                }
            });

            return convertView;
        }
    }

    private static class ViewCache {
        public TextView titleText;
        public TextView descText;
        public Button okBtn;
    }

    private class TestItem {
        public TestItem(int index, String desc) {
            mIndex = index;
            mDesc = desc;
        }

        public int mIndex;
        public String mDesc;
    }
}
