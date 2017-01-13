package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.base.MApplication;
import com.example.yj.mapapp.model.HardSuper;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.test.ClassPathResource;
import com.example.yj.mapapp.test.IsChinese;
import com.example.yj.mapapp.test.isChineseCharacters;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 */
public class IntentActivity extends BaseActivity {

    private final static String tag = "IntentActivity-->";

    private String user;//用户名
    private String pwd;//密码
    private String surePwd;//确认密码
    private String contactPerson;//联系人
    private String contactNumber;//联系电话
    private int type;//注册用户类型

    @Bind(R.id.id_register_user)
    EditText et_user;

    @Bind(R.id.id_register_pwd)
    EditText et_pwd;

    @Bind(R.id.id_register_sure_password)
    EditText et_surePwd;

    @Bind(R.id.id_register_contact_person)
    EditText et_contactPerson;

    @Bind(R.id.id_register_contact_number)
    EditText et_contactNumber;

    @Bind(R.id.id_back)
    ImageView back;

    @OnClick(R.id.id_back)
    public void back() {
        finish();
    }

    @Bind(R.id.id_now_register)
    Button register;

    @OnClick(R.id.id_now_register)
    public void register() {
//        ToastUtil.longT(this, "建设中,敬请期待...");
//        register("test1", "111", "aaa", "12345678909", 11);

        if (MApplication.getInstance().isNetworkConnected()) {
            user = et_user.getText().toString().trim();
            LogUtil.d("user:===============" + user);
            pwd = et_pwd.getText().toString().trim();
            LogUtil.d("pwd:===============" + pwd);
            surePwd = et_surePwd.getText().toString().trim();
            LogUtil.d("surePwd:===============" + surePwd);
            contactPerson = et_contactPerson.getText().toString().trim();
            LogUtil.d("contactPerson:===============" + contactPerson);
            contactNumber = et_contactNumber.getText().toString().trim();
            LogUtil.d("contactNumber:===============" + contactNumber);

            //访问后台接口
            register(user, pwd, contactPerson, contactNumber, type);

        } else {
            ToastUtil.shortT(this, getResources().getString(R.string.network_not_connected));
            return;
        }
    }

    private void register(String name, String pwd, String contacts, String phone, int type) {
        LogUtil.d("TAG", "type:=========" + type);
        if (name.equals("") || pwd.equals("") || contacts.equals("") || phone.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "所有信息不能为空!");
        } else if (name.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "用户名不能为空!");
        } else if (pwd.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "密码不能为空!");
        } else if (contacts.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "联系人不能为空!");
        } else if (phone.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "联系电话不能为空!");
        } else if (surePwd.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "确认密码不能为空!");
        } else if (!pwd.equals(surePwd)) {
            ToastUtil.shortT(IntentActivity.this, "两次输入的密码不一致!");
        } else if (ClassPathResource.isTellPhone(phone) == false) {
            ToastUtil.shortT(IntentActivity.this, "手机号码输入错误!");
        } else if (IsChinese.checkNameChese(contacts) == false || (4 < contacts.length() || contacts.length() < 2)) {
            ToastUtil.shortT(IntentActivity.this, "联系人请输入2~4个汉字!");
        } else if (isChineseCharacters.isNumeric(name)) {
            ToastUtil.shortT(IntentActivity.this, "用户名不能以数字开头!");
        } else if (IsChinese.checkNameChese(name)) {
            ToastUtil.shortT(IntentActivity.this, "用户名不能为汉字");
        } else {
            String url = HttpConfig.REQUEST_URL + "/User/AddUser";
            // 创建请求参数
            RequestParams params = new RequestParams();
            params.put("Name", name);
            params.put("Pwd", pwd);
            params.put("Contacts", contacts);
            params.put("Phone", phone);
            params.put("Type", type);

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
                    //判断注册状态
                    if (s.equals("1")) {
                        ToastUtil.longT(IntentActivity.this, "注册成功!");
                    } else if (s.equals("2")) {
                        ToastUtil.longT(IntentActivity.this, "注册失败!");
                    }
                    //界面跳转
                    toPage(NetActivity.class);
                    finish();
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

    @Bind(R.id.id_corporate_user)
    Button corporate_user;

    @OnClick(R.id.id_corporate_user)
    public void corporate_user() {
        //设置背景颜色
        corporate_user.setBackgroundResource(R.color.white);
        construction_user.setBackgroundResource(R.color.register_btn_color_click);
        //设置注册用户类型
        type = 7;
    }

    @Bind(R.id.id_construction_user)
    Button construction_user;

    @OnClick(R.id.id_construction_user)
    public void construction_user() {
        construction_user.setBackgroundResource(R.color.white);
        corporate_user.setBackgroundResource(R.color.register_btn_color_click);
        type = 8;
    }

    @Bind(R.id.id_frist_tip)
    TextView tip;

    @Override
    public int bindLayout() {
        return R.layout.activity_intent;
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
        et_contactNumber.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        type = 7;

        /**
         * EditText控件获取焦点事件
         */
        et_user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    tip.setVisibility(View.VISIBLE);
                } else {
                    // 此处为失去焦点时的处理内容
                    tip.setVisibility(View.GONE);
                }
            }
        });
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
