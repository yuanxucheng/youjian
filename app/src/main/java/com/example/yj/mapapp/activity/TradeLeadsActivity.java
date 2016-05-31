package com.example.yj.mapapp.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.BuyingLeadsAdapter;
import com.example.yj.mapapp.fragment.BuyingLeadsFragment;
import com.example.yj.mapapp.fragment.SupplyInformationFragment;
import com.example.yj.mapapp.model.BuyingLeads;
import com.example.yj.mapapp.model.HardSuper;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.MListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TradeLeadsActivity extends Activity implements View.OnClickListener {

    private FragmentManager fm;
    private BuyingLeadsFragment fBuyingLeads;
    private SupplyInformationFragment fSupplyInformation;
    private TextView layout_buying_leads;
    private TextView layout_supply_information;

    private ImageView back;
    private TextView release_news;

    //进度对话框
    private ProgressDialog pb;
    private static final int SHOW = 1;
    private static final int DISMISS = 2;

    Handler h = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW:
                    pb.show();
                    break;
                case DISMISS:
                    pb.dismiss();
                    break;
            }
        }
    };

    public ProgressDialog getPb() {
        return pb;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_leads);

        initView();
        initListener();

        fm = getFragmentManager();

        fBuyingLeads = new BuyingLeadsFragment();
        fSupplyInformation = new SupplyInformationFragment();

        layout_buying_leads = (TextView) findViewById(R.id.layout_buying_leads);
        layout_supply_information = (TextView) findViewById(R.id.layout_supply_information);

        fm.beginTransaction().add(R.id.container, fBuyingLeads)
                .add(R.id.container, fSupplyInformation).hide(fBuyingLeads).commit();

        layout_supply_information.setBackgroundResource(R.color.white);
        layout_supply_information.setTextColor(getResources().getColor(R.color.gray));
        layout_buying_leads.setTextColor(getResources().getColor(R.color.white));

        layout_buying_leads.setOnClickListener(this);
        layout_supply_information.setOnClickListener(this);

        pb = new ProgressDialog(this);
        pb.setMessage(getString(R.string.supply_loading));
        // 不延迟，直接发送
        h.sendEmptyMessage(SHOW);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = DISMISS;
                h.sendMessage(msg);
            }
        }, 1000);
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.id_back);
        release_news = (TextView) this.findViewById(R.id.id_trade_leads_release_news);
    }

    private void initListener() {
        back.setOnClickListener(this);
        release_news.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_buying_leads:
                fm.beginTransaction().show(fBuyingLeads).hide(fSupplyInformation)
                        .commit();
                layout_supply_information.setBackgroundResource(R.color.gray);
                layout_buying_leads.setBackgroundResource(R.color.white);
                layout_supply_information.setTextColor(getResources().getColor(R.color.white));
                layout_buying_leads.setTextColor(getResources().getColor(R.color.gray));

                // 不延迟，直接发送
                h.sendEmptyMessage(SHOW);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = DISMISS;
                        h.sendMessage(msg);
                    }
                }, 1000);

                break;
            case R.id.layout_supply_information:
                fm.beginTransaction().show(fSupplyInformation).hide(fBuyingLeads)
                        .commit();
                layout_supply_information.setBackgroundResource(R.color.white);
                layout_buying_leads.setBackgroundResource(R.color.gray);
                layout_supply_information.setTextColor(getResources().getColor(R.color.gray));
                layout_buying_leads.setTextColor(getResources().getColor(R.color.white));
                // 不延迟，直接发送
                h.sendEmptyMessage(SHOW);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = DISMISS;
                        h.sendMessage(msg);
                    }
                }, 1000);
                break;
            case R.id.id_back:
                finish();
                break;
            case R.id.id_trade_leads_release_news:
                startActivity(new Intent(TradeLeadsActivity.this, ReleaseInformationActivity.class));
//                startActivity(new Intent(TradeLeadsActivity.this, UnderConstructionActivity.class));
                break;
        }
    }
}
