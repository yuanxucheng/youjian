package com.example.yj.mapapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.example.yj.mapapp.R;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.manager.PoiOverlay;

import java.io.UnsupportedEncodingException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MarkerOptions;
import com.example.yj.mapapp.model.Companys;
import com.example.yj.mapapp.model.EnterpriseMapPoint;
import com.example.yj.mapapp.model.Firm;
import com.example.yj.mapapp.model.Goods;
import com.example.yj.mapapp.model.IndustryClassification;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.net.handler.HttpUtil;
import com.example.yj.mapapp.net.handler.ResponseHandler;
import com.example.yj.mapapp.util.BitmapUtil;
import com.example.yj.mapapp.util.JsonParser;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.yj.mapapp.view.ExpandTabView;
import com.example.yj.mapapp.view.MProgressDialog;
import com.example.yj.mapapp.view.ViewLeft;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 建材企业
 */
public class BuildEnterprisesActivity extends BaseActivity {

    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private List<String> suggest;
    //自定义进度对话框
    private MProgressDialog pb;
    //显示对话框
    private static final int SHOW = 1;
    //关闭对话框
    private static final int DISMISS = 2;
    //第几次进入该界面
    private int count = 0;

    /**
     * 搜索关键字输入窗口
     */
//    private AutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    private int loadIndex = 0;

    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LatLng cenpt;
    private MapStatus mMapStatus;
    private MapStatusUpdate mMapStatusUpdate;
    private LocationClientOption option;
    private InfoWindow mInfoWindow;
    private List<BitmapDescriptor> bdList = new ArrayList<>();
    // 定位相关
    private LocationClient mLocClient;
    //定位监听事件
    private MyLocationListenner myListener = new MyLocationListenner();
    private boolean isFirstLoc = true; // 是否首次定位
    //二级列表
    private ExpandTabView expandTabView;
    //视图集合
    private ArrayList<View> mViewArray = new ArrayList<View>();
    //左边视图
    private ViewLeft viewLeft;
    //分类广告对话框
    private LayoutInflater inflater;
    //对话框
    private AlertDialog dialog;
    //系统控件
    private TextView tv;
    //是否加载分类气泡点
    private boolean isLoadType;
    //企业搜索对象集合
    private List<Firm> list = new ArrayList<>();
    //企业对象
    private Firm firm;

//    @Bind(R.id.id_dingwei)
//    Button dingwei;
//
//    @OnClick(R.id.id_dingwei)
//    public void dingwei(View v) {
//        ToastUtil.longT(this, "正在定位...");
//        mLocClient.start();
//    }

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
        return R.layout.activity_build_enterprises;
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

        isLoadType = false;//设置初始化值

        pb = new MProgressDialog(this);//实例化进度对话框对象
        pb.setMessage(getString(R.string.buildSites_loadMap));//设置对话框信息
        pb.setCancelable(true);//设置进度条是否可以按退回键取消
        pb.setCanceledOnTouchOutside(true); //设置点击进度对话框外的区域对话框消失

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
//        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
//        mSuggestionSearch.setOnGetSuggestionResultListener(this);
//        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.build_enterprise_search_key);
        sugAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line);
//        keyWorldsView.setAdapter(sugAdapter);
//        keyWorldsView.setThreshold(1);

//        mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager()
//                .findFragmentById(R.id.map))).getBaiduMap();

        //初始化地图
        mMapView = (MapView) findViewById(R.id.build_enterprise_map);
        mBaiduMap = mMapView.getMap();
        //设定中心点坐标
//        LatLng cenpt = new LatLng(31.083397,121.525437);//北京
//        LatLng cenpt = new LatLng(31.14, 121.29);//上海市的经纬度
        cenpt = new LatLng(31.5, 121.5);//上海市的经纬度
        //定义地图状态
        mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(11.5f)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        //注册监听
        mLocClient.registerLocationListener(myListener);
        //创建LocationClientOption对象
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//设置定时定位的时间间隔
        mLocClient.setLocOption(option);
//        mMapView.showScaleControl(true);//隐藏地图上的比例尺
//        mMapView.showZoomControls(false);//隐藏地图上的缩放控件
        /**
         * 当输入关键字变化时，动态更新建议列表
         */
//        keyWorldsView.addTextChangedListener(textWatcherListener);

        //源码
//        mMapView = (MapView) findViewById(R.id.map);
//        mBaiduMap = mMapView.getMap();
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
//        mBaiduMap.setMapStatus(msu);

        initOverlay();

//        HttpUtil.enterpriseCoordinate("121", "31", "121.5", "31.5", enterpriseCoordinateHandler);
//        HttpUtil.enterpriseCoordinate("120", "30", "122", "32", enterpriseCoordinateHandler);

        HttpUtil.enterpriseCoordinate(HttpConfig.startLong, HttpConfig.startLat, HttpConfig.endLong, HttpConfig.endLat, enterpriseCoordinateHandler);
        //地图状态变化事件监听
//        mBaiduMap.setOnMapStatusChangeListener(statusChangeListener);
        //地图气泡点点击事件监听
        mBaiduMap.setOnMarkerClickListener(markerClickListener);
        //开启百度定位
        mLocClient.start();

        initView();
        initVaule();
        initListener();
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


        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            if (isLoadType == false) {//如果isLoadType等于false,则加载企业坐标点搜索接口
                HttpUtil.enterpriseCoordinate(initPoint().get(3), initPoint().get(2), initPoint().get(1), initPoint().get(0), enterpriseCoordinateHandler);
            }
        }
    };

    @Override
    public void doBusiness(Context mContext) {

    }

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
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
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
     * 控件初始化
     */
    private void initView() {
        expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);
        viewLeft = new ViewLeft(this);
    }

    /**
     * 初始化值
     */
    private void initVaule() {
        //将左边视图添加到视图集合中
        mViewArray.add(viewLeft);
//		mViewArray.add(viewMiddle);
//		mViewArray.add(viewRight);
        //创建集合对象
        ArrayList<String> mTextArray = new ArrayList<String>();
        //往集合中添加值
        mTextArray.add("全部分类");
//		mTextArray.add("距离");
        //设置二级列表中的数据
        expandTabView.setValue(mTextArray, mViewArray);
//		expandTabView.setTitle(viewLeft.getShowText(), 0);
//		expandTabView.setTitle(viewMiddle.getShowText(), 1);
//		expandTabView.setTitle(viewRight.getShowText(), 2);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {
            @Override
            public void getValue(IndustryClassification showText) {
                isLoadType = true;//将isLoadType设置成true
                onRefresh(viewLeft, showText);//刷新控件
            }
        });
    }

    private void onRefresh(View view, final IndustryClassification showText) {

        expandTabView.onPressBack();//关闭/隐藏二级列表控件
        int position = getPositon(view);
        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            //根据选择的位置设置tabitem显示的值
            expandTabView.setTitle(showText.getP_Name(), position);
            // 根据选择的位置获取tabitem显示的值
            String value = expandTabView.getTitle(position);
        }
        if (showText.equals("全部")) {
            LogUtil.d("tag", "全部=================");
        } else {

//            classifyAdvertDialog();

            // 不延迟，直接发送
            handler.sendEmptyMessage(SHOW);
            getEnterpriseMapPointByType(HttpConfig.startLong, HttpConfig.startLat, HttpConfig.endLong, HttpConfig.endLat, Integer.valueOf(showText.getP_Id()));
        }
    }

    /**
     * 分类广告对话框
     */
    private void classifyAdvertDialog() {
        //开启线程
        handler.post(run);
        //实例化插入器对象
        inflater = LayoutInflater.from(this);
        //插入布局文件
        View view = inflater.inflate(R.layout.advert_dialog, null);
        //创建对话框对象设置布局文件并显示对话框
        dialog = new AlertDialog.Builder(this).setView(view).show();
        tv = (TextView) view.findViewById(R.id.btn_cloese_dialog);
    }

    private Runnable run = new Runnable() {
        int i = 1;

        public void run() {
            if (i < 4) {
                //设置TextView控件的数据内容
                tv.setText(i++ + "s");
                //延迟一秒加载
                handler.postDelayed(run, 1000);
            } else {
                dialog.dismiss();
                // 不延迟，直接发送
                handler.sendEmptyMessage(SHOW);
            }
        }
    };

    /**
     * 获取position位置值
     *
     * @param tView
     * @return
     */
    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!expandTabView.onPressBack()) {
            finish();
        }

    }

    //    //屏蔽返回键
//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // mapview销毁后不在处理新接收的位置
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
                builder.target(ll).zoom(18.0f);//设置地图信息
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
    private OnMarkerClickListener markerClickListener = new OnMarkerClickListener() {
        public boolean onMarkerClick(final Marker marker) {
            //创建按钮对象
            Button button = new Button(getApplicationContext());
            //设置按钮背景图片
            button.setBackgroundResource(R.mipmap.pop);
            OnInfoWindowClickListener listener = null;
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
                    //跳转到显示建材企业详情界面
                    Intent intent = new Intent();
                    intent.setClass(BuildEnterprisesActivity.this, BuildEnterprisesDetailActivity.class);
                    intent.putExtra("buildEnterprise_id", extro.getInt("buildEnterprise_id"));
                    startActivity(intent);
                }
            });
            LatLng ll = marker.getPosition();//获得气泡点位置的LatLng对象
            mInfoWindow = new InfoWindow(button, ll, -47);//实例化InfoWindow对象
            mBaiduMap.showInfoWindow(mInfoWindow);//地图上显示窗口信息
            return true;
        }
    };

    /**
     * 获取分类气泡点
     *
     * @param startLong
     * @param startLat
     * @param endLong
     * @param endLat
     * @param type
     */
    private void getEnterpriseMapPointByType(String startLong, String startLat, String endLong, String endLat, int type) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startLong", startLong);
            jsonObject.put("startLat", startLat);
            jsonObject.put("endLong", endLong);
            jsonObject.put("endLat", endLat);
            jsonObject.put("type", type);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            String url = HttpConfig.REQUEST_URL + "/Map/GetEnterpriseMapPointByType";
            RequestHandle post = HTTPTool.getClient().post(BuildEnterprisesActivity.this, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    mBaiduMap.clear();//清除地图上的内容

                    Log.d("tag", "企业坐标点搜索接口(类别搜索接口)");
                    Log.d("tag", response.toString());
                    String d = JsonUtil.getData(response.toString());
                    Log.d("-------------", d);

                    try {
                        JSONArray array = new JSONArray(d);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            int id = object.getInt("id");
                            String name = object.getString("name");
                            String pointLong = object.getString("pointLong");
                            String pointLat = object.getString("pointLat");
                            LogUtil.d("tag", id + "-------" + name + "-----" + pointLong + "-----" + pointLat);

                            EnterpriseMapPoint em = new EnterpriseMapPoint();
                            em.setId(id);
                            em.setName(name);
                            em.setPointLong(pointLong);
                            em.setPointLat(pointLat);

                            LatLng ll = new LatLng(Double.parseDouble(pointLat), Double.parseDouble(pointLong));
                            Log.d("tag-------------", "" + Double.parseDouble(pointLat));
                            Log.d("tag-------------", "" + Double.parseDouble(pointLong));

//                            BitmapDescriptor bd = BitmapDescriptorFactory
//                                    .fromResource(R.mipmap.icon_gcoding);

                            //对图片进行压缩处理,在给地图设置压缩后的图片
                            BitmapDescriptor bd = BitmapDescriptorFactory
                                    .fromBitmap(BitmapUtil.readBitMap(BuildEnterprisesActivity.this, R.mipmap.icon_gcoding));
                            //给集合添加数据
                            bdList.add(bd);
                            Log.d("size", bdList.size() + "==============");
                            Bundle bundle = new Bundle();
                            bundle.putInt("buildEnterprise_id", id);
                            bundle.putString("title", name);
                            //设置气泡点信息,包括图片以及文字内容等
                            MarkerOptions oo = new MarkerOptions().position(ll).icon(bd)
                                    .zIndex(5).extraInfo(bundle);
                            //给地图添加气泡点
                            Marker mMarker = (Marker) (mBaiduMap.addOverlay(oo));

                            //发送消息提示关闭对话框
                            Message msg = new Message();
                            msg.what = DISMISS;
                            handler.sendMessage(msg);
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

    private ResponseHandler enterpriseCoordinateHandler = new ResponseHandler() {
        @Override
        public void onCacheData(String content) {
            LogUtil.w("========onCacheData=========" + content);
        }

        @Override
        public void success(String data, String resCode, String info) {

            bdList.clear();

            if (resCode.equals("")) {
                LogUtil.d("data----------------" + data);
                LogUtil.d("企业坐标点搜索接口,成功==============");

                //后台返回的数据进行过处理的,使用GSON解析
                List<Companys> companies = JsonParser.deserializeFromJson(data, new TypeToken<List<Companys>>() {
                }.getType());
                Log.d("size:-------------", "" + companies.size());
                for (Companys c : companies) {
                    LogUtil.d("tag", "id:" + c.getId() + "========name:" + c.getName() + "==========pointLat:" + c.getPointLat() + "==========pointLong:" + c.getPointLong());
                    LatLng ll = new LatLng(Double.parseDouble(c.getPointLat()), Double.parseDouble(c.getPointLong()));
                    Log.d("-------------", "" + Double.parseDouble(c.getPointLat()));
                    Log.d("-------------", "" + Double.parseDouble(c.getPointLong()));

//                    BitmapDescriptor bd = BitmapDescriptorFactory
//                            .fromResource(R.mipmap.icon_gcoding);

                    //对图片进行压缩处理,在给地图设置压缩后的图片
                    BitmapDescriptor bd = BitmapDescriptorFactory
                            .fromBitmap(BitmapUtil.readBitMap(BuildEnterprisesActivity.this, R.mipmap.icon_gcoding));
                    //给集合添加数据
                    bdList.add(bd);
                    Log.d("sizes", bdList.size() + "==============");
                    Bundle bundle = new Bundle();
                    bundle.putInt("buildEnterprise_id", c.getId());
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
            } else {
                ToastUtil.shortT(BuildEnterprisesActivity.this, getText(R.string.buildEnterprises_fail).toString());
//                LogUtil.d("==============企业坐标点搜索接口,失败");
            }
        }

        @Override
        public void onFail(int arg0, String arg2, Throwable arg3) {
            ToastUtil.shortT(BuildEnterprisesActivity.this, getText(R.string.buildEnterprises_fail).toString());
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

    public void initOverlay() {
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
//        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void getEnterpriseSearch(String search, int pageSize, int pageIndex) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("search", search);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("pageIndex", pageIndex);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            String url = HttpConfig.REQUEST_URL + "/Map/GetEnterpriseSearch";
            RequestHandle post = HTTPTool.getClient().post(BuildEnterprisesActivity.this, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
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
                                String address = object.getString("Address");
                                String contacts = object.getString("Contacts");
                                String authentication = object.getString("Authentication");
                                LogUtil.d("tag", id + "-------" + id + "-----" + name + "-----" + name + "------" + address + "----" + address + "----------" + contacts + "----" + contacts + "----" + authentication + authentication + "----");

                                firm = new Firm(R.id.item_search_iv_icon, id, name, address, contacts, authentication);
                                firm.setIconId(R.id.item_search_iv_icon);
                                firm.setId(id);
                                firm.setName(name);
                                firm.setAddress(address);
                                firm.setContacts(contacts);
                                firm.setAuthentication(authentication);
                                list.add(firm);
                                LogUtil.d("tag", firm.getId() + "-------" + firm.getName() + "-----" + firm.getAddress() + "-----" + firm.getContacts() + "-----" + firm.getAuthentication() + "-----");
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
}
