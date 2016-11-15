package com.example.yj.mapapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.view.Image;
import com.example.yj.mapapp.view.MButton;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddModuleAdapter extends BaseAdapter {
    private ArrayList<Image> data;
    private Context context;

    public AddModuleAdapter(ArrayList<Image> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
//        return data == null ? 0 : (data.size() > 8 ? 8 : data.size());
        return 3;
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
                    R.layout.add_module_item, null, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

//        holder.button.setBackgroundResource(data.get(i).getPicture());
        holder.button.setBackgroundResource(R.mipmap.icon_home_bf);
        return convertView;
    }

    static class Holder {
        @Bind(R.id.button)
        MButton button;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}
