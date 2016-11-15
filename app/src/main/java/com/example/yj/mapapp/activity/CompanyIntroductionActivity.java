package com.example.yj.mapapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.net.handler.MxgsaTagHandler;
import com.example.yj.mapapp.util.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 公司介绍
 */
public class CompanyIntroductionActivity extends BaseActivity {
    private final static String tag = "CompanyIntroductionActivity-->";

    @Bind(R.id.id_back)
    ImageView back;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

//    @Bind(R.id.tv_build_enterprise_companyIntroduction)
//    TextView companyIntroduction;

    @Bind(R.id.id_company_webview)
    WebView webView;

    @Bind(R.id.id_company_textview)
    TextView textView;

    @Override
    public int bindLayout() {
        return R.layout.activity_company_introduction;
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

        //界面跳转带过来的数据
        String mContent = getIntent().getStringExtra("content");
        LogUtil.d("mContent", mContent);
        if (mContent.equals("")) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有数据");
        } else {
            webView.setVisibility(View.VISIBLE);

            WebSettings ws = webView.getSettings();

            //html的图片就会以单列显示就不会变形占了别的位置
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            //让缩放显示的最小值为起始
            webView.setInitialScale(145);
            // 设置支持缩放
            ws.setSupportZoom(true);
            // 设置缩放工具的显示
            ws.setBuiltInZoomControls(true);

//            ws.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//            ws.setJavaScriptEnabled(true);
//            ws.setUseWideViewPort(true);//设置此属性，可任意比例缩放
//            ws.setBuiltInZoomControls(true); //显示放大缩小 controler
//            ws.setSupportZoom(true); //// 支持缩放
//            webView.setSaveEnabled(true);
//            ws.setDisplayZoomControls(true);// 设置显示缩放按钮

            String baseUrl = "http://www.51buyjc.com/";
            LogUtil.d(tag, "mContent========" + mContent);
            webView.loadDataWithBaseURL(baseUrl, mContent, "text/html", "utf-8", null);
        }
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
