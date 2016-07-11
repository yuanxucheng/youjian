package com.example.yj.mapapp.net.handler;

import android.content.Context;

import com.example.yj.mapapp.base.MApplication;
import com.example.yj.mapapp.util.CacheShareRefrenceUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class HTTPTool {
    private static final int timeOut = 1000 * 30;
    /**
     * 异步的HTTP客户端实例
     **/
    protected static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * 默认字符集
     **/
    public static String DEFAULT_CHARSET = "UTF-8";
    /**
     * 是否缓存数据
     **/
    private static boolean isCacheData = true;

    private static String cache = "NET_CACHE";

    public HTTPTool() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }
        });
    }


    /**
     * 获取HTTP客户端组件
     *
     * @return
     */
    public static AsyncHttpClient getClient() {
        client.setTimeout(timeOut);
        return client;
    }

    /**
     * 模拟GET表单（无参数）
     *
     * @param url             请求URL
     * @param responseHandler 回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/TextHttpResponseHandler）
     */
    public static void get(String url, ResponseHandler responseHandler) {
        if (isCacheData) {
            String content = CacheShareRefrenceUtil.getString(cache, "");
            responseHandler.onCacheData(content);
        }
        if (checkNetwork()) {
            //关闭过期连接.

//            client.getHttpClient().getConnectionManager().closeExpiredConnections();

            getClient().get(url, responseHandler);
        }
    }

    /**
     * 模拟GET表单（有参数）
     *
     * @param url             请求URL
     * @param parmsMap        参数
     * @param responseHandler 回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/TextHttpResponseHandler）
     */
    public static void get(String url, Map<String, ?> parmsMap, ResponseHandler responseHandler, String... charset) {
        if (isCacheData) {
            String content = CacheShareRefrenceUtil.getString(cache, "");
            responseHandler.onCacheData(content);
        }
        if (checkNetwork()) {
            closeExpiredConnections();
            if (null != charset && charset.length > 0) {
                DEFAULT_CHARSET = charset[0];
            }
            getClient().get(url, fillParms(parmsMap, DEFAULT_CHARSET), responseHandler);
        }
    }

    /**
     * 模拟GET表单（有参数）
     *
     * @param context         上下文
     * @param url             请求URL
     * @param parmsMap        参数
     * @param responseHandler 回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/TextHttpResponseHandler）
     */
    public static void get(Context context, String url, Map<String, ?> parmsMap, ResponseHandler responseHandler, String... charset) {
        if (isCacheData) {
            String content = CacheShareRefrenceUtil.getString(cache, "");
            responseHandler.onCacheData(content);
        }
        if (checkNetwork()) {
            closeExpiredConnections();
            if (null != charset && charset.length > 0) {
                DEFAULT_CHARSET = charset[0];
            }
            getClient().get(context, url, fillParms(parmsMap, DEFAULT_CHARSET), responseHandler);
        }
    }

    /**
     * 模拟POST表单（无参数）
     *
     * @param url             请求URL
     * @param responseHandler 回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/TextHttpResponseHandler）
     */
    public static void post(String url, ResponseHandler responseHandler) {
        if (isCacheData) {
            String content = CacheShareRefrenceUtil.getString(cache, "");
            responseHandler.onCacheData(content);
        }
        if (checkNetwork()) {
            closeExpiredConnections();

            getClient().post(url, responseHandler);
        }
    }

    /**
     * 模拟POST表单（有参数）
     *
     * @param url             请求URL
     * @param parmsMap        参数
     * @param responseHandler 回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/TextHttpResponseHandler）
     */
    public static void post(String cache_name, String url, Map<String, ?> parmsMap, ResponseHandler responseHandler, String... charset) {
        if (isCacheData) {
            String content = CacheShareRefrenceUtil.getString(cache_name, "");
            responseHandler.onCacheData(content);
        }
        if (checkNetwork()) {
            closeExpiredConnections();
            if (null != charset && charset.length > 0) {
                DEFAULT_CHARSET = charset[0];
            }
            getClient().post(url, fillParms(parmsMap, DEFAULT_CHARSET), responseHandler);
        }
    }

    /**
     * 模拟POST表单（有参数）
     *
     * @param context         上下文
     * @param url             请求URL
     * @param parmsMap        参数
     * @param responseHandler 回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/TextHttpResponseHandler）
     */
    public static void post(Context context, String url, Map<String, ?> parmsMap, ResponseHandler responseHandler, String... charset) {
        if (isCacheData) {
            String content = CacheShareRefrenceUtil.getString(cache, "");
            responseHandler.onCacheData(content);
        }
        if (checkNetwork()) {
            closeExpiredConnections();
            if (null != charset && charset.length > 0) {
                DEFAULT_CHARSET = charset[0];
            }
            getClient().post(context, url, fillParms(parmsMap, DEFAULT_CHARSET), responseHandler);

        }
    }

    /**
     * 模拟提交POST表单（无参数）
     *
     * @param context         上下文
     * @param url             请求URL
     * @param entity          请求实体,可以null
     * @param contentType     表单contentType （"multipart/form-data"）
     * @param responseHandler 回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/TextHttpResponseHandler）
     */
    public static void post(Context context, String url, HttpEntity entity, String contentType, ResponseHandler responseHandler) {
        if (isCacheData) {
            String content = CacheShareRefrenceUtil.getString(cache, "");
            responseHandler.onCacheData(content);
        }
        if (checkNetwork()) {
            closeExpiredConnections();
            getClient().post(context, url, entity, contentType, responseHandler);

        }
    }

    /**
     * 模拟提交POST表单（有参数）
     *
     * @param context         上下文
     * @param url             请求URL
     * @param headers         请求Header
     * @param parmsMap        参数
     * @param contentType     表单contentType （"multipart/form-data"）
     * @param responseHandler 回调Handler（BinaryHandler/JSONHandler/JSONArrayHandler/TextHttpResponseHandler）
     */
    public static void post(Context context, String url, Header[] headers, Map<String, ?> parmsMap, String contentType, ResponseHandler responseHandler, String... charset) {
        if (isCacheData) {
            String content = CacheShareRefrenceUtil.getString(cache, "");
            responseHandler.onCacheData(content);
        }
        if (checkNetwork()) {
            closeExpiredConnections();
            if (null != charset && charset.length > 0) {
                DEFAULT_CHARSET = charset[0];
            }
            getClient().post(context, url, headers, fillParms(parmsMap, DEFAULT_CHARSET), contentType, responseHandler);
        }
    }

    /**
     * 装填参数
     *
     * @param map 参数
     * @return
     */
    public static RequestParams fillParms(Map<String, ?> map, String charset) {
        RequestParams params = new RequestParams();
        if (null != map && map.entrySet().size() > 0) {
            //设置字符集,防止参数提交乱码
            if (!"".equals(charset)) {
                params.setContentEncoding(charset);
            }
            for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
                Entry entity = (Entry) iterator.next();
                Object key = entity.getKey();
                Object value = entity.getValue();
                if (value instanceof File) {
                    try {
                        params.put((String) key, new FileInputStream((File) value), ((File) value).getName());
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException("文件不存在！", e);
                    }
                } else if (value instanceof InputStream) {
                    params.put((String) key, (InputStream) value);
                } else {
                    params.put((String) key, value.toString());
                }
            }
        }
        return params;
    }

    /**
     * 检测网络状态
     *
     * @return 网络是否连接
     */
    public static boolean checkNetwork() {
        boolean isConnected = MApplication.isNetworkReady();
        if (isConnected) {
            return true;
        } else {
            ToastUtil.shortT(MApplication.getApplication(), "网络连接失败");
            return false;
        }
    }


    /**
     * 停止请求
     *
     * @param mContext 发起请求的上下文
     */
    public static void stopRequest(Context mContext) {
        getClient().cancelRequests(mContext, true);
    }

    /**
     * 停止全部请求
     */
    public static void stopAllRequest() {
        getClient().cancelAllRequests(true);
    }

    private static void closeExpiredConnections() {
        //关闭过期连接.
//        client.getHttpClient().getConnectionManager().closeExpiredConnections();
    }
}
