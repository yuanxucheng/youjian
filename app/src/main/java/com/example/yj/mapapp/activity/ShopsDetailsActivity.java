package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.util.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopsDetailsActivity extends BaseActivity {

    private final static String tag = "ShopsDetailsActivity-->";

    @Bind(R.id.id_back)
    ImageView back;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

    @Bind(R.id.tv_build_enterprise_companyName)
    TextView build_enterprise_companyName;

    @Bind(R.id.tv_build_enterprise_address)
    TextView build_enterprise_address;

    @Bind(R.id.tv_build_enterprise_contacts)
    TextView build_enterprise_contacts;

    @Bind(R.id.tv_build_enterprise_phone)
    TextView build_enterprise_phone;

    @Bind(R.id.id_buildEnterprise_isShowPhone)
    TextView buildEnterprise_isShowPhone;

    @Override
    public int bindLayout() {
        return R.layout.activity_shops_details;
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

        String SI_CompanyName = getIntent().getStringExtra("SI_CompanyName");
        String SI_Address = getIntent().getStringExtra("SI_Address");
        String SI_Contacts = getIntent().getStringExtra("SI_Contacts");
        String SI_Phone = getIntent().getStringExtra("SI_Phone");

        build_enterprise_companyName.setText(getString(R.string.buildEnterprises_companyName) + SI_CompanyName);
        build_enterprise_address.setText(getString(R.string.buildEnterprises_address) + SI_Address);
        build_enterprise_contacts.setText(getString(R.string.buildEnterprises_contacts) + SI_Contacts);
        build_enterprise_phone.setText(SI_Phone);

        buildEnterprise_isShowPhone.setVisibility(View.VISIBLE);
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
