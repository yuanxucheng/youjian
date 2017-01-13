package com.example.yj.mapapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.HardSuperAdapter;
import com.example.yj.mapapp.base.MApplication;
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

/**
 * 五金超市
 */
public class HardSuperActivity extends Activity implements AbsListView.OnScrollListener, View.OnClickListener, AdapterView.OnItemClickListener {

    //下拉刷新
    private ImageView iv_back;//返回
    private List<HardSuper> mData;//五金超市对象集合
    private MListView lv;//自定义的ListView控件
    private HardSuperAdapter adapter;//五金超市ListView控件的适配器
    //下拉加载
    private int visibleLastIndex = 0;//最后的可视项索引
    private int visibleItemCount;// 当前窗口可见项总数
    private View loadMoreView;//加载更多视图
    private Button loadMoreButton;//加载更多按钮
    private Handler handler = new Handler();//实例化handler对象
    private int parentId = 0;//接口参数值:父节点编号
    private int pageIndex = 1;//接口参数值:当前页数
    private int pageSize = 10;//接口参数值:每页个数
    private int length;//后台接口返回的数据的长度(分页访问)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_hard_super);

        //通过findViewById方法找到控件对象并设置点击事件
        lv = (MListView) findViewById(R.id.lv);
        iv_back = (ImageView) findViewById(R.id.id_back);
        iv_back.setOnClickListener(this);

        mData = new ArrayList<HardSuper>();//初始化集合对象

        if (MApplication.getInstance().isNetworkConnected()) {
            //使用第三方网络框架请求后台接口数据
            getLabourAgencyInformation(parentId, pageIndex, pageSize, "");
        } else {
            ToastUtil.shortT(this, getResources().getString(R.string.network_not_connected));
            return;
        }

        //下拉刷新监听事件
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
                //使用第三方网络框架请求后台接口数据
                getLabourAgencyInformation(parentId, pageIndex, pageSize, "");
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
                getLabourAgencyInformation(parentId, pageIndex, pageSize, "");
            }
        });
        lv.addFooterView(loadMoreView);    //设置列表底部视图
        adapter = new HardSuperAdapter(mData, this);//适配器实例化
        lv.setOnScrollListener(this);//ListView的滚动事件监听
        lv.setAdapter(adapter);//为ListView设置适配器
        lv.setOnItemClickListener(this);
    }

    /**
     * 使用第三方网络框架请求后台接口数据
     *
     * @param parentId
     * @param pageIndex
     * @param pageSize
     * @param search
     */
    private void getLabourAgencyInformation(int parentId, int pageIndex, int pageSize, String search) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("parentId", parentId);
            jsonObject.put("pageIndex", pageIndex);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("search", search);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            String url = HttpConfig.REQUEST_URL + "/Supermarket/GetEquipmentList";
            RequestHandle post = HTTPTool.getClient().post(HardSuperActivity.this, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LogUtil.d(getText(R.string.hard_super_success).toString());
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
                            String area = object.getString("AREA");
                            String category = object.getString("CATEGORY");
                            String contacts = object.getString("CONTACTS");
                            String phone = object.getString("PHONE");
                            String address = object.getString("ADDRESS");
                            LogUtil.d("tag", id + "-------" + name + "-----" + area + "-----" + category + "----------" + contacts + "------" + phone + "----" + address);

                            //创建五金超市对象并设置属性值
                            HardSuper hs = new HardSuper();
                            hs.setId(id);
                            hs.setName(name);
                            hs.setArea(area);
                            hs.setCategory(category);
                            hs.setContacts(contacts);
                            hs.setPhone(phone);
                            hs.setAddress(address);
                            //将对象添加到集合中
                            mData.add(hs);
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
                    ToastUtil.longT(HardSuperActivity.this, getText(R.string.hard_super_fail).toString());
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
     * 监听滚动时的不同状态的值
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
     * 监听滚动动作
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this, "item", Toast.LENGTH_LONG).show();
        int parentId = mData.get(position - 1).getId();
        String name = mData.get(position - 1).getName();

        Intent intent = new Intent();
        intent.setClass(HardSuperActivity.this, HardSuperDetailActivity.class);
        intent.putExtra("parentId", parentId);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}