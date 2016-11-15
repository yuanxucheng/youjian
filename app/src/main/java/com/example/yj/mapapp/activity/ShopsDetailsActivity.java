package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.banner.ADInfo;
import com.example.yj.mapapp.banner.CycleViewPager;
import com.example.yj.mapapp.banner.ViewFactory;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.model.Goods;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品信息详情
 */
public class ShopsDetailsActivity extends BaseActivity {

    private final static String tag = "ShopsDetailsActivity-->";
    private Goods good;

    @Bind(R.id.id_back)
    ImageView back;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

    @Bind(R.id.id_shopDetail_webview_image)
    WebView webView_image;
    @Bind(R.id.id_shopDetail_webview_content)
    WebView webView_content;
    @Bind(R.id.id_shopDetail_tv_name)
    TextView textView_name;
    @Bind(R.id.id_shopDetail_tv_type)
    TextView textView_type;
    @Bind(R.id.id_shopDetail_tv_price)
    TextView textView_price;

    @Override
    public int bindLayout() {
        return R.layout.activity_shops_details;
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

        Intent intent = this.getIntent();
        good = (Goods) intent.getSerializableExtra("good");
        int id = good.getId();
        String name = good.getName();
        String image = good.getImage();
        String type = good.getType();
        String price = good.getPrice();
        String content = good.getContent();

        String baseUrl = "http://www.51buyjc.com/";

        WebSettings ws_image = webView_image.getSettings();
        WebSettings ws_content = webView_content.getSettings();

        //html的图片就会以单列显示就不会变形占了别的位置
        ws_image.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //让缩放显示的最小值为起始
        webView_image.setInitialScale(145);
        // 设置支持缩放
//        ws_image.setSupportZoom(true);
        // 设置缩放工具的显示
//        ws_image.setBuiltInZoomControls(true);
        webView_image.loadUrl(baseUrl + image);

        //html的图片就会以单列显示就不会变形占了别的位置
        ws_content.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //让缩放显示的最小值为起始
        webView_content.setInitialScale(145);
        // 设置支持缩放
        ws_content.setSupportZoom(true);
        // 设置缩放工具的显示
        ws_content.setBuiltInZoomControls(true);
        webView_content.loadDataWithBaseURL(baseUrl, content, "text/html", "utf-8", null);

        textView_name.setText(name);
        textView_type.setText(type);
        textView_price.setText(price);
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
}
