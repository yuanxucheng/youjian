package com.example.yj.mapapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.net.handler.HttpUtil;
import com.example.yj.mapapp.net.handler.ResponseHandler;
import com.example.yj.mapapp.util.AppManager;
import com.example.yj.mapapp.util.JsonParser;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.ExitDialog;
import com.example.yj.mapapp.view.MPopwindow;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.BufferedReader;
import java.io.IOException;
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
    //轮播图集合
    private List<String> imageUrls;

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

    @Bind(R.id.id_frist_btn_add)
    Button frist_btn_add_module;

    @Bind(R.id.id_frist_menu)
    ImageView frist_iv_menu;

    @Bind(R.id.id_current_city)
    TextView current_city;

    @Bind(R.id.id_first_sweep)
    ImageView first_sweep;

    @Bind(R.id.id_first_one_hundreds)
    ImageView first_one_hundreds;

    @Bind(R.id.id_frist_btn_search)
    Button frist_btn_search;

    @Bind(R.id.find)
    Button find;

    @Bind(R.id.id_webview)
    WebView webView;

    @OnClick(R.id.find)
    public void find() {
        toPage(FindFirmActivity.class);
    }

    @OnClick(R.id.id_frist_btn_search)
    public void search() {
        toPage(FindFirmActivity.class);
//        toPage(SearchFirmActivity.class);
    }

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

    @OnClick(R.id.id_frist_btn_add)
    public void addModule() {
        toPage(AddModuleActivity.class);
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

    @SuppressLint("JavascriptInterface")
    @Override
    public void initView(View view) {
        ButterKnife.bind(this);

        //网络连接判断
        MApplication.getInstance().checkOnPosBtn(this);

        //判断网络是否可用
//        if (!MApplication.getInstance().isNetworkConnected()) {
//            ToastUtil.shortT(this, getResources().getString(R.string.network_not_connected));
//            MApplication.showNoNetWorkDlg(this);
//        }

        //监听GPS是否打开并跳转到设置GPS
        MApplication.initGPS(this);

        //Gps是否可用
//        if (!MApplication.getInstance().isGpsEnable()) {
//            ToastUtil.shortT(this, getResources().getString(R.string.gps_not_connected));
//            //进入GPS设置页面:
//            Intent intent = new Intent();
//            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            try {
//                startActivity(intent);
//            } catch (ActivityNotFoundException ex) {
//                intent.setAction(Settings.ACTION_SETTINGS);
//                try {
//                    startActivity(intent);
//                } catch (Exception e) {
//                }
//            }
//        }

        isFrist();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl("http://android.myapp.com/myapp/detail.htm?apkName=com.example.yj.mapapp");
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
        } else {
            Log.d("debug", "不是第一次运行");
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
                imageUrls = new ArrayList<>();
                for (Banner b : banner) {
                    String imaUrl = b.getImgUrl();
                    LogUtil.d("tag", "imgUrl========" + imaUrl);
                    imageUrls.add(imaUrl);
                }
                LogUtil.d("tag", "imageUrls========" + imageUrls.size() + "==========");
                initialize(imageUrls);
            } else {
                ToastUtil.shortT(FristActivity.this, getText(R.string.first_homeCarouse_fail).toString());
//                LogUtil.d("==============获取首页图片轮播接口,失败");
            }
        }

        @Override
        public void onFail(int arg0, String arg2, Throwable arg3) {
            ToastUtil.shortT(FristActivity.this, getText(R.string.first_homeCarouse_fail).toString());
            LogUtil.d("==============获取首页图片轮播接口,失败");
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
     * @param imageUrls
     */
    private void initialize(List<String> imageUrls) {

        String[] newUrls = new String[imageUrls.size()];//新建一个int类型数组
        for (int i = 0; i < imageUrls.size(); i++) {
            newUrls[i] = HttpConfig.FRIST_BANNER_URL + imageUrls.get(i);
        }
        LogUtil.d("tag", "newUrls========" + newUrls.length + "==========");

        //获取CycleViewPager对象
        cycleViewPager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);

        for (int i = 0; i < newUrls.length; i++) {
            ADInfo info = new ADInfo();
            info.setUrl(newUrls[i]);
            info.setContent("图片-->" + i);
            infos.add(info);
        }

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
        cycleViewPager.setTime(5000);
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
                if (position == 2) {
                    Uri uri = Uri.parse("http://www.51buyjc.com/Shop/Index/1258");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (position == 1) {
                    Uri uri = Uri.parse("http://www.51buyjc.com/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
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

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(String html) {
            Log.d("html=", html);//网页源代码

            BufferedReader r = new BufferedReader(new StringReader(new String(html)));
            String line = null;
            try {
                while ((line = r.readLine()) != null) {
                    if (line.contains("版本号：")) {
                        LogUtil.d("line", "line=" + line);
                        String versionLine = r.readLine();
                        LogUtil.d("versionLine", "versionLine=" + versionLine);
                        String versionName = versionLine.substring(versionLine.indexOf(">") + 2, versionLine.lastIndexOf("<"));
                        LogUtil.d("versionName", versionName);
                        LogUtil.d("tag", "versionName:===============" + getAppVersionName(FristActivity.this));
                        if (!isFirstRun) {
//                            ToastUtil.shortT(FristActivity.this, versionName);
//                            ToastUtil.shortT(FristActivity.this, getAppVersionName(FristActivity.this));
                            if (!getAppVersionName(FristActivity.this).equals(versionName)) {
                                showNoticeDialog();
                            }
                        }
                    }
                }
                r.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


final class MyWebViewClient extends WebViewClient {
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d("WebView", "onPageStarted");
        super.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView view, String url) {
        Log.d("WebView", "onPageFinished ");
        //获取网页内容
        view.loadUrl("javascript:window.local_obj.getSource('123:'+document.body.innerHTML+'')");
        LogUtil.d("url=============" + url);
        super.onPageFinished(view, url);
    }
}
