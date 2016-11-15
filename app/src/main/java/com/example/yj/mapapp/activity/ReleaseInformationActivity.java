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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.base.MApplication;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.test.ClassPathResource;
import com.example.yj.mapapp.test.IsChinese;
import com.example.yj.mapapp.test.isChineseCharacters;
import com.example.yj.mapapp.util.AppManager;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.ExitDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布信息
 */
public class ReleaseInformationActivity extends BaseActivity {
    private SharedPreferences spf;
    private String U_Id;
    private int sd_id;

    private String title;//标题
    private String contacts;//联系人
    private String phone;//电话
    private String content;//内容
    private String address;//地址
    private Boolean type;//类别
    private int category;//类型

    @Bind(R.id.id_release_information_title)
    EditText release_information_title;

    @Bind(R.id.id_release_information_contacts)
    EditText release_information_contacts;

    @Bind(R.id.id_release_information_contacts_phone)
    EditText release_information_contacts_phone;

    @Bind(R.id.id_release_information_content)
    EditText release_information_content;

    @Bind(R.id.id_release_information_address)
    EditText release_information_address;

    @Bind(R.id.id_supply_information)
    Button supply_information;

    @Bind(R.id.id_back)
    ImageView back;

    @Bind(R.id.id_buying_leads)
    Button buying_leads;

    @Bind(R.id.id_release_material)
    Button material;

    @Bind(R.id.id_release_appointed)
    Button appointed;

    @Bind(R.id.id_release_vacancy)
    Button vacancy;

    @Bind(R.id.id_now_release)
    Button release;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

    @OnClick(R.id.id_supply_information)
    public void supply_information() {
        type = true;
        supply_information.setBackgroundResource(R.color.white);
        buying_leads.setBackgroundResource(R.color.grays);
    }

    @OnClick(R.id.id_buying_leads)
    public void buying_leads() {
        type = false;
        supply_information.setBackgroundResource(R.color.grays);
        buying_leads.setBackgroundResource(R.color.white);
    }

    @OnClick(R.id.id_release_material)
    public void material() {
        category = 26;
        material.setBackgroundResource(R.color.white);
        vacancy.setBackgroundResource(R.color.grays);
        appointed.setBackgroundResource(R.color.grays);
    }

    @OnClick(R.id.id_release_appointed)
    public void appointed() {
        category = 27;
        material.setBackgroundResource(R.color.grays);
        vacancy.setBackgroundResource(R.color.grays);
        appointed.setBackgroundResource(R.color.white);
    }

    @OnClick(R.id.id_release_vacancy)
    public void vacancy() {
        category = 28;
        material.setBackgroundResource(R.color.grays);
        vacancy.setBackgroundResource(R.color.white);
        appointed.setBackgroundResource(R.color.grays);
    }

    @OnClick(R.id.id_now_release)
    public void release() {
        if (MApplication.getInstance().isNetworkConnected()) {
            final ExitDialog myDialog = new ExitDialog(ReleaseInformationActivity.this,
                    getText(R.string.are_you_sure_release).toString(), getText(R.string.input_search_logout_cancle).toString(), getText(R.string.input_search_logout_ok).toString());
            myDialog.show();
            myDialog.setDialogROnClickListener(new ExitDialog.MyDialogROnClickListener() {

                @Override
                public void onClick(View v) {
                    spf = getSharedPreferences("file", MODE_PRIVATE);
                    U_Id = spf.getString("U_Id", null);
                    sd_id = Integer.valueOf(U_Id);
                    System.out.println("id==============" + sd_id);

                    title = release_information_title.getText().toString().trim();
                    LogUtil.d("title:===============" + title);
                    contacts = release_information_contacts.getText().toString().trim();
                    LogUtil.d("contacts:===============" + contacts);
                    phone = release_information_contacts_phone.getText().toString().trim();
                    LogUtil.d("phone:===============" + phone);
                    content = release_information_content.getText().toString().trim();
                    LogUtil.d("content:===============" + content);
                    address = release_information_address.getText().toString().trim();
                    LogUtil.d("address:===============" + address);

                    release(title, category, type, contacts, phone, content, address, sd_id);
                    myDialog.dismiss();
                }
            });
        } else {
            ToastUtil.shortT(this, getResources().getString(R.string.network_not_connected));
            return;
        }

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_release_information;
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
        category = 26;
        type = true;

        release_information_contacts_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
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

    /**
     * 发布
     *
     * @param title
     * @param category
     * @param type
     * @param contacts
     * @param phone
     * @param content
     * @param address
     * @param user_id
     */
    private void release(String title, int category, Boolean type, String contacts, String phone, String content, String address, int user_id) {
        if (title.equals("") || content.equals("") || address.equals("") || contacts.equals("") || phone.equals("")) {
            ToastUtil.shortT(ReleaseInformationActivity.this, "所有信息不能为空!");
        } else if (title.equals("")) {
            ToastUtil.shortT(ReleaseInformationActivity.this, "信息标题不能为空!");
        } else if (contacts.equals("")) {
            ToastUtil.shortT(ReleaseInformationActivity.this, "联系人不能为空!");
        } else if (phone.equals("")) {
            ToastUtil.shortT(ReleaseInformationActivity.this, "联系电话不能为空!");
        } else if (content.equals("")) {
            ToastUtil.shortT(ReleaseInformationActivity.this, "信息详情不能为空!");
        } else if (address.equals("")) {
            ToastUtil.shortT(ReleaseInformationActivity.this, "地址不能为空!");
        } else if (ClassPathResource.isMobileNO(phone) == false) {
            ToastUtil.shortT(ReleaseInformationActivity.this, "手机号码输入错误!");
        } else if (IsChinese.checkNameChese(contacts) == false || (4 < contacts.length() || contacts.length() < 2)) {
            ToastUtil.shortT(ReleaseInformationActivity.this, "联系人请输入2~4个汉字!");
        } else {
            String url = HttpConfig.REQUEST_URL + "/Release/AddRelease";
            // 创建请求参数
            RequestParams params = new RequestParams();
            params.put("NAME", title);
            params.put("TYPE", category);
            params.put("SUPPLY_OR_DEMAND", type);
            params.put("CONTENT", content);
            params.put("ADDRESS", address);
            params.put("CONTACTS", contacts);
            params.put("PHONE", phone);
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
                    //判断发布状态
                    if (s.equals("1")) {
                        ToastUtil.longT(ReleaseInformationActivity.this, "发布成功!");
                    } else if (s.equals("2")) {
                        ToastUtil.longT(ReleaseInformationActivity.this, "发布失败!");
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
