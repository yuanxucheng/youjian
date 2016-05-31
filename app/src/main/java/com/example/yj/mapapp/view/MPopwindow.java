package com.example.yj.mapapp.view;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.activity.MipcaActivityCapture;
import com.example.yj.mapapp.activity.MyResponseActivity;
import com.example.yj.mapapp.activity.MyTradeLeadsActivity;
import com.example.yj.mapapp.activity.ReleaseInformationActivity;
import com.example.yj.mapapp.activity.TradeLeadsActivity;
import com.example.yj.mapapp.activity.UnderConstructionActivity;
import com.example.yj.mapapp.model.MyTradeLeads;
import com.example.yj.mapapp.util.LogUtil;

public class MPopwindow {
    private TextView tv_demand_release;
    private TextView tv_release_query;
    private TextView tv_wait_respond_query;
    private TextView tv_ok_respond_query;
    private TextView tv_sweep;
    private TextView tv_share;
    private Context context;//上下文对象
    private PopupWindow popupWindow;//泡泡窗口

    /**
     * 构造函数
     *
     * @param context
     */
    public MPopwindow(Context context) {
        this.context = context;
    }

    /**
     * 发布中心popwindow
     */
    public void showPopupWindowReleaseCenter(View v) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.frist_pop_release_center, null);

        tv_demand_release = (TextView) contentView.findViewById(R.id.id_first_demand_release);
        tv_release_query = (TextView) contentView.findViewById(R.id.id_first_release_query);
        //需求发布监听事件
        tv_demand_release.setOnClickListener(demandReleaseListener);
        //设置发布查询监听事件
        tv_release_query.setOnClickListener(releaseQueryListener);
        //设置PopupWindow显示宽度
        int defaultWidth = v.getMeasuredWidth();
        //实例化PopupWindow
        popupWindow = new PopupWindow(contentView,
                defaultWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);//设置PopupWindow可触摸
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);//设置外部控件可获得焦点

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.popwindow));

        // 设置好参数之后再show
        LogUtil.d("defaultWidth-----------------" + defaultWidth);
//        popupWindow.showAsDropDown(v);

//        View view = mInflater.inflate(R.layout.layout_popupwindow, null);
//        PopUpwindowLayout popUpwindowLayout = (PopUpwindowLayout) view.findViewById(R.id.llayout_popupwindow);
//        popUpwindowLayout.initViews(mContext, titles, false);
//        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = contentView.getMeasuredWidth();
        int popupHeight = contentView.getMeasuredHeight();
        int[] location = new int[2];
        // 允许点击外部消失
        popupWindow.setOutsideTouchable(true);
        // 获得位置
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]) - popupWidth / 2, location[1] - popupHeight);

    }

    /**
     * 响应中心popwindow
     */
    public void showPopupWindowResponseCenter(View v) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.frist_pop_response_center, null);
        tv_ok_respond_query = (TextView) contentView.findViewById(R.id.id_first_ok_respond_query);
        tv_wait_respond_query = (TextView) contentView.findViewById(R.id.id_first_wait_respond_query);
        //需求发布监听事件
        tv_ok_respond_query.setOnClickListener(okRespondQueryListener);
        //设置发布查询监听事件
        tv_wait_respond_query.setOnClickListener(waitRespondQueryListener);
        //设置PopupWindow显示宽度
        int defaultWidth = v.getMeasuredWidth();
        //实例化PopupWindow
        popupWindow = new PopupWindow(contentView,
                defaultWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);//设置PopupWindow可触摸
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);//设置外部控件可获得焦点
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.popwindow));
        // 设置好参数之后再show
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = contentView.getMeasuredWidth();
        int popupHeight = contentView.getMeasuredHeight();
        int[] location = new int[2];
        // 允许点击外部消失
        popupWindow.setOutsideTouchable(true);
        // 获得位置
        v.getLocationOnScreen(location);
//        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] * 1 + v.getWidth() / 2 - 40) - popupWidth / 2, location[1] - popupHeight);
    }

    /**
     * 扫一扫，分享popwindow
     */
    public void showPopupWindow(View v) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.frist_pop, null);
        tv_sweep = (TextView) contentView.findViewById(R.id.id_first_sweep);
        tv_share = (TextView) contentView.findViewById(R.id.id_first_share);
        //扫一扫监听事件
        tv_sweep.setOnClickListener(sweepListener);
        //分享询监听事件
        tv_share.setOnClickListener(shareListener);
        //设置PopupWindow显示宽度
//        int defaultWidth = v.getMeasuredWidth();
        //实例化PopupWindow
        popupWindow = new PopupWindow(contentView,
                400, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);//设置PopupWindow可触摸
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);//设置外部控件可获得焦点
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.popwindow));
        // 设置好参数之后再show
//        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        int popupWidth = contentView.getMeasuredWidth();
//        int popupHeight = contentView.getMeasuredHeight();
//        int[] location = new int[2];
        // 允许点击外部消失
        popupWindow.setOutsideTouchable(true);
        // 获得位置
        popupWindow.showAsDropDown(v, 0, 25, Gravity.NO_GRAVITY);
//        v.getLocationOnScreen(location);
//        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] * 1 + v.getWidth() / 2 - 40) - popupWidth / 2, location[1] - popupHeight);
    }

    /**
     * 取消泡泡窗口
     */
    public void dismiss() {
        popupWindow.dismiss();
    }

    /**
     * 扫一扫
     */
    private View.OnClickListener sweepListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(context, MipcaActivityCapture.class);
            context.startActivity(intent);
        }
    };
    /**
     * 分享
     */
    private View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(context, UnderConstructionActivity.class);
            context.startActivity(intent);
        }
    };

    /**
     * 需求发布
     */
    private View.OnClickListener demandReleaseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(context, UnderConstructionActivity.class);
            context.startActivity(intent);

//            context.startActivity(new Intent(context, ReleaseInformationActivity.class));
        }
    };

    /**
     * 发布查询
     */
    private View.OnClickListener releaseQueryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent();
//            intent.setClass(context, UnderConstructionActivity.class);
//            context.startActivity(intent);

//            context.startActivity(new Intent(context, MyTradeLeadsActivity.class));
            context.startActivity(new Intent(context, UnderConstructionActivity.class));
        }
    };

    /**
     * 待响应查询
     */
    private View.OnClickListener waitRespondQueryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(context, UnderConstructionActivity.class);
            context.startActivity(intent);
        }
    };

    /**
     * 已响应查询
     */
    private View.OnClickListener okRespondQueryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(context, UnderConstructionActivity.class);
            context.startActivity(intent);

//            context.startActivity(new Intent(context, MyResponseActivity.class));
        }
    };

}
