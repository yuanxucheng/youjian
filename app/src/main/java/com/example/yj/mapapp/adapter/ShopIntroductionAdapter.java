package com.example.yj.mapapp.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.activity.ShopsDetailsActivity;
import com.example.yj.mapapp.model.Goods;
import com.example.yj.mapapp.util.LogUtil;

import java.util.List;

public class ShopIntroductionAdapter extends BaseAdapter {

    private final static String tag = "ShopIntroductionAdapter-->";
    //商品集合
    private List<Goods> goodsList;
    private LayoutInflater inflater;
    //    private Context context = MApplication.getInstance();
    private int pos;
    private Context mContext;//上下文对象

    public ShopIntroductionAdapter(List<Goods> goodsList, Context mContext) {
        this.goodsList = goodsList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        LogUtil.d(tag, "size=============" + goodsList.size());
        return goodsList == null ? 0 : goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        pos = position;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(
                    R.layout.shop_introduction_item, null);
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.id_shop_tv);
            holder.web_image = (WebView) convertView
                    .findViewById(R.id.id_shop_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Goods goods = goodsList.get(position);
        String baseUrl = "http://www.51buyjc.com/";
        String newImage = goods.getImage().substring(6);
        LogUtil.d(tag, "newImage========" + newImage);

//        holder.web_image.loadDataWithBaseURL(baseUrl, newImage, "text/html", "utf-8", null);

        WebSettings ws = holder.web_image.getSettings();

        //html的图片就会以单列显示就不会变形占了别的位置
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //让缩放显示的最小值为起始
        holder.web_image.setInitialScale(150);
        // 设置支持缩放
//        ws.setSupportZoom(true);
        // 设置缩放工具的显示
//        ws.setBuiltInZoomControls(true);

        holder.web_image.loadUrl(baseUrl + newImage);
        holder.tv_name.setText(goods.getName());
        return convertView;
    }

    class ViewHolder {

        TextView tv_name;
        WebView web_image;

    }
}
