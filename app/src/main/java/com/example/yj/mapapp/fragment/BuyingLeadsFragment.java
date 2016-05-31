package com.example.yj.mapapp.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.activity.ResponseMessagesActivity;
import com.example.yj.mapapp.adapter.BuyingLeadsAdapter;
import com.example.yj.mapapp.model.BuyingLeads;
import com.example.yj.mapapp.model.News;
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

public class BuyingLeadsFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    //下拉刷新
    private List<BuyingLeads> mData;//求购信息对象集合
    private MListView lv;//自定义的ListView控件
    private BuyingLeadsAdapter adapter;//求购信息ListView控件的适配器
    //下拉加载
    private int visibleLastIndex = 0;//最后的可视项索引
    private int visibleItemCount;// 当前窗口可见项总数
    private View loadMoreView;//加载更多视图
    private Button loadMoreButton;//加载更多按钮
    private Handler handler = new Handler();

    //接口参数
    private int uId = 0;//接口参数值:用户编号
    private boolean SupplyOrDemand = false;//接口参数值:供（true）求（false）
    private int pageIndex = 1;//接口参数值:当前页数
    private int pageSize = 10;//接口参数值:每页个数
    private String search = "";//接口参数值:搜索内容

    private LinearLayout layout;
    private int length;//后台接口返回的数据的长度(分页访问)

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        new Thread() {
//            @Override
//            public void run() {
//                // 不延迟，直接发送
//                handler.sendEmptyMessage(SHOW);
//            }
//        }.start();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.buying_leads, container,
                false);
        layout = (LinearLayout) v.findViewById(R.id.layouts);

        lv = (MListView) layout.findViewById(R.id.lv);
        lv.setOnItemClickListener(this);
        mData = new ArrayList<BuyingLeads>();

        //使用第三方网络框架请求后台接口数据
        getBuyingLeadsInformation(uId, SupplyOrDemand, pageIndex, pageSize, search);

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
                getBuyingLeadsInformation(uId, SupplyOrDemand, pageIndex, pageSize, search);
                LogUtil.d(mData.size() + "=============3");
//                adapter.notifyDataSetChanged();
//                lv.onRefreshComplete();
            }
        });
        //使用打气筒插入顶部试图:加载更多布局
        loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.lv_loadmore, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
        //加载更多按钮点击事件的监听
        loadMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadMoreButton.setText("正在加载中...");   //设置按钮文字
                pageIndex = pageIndex + 1;
                getBuyingLeadsInformation(uId, SupplyOrDemand, pageIndex, pageSize, search);
            }
        });

        lv.addFooterView(loadMoreView);    //设置列表底部视图

        adapter = new BuyingLeadsAdapter(mData, getActivity());//适配器实例化
        lv.setOnScrollListener(this);//ListView的滚动事件监听
        lv.setAdapter(adapter);//为ListView设置适配器
        return v;
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
    private void getBuyingLeadsInformation(int uId, boolean SupplyOrDemand, int pageIndex, int pageSize, String search) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uId", uId);
            jsonObject.put("SupplyOrDemand", SupplyOrDemand);
            jsonObject.put("pageIndex", pageIndex);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("search", search);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            String url = HttpConfig.REQUEST_URL + "/Release/GetReleaseList";
            RequestHandle post = HTTPTool.getClient().post(getActivity(), url, stringEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LogUtil.d(getText(R.string.buying_leads_success).toString());
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
                            LogUtil.d("tag", name + "-----" + type + "-----" + contacts + "----------" + phone + "------" + content + "----" + address + "--------" + ctime);

                            //创建求购信息对象并设置属性值
                            BuyingLeads bl = new BuyingLeads();
                            bl.setID(id);
                            bl.setNAME(name);
                            bl.setTYPE(type);
                            bl.setCONTACTS(contacts);
                            bl.setPHONE(phone);
                            bl.setCONTENT(content);
                            bl.setADDRESS(address);
                            bl.setCTIME(ctime);
                            //将对象添加到集合中
                            mData.add(bl);

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
                    ToastUtil.longT(getActivity(), getText(R.string.buying_leads_fail).toString());
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

        Log.e("firstVisibleItem = ", firstVisibleItem + "");
        Log.e("visibleItemCount = ", visibleItemCount + "");
        Log.e("totalItemCount = ", totalItemCount + "");

        //如果所有的记录选项等于数据集的条数，则移除列表底部视图
        if (totalItemCount == mData.size() + 1) {
//            listView.removeFooterView(loadMoreView);
//            Toast.makeText(getActivity(), "数据全部加载完!", Toast.LENGTH_LONG).show();
            loadMoreButton.setText("数据全部加载完!");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ToastUtil.shortT(getActivity(), "position:" + position);

        BuyingLeads bl = mData.get(position - 1);

        int sd_id = bl.getID();
        String name = bl.getNAME();
        String type = bl.getTYPE();

        LogUtil.d("sd_id===============" + sd_id);
        LogUtil.d("name===============" + name);
        LogUtil.d("type===============" + type);

        Intent intent = new Intent(getActivity(), ResponseMessagesActivity.class);
        intent.putExtra("sd_id", sd_id);
        intent.putExtra("name", name);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}
