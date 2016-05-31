package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.yj.mapapp.adapter.MPagerAdapter;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.R;
import com.example.yj.mapapp.jpushdemo.*;
import com.example.yj.mapapp.util.ToastUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StartActivity extends BaseActivity {

    private ArrayList<View> list = new ArrayList<>();

    @Bind(R.id.id_imageView1)
    ImageView imageView1;

    @Bind(R.id.id_imageView2)
    ImageView imageView2;

    @Bind(R.id.id_imageView3)
    ImageView imageView3;

    @Bind(R.id.id_viewPager)
    ViewPager viewPager;

    @Override
    public int bindLayout() {
        return R.layout.activity_start;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this);
//        全屏：既没有标题栏也没有通知栏
//        andriod:theme:"@android:style/Theme.Black.NoTileBar.FullScreen"
        //去标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences preferences = getSharedPreferences("", Context.MODE_PRIVATE);
        boolean flag = preferences.getBoolean("login", false);
        if (!flag) {
            //处理滑动界面的显示
            Editor editor = preferences.edit();
            editor.putBoolean("login", true);
        } else {
            //直接跳转到主界面
            Intent intent = new Intent(StartActivity.this, FristActivity.class);
            startActivity(intent);
            finish();
        }
        LayoutInflater inflater = LayoutInflater.from(this);

        View view1 = inflater.inflate(R.layout.pager_item, null);
        LinearLayout layout1 = (LinearLayout) view1.findViewById(R.id.id_linearLayout);
        layout1.setBackgroundResource(R.mipmap.shop_commodity_1);
        list.add(view1);

        View view2 = inflater.inflate(R.layout.pager_item, null);
        LinearLayout layout2 = (LinearLayout) view2.findViewById(R.id.id_linearLayout);
        layout2.setBackgroundResource(R.mipmap.shop_commodity_2);
        list.add(view2);

        View view3 = inflater.inflate(R.layout.pager_item, null);
        LinearLayout layout3 = (LinearLayout) view3.findViewById(R.id.id_linearLayout);
        layout3.setBackgroundResource(R.mipmap.shop_commodity_3);
        list.add(view3);

        viewPager.setAdapter(new MPagerAdapter(this, list));

        imageView1.setBackgroundResource(R.mipmap.icon_point_pre);
        imageView2.setBackgroundResource(R.mipmap.icon_point);
        imageView3.setBackgroundResource(R.mipmap.icon_point);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //在此方法中处理圆点的显示问题
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    imageView1.setBackgroundResource(R.mipmap.icon_point_pre);
                    imageView2.setBackgroundResource(R.mipmap.icon_point);
                    imageView3.setBackgroundResource(R.mipmap.icon_point);
                } else if (position == 1) {
                    imageView2.setBackgroundResource(R.mipmap.icon_point_pre);
                    imageView1.setBackgroundResource(R.mipmap.icon_point);
                    imageView3.setBackgroundResource(R.mipmap.icon_point);
                } else if (position == 2) {
                    imageView3.setBackgroundResource(R.mipmap.icon_point_pre);
                    imageView1.setBackgroundResource(R.mipmap.icon_point);
                    imageView2.setBackgroundResource(R.mipmap.icon_point);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void resume() {
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onBackPressed() {

    }
}
