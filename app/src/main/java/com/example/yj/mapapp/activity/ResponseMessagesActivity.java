package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.test.ClassPathResource;
import com.example.yj.mapapp.test.IsChinese;
import com.example.yj.mapapp.util.AppManager;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.ExitDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 响应信息
 */
public class ResponseMessagesActivity extends BaseActivity {

    private String contacts;
    private String phone;
    private String content;
    private int sd_id;
    private String U_Id;
    private int user_id;
    private SharedPreferences spf;

    @Bind(R.id.id_back)
    ImageView back;

    @Bind(R.id.id_response_title)
    EditText response_title;

    @Bind(R.id.id_response_type)
    EditText response_type;

    @Bind(R.id.id_response_contacts)
    TextView response_contacts;

    @Bind(R.id.id_response_phone)
    TextView response_phone;

    @Bind(R.id.id_response_content)
    TextView response_content;

    @Bind(R.id.id_now_response)
    Button response;


    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

    @OnClick(R.id.id_now_response)
    public void response() {
        final ExitDialog myDialog = new ExitDialog(ResponseMessagesActivity.this,
                getText(R.string.are_you_sure_response).toString(), getText(R.string.input_search_logout_cancle).toString(), getText(R.string.input_search_logout_ok).toString());
        myDialog.show();
        myDialog.setDialogROnClickListener(new ExitDialog.MyDialogROnClickListener() {

            @Override
            public void onClick(View v) {
                spf = getSharedPreferences("file", MODE_PRIVATE);
                U_Id = spf.getString("U_Id", null);
                user_id = Integer.valueOf(U_Id);
                System.out.println("user_id==============" + user_id);

                contacts = response_contacts.getText().toString().trim();
                LogUtil.d("contacts:===============" + contacts);
                phone = response_phone.getText().toString().trim();
                LogUtil.d("phone:===============" + phone);
                content = response_content.getText().toString().trim();
                LogUtil.d("content:===============" + content);

                response(sd_id, contacts, phone, content, user_id);
                myDialog.dismiss();
            }
        });
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_response_messages;
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

        sd_id = this.getIntent().getIntExtra("sd_id", -1);
        String name = this.getIntent().getStringExtra("name");
        String type = this.getIntent().getStringExtra("type");

        LogUtil.d("sd_id===============" + sd_id);
        LogUtil.d("name===============" + name);
        LogUtil.d("type===============" + type);

        response_title.setText(name);
        response_type.setText(type);
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

    private void response(int sd_id, String contacts, String phone, String content, int user_id) {
        if (contacts.equals("") || phone.equals("") || content.equals("")) {
            ToastUtil.shortT(ResponseMessagesActivity.this, "所有信息不能为空!");
        } else if (contacts.equals("")) {
            ToastUtil.shortT(ResponseMessagesActivity.this, "联系人不能为空!");
        } else if (phone.equals("")) {
            ToastUtil.shortT(ResponseMessagesActivity.this, "联系电话不能为空!");
        } else if (content.equals("")) {
            ToastUtil.shortT(ResponseMessagesActivity.this, "响应详情不能为空!");
        } else if (ClassPathResource.isMobileNO(phone) == false) {
            ToastUtil.shortT(ResponseMessagesActivity.this, "手机号码输入错误!");
        } else if (IsChinese.checkNameChese(contacts) == false || (4 < contacts.length() || contacts.length() < 2)) {
            ToastUtil.shortT(ResponseMessagesActivity.this, "联系人请输入2~4个汉字!");
        } else {
            String url = HttpConfig.REQUEST_URL + "/Response/AddResponse";
            // 创建请求参数
            RequestParams params = new RequestParams();
            params.put("sdId", sd_id);
            params.put("Contacts", contacts);
            params.put("Phone", phone);
            params.put("Content", content);
            params.put("USER_ID", user_id);

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
                    //判断响应状态
                    if (s.equals("1")) {
                        ToastUtil.longT(ResponseMessagesActivity.this, "响应成功!");
                        LogUtil.d("响应成功!===========");
                    } else if (s.equals("2")) {
                        ToastUtil.longT(ResponseMessagesActivity.this, "响应失败!");
                        LogUtil.d("响应失败!===========");
                    }
                    //界面跳转
                    toPage(TradeLeadsActivity.class);
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
}
