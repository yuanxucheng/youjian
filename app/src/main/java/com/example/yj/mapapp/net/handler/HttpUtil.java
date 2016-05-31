package com.example.yj.mapapp.net.handler;

import android.content.Context;
import android.util.Log;

import com.example.yj.mapapp.util.LogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    /**
     * 注册
     *
     * @param name
     * @param pwd
     * @param contacts
     * @param phone
     * @param type
     * @param responseHandler
     */
    public static void register(String name, String pwd, String contacts, String phone, String type, ResponseHandler responseHandler) {
//        String url=HttpConfig.REQUEST_URL+"/User/AddUser";
        String url = HttpConfig.REQUEST_URL + "/User/AddUser";
        Map<String, String> map = new HashMap<String, String>();
        map.put("userName", name);
        map.put("userPassword", pwd);
        map.put("Contacts", contacts);
        map.put("Phone", phone);
        map.put("Type", type);

        HTTPTool.post("register", url, map, responseHandler);
    }

    /**
     * 企业坐标点搜索接口
     *
     * @param startLong
     * @param startLat
     * @param endLong
     * @param endLat
     * @param responseHandler
     */
    public static void enterpriseCoordinate(String startLong, String startLat, String endLong, String endLat, ResponseHandler responseHandler) {
        String url = HttpConfig.REQUEST_URL + "/Map/GetEnterpriseMapPoint";
        Map<String, String> map = new HashMap<String, String>();
        map.put("startLong", startLong);
        map.put("startLat", startLat);
        map.put("endLong", endLong);
        map.put("endLat", endLat);
        HTTPTool.post("company", url, map, responseHandler);
    }

    /**
     * 获取指定企业信息
     *
     * @param buildEnterprise_id
     */
    public static void specifyEnterpriseInformation(int buildEnterprise_id, ResponseHandler responseHandler) {
        String url = HttpConfig.REQUEST_URL + "/Map/GetEnterpriseById";
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("id", buildEnterprise_id);
        HTTPTool.post("companyDetail", url, map, responseHandler);
    }

    /**
     * 获取指定父类节点的所有类别
     *
     * @param pid
     */
    public static void allCategories(int pid, ResponseHandler responseHandler) {
        String url = HttpConfig.REQUEST_URL + "/Sys/GetShopClassByPId";
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("pid", pid);
        HTTPTool.post("category", url, map, responseHandler);
    }

    /**
     * 承建坐标搜索接口
     *
     * @param startLong
     * @param startLat
     * @param endLong
     * @param endLat
     * @param responseHandler
     */
    public static void ConstructionCoordinate(String startLong, String startLat, String endLong, String endLat, ResponseHandler responseHandler) {
        String url = HttpConfig.REQUEST_URL + "/Map/GetConstructionMapPoint";
        Map<String, String> map = new HashMap<String, String>();
        map.put("startLong", startLong);
        map.put("startLat", startLat);
        map.put("endLong", endLong);
        map.put("endLat", endLat);
        HTTPTool.post("sites", url, map, responseHandler);
    }

    /**
     * 获取指定承建信息
     *
     * @param buildSite_id
     */
    public static void specifySitesInformation(int buildSite_id, ResponseHandler responseHandler) {
        String url = HttpConfig.REQUEST_URL + "/Map/GetConstructionById";
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("id", buildSite_id);
        HTTPTool.post("buildDetail", url, map, responseHandler);
    }

    /**
     * 分页获取五金超市信息
     *
     * @param parentId
     * @param pageIndex
     * @param pageSize
     * @param responseHandler
     */
    public static void hardSuperInformation(String parentId, String pageIndex, String pageSize, String search, ResponseHandler responseHandler) {
        String url = HttpConfig.REQUEST_URL + "/Supermarket/GetEquipmentList";
        Map<String, String> map = new HashMap<String, String>();
        map.put("parentId", parentId);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        map.put("search", search);
        LogUtil.d("params====", map.toString());
        HTTPTool.post("hardSuper", url, map, responseHandler);
    }

    /**
     * 分页获取五金超市信息
     *
     * @param context
     */
    public static void hardSuperInformation(Context context) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("parentId", "0");
            jsonObject.put("pageIndex", "1");
            jsonObject.put("pageSize", "10");
            jsonObject.put("search", "");
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            String url = HttpConfig.REQUEST_URL + "/Supermarket/GetEquipmentList";
            RequestHandle post = HTTPTool.getClient().post(context, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("==============", response.toString());
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

    /**
     * 获取首页图片轮播
     */
    public static void homeCarousel(ResponseHandler responseHandler) {
        String url = HttpConfig.REQUEST_URL + "/Other/GetHomeImages";
        HTTPTool.get(url, responseHandler);
    }

    /**
     * 登陆
     *
     * @param name
     * @param pwd
     * @param responseHandler
     */
    public static void login(String name, String pwd, ResponseHandler responseHandler) {
        String url = HttpConfig.REQUEST_URL + HttpConfig.TYPE_USER + "/login";
        Map<String, String> map = new HashMap<String, String>();
        map.put("userName", name);
        map.put("userPassword", pwd);
        map.put("versionInfo", "");
        map.put("deviceInfo", "");
        HTTPTool.post(name, url, map, responseHandler);
    }

    /**
     * 获取好友列表
     *
     * @param name
     * @param pwd
     * @param responseHandler
     */
    public static void getFriendList(String name, String pwd, ResponseHandler responseHandler) {
        String url = HttpConfig.REQUEST_URL + HttpConfig.TYPE_ROOM + "/" + HttpConfig.TYPE_MEMBER;
        Map<String, String> map = new HashMap<String, String>();
        map.put("userName", name);
        map.put("userPassword", pwd);
        map.put("versionInfo", "");
        map.put("deviceInfo", "");
        HTTPTool.post(name, url, map, responseHandler);
    }

}
