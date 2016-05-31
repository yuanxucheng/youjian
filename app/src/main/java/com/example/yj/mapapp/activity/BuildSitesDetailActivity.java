package com.example.yj.mapapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.net.handler.HttpUtil;
import com.example.yj.mapapp.net.handler.ResponseHandler;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuildSitesDetailActivity extends BaseActivity {

    private final static String tag = "BuildSitesDetailActivity-->";

    @Bind(R.id.id_back)
    ImageView back;

    @Bind(R.id.tv_build_site_projectName)
    TextView build_site_projectName;

    @Bind(R.id.tv_build_site_CUName)
    TextView build_site_CUName;

    @Bind(R.id.tv_build_site_contacts)
    TextView build_site_contacts;

    @Bind(R.id.tv_build_site_startTime)
    TextView build_site_startTime;

    @Bind(R.id.tv_build_site_endTime)
    TextView build_site_endTime;

    @Bind(R.id.tv_build_site_address)
    TextView build_site_address;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_build_sites_detail;
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

        //界面跳转带过来的数据
        int buildSites_id = getIntent().getIntExtra("buildSites_id", 0);
        LogUtil.d(tag, "buildSites_id=========" + buildSites_id);

        HttpUtil.specifySitesInformation(buildSites_id, specifyBuildersInformationHandler);
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

    private ResponseHandler specifyBuildersInformationHandler = new ResponseHandler() {
        @Override
        public void onCacheData(String content) {
            LogUtil.w("========onCacheData=========" + content);
        }

        @Override
        public void success(String data, String resCode, String info) {
            if (resCode.equals("")) {
                LogUtil.d("data===================" + data);
                LogUtil.d("获取指定承建信息接口,成功==============");

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int CU_Id = jsonObject.optInt("CU_Id");
                    String CU_ProjectName = jsonObject.optString("CU_ProjectName");
                    String CU_CUName = jsonObject.optString("CU_CUName");
                    String CU_Contacts = jsonObject.optString("CU_Contacts");
                    String CU_StartTime = jsonObject.optString("CU_StartTime");
                    String CU_EndTime = jsonObject.optString("CU_EndTime");
                    String CU_Address = jsonObject.optString("CU_Address");

                    LogUtil.d(tag, "CU_Id============" + CU_Id);

                    build_site_projectName.setText(getString(R.string.buildSites_projectName) + CU_ProjectName);
                    build_site_CUName.setText(getString(R.string.buildSites_CUName) + CU_CUName);
                    build_site_contacts.setText(getString(R.string.buildSites_contacts) + CU_Contacts);
                    build_site_startTime.setText(getString(R.string.buildSites_startTime) + CU_StartTime);
                    build_site_endTime.setText(getString(R.string.buildSites_endTime) + CU_EndTime);
                    build_site_address.setText(getString(R.string.buildSites_address) + CU_Address);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
//                ToastUtil.shortT(BuildSitesDetailActivity.this, getText(R.string.buildSites_fail).toString());
                LogUtil.d("==============获取指定承建信息接口,失败");
            }
        }

        @Override
        public void onFail(int arg0, String arg2, Throwable arg3) {
            ToastUtil.shortT(BuildSitesDetailActivity.this, getText(R.string.buildSites_fail).toString());
        }
    };

    /**
     * JSON
     *
     * @param data
     */
    private void ParserJson(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            int SI_Id = jsonObject.optInt("SI_Id");
            String imagesURL = jsonObject.optString("imagesURL");
            String SI_CompanyName = jsonObject.optString("SI_CompanyName");
            String SI_Address = jsonObject.optString("SI_Address");
            String SI_Contacts = jsonObject.optString("SI_Contacts");
            String SI_Phone = jsonObject.optString("SI_Phone");
            int SI_YJAuthentication = jsonObject.optInt("SI_YJAuthentication");

            LogUtil.d("tag", "SI_id:" + SI_Id + "========ImagesURL:" + imagesURL
                    + "==========SI_CompanyName:" + SI_CompanyName + "==========SI_Address:"
                    + SI_Address + "==========SI_Contacts" + SI_Contacts
                    + "===========SI_Phone" + SI_Phone + "==========SI_YJAuthentication" + SI_YJAuthentication);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
