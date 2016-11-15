package com.example.yj.mapapp.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.ContactAdapter;
import com.example.yj.mapapp.model.Contact;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.view.MProgressDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FindFirmActivity extends Activity implements
        OnItemClickListener, OnClickListener, TextWatcher {

    private ListView lv;
    private EditText et_contact;

    private ContactAdapter adapter;

    private ArrayList<Contact> infos;

    private int pageSize = 10000;//每页个数
    private int pageIndex = 1;//当前页数

    //显示对话框
    private static final int SHOW = 1;
    //关闭对话框
    private static final int DISMISS = 2;

    //自定义进度对话框
    private MProgressDialog pb;
    //返回键
    private ImageView back;
    //删除键
    private ImageView ivDelete;
    //第几次进入该界面
    private int count = 0;
    private TextView tip;

    Handler handler = new Handler() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_firm);

        pb = new MProgressDialog(this);//实例化进度对话框对象
        pb.setMessage(getString(R.string.buildSites_loadMap));//设置对话框信息
        pb.setCancelable(true);//设置进度条是否可以按退回键取消
        pb.setCanceledOnTouchOutside(true); //设置点击进度对话框外的区域对话框消失

        lv = (ListView) findViewById(R.id.lv_contact);
        et_contact = (EditText) findViewById(R.id.search_et_input);
        back = (ImageView) findViewById(R.id.id_back);
        tip = (TextView) findViewById(R.id.search_firm_tip);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);

        //TODO
        infos = new ArrayList<>();

        getEnterpriseSearch("", pageSize, pageIndex);

        LogUtil.d("tag", "size===" + infos.size());
        adapter = new ContactAdapter(infos, this);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
        back.setOnClickListener(this);
        et_contact.addTextChangedListener(this);
        ivDelete.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        count++;
        new Thread() {
            @Override
            public void run() {
                // 不延迟，直接发送
                if (count == count % 2) {
                    handler.sendEmptyMessage(SHOW);
                }
            }
        }.start();
        et_contact.setText("");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Contact info = (Contact) parent.getItemAtPosition(position);
        //跳转到显示建材企业详情界面
        Intent intent = new Intent();
        intent.setClass(FindFirmActivity.this, BuildEnterprisesDetailActivity.class);
//        intent.putExtra("buildEnterprise_id", infos.get(position).getId());
        intent.putExtra("buildEnterprise_id", info.getId());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.search_iv_delete:
                et_contact.setText("");
                ivDelete.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (infos != null && infos.size() != 0) {
            String str = s.toString();
            ArrayList<Contact> tempInfo = null;
            for (Contact cInfo : infos) {
                String name = cInfo.getName();
                String address = cInfo.getAddress();
                String contacts = cInfo.getContacts();
                if (name != null && name.contains(str) || address != null && address.contains(str) || contacts != null && contacts.contains(str)) {
                    ivDelete.setVisibility(View.GONE);
                    if (tempInfo == null) {
                        tempInfo = new ArrayList<Contact>();
                    }
                    tempInfo.add(cInfo);
                } else {
                    ivDelete.setVisibility(View.VISIBLE);

//                    lv.setVisibility(View.GONE);
//                    tip.setVisibility(View.VISIBLE);
                }
            }
            if (tempInfo != null) {
                adapter.setData(tempInfo);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 模糊搜索企业（企业名称/地址/联系人）
     *
     * @param search
     * @param pageSize
     * @param pageIndex
     */
    private void getEnterpriseSearch(final String search, int pageSize, int pageIndex) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("search", search);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("pageIndex", pageIndex);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            String url = HttpConfig.REQUEST_URL + "/Map/GetEnterpriseSearch";
            RequestHandle post = HTTPTool.getClient().post(FindFirmActivity.this, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("==============", response.toString());
                    String d = JsonUtil.getData(response.toString());
                    Log.d("-------------", d);

                    try {
                        JSONArray array = new JSONArray(d);

                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int id = object.getInt("Id");
                                String name = object.getString("Name");
                                String address = object.getString("Address");
                                String contacts = object.getString("Contacts");
                                String authentication = object.getString("Authentication");
                                LogUtil.d("tag", id + "-------" + name + "-----" + address + "-----" + contacts + "------" + authentication);

                                LogUtil.d("tag", "size:===============" + infos.size());
                                infos.add(new Contact(R.mipmap.desktop_icon, id, name, address, contacts, authentication));
//                                Contact info = new Contact(R.mipmap.desktop_icon, id, name, address, contacts, authentication);
//                                infos.add(info);
                                LogUtil.d("tag", "size:===============" + infos.size());

                                //发送消息提示关闭对话框
                                Message msg = new Message();
                                msg.what = DISMISS;
                                handler.sendMessage(msg);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("================", responseString);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}