package com.example.yj.mapapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.MyTradeLeadsAdapter;
import com.example.yj.mapapp.model.MyTradeLeads;
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

/**
 * 我的供求
 */
public class MyTradeLeadsActivity extends Activity implements AbsListView.OnScrollListener, View.OnClickListener, AdapterView.OnItemClickListener {

    //下拉刷新
    private ImageView iv_back;//返回
    private List<MyTradeLeads> mData;//我的供求对象集合
    private MListView lv;//自定义ListView对象
    private MyTradeLeadsAdapter adapter;//我的供求适配器对象

    //下拉加载
    private int visibleLastIndex = 0;//最后的可视项索引
    private int visibleItemCount;// 当前窗口可见项总数
    private View loadMoreView;//加载更多视图
    private Button loadMoreButton;//加载更多按钮
    private Handler handler = new Handler();//实例化Handler对象

    //接口参数
    private boolean SupplyOrDemand = false;//接口参数值:供（true）求（false）
    private int pageIndex = 1;//接口参数值:当前页数
    private int pageSize = 5;//接口参数值:每页个数
    private String search = "";//接口参数值:搜索内容

    private String U_Id;
    private SharedPreferences spf;
    private int uId;//接口参数值:用户编号
    private int length;//后台接口返回的数据的长度(分页访问)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_my_trade_leads);

        //通过findViewById方法找到控件对象并设置点击事件
        lv = (MListView) findViewById(R.id.lv);
        iv_back = (ImageView) findViewById(R.id.id_back);
        iv_back.setOnClickListener(this);

        mData = new ArrayList<MyTradeLeads>();//实例化我的供求对象集合

        //创建SharedPreferences对象并保存数据
        spf = getSharedPreferences("file", MODE_PRIVATE);
        U_Id = spf.getString("U_Id", null);
        uId = Integer.valueOf(U_Id);
        System.out.println("uId==============" + uId);

        //使用第三方网络框架请求后台接口数据
        getMyTradeLeadsInformation(uId, SupplyOrDemand, pageIndex, pageSize, search);

        //设置ListView控件的item点击事件
        lv.setOnItemClickListener(this);
        //设置ListView控件的刷新事件
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
                mData.clear();//清空集合
                LogUtil.d(mData.size() + "=============2");
                //使用第三方网络框架请求后台接口数据
                getMyTradeLeadsInformation(uId, SupplyOrDemand, pageIndex, pageSize, search);
                LogUtil.d(mData.size() + "=============3");
//                adapter.notifyDataSetChanged();
//                lv.onRefreshComplete();
            }
        });
        //使用打气筒插入顶部试图:加载更多布局
        loadMoreView = getLayoutInflater().inflate(R.layout.lv_loadmore, null);
        //通过findViewById方法找到控件对象
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
        //加载更多按钮点击事件的监听
        loadMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadMoreButton.setText("正在加载中...");   //设置按钮文字
                pageIndex = pageIndex + 1;
                //使用第三方网络框架请求后台接口数据
                getMyTradeLeadsInformation(uId, SupplyOrDemand, pageIndex, pageSize, search);
            }
        });
        lv.addFooterView(loadMoreView);    //设置列表底部视图
        adapter = new MyTradeLeadsAdapter(mData, this);//实例化适配器
        lv.setOnScrollListener(this);//ListView控件滚动监听事件
        lv.setAdapter(adapter);//为ListView设置适配器
    }

    /**
     * 使用第三方网络框架请求后台接口数据
     *
     * @param uId
     * @param SupplyOrDemand
     * @param pageIndex
     * @param pageSize
     * @param search
     */
    private void getMyTradeLeadsInformation(int uId, boolean SupplyOrDemand, int pageIndex, int pageSize, String search) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uId", uId);
            jsonObject.put("SupplyOrDemand", SupplyOrDemand);
            jsonObject.put("pageIndex", pageIndex);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("search", search);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            String url = HttpConfig.REQUEST_URL + "/Release/GetReleaseList";
            RequestHandle post = HTTPTool.getClient().post(MyTradeLeadsActivity.this, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LogUtil.d(getText(R.string.my_buying_leads_success).toString());
                    Log.d("==============", response.toString());
                    String d = JsonUtil.getData(response.toString());
                    Log.d("-------------", d);

                    //解析JSON数据
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
                            String type = object.getString("TYPE");
                            String contacts = object.getString("CONTACTS");
                            String phone = object.getString("PHONE");
                            String content = object.getString("CONTENT");
                            String address = object.getString("ADDRESS");
                            String ctime = object.getString("CTIME");
                            LogUtil.d("tag", id + "----" + name + "-----" + type + "-----" + contacts + "----------" + phone + "------" + content + "----" + address + "--------" + ctime);

                            //创建求购信息对象并设置属性值
                            MyTradeLeads mtl = new MyTradeLeads();
                            mtl.setID(id);
                            mtl.setNAME(name);
                            mtl.setTYPE(type);
                            mtl.setCONTACTS(contacts);
                            mtl.setPHONE(phone);
                            mtl.setCONTENT(content);
                            mtl.setADDRESS(address);
                            mtl.setCTIME(ctime);
                            //将对象添加到集合中
                            mData.add(mtl);
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
                    ToastUtil.longT(MyTradeLeadsActivity.this, getText(R.string.my_buying_leads_fail).toString());
                    Log.d("================", responseString);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 滚动状态改变
     *
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int itemsLastIndex = adapter.getCount() - 1;  //数据集最后一项的索引
        int lastIndex = itemsLastIndex + 1;
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && visibleLastIndex == lastIndex) {
            // 如果是自动加载,可以在这里放置异步加载数据的代码
        }
    }

    /**
     * 滚动事件
     *
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        this.visibleItemCount = visibleItemCount;
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
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

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
                finish();
                break;
        }
    }

    /**
     * item点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ToastUtil.shortT(MyTradeLeadsActivity.this, "position:" + position);

        MyTradeLeads mtl = mData.get(position - 1);

        int sd_id = mtl.getID();
        String name = mtl.getNAME();
        String type = mtl.getTYPE();
        String contacts = mtl.getCONTACTS();
        String phone = mtl.getPHONE();
        String content = mtl.getCONTENT();
        String address = mtl.getADDRESS();
        String ctime = mtl.getCTIME();

        LogUtil.d("sd_id===============" + sd_id);
        LogUtil.d("name===============" + name);
        LogUtil.d("type===============" + type);
        LogUtil.d("contacts===============" + contacts);
        LogUtil.d("phone===============" + phone);
        LogUtil.d("content===============" + content);
        LogUtil.d("address===============" + address);
        LogUtil.d("ctime===============" + ctime);

        Intent intent = new Intent(MyTradeLeadsActivity.this, TradeLeadsDetailActivity.class);
        intent.putExtra("sd_id", sd_id);
        intent.putExtra("name", name);
        intent.putExtra("type", type);
        intent.putExtra("contacts", contacts);
        intent.putExtra("phone", phone);
        intent.putExtra("content", content);
        intent.putExtra("address", address);
        intent.putExtra("ctime", ctime);
        startActivity(intent);
    }
}