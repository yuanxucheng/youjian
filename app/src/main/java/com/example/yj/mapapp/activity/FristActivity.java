package com.example.yj.mapapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.example.yj.mapapp.R;
import com.example.yj.mapapp.banner.ADInfo;
import com.example.yj.mapapp.banner.CycleViewPager;
import com.example.yj.mapapp.banner.ViewFactory;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.base.MApplication;
import com.example.yj.mapapp.location.LocationService;
import com.example.yj.mapapp.model.Banner;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.net.handler.HttpUtil;
import com.example.yj.mapapp.net.handler.ResponseHandler;
import com.example.yj.mapapp.util.AppManager;
import com.example.yj.mapapp.util.DipUtil;
import com.example.yj.mapapp.util.JsonParser;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.ExitDialog;
import com.example.yj.mapapp.view.MPopwindow;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主界面
 */
public class FristActivity extends BaseActivity {

    private final static String tag = "FristActivity-->";
    //ImageView视图对象集合
    private List<ImageView> views = new ArrayList<ImageView>();
    //广告信息对象集合
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    //轮播对象
    private CycleViewPager cycleViewPager;
    //泡泡窗口对象
    private MPopwindow popwindow;
    //定位服务对象
    private LocationService locationService;
    //对话框对象
    private AlertDialog noticeDialog;
    //是否是第一次运行/启动
    private boolean isFirstRun;

//    private String[] imageUrls = {"http://img3.imgtn.bdimg.com/it/u=2329946235,696526590&fm=21&gp=0.jpg",
//            "http://img4.imgtn.bdimg.com/it/u=1233057038,2443583978&fm=21&gp=0.jpg",
//            "http://img4.imgtn.bdimg.com/it/u=3938755445,2824571223&fm=21&gp=0.jpg",
//            "http://img3.imgtn.bdimg.com/it/u=2037590579,3347768072&fm=21&gp=0.jpg",
//            "http://img1.imgtn.bdimg.com/it/u=2460458394,395581867&fm=21&gp=0.jpg"};

    private String imageUrls = "http://api.51buyjc.com/Content/UploadImage/icon_home_bg.png";

    //    @Bind(R.id.frist_btn_release_center)
//    Button btn_release_center;
//    @Bind(R.id.frist_btn_response_center)
//    Button btn_response_center;
    @Bind(R.id.id_first_personal_center)
    ImageView personal_center;

    @Bind(R.id.frist_btn_build_site)
    Button frist_btn_build_site;

    @Bind(R.id.frist_btn_build_enterprise)
    Button frist_btn_build_enterprise;

    @Bind(R.id.id_home_glqy)
    Button frist_btn_glqy;

    @Bind(R.id.id_home_wjcs)
    Button frist_btn_wjcs;

    @Bind(R.id.id_home_lwzj)
    Button frist_btn_lwzj;

    @Bind(R.id.frist_btn_gqxx)
    Button frist_btn_gqxx;

    @Bind(R.id.frist_btn_enterprise_certification)
    Button frist_btn_enterprise_certification;

    @Bind(R.id.frist_btn_legal_advice)
    Button frist_btn_legal_advice;

    @Bind(R.id.frist_btn_financing_loan)
    Button frist_btn_financing_loan;

    @Bind(R.id.id_home_esxx)
    Button secondHandInformation;

    @Bind(R.id.id_frist_menu)
    ImageView frist_iv_menu;

    @Bind(R.id.id_current_city)
    TextView current_city;

    @Bind(R.id.id_first_sweep)
    ImageView first_sweep;

    @Bind(R.id.id_first_one_hundreds)
    ImageView first_one_hundreds;

    @OnClick(R.id.id_frist_menu)
    public void menu(View v) {
        popwindow.showPopupWindow(v);
    }

    @OnClick(R.id.id_home_glqy)
    public void relatedCompanies() {
        toPage(RelatedCompaniesActivity.class);
    }

    @OnClick(R.id.id_home_wjcs)
    public void HardwareSupermarket() {
        //跳转界面
        toPage(HardSuperActivity.class);
//        toPage(UnderConstructionActivity.class);
    }

    @OnClick(R.id.id_home_lwzj)
    public void labourAgency() {
        toPage(LabourAgencyActivity.class);
    }

    @OnClick(R.id.id_current_city)
    public void currentCity() {
//        toPage(UnderConstructionActivity.class);
    }

    @OnClick(R.id.id_home_esxx)
    public void secondHandInformation() {
        toPage(UnderConstructionActivity.class);
    }

    @OnClick(R.id.frist_btn_enterprise_certification)
    public void enterpriseCertification() {
        toPage(UnderConstructionActivity.class);
    }

    @OnClick(R.id.frist_btn_legal_advice)
    public void legalAdvice() {
        toPage(LegalAdviceActivity.class);
    }

    @OnClick(R.id.frist_btn_financing_loan)
    public void financingLoan() {
        toPage(FinancingLoanActivity.class);
    }

    @OnClick(R.id.frist_btn_build_site)
    public void buildSite() {
        toPage(BuildSitesActivity.class);
    }

    @OnClick(R.id.frist_btn_build_enterprise)
    public void buildEnterprise() {
        toPage(BuildEnterprisesActivity.class);
    }

//    //发布中心
//    @OnClick(R.id.frist_btn_release_center)
//    public void releaseCenter(View view) {
//        //显示泡泡窗口
//        popwindow.showPopupWindowReleaseCenter(view);
//    }

    //响应中心
//    @OnClick(R.id.frist_btn_response_center)
//    public void responseCenter(View view) {
//        //显示泡泡窗口
//        popwindow.showPopupWindowResponseCenter(view);
//    }

    //扫一扫
    @OnClick(R.id.id_first_sweep)
    public void sweep(View view) {
        //跳转到指定界面
        Intent intent = new Intent();
        intent.setClass(FristActivity.this, MipcaActivityCapture.class);
        startActivity(intent);
    }

    @OnClick(R.id.id_first_one_hundreds)
    public void one_hundreds() {
        if (MApplication.login == true) {//判断是否是登录状态
            toPage(TradeLeadsActivity.class);
        } else {
            toPage(NetActivity.class);
        }
    }

    //个人中心
    @OnClick(R.id.id_first_personal_center)
    public void personal_center(View view) {
//        toPage(LoginActivity.class);

        if (MApplication.login == true) {
            toPage(PersonalCenterActivity.class);
        } else {
            toPage(NetActivity.class);
        }
    }

//    @OnClick(R.id.frist_btn_gqxx)
//    public void gqxx() {
//        if (MApplication.login == true) {
//            toPage(TradeLeadsActivity.class);
//        } else {
//            toPage(NetActivity.class);
//        }
//    }

    @Override
    public int bindLayout() {
        return R.layout.activity_frist;
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

        // 网络连接判断
        if (!MApplication.getInstance().isNetworkConnected())
            ToastUtil.shortT(this, getResources().getString(R.string.network_not_connected));

//        getVersionMessage();

        isFrist();

        current_city.setMovementMethod(ScrollingMovementMethod.getInstance());

        HttpUtil.homeCarousel(homeCarouselHandler);

        configImageLoader();

        //创建泡泡窗口对象
        popwindow = new MPopwindow(this);

    }

    /**
     * 判断是否第一次执行
     */
    private void isFrist() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun) {
            Log.d("debug", "第一次运行");
            editor.putBoolean("isFirstRun", false);
            editor.commit();
            getVersionMessage();
        } else {
            Log.d("debug", "不是第一次运行");
            getVersionMessage();
        }
    }

    /**
     * 显示版本更新通知对话框
     */
    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("软件版本更新");
        builder.setMessage("是否更新应用版本?");
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.51buyjc.com/Download_ios_android.html");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 读取SharedPreferences数据内容
     *
     * @return
     */
    private String readVersionMessage() {
        SharedPreferences sp = this.getSharedPreferences("version", MODE_PRIVATE);
        String oldVersionName = sp.getString("versionName", "");
        LogUtil.d("oldVersionName=" + oldVersionName);
        return oldVersionName;
    }

    //获取当前版本号
    private String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo("com.example.yj.mapapp", 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取版本信息
     */
    private void getVersionMessage() {
        HTTPTool.getClient().post(HttpConfig.VERSION_NUMBER_URL, null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    BufferedReader r = new BufferedReader(new StringReader(new String(responseBody)));
                    String line = null;
                    while ((line = r.readLine()) != null) {
                        if (line.contains("window.AppInfoData={")) {
                            String content = line.trim().replace("window.AppInfoData=", "");
                            content = content.substring(0, content.length() - 1);
                            LogUtil.d("content=" + content);
                            JSONObject jsonObject = new JSONObject(content);
                            JSONObject appDetail = jsonObject.getJSONObject("appDetail");
                            String versionName = appDetail.getString("versionName");
                            String versionCode = appDetail.getString("versionCode");
                            String packageName = appDetail.getString("packageName");
                            String appId = appDetail.getString("appId");
                            String appName = appDetail.getString("appName");
                            String categoryId = appDetail.getString("categoryId");
                            String categoryName = appDetail.getString("categoryName");
                            String author = appDetail.getString("author");
                            String apkUrl = appDetail.getString("apkUrl");
                            String publishTime = appDetail.getString("publishTime");
                            String downCount = appDetail.getString("downCount");
                            JSONObject object = new JSONObject(downCount);
                            String bytes = object.getString("bytes");
                            String desc = object.getString("desc");
                            LogUtil.d("versionName=" + versionName + "versionCode=" + versionCode + "packageName=" + packageName + "appId=" + appId + "appName=" + appName + "categoryId=" + categoryId + "categoryName=" + categoryName + "author=" + author + "publishTime=" + publishTime + "apkUrl=" + apkUrl + "downCount=" + downCount);

//                            if (!isFirstRun) {
//                                if (!readVersionMessage().equals(versionName)) {
//                                    showNoticeDialog();
//                                }
//                            }

                            if (!isFirstRun) {
                                LogUtil.d("tag", "versionName:===============" + getAppVersionName(FristActivity.this));
                                if (!getAppVersionName(FristActivity.this).equals(versionName)) {
                                    showNoticeDialog();
                                }
                            }

                            SharedPreferences sp = getSharedPreferences("version", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("versionName", versionName);
                            editor.putString("versionCode", versionCode);
                            editor.putString("packageName", packageName);
                            editor.putString("appId", appId);
                            editor.putString("appName", appName);
                            editor.putString("categoryId", categoryId);
                            editor.putString("categoryName", categoryName);
                            editor.putString("author", author);
                            editor.putString("apkUrl", apkUrl);
                            editor.putString("publishTime", publishTime);
                            editor.putString("bytes", bytes);
                            editor.putString("desc", desc);
                            editor.commit();
                        }
                    }
                    r.close();
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

    /**
     * 显示请求字符串
     * 将百度返回的定位信息进行处理(获取城市和当前地址)
     */
    public void logMsg(String str) {
        try {
            if (current_city != null) {
                current_city.setText(str);
            }
            System.out.println("str:" + str + "===========");
            String[] ss = str.split("\n");
            for (String s : ss) {
                if (s.contains("city") && !s.contains("code")) {
                    String[] list = s.split(":");
                    String city = list[1];
                    //当前定位城市
                    current_city.setText(city);
                    System.out.println("city:" + city + "===========");
                } else if (s.contains("Poi")) {
                    String[] list = s.split(":");
                    String st = list[1];
                    String[] string = st.split(";");
                    String locAddress = string[0];
                    //设置当前所在地址
//                    load_textview.setText(locAddress);
                    System.out.println("locAddress:" + locAddress + "===========");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    /**
     * Start location service
     */
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        locationService = ((MApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 开始定位
        current_city.setVisibility(View.VISIBLE);
    }


    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                logMsg(sb.toString());
            }
        }
    };

    private ResponseHandler homeCarouselHandler = new ResponseHandler() {
        @Override
        public void onCacheData(String content) {
            LogUtil.w("========onCacheData=========" + content);
        }

        @Override
        public void success(String data, String resCode, String info) {
            if (resCode.equals("")) {
                LogUtil.d("data===================" + data);
                LogUtil.d("获取首页图片轮播接口,成功==============");

                //后台返回的数据进行过处理的,使用GSON解析
                List<Banner> banner = JsonParser.deserializeFromJson(data, new TypeToken<List<Banner>>() {
                }.getType());
                for (Banner b : banner) {
                    String imaUrl = b.getImgUrl();
                    LogUtil.d("tag", "imgUrl========" + imaUrl);
                    initialize(imaUrl);
                }
            } else {
                ToastUtil.shortT(FristActivity.this, getText(R.string.first_homeCarouse_fail).toString());
//                LogUtil.d("==============获取首页图片轮播接口,失败");
            }
        }

        @Override
        public void onFail(int arg0, String arg2, Throwable arg3) {
//            ToastUtil.shortT(FristActivity.this, getText(R.string.first_homeCarouse_fail).toString());
        }
    };

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }

//    //屏蔽返回键
//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//    }

    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            final ExitDialog myDialog = new ExitDialog(FristActivity.this,
                    getText(R.string.input_search_logout_title).toString(), getText(R.string.input_search_logout_cancle).toString(), getText(R.string.input_search_logout_ok).toString());
            myDialog.show();
            myDialog.setDialogROnClickListener(new ExitDialog.MyDialogROnClickListener() {

                @Override
                public void onClick(View v) {
                    AppManager.getInstance().AppExit(FristActivity.this);
                    myDialog.dismiss();
                }
            });
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化轮播图
     *
     * @param imaUrl
     */
    private void initialize(String imaUrl) {
        //获取CycleViewPager对象
        cycleViewPager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);

//        for (int i = 0; i < imageUrls.length; i++) {
//            ADInfo info = new ADInfo();
//            info.setUrl(imageUrls[i]);
//            info.setContent("图片-->" + i);
//            infos.add(info);
//        }

//        String imageUrls = HttpConfig.BANNER_URL + "/Content/UploadImage/icon_home_bg.png";

        String imageUrls = HttpConfig.FRIST_BANNER_URL + imaUrl;

        LogUtil.d(imageUrls + "============imageUrls");

        ADInfo info = new ADInfo();
        info.setUrl(imageUrls);
        info.setContent("图片-->");
        infos.add(info);

        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getUrl()));
        //循环添加ImageView
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));
        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);
        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);
        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    /**
     * 轮播图点击事件监听
     */
    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
//                Toast.makeText(FristActivity.this,
//                        "position-->" + info.getContent(), Toast.LENGTH_SHORT)
//                        .show();
                LogUtil.d(tag, "click picture!!");
            }
        }

    };

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//				 .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }
}
