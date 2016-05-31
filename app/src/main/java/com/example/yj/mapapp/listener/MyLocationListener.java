package com.example.yj.mapapp.listener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.example.yj.mapapp.base.MApplication;
import com.example.yj.mapapp.util.CheckUtil;
import com.example.yj.mapapp.util.LogUtil;

public class MyLocationListener implements BDLocationListener {
	private String result;
	private int code;

	private String now_province;// 当前定位到的省份
	private String now_city;// 当前定位到的市
	private String now_district;// 当前定位到的区/县
	private String now_street;// 当前定位到的街道

	private LocationClient mLocationClient;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}



	public MyLocationListener(LocationClient mLocationClient) {
		super();
		this.mLocationClient = mLocationClient;
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		now_province = location.getProvince();
		if (!CheckUtil.isEmpty(now_province)) {
            MApplication.getApplication().setNow_province(now_province);
		}
		now_city = location.getCity();
		if (!CheckUtil.isEmpty(now_city)) {
            MApplication.getApplication().setNow_city(now_city);
		}
		now_district = location.getDistrict();
		if (!CheckUtil.isEmpty(now_district)) {
            MApplication.getApplication().setNow_district(now_district);
		}
        now_street = location.getStreet();
		if (!CheckUtil.isEmpty(now_street)) {
            MApplication.getApplication().setNow_street(now_street);
        }
        LogUtil.d( "详细地址=="+location.getAddrStr());

        if(!CheckUtil.isEmpty(now_province) && !CheckUtil.isEmpty(now_city) && !CheckUtil.isEmpty(now_district)){
			LogUtil.e("now_province=" + now_province + ",now_city==" + now_city + ",now_district==" + now_district);
			if(!"null".equals(now_province) && !"null".equals(now_city) && !"null".equals(now_district) && !"null".equals(now_street)){
				mLocationClient.stop();
			}
		}
		// Receive Location
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		code = location.getLocType();
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
        sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());// 单位：公里每小时
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
			sb.append("\nheight : ");
			sb.append(location.getAltitude());// 单位：米
			sb.append("\ndirection : ");
			sb.append(location.getDirection());
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			sb.append("\ndescribe : ");
			sb.append("gps定位成功");

		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
			sb.append("\naddr : ");
			sb.append(location.getCity());
			result = location.getCity();
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

		LogUtil.w(sb.toString()+"=======dghoijpojhihogifuyf=========");

		// mLocationClient.setEnableGpsRealTimeTransfer(true);

	}

}
