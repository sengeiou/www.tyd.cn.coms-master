package com.touedian.com.facetyd.alladapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.bean.GridviewBean;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<GridviewBean> {


    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GridviewBean> mGridData = new ArrayList<GridviewBean>();
   //认证指定position

    //IeGridOne 蓝色，IeGridTwo 灰色
    private int  IeGridOne =1 ;
    private int  IeGridTwo =0;
    private int  ieGrid ;


    public GridViewAdapter(Context context, int resource, ArrayList<GridviewBean> objects, int ieGrid) {
        super(context, resource, objects);
        this.mContext = context;
        this.layoutResourceId = resource;
        this.mGridData = objects;
        this.ieGrid=ieGrid;
    }



    public void setGridData(ArrayList<GridviewBean> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;



        if (convertView == null) {

            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();

            convertView = inflater.inflate(R.layout.gridview_layourt, parent, false);
            holder = new ViewHolder();
            //holder.textView = (TextView) convertView.findViewById(R.id.title);

            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GridviewBean item = mGridData.get(position);

        Glide.with(mContext).load(item.getImage()).into(holder.imageView);

        if (ieGrid== IeGridOne) {//图标全部蓝色
            holder.imageView.setBackgroundResource(R.drawable.launch_logo);
            if(0 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.vivodetection);
                notifyDataSetChanged();
            }
            if(1 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.facecontrast);
                notifyDataSetChanged();
            }
            if(2 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.deblocking);
                notifyDataSetChanged();
            }
            if(3 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.bankcard);
                notifyDataSetChanged();
            }
            if(4 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.downattestation);
                notifyDataSetChanged();
            }
            if(5 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.pay);
                notifyDataSetChanged();
            }
            if(6 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.exchangenumber);
                notifyDataSetChanged();
            }
            if(7 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.clause);
                notifyDataSetChanged();
            }
            if(8 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.installs);
                notifyDataSetChanged();
            }
            if(9 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.drivingimg);
                notifyDataSetChanged();
            }
            if(10 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap. linecardimg);
                notifyDataSetChanged();
            }
            if(11 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.cardimg);
                notifyDataSetChanged();
            }
            if(12 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.businessimg);
                notifyDataSetChanged();
            }
            if(13 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.billimg);
                notifyDataSetChanged();
            }
            if(14 == position  ){

                holder.imageView.setBackgroundResource(R.mipmap.commonlanguageimg);
                notifyDataSetChanged();
            }



            //灰色



          } else if(ieGrid== IeGridTwo){
            if(0 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downdetection);
                notifyDataSetChanged();
            }
            if(1 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downfacecontrast);
                notifyDataSetChanged();
            }
            if(2 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downdeblocking);
                notifyDataSetChanged();
            }
            if(3 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downbankcard);
                notifyDataSetChanged();
            }
            if(4 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.attestation);
                notifyDataSetChanged();
            }
            if(5 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downpay);
                notifyDataSetChanged();
            }
            if(6 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downexchangenumber);
                notifyDataSetChanged();
            }
            if(7 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downclause);
                notifyDataSetChanged();
            }
            if(8 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.installsbtn);
                notifyDataSetChanged();
            }
            if(9 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downdrivingimg);
                notifyDataSetChanged();
            }
            if(10 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downlinecardimg);
                notifyDataSetChanged();
            }
            if(11 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downcardimg);
                notifyDataSetChanged();
            }
            if(12 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downbusinessimg);
                notifyDataSetChanged();
            }
            if(13 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downbillimg);
                notifyDataSetChanged();
            }
            if(14 == position ){
                holder.imageView.setBackgroundResource(R.mipmap.downcommonlanguage);
                notifyDataSetChanged();
            }

          }



        return convertView;
    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}

