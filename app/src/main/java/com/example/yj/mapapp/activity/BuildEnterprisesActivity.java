package com.example.yj.mapapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MarkerOptions;
import com.example.yj.mapapp.model.Companys;
import com.example.yj.mapapp.model.EnterpriseMapPoint;
import com.example.yj.mapapp.model.IndustryClassification;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.net.handler.HttpUtil;
import com.example.yj.mapapp.net.handler.ResponseHandler;
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
import com.example.yj.mapapp.view.ViewBaseAction;
import com.example.yj.mapapp.view.ViewLeft;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * poi搜索功能
 */
public class BuildEnterprisesActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private List<String> suggest;

    //进度对话框
    private ProgressDialog pb;
    private static final int SHOW = 1;
    private static final int DISMISS = 2;
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

    private InfoWindow mInfoWindow;

    private List<BitmapDescriptor> bdList = new ArrayList<>();
    private List<BitmapDescriptor> testList = new ArrayList<>();

    // 定位相关
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private boolean isFirstLoc = true; // 是否首次定位
    //ExpandableListView
    private ExpandTabView expandTabView;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ViewLeft viewLeft;

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

        pb = new ProgressDialog(this);
        pb.setMessage(getString(R.string.buildSites_loadMap));
        pb.setCancelable(true);//设置进度条是否可以按退回键取消
        pb.setCanceledOnTouchOutside(true); //设置点击进度对话框外的区域对话框消失

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
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
        LatLng cenpt = new LatLng(31.5, 121.5);//上海市的经纬度
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(11.5f)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);

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
        HttpUtil.enterpriseCoordinate("120", "30", "122", "32", enterpriseCoordinateHandler);

        mBaiduMap.setOnMarkerClickListener(markerClickListener);

        mLocClient.start();

        initView();
        initVaule();
        initListener();
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
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
//        mMapView.onDestroy();
//        super.onDestroy();
        // 回收 bitmap 资源
        for (BitmapDescriptor bd : bdList) {
            bd.recycle();
        }
        for (BitmapDescriptor test : testList) {
            test.recycle();
        }
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView = null;
    }

    private void initView() {

        expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);
        viewLeft = new ViewLeft(this);

    }

    private void initVaule() {

        mViewArray.add(viewLeft);
//		mViewArray.add(viewMiddle);
//		mViewArray.add(viewRight);
        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add("全部分类");
//		mTextArray.add("距离");
        expandTabView.setValue(mTextArray, mViewArray);
//		expandTabView.setTitle(viewLeft.getShowText(), 0);
//		expandTabView.setTitle(viewMiddle.getShowText(), 1);
//		expandTabView.setTitle(viewRight.getShowText(), 2);

    }

    private void initListener() {

        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

            @Override
            public void getValue(IndustryClassification showText) {

                onRefresh(viewLeft, showText);

            }
        });
    }

    private void onRefresh(View view, final IndustryClassification showText) {

        expandTabView.onPressBack();
        int position = getPositon(view);
        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            expandTabView.setTitle(showText.getP_Name(), position);
        }
        if (showText.equals("全部")) {
//            getetEnterpriseMapPointByType("120", "30", "122", "32", Integer.valueOf(showText.getP_Id()));
            ToastUtil.shortT(BuildEnterprisesActivity.this, "全部");
        } else {
            // 不延迟，直接发送
            handler.sendEmptyMessage(SHOW);
            getetEnterpriseMapPointByType("120", "30", "122", "32", Integer.valueOf(showText.getP_Id()));
        }
    }

    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }

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
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private OnMarkerClickListener markerClickListener = new OnMarkerClickListener() {
        public boolean onMarkerClick(final Marker marker) {
            Button button = new Button(getApplicationContext());
            button.setBackgroundResource(R.mipmap.pop);
            OnInfoWindowClickListener listener = null;
            final Bundle extro = marker.getExtraInfo();
//            button.setText(marker.getTitle());
            button.setText(extro.getString("title"));
            button.setTextColor(getResources().getColor(R.color.black));

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //跳转到显示建材企业详情界面
                    Intent intent = new Intent();
                    intent.setClass(BuildEnterprisesActivity.this, BuildEnterprisesDetailActivity.class);
                    intent.putExtra("buildEnterprise_id", extro.getInt("buildEnterprise_id"));
                    startActivity(intent);
                }
            });
            LatLng ll = marker.getPosition();
            mInfoWindow = new InfoWindow(button, ll, -47);
            mBaiduMap.showInfoWindow(mInfoWindow);

            return true;
        }
    };

    private void getetEnterpriseMapPointByType(String startLong, String startLat, String endLong, String endLat, int type) {
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

                    mBaiduMap.clear();

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
                            BitmapDescriptor bd = BitmapDescriptorFactory
                                    .fromResource(R.mipmap.icon_gcoding);

                            bdList.add(bd);
                            Log.d("size", bdList.size() + "==============");
                            Bundle bundle = new Bundle();
                            bundle.putInt("buildEnterprise_id", id);
                            bundle.putString("title", name);
                            MarkerOptions oo = new MarkerOptions().position(ll).icon(bd)
                                    .zIndex(5).extraInfo(bundle);
                            Marker mMarker = (Marker) (mBaiduMap.addOverlay(oo));

                        }
                        Message msg = new Message();
                        msg.what = DISMISS;
                        handler.sendMessage(msg);
                        Log.d("sizesss", bdList.size() + "==============");
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
                    BitmapDescriptor bd = BitmapDescriptorFactory
                            .fromResource(R.mipmap.icon_gcoding);

                    bdList.add(bd);
                    Log.d("sizes", bdList.size() + "==============");
                    Bundle bundle = new Bundle();
                    bundle.putInt("buildEnterprise_id", c.getId());
                    bundle.putString("title", c.getName());
                    MarkerOptions oo = new MarkerOptions().position(ll).icon(bd)
                            .zIndex(5).extraInfo(bundle);
                    Marker mMarker = (Marker) (mBaiduMap.addOverlay(oo));

                }

                Log.d("sizess", bdList.size() + "==============");
                Message msg = new Message();
                msg.what = DISMISS;
                handler.sendMessage(msg);
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
     * JSON
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

//    private TextWatcher textWatcherListener = new TextWatcher() {
//        @Override
//        public void afterTextChanged(Editable arg0) {
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence arg0, int arg1,
//                                      int arg2, int arg3) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence cs, int arg1, int arg2,
//                                  int arg3) {
//            if (cs.length() <= 0) {
//                return;
//            }
//            String city = ((EditText) findViewById(R.id.build_enterprise_city)).getText()
//                    .toString();
//            /**
//             * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
//             */
//            mSuggestionSearch
//                    .requestSuggestion((new SuggestionSearchOption())
//                            .keyword(cs.toString()).city(city));
//        }
//    };

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

//    /**
//     * 影响搜索按钮点击事件
//     * @param v
//     */
//    public void searchButtonProcess(View v) {
//        EditText editCity = (EditText) findViewById(R.id.build_enterprise_city);
//        EditText editSearchKey = (EditText) findViewById(R.id.build_enterprise_search_key);
//        mPoiSearch.searchInCity((new PoiCitySearchOption())
//                .city(editCity.getText().toString())
//                .keyword(editSearchKey.getText().toString())
//                .pageNum(loadIndex));
//    }

    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(BuildEnterprisesActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(BuildEnterprisesActivity.this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BuildEnterprisesActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<String>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }
        sugAdapter = new ArrayAdapter<String>(BuildEnterprisesActivity.this, android.R.layout.simple_dropdown_item_1line, suggest);
//        keyWorldsView.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }
}
