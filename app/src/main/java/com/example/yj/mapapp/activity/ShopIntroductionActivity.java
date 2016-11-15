package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.ShopIntroductionAdapter;
import com.example.yj.mapapp.banner.ADInfo;
import com.example.yj.mapapp.banner.ViewFactory;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.model.Goods;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 商品介绍
 */
public class ShopIntroductionActivity extends BaseActivity {

    private final static String tag = "ShopIntroductionActivity-->";
    //商品集合
    private List<Goods> goodsList;
    //适配器
    private ShopIntroductionAdapter adapter;
    private Goods good;

    @Bind(R.id.id_back)
    ImageView back;
    private GridView shop_grid;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

//    @Bind(R.id.id_shop_grid)
//    GridView shop_grid;

//    @OnItemClick(R.id.id_shop_grid)
//    void onItemClick(int position) {
//        Toast.makeText(this, "Clicked position " + position + "!", Toast.LENGTH_LONG).show();
//    }

    @Override
    public int bindLayout() {
        return R.layout.activity_shop_introduction;
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

        goodsList = new ArrayList<>();

        Intent intent = this.getIntent();
        goodsList = (List<Goods>) intent.getSerializableExtra("list");//获取list方式
        LogUtil.d(tag, "size..............." + goodsList.size());
        adapter = new ShopIntroductionAdapter(goodsList, ShopIntroductionActivity.this);
        shop_grid = (GridView) this.findViewById(R.id.id_shop_grid);
        shop_grid.setAdapter(adapter);
        shop_grid.setClickable(false);
        shop_grid.setFocusable(true);
        shop_grid.setOnItemLongClickListener(shopGridItemLongOnclick);
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

    private AdapterView.OnItemLongClickListener shopGridItemLongOnclick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//            Toast.makeText(ShopIntroductionActivity.this, "" + position, Toast.LENGTH_LONG).show();
            Goods good = goodsList.get(position);
            //跳转界面并传递对象
            Intent intent = new Intent();
            intent.setClass(ShopIntroductionActivity.this, ShopsDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("good", good);
            intent.putExtras(bundle);
            ShopIntroductionActivity.this.startActivity(intent);
            return false;
        }
    };

}
