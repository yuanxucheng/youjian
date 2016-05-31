package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.base.MApplication;
import com.example.yj.mapapp.model.HardSuper;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NetActivity extends BaseActivity {
    private final static String tag = "NetActivity-->";

    private String user;
    private String pwd;

    @Bind(R.id.id_login_user)
    EditText et_user;

    @Bind(R.id.id_login_pwd)
    EditText et_pwd;

    @Bind(R.id.id_back)
    ImageView back;

    @OnClick(R.id.id_back)
    public void back() {
        finish();
    }

    @Bind(R.id.id_login)
    Button login;

    private String userName;
    private String u;

    @OnClick(R.id.id_login)
    public void login() {
//        ToastUtil.longT(this, "建设中,敬请期待...");
//        login("test1", "111");

        user = et_user.getText().toString().trim();
        LogUtil.d("user:===============" + user);
        pwd = et_pwd.getText().toString().trim();
        LogUtil.d("pwd:===============" + pwd);


        try {
//          u=  URLDecoder.decode(user, "utf-8");
            //u = new String(user.getBytes("ISO8859-1"), "UTF-8");
            //u= URLEncoder.encode(URLEncoder.encode(user,"UTF-8"),"UTF-8");
            u = user;
        } catch (Exception e) {
            e.printStackTrace();
        }

        login(u, pwd);

//        login("曾斌", "123");
//        login("test1", "111");
    }

    @Bind(R.id.id_register)
    TextView register;

    @OnClick(R.id.id_register)
    public void register() {
        toPage(IntentActivity.class);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_net;
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

    private void login(String Name, String Pwd) {
        if (Name.equals("") && Pwd.equals("")) {
            ToastUtil.longT(NetActivity.this, "用户名和密码不能为空!");
        } else if (Name.equals("")) {
            ToastUtil.longT(NetActivity.this, "用户名不能为空!");
        } else if (Pwd.equals("")) {
            ToastUtil.longT(NetActivity.this, "密码不能为空!");
        } else {
            String url = HttpConfig.REQUEST_URL + "/User/UserLogin";
            // 创建请求参数
            RequestParams params = new RequestParams();
            params.put("Name", Name);
            params.put("Pwd", Pwd);
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
                        ToastUtil.longT(NetActivity.this, "登录成功!");
                        MApplication.login = true;
                        toPage(FristActivity.class);
                    } else if (s.equals("2")) {
                        ToastUtil.longT(NetActivity.this, m);
                    }
                    //解析JSON数据
                    try {
                        JSONObject object = new JSONObject(d);
                        String U_Id = object.getString("U_Id");
                        String U_Name = object.getString("U_Name");
                        String U_Password = object.getString("U_Password");
                        String U_Right = object.getString("U_Right");
                        String U_UserGroupId = object.getString("U_UserGroupId");
                        String U_Email = object.getString("U_Email");
                        String U_Phone = object.getString("U_Phone");
                        String U_NickName = object.getString("U_NickName");
                        String SI_Id = object.getString("SI_Id");
                        String U_Avatar = object.getString("U_Avatar");
                        String SD_Id = object.getString("SD_Id");

                        LogUtil.d("tag", U_Id + "-------" + U_Name + "-----" + U_Password + "-----------" + U_NickName);

                        SharedPreferences sp = getSharedPreferences("file", MODE_PRIVATE);
                        Editor editor = sp.edit();
                        editor.putString("U_Id", U_Id);
                        editor.putString("U_Name", U_Name);
                        editor.putString("U_Password", U_Password);
                        editor.putString("U_Right", U_Right);
                        editor.putString("U_UserGroupId", U_UserGroupId);
                        editor.putString("U_Email", U_Email);
                        editor.putString("U_Phone", U_Phone);
                        editor.putString("U_NickName", U_NickName);
                        editor.putString("SI_Id", SI_Id);
                        editor.putString("U_Avatar", U_Avatar);
                        editor.putString("SD_Id", SD_Id);
                        editor.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
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
}
