package com.example.yj.mapapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.SearchAdapter;
import com.example.yj.mapapp.model.Contact;
import com.example.yj.mapapp.model.Firm;
import com.example.yj.mapapp.model.HistoryAdapter;
import com.example.yj.mapapp.model.SearchBeams;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.MProgressDialog;
import com.example.yj.mapapp.view.SearchView;
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

public class SearchFirmActivity extends Activity implements SearchView.SearchViewListener {

    /**
     * 搜索结果列表view
     */
    private ListView lvResults;

    /**
     * 搜索view
     */
    private SearchView searchView;


    /**
     * 热搜框列表adapter
     */
    private ArrayAdapter<String> hintAdapter;

    /**
     * 自动补全列表adapter
     */
    private ArrayAdapter<String> autoCompleteAdapter;

    /**
     * 搜索结果列表adapter
     */
    private SearchAdapter resultAdapter;

    private List<Firm> dbData;

    /**
     * 热搜版数据
     */
    private List<String> hintData;

    /**
     * 搜索过程中自动补全数据
     */
    private List<String> autoCompleteData;

    /**
     * 搜索结果的数据
     */
    private List<Firm> resultData;

    /**
     * 默认提示框显示项的个数
     */
    private static int DEFAULT_HINT_SIZE = 4;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;
    private HistoryAdapter history_adapter;
    private SearchBeams his_beams;
    private List<SearchBeams> data;
    private List<Firm> iData;
    private Firm firm;
    private List<Firm> list = new ArrayList<>();
    private String search;//搜索内容
    private int pageSize = 1000;//每页个数
    private int pageIndex = 1;//当前页数
    private TextView tip;//提示信息
    private Button loadMoreButton;//加载更多
    private View loadMoreView;//加载更多视图

    //显示对话框
    private static final int SHOW = 1;
    //关闭对话框
    private static final int DISMISS = 2;

    //自定义进度对话框
    private MProgressDialog pb;
    //返回
    private ImageView back;
    //第几次进入该界面
    private int count = 0;

    /**
     * 设置提示框显示项的个数
     *
     * @param hintSize 提示框显示个数
     */
    public static void setHintSize(int hintSize) {
        SearchFirmActivity.hintSize = hintSize;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_firm);

        pb = new MProgressDialog(this);//实例化进度对话框对象
        pb.setMessage(getString(R.string.buildSites_loadMap));//设置对话框信息
        pb.setCancelable(true);//设置进度条是否可以按退回键取消
        pb.setCanceledOnTouchOutside(true); //设置点击进度对话框外的区域对话框消失

        initData();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    /**
     * 初始化视图
     */
    private void initViews() {
        back = (ImageView) findViewById(R.id.id_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lvResults = (ListView) findViewById(R.id.main_lv_search_results);
        searchView = (SearchView) findViewById(R.id.main_search_layout);
        tip = (TextView) findViewById(R.id.search_firm_tip);
        //使用打气筒插入顶部试图:加载更多布局
        loadMoreView = getLayoutInflater().inflate(R.layout.lv_loadmore, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
        //设置监听
        searchView.setSearchViewListener(this);
        //设置adapter
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);
        lvResults.setOnItemClickListener(lvResultsListener);
//        loadMoreButton.setOnClickListener(loadMoreListener);
//        lvResults.addFooterView(loadMoreView);    //设置列表底部视图
    }

    private View.OnClickListener loadMoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            loadMoreButton.setText("正在加载中...");   //设置按钮文字
//            if (resultData.size() == 0) {
//                loadMoreButton.setText("数据全部加载完!");
//            }
            pageIndex = pageIndex + 1;
            LogUtil.d("tag", "pageIndex:============" + pageIndex);
            LogUtil.d("tag", "pageSize:============" + pageSize);
            //访问后台接口
            getEnterpriseSearch("", pageIndex, pageSize);
            resultAdapter.notifyDataSetChanged();
//            adapter = new LabourAgencyAdapter(mData, this);//实例化适配器
//            lv.setAdapter(adapter);//为ListView设置适配器
        }
    };

    private AdapterView.OnItemClickListener lvResultsListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Toast.makeText(SearchFirmActivity.this, position + "", Toast.LENGTH_SHORT).show();

            //跳转到显示建材企业详情界面
//            Intent intent = new Intent();
//            intent.setClass(SearchFirmActivity.this, BuildEnterprisesDetailActivity.class);
//            intent.putExtra("buildEnterprise_id", iData.get(position).getId());
//            startActivity(intent);

            Firm info = (Firm) parent.getItemAtPosition(position);
            //跳转到显示建材企业详情界面
            Intent intent = new Intent();
            intent.setClass(SearchFirmActivity.this, BuildEnterprisesDetailActivity.class);
            intent.putExtra("buildEnterprise_id", info.getId());
            startActivity(intent);
        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        //从数据库获取数据
        getEnterpriseSearch("", pageSize, pageIndex);
        //初始化热搜版数据
        getHintData();
        //初始化自动补全数据
        getAutoCompleteDatas(null);
        //初始化搜索结果数据
        getResultData(null);
    }

    /**
     * 获取热搜版data 和adapter
     */
    private void getHintData() {
        hintData = new ArrayList<>(hintSize);
        LogUtil.d("tag", "hintData:--------------" + hintData.size());
        LogUtil.d("tag", "hintSize:--------------" + hintSize);
//        for (int i = 1; i <= hintSize; i++) {
//            hintData.add("热搜版" + i + "：Android自定义View");
        hintData.add("吴善利");
        hintData.add("丁兴仁");
        hintData.add("张盛林");
        hintData.add("柯伟强");
//        }
        LogUtil.d("tag", "hintData:--------------" + hintData.size());
        LogUtil.d("tag", "hintSize:--------------" + hintSize);
        hintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hintData);
    }

    private void getAutoCompleteDatas(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();

//            LogUtil.d("tag", " iData.size1:==========" + iData.size());

            for (int i = 0, count = 0; i < iData.size()
                    && count < hintSize; i++) {
                if (iData.get(i).getName().contains(text.trim()) || iData.get(i).getContacts().contains(text.trim()) || iData.get(i).getAddress().contains(text.trim())) {
                    autoCompleteData.add(iData.get(i).getName());
                    autoCompleteData.add(iData.get(i).getContacts());
                    autoCompleteData.add(iData.get(i).getAddress());
                    count++;
                }
            }

            LogUtil.d("tag", " iData.size2:==========" + iData.size());
        }
        if (autoCompleteAdapter == null) {
//            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
            autoCompleteAdapter = new ArrayAdapter<>(SearchFirmActivity.this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取搜索结果data和adapter
     */
    private void getResultData(String text) {
        if (resultData == null) {
            // 初始化
            resultData = new ArrayList<>();
        } else {
            resultData.clear();
            for (int i = 0; i < iData.size(); i++) {
                if (iData.get(i).getName().contains(text.trim()) || iData.get(i).getContacts().contains(text.trim()) || iData.get(i).getAddress().contains(text.trim())) {
                    resultData.add(iData.get(i));
                }
            }
        }
        if (resultAdapter == null) {
            resultAdapter = new SearchAdapter(this, resultData, R.layout.item_firm_list);
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     *
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        getAutoCompleteDatas(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     *
     * @param text
     */
    @Override
    public void onSearch(String text) {
        //更新result数据
        getResultData(text);
        lvResults.setVisibility(View.VISIBLE);
        //第一次获取结果 还未配置适配器
        if (lvResults.getAdapter() == null) {
            //获取搜索数据 设置适配器
            lvResults.setAdapter(resultAdapter);
        } else {
            //更新搜索数据
            resultAdapter.notifyDataSetChanged();
        }
        if (resultData.size() == 0) {
            tip.setVisibility(View.VISIBLE);
            lvResults.setVisibility(View.GONE);
        } else {
            tip.setVisibility(View.GONE);
        }
        Toast.makeText(this, "完成搜索", Toast.LENGTH_SHORT).show();

    }

    /**
     * 搜索框
     *
     * @param path
     */
    public void writeHistory(String path) {
//        LostFocus.HideInputText(context, title_search_tv_search);
        Context ctx = this;
        SharedPreferences SAVE = ctx.getSharedPreferences("save", MODE_PRIVATE);
        List<String> date = readHistory();
        if (!date.contains(path)) {
            int n = SAVE.getInt("point", 0);
            SharedPreferences.Editor editor = SAVE.edit();
            editor.putString("path" + n, path);
            editor.putInt("point", (n + 1) % 16);
            editor.commit();
        } else {
            int k = SAVE.getInt("point", 0);
            date.remove(path);
            date.add(k - 1, path);
            cleanHistory();
            for (String str : date) {
                int n = SAVE.getInt("point", 0);
                SharedPreferences.Editor editor = SAVE.edit();
                editor.putString("path" + n, str);
                editor.putInt("point", (n + 1) % 16);
                editor.commit();
            }
        }
    }

    // 将存的本地文件读取出来，返回一个list：
    public List<String> readHistory() {
        List<String> list = new ArrayList<String>();
        Context ctx = this;
        SharedPreferences SAVE = ctx.getSharedPreferences("save", MODE_PRIVATE);
        int point = SAVE.getInt("point", 0);
        String path;
        final int N = 16;
        for (int i = 0, n = point; i <= N; i++) {
            path = SAVE.getString("path" + n, null);
            if (path != null) {
                list.add(path);
            }
            n = n > 0 ? (--n) : (--n + N) % 16;
        }
        return list;
    }

    //清空历史记录：
    public void cleanHistory() {
        SharedPreferences sp = getSharedPreferences("save", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        super.onDestroy();
    }

    // 历史记录列表
    private void History_list() {
        data.clear();
        List<String> date = readHistory();
        String[] history_arr = date.toArray(new String[date.size()]);
        if (history_arr.length != 0) {
            if (history_arr.length > 5) {
                String[] newArrays = new String[5];
                // 实现数组之间的复制
                System.arraycopy(history_arr, 0, newArrays, 0, 5);
                for (int i = 0; i < newArrays.length; i++) {
                    his_beams = new SearchBeams();
                    String str = newArrays[i];
                    his_beams.setHistoryTitle(str);
                    data.add(his_beams);
                }
            } else {
                for (int i = 0; i < history_arr.length; i++) {
                    his_beams = new SearchBeams();
                    String str = history_arr[i];
                    his_beams.setHistoryTitle(str);
                    data.add(his_beams);
                }
            }
            history_adapter = new HistoryAdapter(SearchFirmActivity.this, data);
//            main_search_history_listview.setAdapter(history_adapter);
        }
    }

    //读取本地文件，将搜索记录自动匹配展示出来：
//    private void ReadAuto() {
//        List<String> date = readHistory();
//        arr_adapter = new ArrayAdapter<>(context, R.layout.main_search_auto_item_01, date);
//        main_search_title_actv.setAdapter(arr_adapter);
//        main_search_title_actv.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        main_search_title_actv.setThreshold(1);
//    }

    /**
     * 模糊搜索企业（企业名称/地址/联系人）
     *
     * @param search
     * @param pageSize
     * @param pageIndex
     */
    private void getEnterpriseSearch(final String search, int pageSize, int pageIndex) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("search", search);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("pageIndex", pageIndex);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            String url = HttpConfig.REQUEST_URL + "/Map/GetEnterpriseSearch";
            RequestHandle post = HTTPTool.getClient().post(SearchFirmActivity.this, url, stringEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("==============", response.toString());
                    String d = JsonUtil.getData(response.toString());
                    Log.d("-------------", d);

                    try {
                        JSONArray array = new JSONArray(d);
                        //TODO
                        iData = new ArrayList<>();
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int id = object.getInt("Id");
                                String name = object.getString("Name");
                                String address = object.getString("Address");
                                String contacts = object.getString("Contacts");
                                String authentication = object.getString("Authentication");
                                LogUtil.d("tag", id + "-------" + name + "-----" + address + "-----" + contacts + "------" + authentication);

                                iData.add(new Firm(R.mipmap.desktop_icon, id, name, address, contacts, authentication));

                                //发送消息提示关闭对话框
                                Message msg = new Message();
                                msg.what = DISMISS;
                                handler.sendMessage(msg);
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