package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.view.View;
import android.view.WindowManager;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.base.MApplication;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 启动页
 */
public class MainActivity extends BaseActivity implements Runnable {

    private int res[] = {R.mipmap.main, R.mipmap.main, R.mipmap.main};
    private Handler handler;
    private ImageView imageView;
    private int n;//图片计数器
    private SharedPreferences spf;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
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
        //去标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //通过findViewById方法找到ImageView控件对象
        imageView = (ImageView) findViewById(R.id.id_imageView);
        //创建Handler对象并开启线程
        handler = new Handler();
        handler.post(this);
        //创建SharedPreferences对象并保存数据
        spf = getSharedPreferences("file", MODE_PRIVATE);
        String U_Name = spf.getString("U_Name", "null");
        String U_Password = spf.getString("U_Password", "null");

        System.out.println("U_Name==============" + U_Name);
        System.out.println("U_Password:==============" + U_Password);

        //访问后台接口
        login(U_Name, U_Password);
    }

    /**
     * 登录
     * @param Name
     * @param Pwd
     */
    private void login(String Name, String Pwd) {
        if (Name.equals("") && Pwd.equals("")) {
            LogUtil.d("tag", "用户名和密码不能为空!");
        } else if (Name.equals("")) {
            LogUtil.d("tag", "用户名不能为空!");
        } else if (Pwd.equals("")) {
            LogUtil.d("tag", "密码不能为空!");
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
                        LogUtil.d("tag", "登录成功!");
                        MApplication.login = true;
//                        toPage(FristActivity.class);
                    } else if (s.equals("2")) {
                        LogUtil.d("tag", m);
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
                        SharedPreferences.Editor editor = sp.edit();
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

    /**
     * 图片切换
     */
    @Override
    public void run() {
        if (n < 3) {
            //设置图片资源
            imageView.setImageResource(res[n++]);
            //延迟线程
            handler.postDelayed(this, 1000);
        } else {
            //跳转界面
            startActivity(new Intent(getApplicationContext(),
                    StartActivity.class));
//            startActivity(new Intent(getApplicationContext(),
//                    FristActivity.class));
            //关闭Activity
            finish();
        }
    }

    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {

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

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
}
