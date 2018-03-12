package com.touedian.com.facetyd.alladapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public List<CardviewBean> cardviewBeanList=new ArrayList<CardviewBean>();

    private Context context;
    private LinearLayout cardLinearLayout;


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public  TextView itcard;
        public TextView card_number;

        public LinearLayout linearLayout;
        ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.card_name);
            itcard = v.findViewById(R.id.itcard);
            card_number = v.findViewById(R.id.card_number);
            linearLayout=v.findViewById(R.id.CardLinearLayout);


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

       /*

        */

       if(holder.mTextView.getText().equals("建设银行")){

           holder.linearLayout.setBackgroundColor(Color.parseColor("#16347c"));
       }
       if(holder.mTextView.getText().equals("招商银行")){
           holder.linearLayout.setBackgroundColor(Color.parseColor("#991c37"));
       }

    }

    @Override
    public int getItemCount() {
        if(cardviewBean!=null){
            if(cardviewBean.getData()!=null){

                return cardviewBean.getData().size();


            }
            notifyDataSetChanged();
            return 0;
        }
        return 0;

    }
}
