package com.touedian.tyd.com.alladapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.touedian.tyd.com.R;
import com.touedian.tyd.com.bean.CardviewBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwr on 2018/1/17.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    public CardviewBean cardviewBean;
    public List<CardviewBean> cardviewBeanList=new ArrayList<CardviewBean>();



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        TextView itcard;
        TextView card_number;

        ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.card_name);

            itcard = v.findViewById(R.id.itcard);
            card_number = v.findViewById(R.id.card_number);
        }
    }

    public void  setDatas(CardviewBean cardviewBean) {
        this. cardviewBean =cardviewBean;

        notifyDataSetChanged();
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

    }

    @Override
    public int getItemCount() {
        if(cardviewBean!=null){
            if(cardviewBean.getData()!=null){

                return cardviewBean.getData().size();


            }
            return 0;
        }
        return 0;
    }
}
