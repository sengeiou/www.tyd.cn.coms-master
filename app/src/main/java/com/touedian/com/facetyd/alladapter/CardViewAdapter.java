package com.touedian.com.facetyd.alladapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.bean.CardviewBean;

import java.util.ArrayList;
import java.util.List;

import static com.touedian.com.facetyd.R.color.corner_color;

/**
 * Created by hwr on 2018/1/17.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    public CardviewBean cardviewBean;
    public List<CardviewBean> cardviewBeanList = new ArrayList<CardviewBean>();

    private Context context;
    private LinearLayout cardLinearLayout;


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView itcard;
        public TextView card_number;

        public LinearLayout linearLayout;
        public ImageView bank_img;
        public ImageView banklog_img;

        ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.card_name);
            itcard = v.findViewById(R.id.itcard);
            card_number = v.findViewById(R.id.card_number);
            linearLayout = v.findViewById(R.id.CardLinearLayout);

            bank_img = v.findViewById(R.id.bank_img);

            banklog_img = v.findViewById(R.id.banklog_img);
        }
    }

    public void setDatas(CardviewBean cardviewBean) {
        this.cardviewBean = cardviewBean;


    }

    // 创建视图
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recyclerview_item_view, parent, false);


        return new ViewHolder(v);

    }

    // 为Item绑定数据
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder" + "(" + position + ")");

        holder.mTextView.setText(cardviewBean.getData().get(position).getMcard_name());
        holder.itcard.setText(cardviewBean.getData().get(position).getMcard_type());
        holder.card_number.setText(cardviewBean.getData().get(position).getMcard_id());

       /*

        */

        if (holder.mTextView.getText().equals("建设银行") && holder.mTextView.getText() != null) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#16347c"));
            holder.bank_img.setBackgroundResource(R.mipmap.jsbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.jsbanklog);
        }
        if (holder.mTextView.getText().equals("北京银行") && holder.mTextView.getText() != null) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#dd5662"));
            holder.bank_img.setBackgroundResource(R.mipmap.bjbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.bjbanklog);

        }
        if (holder.mTextView.getText().equals("渤海银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#4769ad"));
            holder.bank_img.setBackgroundResource(R.mipmap.bhbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.bohaibanklog);
        }
        if (holder.mTextView.getText().equals("广发")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#b8303e"));
            holder.bank_img.setBackgroundResource(R.mipmap.gfbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.gfbanklog);
        }
        if (holder.mTextView.getText().equals("国家开发银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#bd8b4d"));
            holder.bank_img.setBackgroundResource(R.mipmap.gjkfbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.gjkflog);
        }
        if (holder.mTextView.getText().equals("恒丰银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#b78b22"));
            holder.bank_img.setBackgroundResource(R.mipmap.hfbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.hxbanklog);
        }
        if (holder.mTextView.getText().equals("华夏银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ed4a56"));
            holder.bank_img.setBackgroundResource(R.mipmap.hxbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.hxbanklog);
        }
        if (holder.mTextView.getText().equals("华夏银行总行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ed4a56"));
            holder.bank_img.setBackgroundResource(R.mipmap.hxbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.hxbanklog);
        }
        if (holder.mTextView.getText().equals("江苏银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#3a76d5"));
            holder.bank_img.setBackgroundResource(R.mipmap.jsbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.jsbanklog);
        }
        if (holder.mTextView.getText().equals("江苏银行贷记卡")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#3a76d5"));
            holder.bank_img.setBackgroundResource(R.mipmap.jsbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.jsbanklog);
        }
        if (holder.mTextView.getText().equals("交通银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#27487b"));
            holder.bank_img.setBackgroundResource(R.mipmap.jtbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.jtbanklog);
        }
        if (holder.mTextView.getText().equals("宁波商行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#e2a13e"));
            holder.bank_img.setBackgroundResource(R.mipmap.ningbobankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.ningbobanklog);
        }
        if (holder.mTextView.getText().equals("宁波银行（贷）")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#e2a13e"));
            holder.bank_img.setBackgroundResource(R.mipmap.ningbobankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.ningbobanklog);
        }
        if (holder.mTextView.getText().equals("平安银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ee613f"));
            holder.bank_img.setBackgroundResource(R.mipmap.pabankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.pabanklog);
        }
        if (holder.mTextView.getText().equals("浦东发展")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#332448"));
            holder.bank_img.setBackgroundResource(R.mipmap.pdbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.pfbanklog);
        }
        if (holder.mTextView.getText().equals("兴业银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#004087"));
            holder.bank_img.setBackgroundResource(R.mipmap.xybankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.xybanklog);
        }
        if (holder.mTextView.getText().equals("招商银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#991c37"));
            holder.bank_img.setBackgroundResource(R.mipmap.zsbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.zsbanklog);
        }
        if (holder.mTextView.getText().equals("浙商总行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#c62a3c"));
            holder.bank_img.setBackgroundResource(R.mipmap.zheshangbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.zheshangbanklog);
        }
        if (holder.mTextView.getText().equals("工商银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#da4c49"));
            holder.bank_img.setBackgroundResource(R.mipmap.icbcbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.icbcbanklog);
        }
        if (holder.mTextView.getText().equals("光大银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#5f3c76"));
            holder.bank_img.setBackgroundResource(R.mipmap.gdbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.gdbanklog);
        }
        if (holder.mTextView.getText().equals("光大信用卡中心")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#5f3c76"));
            holder.bank_img.setBackgroundResource(R.mipmap.gdbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.gdbanklog);
        }
        if (holder.mTextView.getText().equals("中国进出口银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#384a96"));
            holder.bank_img.setBackgroundResource(R.mipmap.gdbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.zgjcklog);
        }
        if (holder.mTextView.getText().equals("民生银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#27a1a6"));
            holder.bank_img.setBackgroundResource(R.mipmap.msbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.msbanklog);
        }
        if (holder.mTextView.getText().equals("农业发展银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#a38c52"));
            holder.bank_img.setBackgroundResource(R.mipmap.nyfzbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.nyfzbanklog);
        }
        if (holder.mTextView.getText().equals("农业银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#03917b"));
            holder.bank_img.setBackgroundResource(R.mipmap.nybankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.nybanklog);
        }
        if (holder.mTextView.getText().equals("中国银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#e24b4b"));
            holder.bank_img.setBackgroundResource(R.mipmap.chinabankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.chinabanklog);
        }
        if (holder.mTextView.getText().equals("邮储银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#0a984a"));
            holder.bank_img.setBackgroundResource(R.mipmap.yzbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.yzbanklog);
        }
        if (holder.mTextView.getText().equals("邮政储汇")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#0a984a"));
            holder.bank_img.setBackgroundResource(R.mipmap.yzbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.yzbanklog);
        }
        if (holder.mTextView.getText().equals("中信银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#bf1f2b"));
            holder.bank_img.setBackgroundResource(R.mipmap.zxbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.zxbanklog);
        }
        if (holder.mTextView.getText().equals("南京银行")) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#dc2433"));
            holder.bank_img.setBackgroundResource(R.mipmap.njbankicon);
            holder.banklog_img.setBackgroundResource(R.mipmap.njbanklog);
        }

//        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (cardviewBean != null) {
            if (cardviewBean.getData() != null) {

                return cardviewBean.getData().size();


            }
            notifyDataSetChanged();
            return 0;
        }
        return 0;

    }


}
