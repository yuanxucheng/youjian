package com.example.yj.mapapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.LabourAgencyAdapter;
import com.example.yj.mapapp.model.LabourAgency;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.MListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.widget.AbsListView.OnScrollListener;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LabourAgencyActivity extends Activity implements OnScrollListener, View.OnClickListener {

    //下拉刷新
    private ImageView iv_back;
    private List<LabourAgency> mData;
    private MListView lv;
    private LabourAgencyAdapter adapter;
    //下拉加载
    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数
    private View loadMoreView;
    private Button loadMoreButton;
    private Handler handler = new Handler();
    private int totalItemCount;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int length;//后台接口返回的数据的长度(分页访问)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_labour_agency);

        lv = (MListView) findViewById(R.id.lv);
        iv_back = (ImageView) findViewById(R.id.id_back);
        iv_back.setOnClickListener(this);

        mData = new ArrayList<LabourAgency>();

        getLabourAgencyInformation(pageIndex, pageSize, "");

        lv.setonRefreshListener(new MListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 让线程停止1000毫秒
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                //下拉刷新时先清空在添加
                LogUtil.d(mData.size() + "=============1");
                mData.clear();
                LogUtil.d(mData.size() + "=============2");
                getLabourAgencyInformation(pageIndex, pageSize, "");
                LogUtil.d(mData.size() + "=============3");
//                adapter.notifyDataSetChanged();
//                lv.onRefreshComplete();
            }
        });

        //使用打气筒插入顶部试图:加载更多布局
        loadMoreView = getLayoutInflater().inflate(R.layout.lv_loadmore, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
        //加载更多按钮点击事件的监听
        loadMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadMoreButton.setText("正在加载中...");   //设置按钮文字
                pageIndex = pageIndex + 1;
                getLabourAgencyInformation(pageIndex, pageSize, "");
            }
        });

        lv.addFooterView(loadMoreView);    //设置列表底部视图

        adapter = new LabourAgencyAdapter(mData, this);

        lv.setOnScrollListener(this);
        lv.setAdapter(adapter);
    }

    private void getLabourAgencyInformation(int pageIndex, int pageSize, String search) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pageIndex", pageIndex);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("search", search);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            String url = HttpConfig.REQUEST_URL + "/LaborAgency/GetEquipmentList";
            RequestHandle post = HTTPTool.getClient().post(LabourAgencyActivity.this, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LogUtil.d(getText(R.string.labour_agency_success).toString());
                    Log.d("==============", response.toString());
                    String d = JsonUtil.getData(response.toString());
                    Log.d("-------------", d);

                    try {
                        JSONArray array = new JSONArray(d);

                        length = array.length();
                        Log.d("length:-------------", length + "================");
                        if (length != 0) {
                            loadMoreButton.setText("查看更多...");  //恢复按钮文字
                        } else if (length == 0) {
                            loadMoreButton.setText("数据加载完毕!");  //变换按钮文字
                        }

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            int id = object.getInt("ID");
                            String name = object.getString("NAME");
                            String area = object.getString("AREA");
                            String contacts = object.getString("CONTACTS");
                            String phone = object.getString("PHONE");
                            String address = object.getString("ADDRESS");
                            LogUtil.d("tag", id + "-------" + name + "-----" + area + "-----" + contacts + "------" + phone + "----" + address);

                            LabourAgency la = new LabourAgency();
                            la.setName(name);
                            la.setArea(area);
                            la.setContacts(contacts);
                            la.setPhone(phone);
                            la.setAddress(address);
                            mData.add(la);

                        }
                        //设置适配器adapter
//                        lv.setAdapter(adapter);
                        //适配器的刷新
                        adapter.notifyDataSetChanged();
                        //ListView完成刷新动作
//                        lv.onRefreshComplete();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ToastUtil.longT(LabourAgencyActivity.this, getText(R.string.labour_agency_fail).toString());
                    Log.d("================", responseString);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int itemsLastIndex = adapter.getCount() - 1;  //数据集最后一项的索引
        int lastIndex = itemsLastIndex + 1;
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && visibleLastIndex == lastIndex) {
            // 如果是自动加载,可以在这里放置异步加载数据的代码
        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        this.visibleItemCount = visibleItemCount;
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        this.totalItemCount = totalItemCount;
//        Log.e("firstVisibleItem = ", firstVisibleItem + "");
//        Log.e("visibleItemCount = ", visibleItemCount + "");
//        Log.e("totalItemCount = ", totalItemCount + "");

        //如果所有的记录选项等于数据集的条数，则移除列表底部视图
        if (totalItemCount == mData.size() + 1) {
//            listView.removeFooterView(loadMoreView);
//            Toast.makeText(this, "数据全部加载完!", Toast.LENGTH_LONG).show();
            loadMoreButton.setText("数据全部加载完!");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
                finish();
                break;
        }
    }
}