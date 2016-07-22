package com.example.yj.mapapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.MyResponseAdapter;
import com.example.yj.mapapp.base.MApplication;
import com.example.yj.mapapp.model.MyResponse;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.util.AppManager;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.ExitDialog;
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
import java.util.Objects;

/**
 * 我的响应
 */
public class MyResponseActivity extends Activity implements AbsListView.OnScrollListener, View.OnClickListener, AdapterView.OnItemClickListener {

    //下拉刷新
    private ImageView iv_back;//返回
    private List<MyResponse> mData;//我的响应集合
    private MListView lv;//自定义适配器
    private MyResponseAdapter adapter;//适配器对象
    //下拉加载
    private int visibleLastIndex = 0;//最后的可视项索引
    private int visibleItemCount; // 当前窗口可见项总数
    private View loadMoreView;//加载更多视图
    private Button loadMoreButton;//加载更多按钮
    private Handler handler = new Handler();//线程Handler对象

    //接口参数
    private int sdId = 0;//接口参数值:供求编号
    private int pageIndex = 1;//接口参数值:当前页数
    private int pageSize = 10;//接口参数值:每页个数
    private String search = "";//接口参数值:搜索内容

    private String U_Id;
    private SharedPreferences spf;
    private int uId;//接口参数值:用户编号
    private int length;//后台接口返回的数据的长度(分页访问)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_my_response);

        //通过findViewById方法找到控件对象并设置点击事件监听
        lv = (MListView) findViewById(R.id.lv);
        iv_back = (ImageView) findViewById(R.id.id_back);
        iv_back.setOnClickListener(this);

        //实例化我的响应对象集合
        mData = new ArrayList<MyResponse>();

        //创建SharedPreferences对象并保存数据
        spf = getSharedPreferences("file", MODE_PRIVATE);
        U_Id = spf.getString("U_Id", null);
        uId = Integer.valueOf(U_Id);
        System.out.println("uId==============" + uId);

        if (MApplication.getInstance().isNetworkConnected()) {
            //使用第三方网络框架请求后台接口数据
            getMyResponseInformation(uId, sdId, pageIndex, pageSize, search);
        } else {
            ToastUtil.shortT(this, getResources().getString(R.string.network_not_connected));
            return;
        }

        //ListView控件点击事件
        lv.setOnItemClickListener(this);
        //ListView控件刷新事件
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
                getMyResponseInformation(uId, sdId, pageIndex, pageSize, search);
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
                getMyResponseInformation(uId, sdId, pageIndex, pageSize, search);
            }
        });

        lv.addFooterView(loadMoreView);//设置列表底部视图
        adapter = new MyResponseAdapter(mData, this);//实例化适配器
        lv.setOnScrollListener(this);//ListView控件滚动监听事件
        lv.setAdapter(adapter);//为ListView设置适配器
    }

    /**
     * 使用第三方网络框架请求后台接口数据
     *
     * @param uId
     * @param sdId
     * @param pageIndex
     * @param pageSize
     * @param search
     */
    private void getMyResponseInformation(int uId, int sdId, int pageIndex, int pageSize, String search) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uId", uId);
            jsonObject.put("sdId", sdId);
            jsonObject.put("pageIndex", pageIndex);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("search", search);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            String url = HttpConfig.REQUEST_URL + "/Response/GetResponseList";
            RequestHandle post = HTTPTool.getClient().post(MyResponseActivity.this, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LogUtil.d(getText(R.string.my_response_success).toString());
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
                            int id = object.getInt("Id");
                            String name = object.getString("sd_name");
                            String contacts = object.getString("Contacts");
                            String phone = object.getString("Phone");
                            String content = object.getString("Content");
                            String user = object.getString("USER");
                            String ctime = object.getString("Ctime");
                            Boolean intention = object.getBoolean("Isintention");

                            LogUtil.d("tag", id + "----" + name + "-----" + contacts + "----------" + phone + "------" + content + "--------" + ctime);

                            //创建求购信息对象并设置属性值
                            MyResponse mr = new MyResponse();
                            mr.setId(id);
                            mr.setSd_name(name);
                            mr.setContacts(contacts);
                            mr.setPhone(phone);
                            mr.setContent(content);
                            mr.setUSER(user);
                            mr.setCtime(ctime);
                            mr.setIsintention(intention);
                            //将对象添加到集合中
                            mData.add(mr);

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
                    ToastUtil.longT(MyResponseActivity.this, getText(R.string.my_response_fail).toString());
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
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ToastUtil.shortT(MyResponseActivity.this, "position:" + position);

        MyResponse mr = mData.get(position - 1);

        int sd_id = mr.getId();
        String name = mr.getSd_name();
        String contacts = mr.getContacts();
        final String phone = mr.getPhone();
        String content = mr.getContent();
        Object user = mr.getUSER();
        String ctime = mr.getCtime();
        Boolean intention = mr.isIsintention();

        LogUtil.d("sd_id===============" + sd_id);
        LogUtil.d("name===============" + name);
        LogUtil.d("contacts===============" + contacts);
        LogUtil.d("phone===============" + phone);
        LogUtil.d("content===============" + content);
        LogUtil.d("user===============" + user);
        LogUtil.d("ctime===============" + ctime);
        LogUtil.d("intention===============" + intention);

        if (intention == false) {
            ToastUtil.longT(MyResponseActivity.this, getText(R.string.no_call_phone).toString());
        } else {
            final ExitDialog myDialog = new ExitDialog(MyResponseActivity.this,
                    getText(R.string.whether_to_dial_telephone).toString(), getText(R.string.cancel).toString(), getText(R.string.ok).toString());
            myDialog.show();
            myDialog.setDialogROnClickListener(new ExitDialog.MyDialogROnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);//指定意图动作
                    intent.setData(Uri.parse("tel:" + phone));//指定电话号码
                    startActivity(intent);
                }
            });
        }
    }
}