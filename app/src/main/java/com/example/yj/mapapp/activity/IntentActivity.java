package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

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

    @Bind(R.id.id_corporate_user)
    Button corporate_user;

    @OnClick(R.id.id_corporate_user)
    public void corporate_user() {
        //设置背景颜色
        corporate_user.setBackgroundResource(R.color.white);
        construction_user.setBackgroundResource(R.color.register_btn_color_click);
        //设置注册用户类型
        type = 11;
    }

    @Bind(R.id.id_construction_user)
    Button construction_user;

    @OnClick(R.id.id_construction_user)
    public void construction_user() {
        construction_user.setBackgroundResource(R.color.white);
        corporate_user.setBackgroundResource(R.color.register_btn_color_click);
        type = 3;
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

        type = 11;

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

    private void register(String name, String pwd, String contacts, String phone, int type) {
        if (name.equals("") || pwd.equals("") || et_surePwd.getText().equals("") || contacts.equals("") || phone.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "所有信息不能为空!");
        } else if (name.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "用户名不能为空!");
        } else if (pwd.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "密码不能为空!");
        } else if (surePwd.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "确认密码不能为空!");
        } else if (contacts.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "联系人不能为空!");
        } else if (phone.equals("")) {
            ToastUtil.shortT(IntentActivity.this, "联系电话不能为空!");
        } else if (!pwd.equals(surePwd)) {
            ToastUtil.shortT(IntentActivity.this, "两次输入的密码不匹配!");
        } else if (ClassPathResource.isMobileNO(phone) == false) {
            ToastUtil.shortT(IntentActivity.this, "手机号码输入错误!");
        } else if (IsChinese.checkNameChese(contacts) == false || (4 < contacts.length() || contacts.length() < 2)) {
            ToastUtil.shortT(IntentActivity.this, "联系人请输入2~4个汉字!");
        } else if (isChineseCharacters.isNumeric(name)) {
            ToastUtil.shortT(IntentActivity.this, "用户名不能以数字开头!");
        } else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Name", name);
                jsonObject.put("Pwd", pwd);
                jsonObject.put("Contacts", contacts);
                jsonObject.put("Phone", phone);
                jsonObject.put("type", type);
                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                String url = HttpConfig.REQUEST_URL + "/User/AddUser";
                RequestHandle post = HTTPTool.getClient().post(IntentActivity.this, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d("==============", response.toString());

                        String s = JsonUtil.getS(response.toString());
                        Log.d("-------------", "s:========" + s.toString());
                        String d = JsonUtil.getData(response.toString());
                        Log.d("-------------", "d:========" + d.toString());
                        String m = JsonUtil.getMessage(response.toString());
                        Log.d("-------------", "m:========" + m.toString());

                        if (s.equals("1")) {
                            ToastUtil.longT(IntentActivity.this, d);
                            toPage(NetActivity.class);
                        } else if (s.equals("2")) {
                            ToastUtil.longT(IntentActivity.this, m);
                        } else if (s.equals("3")) {
                            ToastUtil.longT(IntentActivity.this, m);
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
}
