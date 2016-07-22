package com.example.yj.mapapp.base;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.example.yj.mapapp.R;
import com.example.yj.mapapp.listener.MyLocationListener;
import com.example.yj.mapapp.exception.CrashHandlers;
import com.example.yj.mapapp.location.LocationService;
import com.example.yj.mapapp.model.UserInfo;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.util.ToolNetwork;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.jpush.android.api.JPushInterface;

public class MApplication extends Application {

    private static final String TAG = "MApplication";

    private static MApplication application;
    public LocationService locationService;
    public Vibrator mVibrator;
//    private static MApplication application;
    /**
     * 对外提供整个应用生命周期的Context
     **/
    private static Context instance;
    private UserInfo userInfo;// 当前登录的用户信息

    public LocationClient mLocationClient;
    public MyLocationListener locationListener;

    private String now_province;// 当前定位到的省份
    private String now_city;// 当前定位到的市
    private String now_district;// 当前定位到的区/县
    private String now_street;// 当前定位到的街道地址

    public static boolean login = false;    //登录状态

    public static MApplication getApplication() {
        return application;
    }

    /**
     * 对外提供Application Context
     *
     * @return
     */
    public static Context getContext() {
        return instance;
    }

    /**
     * 用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        this.login = false;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.login = false;
    }

    /**
     * 保存登录信息
     */
    public void saveLoginInfo() {
        this.login = true;
    }

    public static MApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.login = false;

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());

//        MApplication.application = this;

        initCrashHandler();
        application = this;
        initLocation();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        /**
         * ImageLoaderConfiguration是图片加载器ImageLoader的配置参数，使用了建造者模式，
         * 这里是直接使用了createDefault()方法创建一个默认的ImageLoaderConfiguration，
         * 当然我们还可以自己设置ImageLoaderConfiguration
         */

       /* //集成第三方网络框架
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);*/

        //集成第三方网络框架
        //使用displayImage()方法 他会根据控件的大小和imageScaleType来自动裁剪图片我们修改下MyApplication，开启Log打印
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configurations = new ImageLoaderConfiguration.Builder(this)
                .writeDebugLogs() //打印log信息
                .build();
        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configurations);

        Log.d(TAG, "[ExampleApplication] onCreate");
        super.onCreate();

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }

    public UserInfo getUserInfo() {
        if (userInfo == null) {
            return null;
        }
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * 初始化异常捕获Handler
     */
    public void initCrashHandler() {
        CrashHandlers handler = CrashHandlers.getInstance();
        handler.init(this);
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        locationListener = new MyLocationListener(mLocationClient);
        mLocationClient.registerLocationListener(locationListener);
        if (mLocationClient != null) {
//            LogUtil.d("mLocationClient创建成功");
//            locationListener.onReceiveLocation();
        }
    }

    /**
     * 获取网络是否已连接
     *
     * @return
     */
    public static boolean isNetworkReady() {
        return ToolNetwork.getInstance(instance).isConnected();
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * Wifi是否可用
     *
     * @return
     */
    public boolean isWifiEnable() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * Gps是否可用
     *
     * @return
     */
    public boolean isGpsEnable() {
        LocationManager locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     *
     */
    public void checkOnPosBtn(final Context context) {
        if (!isNetworkConnected()) {
            showNoNetWorkDlg(context);
        } else {
            // TODO Login logic
            ToastUtil.shortT(context, context.getResources().getString(R.string.network_connected));
        }
    }

    /**
     * 当判断当前手机没有网络时选择是否打开网络设置
     *
     * @param context
     */
    public static void showNoNetWorkDlg(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.desktop_icon)
                .setTitle(R.string.app_name)
                .setMessage("当前无网络").setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 跳转到系统的网络设置界面
                Intent intent = null;
                // 先判断当前系统版本
                if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                }
                context.startActivity(intent);
            }
        }).setNegativeButton("知道了", null).show();
    }

    /**
     * 监听GPS
     */
    public static void initGPS(final Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(context, "请打开GPS", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("请打开GPS");
            dialog.setPositiveButton("确定",
                    new android.content.DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent); // 设置完成后返回到原来的界面
                        }
                    });
            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
        } else {
            // 弹出Toast
            Toast.makeText(context, "GPS已开启",
                    Toast.LENGTH_LONG).show();
            // 弹出对话框
//            new AlertDialog.Builder(this).setMessage("GPS已开启")
//                    .setPositiveButton("确定", null).show();
        }
    }


    public String getNow_province() {
        return now_province;
    }

    public void setNow_province(String now_province) {
        this.now_province = now_province;
    }

    public String getNow_city() {
        return now_city;
    }

    public void setNow_city(String now_city) {
        this.now_city = now_city;
    }

    public String getNow_district() {
        return now_district;
    }

    public void setNow_district(String now_district) {
        this.now_district = now_district;
    }

    public String getNow_street() {
        return now_street;
    }

    public void setNow_street(String now_street) {
        this.now_street = now_street;
    }
}
