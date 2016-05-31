package com.example.yj.mapapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.MyResponseAdapter;
import com.example.yj.mapapp.base.MApplication;
import com.example.yj.mapapp.model.MyResponse;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.ExitDialog;
import com.example.yj.mapapp.view.MListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TradeLeadsDetailActivity extends Activity implements View.OnClickListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private TextView buying_leads_btitle;
    private TextView buying_leads_title;
    private TextView buying_leads_category;
    private TextView buying_leads_contacts;
    private TextView buying_leads_phone;
    private TextView buying_leads_content;
    private TextView buying_leads_address;
    private TextView buying_leads_ctime;

    //下拉刷新
    private ImageView iv_back;
    private List<MyResponse> mData;
    private MListView lv;
    private MyResponseAdapter adapter;
    //下拉加载
    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数
    private View loadMoreView;
    private Button loadMoreButton;
    private Handler handler = new Handler();

    //接口参数
    private int sdId;//接口参数值:供求编号
    private int pageIndex = 1;//接口参数值:当前页数
    private int pageSize = 100;//接口参数值:每页个数
    private String search = "";//接口参数值:搜索内容
    private String U_Id;
    private SharedPreferences spf;
    private int uId;//接口参数值:用户编号
    private boolean intention;//是否有意向
    private int sure_id;//响应编号
    private int length;//后台接口返回的数据的长度(分页访问)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_leads_detail);
        initView();
        initData();

        lv = (MListView) findViewById(R.id.lv);
        iv_back = (ImageView) findViewById(R.id.id_back);
        iv_back.setOnClickListener(this);

        mData = new ArrayList<MyResponse>();

        spf = getSharedPreferences("file", MODE_PRIVATE);

        U_Id = spf.getString("U_Id", null);

        sdId = this.getIntent().getIntExtra("sd_id", -1);

        uId = Integer.valueOf(U_Id);

        LogUtil.d("sdId===============" + sdId);
        System.out.println("uId==============" + uId);
        System.out.println("sdId==============" + sdId);

        //使用第三方网络框架请求后台接口数据
        getMyResponseInformation(uId, sdId, pageIndex, pageSize, search);

        lv.setOnItemClickListener(this);

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
                getMyResponseInformation(uId, sdId, pageIndex, pageSize, search);
                LogUtil.d(mData.size() + "=============3");
//                adapter.notifyDataSetChanged();
//                lv.onRefreshComplete();
            }
        });


        loadMoreView = getLayoutInflater().inflate(R.layout.lv_loadmore, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
        //加载更多按钮点击事件的监听
        loadMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadMoreButton.setText("正在加载中...");   //设置按钮文字
                pageIndex = pageIndex + 1;
                getMyResponseInformation(uId, sdId, pageIndex, pageSize, search);
            }
        });

        lv.addFooterView(loadMoreView);    //设置列表底部视图

        adapter = new MyResponseAdapter(mData, this);

        lv.setOnScrollListener(this);
        lv.setAdapter(adapter);
    }

    private void initView() {
        buying_leads_btitle = (TextView) findViewById(R.id.id_my_buying_leads_btitle);
        buying_leads_title = (TextView) findViewById(R.id.id_my_buying_leads_title);
        buying_leads_category = (TextView) findViewById(R.id.id_my_buying_leads_category);
        buying_leads_contacts = (TextView) findViewById(R.id.id_my_buying_leads_contacts);
        buying_leads_phone = (TextView) findViewById(R.id.id_my_buying_leads_phone);
        buying_leads_content = (TextView) findViewById(R.id.id_my_buying_leads_content);
        buying_leads_address = (TextView) findViewById(R.id.id_my_buying_leads_address);
        buying_leads_ctime = (TextView) findViewById(R.id.id_my_buying_leads_ctime);
    }

    private void initData() {
        int sd_id = this.getIntent().getIntExtra("sd_id", -1);
        String name = this.getIntent().getStringExtra("name");
        String type = this.getIntent().getStringExtra("type");
        String contacts = this.getIntent().getStringExtra("contacts");
        String phone = this.getIntent().getStringExtra("phone");
        String content = this.getIntent().getStringExtra("content");
        String address = this.getIntent().getStringExtra("address");
        String ctime = this.getIntent().getStringExtra("ctime");

        LogUtil.d("sd_id===============" + sd_id);
        LogUtil.d("name===============" + name);
        LogUtil.d("type===============" + type);
        LogUtil.d("contacts===============" + contacts);
        LogUtil.d("phone===============" + phone);
        LogUtil.d("content===============" + content);
        LogUtil.d("address===============" + address);
        LogUtil.d("ctime===============" + ctime);

        buying_leads_btitle.setText(name);
        buying_leads_title.setText(name);
        buying_leads_category.setText(type);
        buying_leads_contacts.setText(contacts);
        buying_leads_phone.setText(phone);
        buying_leads_content.setText(content);
        buying_leads_address.setText(address);
        buying_leads_ctime.setText(ctime);
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
            RequestHandle post = HTTPTool.getClient().post(TradeLeadsDetailActivity.this, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
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
                            sure_id = object.getInt("Id");
                            String name = object.getString("sd_name");
                            String contacts = object.getString("Contacts");
                            String phone = object.getString("Phone");
                            String content = object.getString("Content");
                            String user = object.getString("USER");
                            String ctime = object.getString("Ctime");
                            Boolean intention = object.getBoolean("Isintention");

                            LogUtil.d("tag", sure_id + "----" + name + "-----" + contacts + "----------" + phone + "------" + content + "--------" + ctime);

                            //创建求购信息对象并设置属性值
                            MyResponse mr = new MyResponse();
                            mr.setId(sure_id);
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
                    ToastUtil.longT(TradeLeadsDetailActivity.this, getText(R.string.my_response_fail).toString());
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
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && visibleLastIndex == lastIndex) {
            // 如果是自动加载,可以在这里放置异步加载数据的代码
        }
    }


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
//        ToastUtil.shortT(MyResponseActivity.this, "position:" + position);

        MyResponse mr = mData.get(position - 1);

        final int sd_id = mr.getId();
        String name = mr.getSd_name();
        String contacts = mr.getContacts();
        final String phone = mr.getPhone();
        String content = mr.getContent();
        Object user = mr.getUSER();
        String ctime = mr.getCtime();
        intention = mr.isIsintention();

        LogUtil.d("sd_id===============" + sd_id);
        LogUtil.d("name===============" + name);
        LogUtil.d("contacts===============" + contacts);
        LogUtil.d("phone===============" + phone);
        LogUtil.d("content===============" + content);
        LogUtil.d("user===============" + user);
        LogUtil.d("ctime===============" + ctime);
        LogUtil.d("intention===============" + intention);

        if (intention == true) {
            final ExitDialog myDialog = new ExitDialog(TradeLeadsDetailActivity.this,
                    getText(R.string.whether_to_dial_telephone).toString(), getText(R.string.cancel).toString(), getText(R.string.ok).toString());
            myDialog.show();
            myDialog.setDialogROnClickListener(new ExitDialog.MyDialogROnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);//指定意图动作
                    intent.setData(Uri.parse("tel:" + phone));//指定电话号码
                    startActivity(intent);
                    myDialog.dismiss();
                }
            });
        } else {
            final ExitDialog myDialog = new ExitDialog(TradeLeadsDetailActivity.this,
                    getText(R.string.is_recognized_intentional_response).toString(), getText(R.string.cancel).toString(), getText(R.string.ok).toString());
            myDialog.show();
            myDialog.setDialogROnClickListener(new ExitDialog.MyDialogROnClickListener() {

                @Override
                public void onClick(View v) {

                    intention = true;

                    mData.get(position - 1).setIsintention(true);
                    mData.get(position - 1).setPhone(phone);

                    adapter.setData(mData);
                    adapter.notifyDataSetChanged();

                    LogUtil.d("tag==============", sure_id + "");
                    LogUtil.d("tag==============", sd_id + "");
//                    sureIntention(sure_id);
                    sureIntention(sd_id);
                    myDialog.dismiss();
                }
            });
        }
    }

    private void sureIntention(int prId) {
        String url = HttpConfig.REQUEST_URL + "/Response/intentionResponse";
        // 创建请求参数
        RequestParams params = new RequestParams();
        params.put("prId", prId);
        // 执行post方法
        HTTPTool.getClient().post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                String response = new String(responseBody);
                LogUtil.d("tag----------", response);

                Log.d("response==============", response.toString());
                String d = JsonUtil.getData(response.toString());
                Log.d("d-------------", d);
                String s = JsonUtil.getS(response.toString());
                Log.d("s-------------", s);
                String m = JsonUtil.getMessage(response.toString());
                Log.d("m-------------", m);

                if (s.equals("1")) {
                    LogUtil.d("tag", "意向响应成功!");

                } else if (s.equals("2")) {
                    LogUtil.d("tag", "意向响应失败!");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                // 打印错误信息
                error.printStackTrace();
            }
        });
    }
}
