package com.example.yj.mapapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.back)
    Button back;

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @Bind(R.id.id_register)
    TextView register;

    @OnClick(R.id.id_register)
    public void register() {
//        toPage(CityListActivity.class);
        //界面跳转
        toPage(IntentActivity.class);
//        toPage(RegisterActivity.class);
//        toPage(UnderConstructionActivity.class);
    }

    @Bind(R.id.id_login)
    TextView login;

    @OnClick(R.id.id_login)
    public void login() {
        ToastUtil.longT(this, "建设中,敬请期待...");
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
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

}
