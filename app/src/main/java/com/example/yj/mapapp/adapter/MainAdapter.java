package com.example.yj.mapapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseAdapter;
import com.example.yj.mapapp.util.LogUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class MainAdapter extends BaseAdapter {
    private ArrayList<View> data;
    private Context context;

    public MainAdapter(ArrayList<View> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        LogUtil.d("getCount==" + data.size());
        return data == null ? 0 : (data.size()>12? 12 :data.size());

    }

    @Override
    public Object getItem(int i) {

        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.exit_dialog, null, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
//        holder.rect.setText(data.get(i).getContent());
//        holder.rect.setBackgroundColor(context.getResources().getColor(data.get(i).getColor()));
        return convertView;
    }

    static class Holder {
//        @Bind(R.id.rect)
//        RectLabel rect;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
