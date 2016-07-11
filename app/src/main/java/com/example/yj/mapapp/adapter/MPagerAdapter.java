package com.example.yj.mapapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.activity.FristActivity;
import com.example.yj.mapapp.activity.StartActivity;
import com.example.yj.mapapp.base.MApplication;

import java.util.ArrayList;

public class MPagerAdapter extends PagerAdapter {
    private ArrayList<View> list = new ArrayList<>();//集合对象
    private Context context;//上下文对象

    public MPagerAdapter(Context context, ArrayList<View> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = list.get(position);//获取对应位置上的view
        container.addView(view);//将view添加到容器中
        if (position == list.size() - 1) {
            //通过findViewById找到控件
            Button button = (Button) view.findViewById(R.id.id_button);
            //将按钮显示出来
            button.setVisibility(View.VISIBLE);
            //监听按钮点击事件
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转界面
                    Intent intent = new Intent(context, FristActivity.class);
//                    Intent intent = new Intent(context, com.example.yj.mapapp.jpushdemo.MainActivity.class);
                    context.startActivity(intent);
                    StartActivity activity = (StartActivity) context;
                    activity.finish();
//                    context.finish();
                }
            });
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        //移除对应位置上的view
        container.removeView(list.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
//        return false;
        return view == object;
    }
}
