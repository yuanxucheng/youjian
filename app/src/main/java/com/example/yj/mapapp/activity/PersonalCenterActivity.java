package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.base.MApplication;
import com.example.yj.mapapp.util.AppManager;
import com.example.yj.mapapp.util.DialogUtils;
import com.example.yj.mapapp.view.ExitDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人中心
 */
public class PersonalCenterActivity extends BaseActivity {

    private SharedPreferences spf;
    private String U_Id;
    private Integer id;

    @Bind(R.id.id_my_release_message)
    TextView release_message;

    @OnClick(R.id.id_my_release_message)
    public void releaseMessage(View v) {
//        toPage(UnderConstructionActivity.class);
        toPage(MyTradeLeadsActivity.class);
    }

    @Bind(R.id.id_my_respone_message)
    TextView respone_message;

    @OnClick(R.id.id_my_respone_message)
    public void responeMessage(View v) {
//        toPage(UnderConstructionActivity.class);
        toPage(MyResponseActivity.class);
    }

    @Bind(R.id.id_logged_on)
    Button logged_on;

    @OnClick(R.id.id_logged_on)
    public void logged_on(View v) {
        final ExitDialog myDialog = new ExitDialog(PersonalCenterActivity.this,
                getText(R.string.exit_logout_title).toString(), getText(R.string.input_search_logout_cancle).toString(), getText(R.string.input_search_logout_ok).toString());
        myDialog.show();
        myDialog.setDialogROnClickListener(new ExitDialog.MyDialogROnClickListener() {

            @Override
            public void onClick(View v) {
                //不是登录状态
                MApplication.login = false;
                //创建删除方法
                spf.edit().clear().commit();
                //界面跳转
                toPage(FristActivity.class);
                myDialog.dismiss();
            }
        });
    }

    @Bind(R.id.id_back)
    ImageView back;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

    @Bind(R.id.id_user)
    TextView user;

    @Bind(R.id.id_contact)
    TextView contact;

    @Bind(R.id.id_phone)
    TextView phone;

    @Override
    public int bindLayout() {
        return R.layout.activity_personal_center;
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
        initData();
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
     * 初始化数据
     */
    private void initData() {
        spf = getSharedPreferences("file", MODE_PRIVATE);
        String U_Name = spf.getString("U_Name", "null");
        String U_NickName = spf.getString("U_NickName", "null");
        String U_Phone = spf.getString("U_Phone", "null");

        U_Id = spf.getString("U_Id", null);
        id = Integer.valueOf(U_Id);
        System.out.println("id==============" + id);

        System.out.println("U_Name==============" + U_Name);
        System.out.println("U_NickName:===============" + U_NickName);
        System.out.println("U_Phone:==============" + U_Phone);

        user.setText(U_Name);
        contact.setText(U_NickName);
        phone.setText(U_Phone + "");
    }
}
