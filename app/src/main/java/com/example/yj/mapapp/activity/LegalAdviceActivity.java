package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LegalAdviceActivity extends BaseActivity {

    @Bind(R.id.id_back)
    ImageView back;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

//    @Bind(R.id.call_phone)
//    ImageView call_phone;
//
//    @Bind(R.id.phone_number)
//    TextView phone_number;
//
//    @OnClick(R.id.call_phone)
//    public void call_phone(View v) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_CALL);//指定意图动作
//        intent.setData(Uri.parse("tel:" + phone_number.getText().toString()));//指定电话号码
//        startActivity(intent);
//    }

    @Override
    public int bindLayout() {
        return R.layout.activity_legal_advice;
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
