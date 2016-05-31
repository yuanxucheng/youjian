package com.example.yj.mapapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.net.handler.ResponseHandler;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.id_login)
    TextView login;

    @OnClick(R.id.id_login)
    public void login() {
        toPage(LoginActivity.class);
    }

    @Bind(R.id.id_finish_register)
    TextView finish_register;

    @OnClick(R.id.id_finish_register)
    public void finishRegister() {
        ToastUtil.longT(this, "建设中,敬请期待...");
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_register;
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

//    private boolean check() {
//        if (CheckUtil.isEmpty(input_tel.getText().toString())) {
//            ToastUtil.shortT(RegisterActivity.this, getText(R.string.name_is_null).toString());
//            return false;
//        }
//        if (CheckUtil.isEmpty(input_pwd.getText().toString())) {
//            ToastUtil.shortT(RegisterActivity.this, getText(R.string.pwd_is_null).toString());
//            return false;
//        }
//        if (CheckUtil.isEmpty(re_input_pwd.getText().toString())) {
//            ToastUtil.shortT(RegisterActivity.this, getText(R.string.repwd_is_null).toString());
//            return false;
//        }
//        if (!(re_input_pwd.getText().toString()).equals(input_pwd.getText().toString())) {
//            ToastUtil.shortT(RegisterActivity.this, getText(R.string.two_pwd_is_not_same).toString());
//            return false;
//        }
//        if (CheckUtil.isEmpty(input_code.getText().toString())) {
//            ToastUtil.shortT(RegisterActivity.this, getText(R.string.code_is_null).toString());
//            return false;
//        }
//        return true;
//    }

    private ResponseHandler registerHandler = new ResponseHandler() {
        @Override
        public void onCacheData(String content) {
            LogUtil.w("========onCacheData=========" + content);
        }

        @Override
        public void success(String data, String resCode, String info) {
            LogUtil.d("resCode========" + resCode + "data========" + data + "info======" + info);
            if (resCode.equals("")) {
                ToastUtil.shortT(RegisterActivity.this, getText(R.string.register_sucess).toString());
            } else {
                ToastUtil.shortT(RegisterActivity.this, getText(R.string.register_fail).toString());
            }
        }

        @Override
        public void onFail(int arg0, String arg2, Throwable arg3) {
            ToastUtil.shortT(RegisterActivity.this, getText(R.string.register_fail).toString());
        }
    };
}
