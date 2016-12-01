package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.manager.PoiOverlay;
import com.example.yj.mapapp.model.Area;
import com.example.yj.mapapp.model.Companys;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.net.handler.HttpUtil;
import com.example.yj.mapapp.net.handler.ResponseHandler;
import com.example.yj.mapapp.util.BitmapUtil;
import com.example.yj.mapapp.util.JsonParser;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.MProgressDialog;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 建筑工地
 */
public class BuildSitesActivity extends BaseActivity {

    /**
     * Called when the activity is first created.
     */
    private List<String> list = new ArrayList<String>();
    private Spinner mySpinner;
    private ArrayAdapter<String> adapter;
    private List<Area> infos;//区域对象集合
    private List<String> nameList;//区域名称对象集合
    //自定义进度对话框
    private MProgressDialog pb;
    //显示对话框
    private static final int SHOW = 1;
    //隐藏对话框
    private static final int DISMISS = 2;
    //第几次进入该界面
    private int count = 0;
    //MapView 是地图主控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LatLng cenpt;
    private MapStatus mMapStatus;
    private MapStatusUpdate mMapStatusUpdate;
    private LocationClientOption option;
    private InfoWindow mInfoWindow;//地图气泡点窗口
    //地图覆盖物集合对象
    private List<BitmapDescriptor> bdList = new ArrayList<>();
    // 定位相关
    LocationClient mLocClient;
    //实例化定位监听对象
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    //地图缩放级别数
    private static float TWENTY = 20.0f;
    private static float TWELVE = 12.0f;

    @Bind(R.id.id_back)
    ImageView back;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW:
                    pb.show();
                    break;
                case DISMISS:
                    pb.dismiss();
                    break;
            }
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_building_sites;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initParms(Bundle parms) {

    }

    //屏蔽返回键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this);
        //创建对话框对象
        pb = new MProgressDialog(this);
//        pb.setMessage(getString(R.string.buildEnterprises_loadMap));
        pb.setCancelable(true);//设置进度条是否可以按退回键取消
        pb.setCanceledOnTouchOutside(true); //设置点击进度对话框外的区域对话框消失

        infos = new ArrayList<>();
        nameList = new ArrayList<>();
        nameList.add("上海市");

        //初始化地图
        mMapView = (MapView) findViewById(R.id.build_site_map);
        mBaiduMap = mMapView.getMap();
        //设定中心点坐标
        init(31.5, 121.5, TWENTY);//上海市的经纬度

//        mMapView.showScaleControl(true);//隐藏地图上的比例尺
//        mMapView.showZoomControls(false);//隐藏地图上的缩放控件

        initOverlay();

//        HttpUtil.constructionCoordinate("121", "31", "121.5", "31.5", constructionCoordinateHandler);

        HttpUtil.constructionCoordinate(HttpConfig.startLong, HttpConfig.startLat, HttpConfig.endLong, HttpConfig.endLat, constructionCoordinateHandler);
        //地图状态变化事件监听
//        mBaiduMap.setOnMapStatusChangeListener(statusChangeListener);
        //地图气泡点点击事件监听
        mBaiduMap.setOnMarkerClickListener(markerClickListener);
        //开启百度定位
        mLocClient.start();

        getMapAreaByPid(1);

//        HttpUtil.constructionCoordinate(initPoint().get(3), initPoint().get(2), initPoint().get(1), initPoint().get(0), constructionCoordinateHandler);

        mySpinner = (Spinner) findViewById(R.id.build_site_spinner_city);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nameList);
        //第三步：为适配器设置下拉列表下拉时的菜单样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        mySpinner.setAdapter(adapter);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        mySpinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new Spinner.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (arg2 == 0) {
                init(31.234829, 121.483443, TWELVE);
            } else {
                LogUtil.d("tag", "arg2:=======" + arg2);
                LogUtil.d("tag", "size:=======" + infos.size());
                double latitude = Double.valueOf(infos.get(arg2 - 1).getLatitude());
                double longitude = Double.valueOf(infos.get(arg2 - 1).getLongitude());
                LogUtil.d("tag", "laaaaaaaaa:=======" + latitude);
                LogUtil.d("tag", "loooooooo:=======" + longitude);
                init(latitude, longitude, TWELVE);
            }
            arg0.getItemAtPosition(arg2);
            // TODO Auto-generated method stub
                /* 将mySpinner 显示*/
            arg0.setVisibility(View.VISIBLE);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            arg0.setVisibility(View.VISIBLE);
        }
    };

    /**
     * @param latitude  纬度
     * @param longitude 经度
     * @param f         缩放级别
     */
    private void init(double latitude, double longitude, float f) {
        //设定中心点坐标
        cenpt = new LatLng(latitude, longitude);//区域的经纬度
        //定义地图状态
        mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(f)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(BuildSitesActivity.this);
        //注册监听
        mLocClient.registerLocationListener(myListener);
        //创建LocationClientOption对象
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//设置定时定位的时间间隔
        mLocClient.setLocOption(option);
    }

    /**
     * 地图状态改变监听
     */
    BaiduMap.OnMapStatusChangeListener statusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
//            LatLng pointLeft = mBaiduMap.getProjection().fromScreenLocation(new Point(0, 0));
//            LatLng pointRight = mBaiduMap.getProjection().fromScreenLocation(new Point(mMapView.getWidth(), mMapView.getHeight()));
//            LogUtil.d("tag", "pointLeft:=============" + pointLeft);
//            LogUtil.d("tag", "pointRight:==============" + pointRight);
//
//            int b = mMapView.getBottom();
//            LogUtil.d("tag", "b:==============" + b);
//            int t = mMapView.getTop();
//            LogUtil.d("tag", "t:==============" + t);
//            int r = mMapView.getRight();
//            LogUtil.d("tag", "r:==============" + r);
//            int l = mMapView.getLeft();
//            LogUtil.d("tag", "lb:==============" + l);
//            LatLng ne = mBaiduMap.getProjection().fromScreenLocation(new Point(r, t));
//            LogUtil.d("tag", "ne:==============" + ne);
//            LatLng sw = mBaiduMap.getProjection().fromScreenLocation(new Point(l, b));
//            LogUtil.d("tag", "sw:==============" + sw);
//
//            String[] str = ne.toString().split(",");
//            String s = str[0].split(":")[1];
//            String ss = str[1].split(":")[1];
//            LogUtil.d("tag", "s:==============" + s + "ss:===========" + ss);
//            String[] string = sw.toString().split(",");
//            String sss = string[0].split(":")[1];
//            String ssss = string[1].split(":")[1];
//            LogUtil.d("tag", "sss:==============" + sss + "ssss:===========" + ssss);

            //===========================
//                GeoPoint centerPoint = mMapView.getMapCenter();// 地图中心坐标点
//                int tpSpan = mMapView.getLatitudeSpan();// 当前纬线的跨度（从地图的上边缘到下边缘）int lrSpan = mMapView.getLongitudeSpan();// 当前经度的跨度（从地图的左边缘到右边缘）
//                GeoPoint point = new GeoPoint(centerPoint.getLatitudeE6() - tpSpan / 2, centerPoint.getLongitudeE6() + lrSpan / 2);// 右上角
//                GeoPoint point2 = new GeoPoint(centerPoint.getLatitudeE6() + tpSpan / 2, centerPoint.getLongitudeE6() - lrSpan / 2);// 左下角

//            HttpUtil.constructionCoordinate(ssss, sss, ss, s, constructionCoordinateHandler);


            //===========================
//            DisplayMetrics dm = new DisplayMetrics();
//            // 获取屏幕信息
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
//            int screenWidth = dm.widthPixels;
//            LogUtil.d("tag", "screenWidth:==============" + screenWidth);
//            int screenHeigh = dm.heightPixels;
//            LogUtil.d("tag", "screenHeigh:==============" + screenHeigh);
//            int targetScreenX = mBaiduMap.getMapStatus().targetScreen.x;// 地图操作中心点在屏幕中的坐标x
//            LogUtil.d("tag", "targetScreenX:==============" + targetScreenX);
//            int targetScreenY = mBaiduMap.getMapStatus().targetScreen.y;// 地图操作中心点在屏幕中的坐标y
//            LogUtil.d("tag", "targetScreenY:==============" + targetScreenY);
//            int navHeight = screenHeigh - 2 * targetScreenY;// 导航条(除地图之外的部分)的高度
//            LogUtil.d("tag", "navHeight:==============" + navHeight);
//
//            Point rightup = new Point(screenWidth, navHeight);
//            Point leftdown = new Point(0, screenHeigh);
//
//            LatLng northeast = mBaiduMap.getProjection().fromScreenLocation(rightup);
//            LatLng southwest = mBaiduMap.getProjection().fromScreenLocation(leftdown);
//            LogUtil.d("tag", "northeast:==============" + northeast);
//            LogUtil.d("tag", "southwest:==============" + southwest);

//            //===========================
//            String[] strings = northeast.toString().split(",");
//            String st = strings[0].split(":")[1];
//            String stt = strings[1].split(":")[1];
//            LogUtil.d("tag", "st:==============" + st + "stt:===========" + stt);
//            String[] stringss = southwest.toString().split(",");
//            String sttt = stringss[0].split(":")[1];
//            String stttt = stringss[1].split(":")[1];
//            LogUtil.d("tag", "sttt:==============" + sttt + "stttt:===========" + stttt);

//            HttpUtil.ConstructionCoordinate(stttt, sttt, stt, st, constructionCoordinateHandler);

            //===========================
//            MapStatus mmapStatus = mBaiduMap.getMapStatus();
//            LatLng center = mmapStatus.target;
//            LogUtil.d("tag", "center:==============" + center);
//            String location = center.latitude + "," + center.longitude;
//            LogUtil.d("tag", "location:==============" + location);

            //=============================
//            LatLng target = mBaiduMap.getMapStatus().target;
//            LogUtil.d("tag", "target:==============" + target);
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            HttpUtil.constructionCoordinate(initPoint().get(3), initPoint().get(2), initPoint().get(1), initPoint().get(0), constructionCoordinateHandler);
        }
    };

    /**
     * 获取百度地图手机屏幕的四个坐标点
     *
     * @return List<String>
     */
    private List<String> initPoint() {
        List<String> list = new ArrayList<>();
        LatLng pointLeft = mBaiduMap.getProjection().fromScreenLocation(new Point(0, 0));
        LatLng pointRight = mBaiduMap.getProjection().fromScreenLocation(new Point(mMapView.getWidth(), mMapView.getHeight()));
        LogUtil.d("tag", "pointLeft:=============" + pointLeft);
        LogUtil.d("tag", "pointRight:==============" + pointRight);

        int b = mMapView.getBottom();
        LogUtil.d("tag", "b:==============" + b);
        int t = mMapView.getTop();
        LogUtil.d("tag", "t:==============" + t);
        int r = mMapView.getRight();
        LogUtil.d("tag", "r:==============" + r);
        int l = mMapView.getLeft();
        LogUtil.d("tag", "lb:==============" + l);
        LatLng ne = mBaiduMap.getProjection().fromScreenLocation(new Point(r, t));
        LogUtil.d("tag", "ne:==============" + ne);
        LatLng sw = mBaiduMap.getProjection().fromScreenLocation(new Point(l, b));
        LogUtil.d("tag", "sw:==============" + sw);

        String[] str = ne.toString().split(",");
        String s = str[0].split(":")[1];
        String ss = str[1].split(":")[1];
        LogUtil.d("tag", "s:==============" + s + "ss:===========" + ss);
        list.add(s);
        list.add(ss);
        String[] string = sw.toString().split(",");
        String sss = string[0].split(":")[1];
        String ssss = string[1].split(":")[1];
        LogUtil.d("tag", "sss:==============" + sss + "ssss:===========" + ssss);
        list.add(sss);
        list.add(ssss);
        return list;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void resume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
//        mMapView.onResume();
//        super.onResume();

        count++;
        new Thread() {
            @Override
            public void run() {
                // 不延迟，直接发送
                if (count == count % 2) {
                    handler.sendEmptyMessage(SHOW);
                }
            }
        }.start();
    }

    @Override
    public void destroy() {
//        mPoiSearch.destroy();
//        mSuggestionSearch.destroy();
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
//        mMapView.onDestroy();
//        super.onDestroy();

        // 退出时销毁定位
        mLocClient.stop();
        mLocClient = null;
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.clear();
        mBaiduMap = null;
        // 回收 bitmap 资源
        for (BitmapDescriptor bd : bdList) {
            bd.recycle();
        }
        //清空mMapView对象
        mMapView = null;
        pb = null;
        cenpt = null;
        mMapStatus = null;
        mMapStatusUpdate = null;
        option = null;
        System.gc();  //提醒系统及时回收
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            //创建MyLocationData对象
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            //为百度地图设置定位
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;//将isFirstLoc变量设置成false
                //创建LatLng对象
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                //实例化Builder对象
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(15.0f);//设置地图信息
                //设置地图状态
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 地图气泡点点击事件监听
     */
    private BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
        public boolean onMarkerClick(final Marker marker) {
            //创建按钮对象
            Button button = new Button(getApplicationContext());
            //设置按钮背景图片
            button.setBackgroundResource(R.mipmap.pop);
            InfoWindow.OnInfoWindowClickListener listener = null;
            //获取气泡点中的基本信息
            final Bundle extro = marker.getExtraInfo();
//            button.setText(marker.getTitle());
            //获得气泡点的标题并设置给按钮显示内容
            button.setText(extro.getString("title"));
            //设置按钮的字体颜色
            button.setTextColor(getResources().getColor(R.color.black));
            //按钮点击事件
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //跳转到显示建筑工地详情界面
                    Intent intent = new Intent();
                    intent.setClass(BuildSitesActivity.this, BuildSitesDetailActivity.class);
                    intent.putExtra("buildSites_id", extro.getInt("buildSites_id"));
                    startActivity(intent);
                }
            });
            LatLng ll = marker.getPosition();//获得气泡点位置的LatLng对象
            mInfoWindow = new InfoWindow(button, ll, -47);//实例化InfoWindow对象
            mBaiduMap.showInfoWindow(mInfoWindow);//地图上显示窗口信息
            return true;
        }
    };

    private ResponseHandler constructionCoordinateHandler = new ResponseHandler() {
        @Override
        public void onCacheData(String content) {
            LogUtil.w("========onCacheData=========" + content);
        }

        @Override
        public void success(String data, String resCode, String info) {

            bdList.clear();//清空集合

            if (resCode.equals("")) {
                LogUtil.d("data===================" + data);
                LogUtil.d("承建坐标点搜索接口,成功==============");

//                ParserJson(data);//使用原生的解析方式

                //后台返回的数据进行过处理的,使用GSON解析
                List<Companys> companies = JsonParser.deserializeFromJson(data, new TypeToken<List<Companys>>() {
                }.getType());
                Log.d("size:-------------", "" + companies.size());
                for (Companys c : companies) {
                    LogUtil.d("tag", "id:" + c.getId() + "========name:" + c.getName() + "==========pointLat:" + c.getPointLat() + "==========pointLong:" + c.getPointLong());
                    LatLng ll = new LatLng(Double.parseDouble(c.getPointLat()), Double.parseDouble(c.getPointLong()));
                    Log.d("-------------", "" + Double.parseDouble(c.getPointLat()));
                    Log.d("-------------", "" + Double.parseDouble(c.getPointLong()));

//                    InputStream is = getResources().openRawResource(R.mipmap.icon_gcoding);
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = false;
//                    options.inSampleSize = 10;   // width，hight设为原来的十分一
//                    Bitmap btp = BitmapFactory.decodeStream(is, null, options);
//                    BitmapDescriptor bd = BitmapDescriptorFactory
//                            .fromBitmap(btp);

//                    BitmapDescriptor bd = BitmapDescriptorFactory
//                            .fromResource(R.mipmap.icon_gcoding);

                    //对图片进行压缩处理,在给地图设置压缩后的图片
                    BitmapDescriptor bd = BitmapDescriptorFactory
                            .fromBitmap(BitmapUtil.readBitMap(BuildSitesActivity.this, R.mipmap.icon_gcoding));
                    //给集合添加数据
                    bdList.add(bd);
                    Bundle bundle = new Bundle();
                    bundle.putInt("buildSites_id", c.getId());
                    bundle.putString("title", c.getName());
                    //设置气泡点信息,包括图片以及文字内容等
                    MarkerOptions oo = new MarkerOptions().position(ll).icon(bd)
                            .zIndex(5).extraInfo(bundle);
                    //给地图添加气泡点
                    Marker mMarker = (Marker) (mBaiduMap.addOverlay(oo));

                    //发送消息提示关闭对话框
                    Message msg = new Message();
                    msg.what = DISMISS;
                    handler.sendMessage(msg);
                    // 回收资源
                    for (BitmapDescriptor bitmap : bdList) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    companies = null;
                    bundle = null;
                    oo = null;
                    msg = null;
                    mMarker = null;
                }

                //后台返回的数据进行过处理的,使用GSON解析
//                List<Companys> companies= JsonParser.deserializeFromJson(data,new TypeToken<List<Companys>>(){}.getType());
//                for (Companys c : companies) {
//                    LogUtil.d("tag", "id:" + c.getId() + "========name:" + c.getName() + "==========pointLat:" + c.getPointLat() + "==========pointLong:" + c.getPointLong());
//                }

            } else {
                ToastUtil.shortT(BuildSitesActivity.this, getText(R.string.buildSites_fail).toString());
                LogUtil.d("==============承建坐标点搜索接口,失败");
            }
        }

        @Override
        public void onFail(int arg0, String arg2, Throwable arg3) {
            ToastUtil.shortT(BuildSitesActivity.this, getText(R.string.buildSites_fail).toString());
        }
    };

    /**
     * 解析JSON
     *
     * @param data
     */
    private void ParserJson(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String name = jsonObject.optString("name");
                String id = jsonObject.optString("id");
                String pointLat = jsonObject.optString("pointLat");
                String pointLong = jsonObject.optString("pointLong");
                LogUtil.d("tag", "id:" + id + "========name:" + name + "==========pointLat:" + pointLat + "==========pointLong:" + pointLong);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有的区域
     *
     * @param pid
     */
    private void getMapAreaByPid(int pid) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pid", pid);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            String url = HttpConfig.REQUEST_URL + "/Sys/GetMapAreaByPid";
            RequestHandle post = HTTPTool.getClient().post(BuildSitesActivity.this, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("==============", response.toString());
                    String d = JsonUtil.getData(response.toString());
                    Log.d("-------------", d);

                    try {
                        JSONArray array = new JSONArray(d);

                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int id = object.getInt("Id");
                                String name = object.getString("Name");
                                int pid = object.getInt("Pid");
                                String longitude = object.getString("Longitude");
                                String latitude = object.getString("Latitude");
                                LogUtil.d("tag", id + "-------" + name + "-----" + pid + "-----" + longitude + "------" + latitude);

                                Area info = new Area();
                                info.setId(id);
                                info.setName(name);
                                info.setPid(pid);
                                info.setLatitude(latitude);
                                info.setLongitude(longitude);
                                infos.add(info);

                                //第一步：添加数据
                                String mName = infos.get(i).getName();
                                nameList.add(mName);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("================", responseString);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void initOverlay() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);

            return true;
        }
    }
}