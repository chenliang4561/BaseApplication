package com.cl.baseapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cl.baseapplication.R;

import java.util.ArrayList;

/**
 * XLixtView
 *
 * @author admin
 * @date 2017-12-20
 */

public class XLixtViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> datas;

    public XLixtViewAdapter(Context context){
        this.context = context;
    }

    public void setDatas(ArrayList<String> datas){
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder myViewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
            myViewHolder = new MyViewHolder(view);
            view.setTag(myViewHolder);
        }else {
            myViewHolder = (MyViewHolder) view.getTag();
        }
        //设置数据
        myViewHolder.textView.setText(datas.get(i));

        return view;
    }

    class MyViewHolder {

        private TextView textView;

        MyViewHolder(View itemView) {
            textView = (TextView) itemView.findViewById(R.id.list_item_textview);
        }
    }
}
