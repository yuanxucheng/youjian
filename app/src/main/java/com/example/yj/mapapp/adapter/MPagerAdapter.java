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
    private ArrayList<View> list = new ArrayList<>();
    private Context context;

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
        View view = list.get(position);
        container.addView(view);
        if (position == list.size() - 1) {
            Button button = (Button) view.findViewById(R.id.id_button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
        container.removeView(list.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
//        return false;
        return view == object;
    }
}
