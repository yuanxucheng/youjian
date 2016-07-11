package com.example.yj.mapapp.activity;

import android.os.Bundle;
import android.view.View;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.CityAdapter;
import com.example.yj.mapapp.data.CityData;
import com.example.yj.mapapp.view.CityItem;
import com.example.yj.mapapp.view.ContactItemInterface;
import com.example.yj.mapapp.view.ContactListViewImpl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

public class CityListActivity extends Activity implements TextWatcher {

    private final static String TAG = "CityListActivity";

    private Context context_ = CityListActivity.this;
    //ListView实现类
    private ContactListViewImpl listview;
    //EditText对象
    private EditText searchBox;
    //搜索字符串
    private String searchString;
    //搜索锁对象
    private Object searchLock = new Object();
    boolean inSearchMode = false;
    private List<ContactItemInterface> contactList;
    private List<ContactItemInterface> filterList;
    //搜索列表任务对象
    private SearchListTask curSearchTask = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        //创建ContactItemInterface集合
        filterList = new ArrayList<ContactItemInterface>();
        //获取城市列表集合
        contactList = CityData.getSampleContactList();
        //实例化城市适配器对象
        CityAdapter adapter = new CityAdapter(this, R.layout.city_item, contactList);
        //通过findViewById方法获得listview对象
        listview = (ContactListViewImpl) this.findViewById(R.id.listview);
        //设置为true,表示快速滑动到指定的位置
        listview.setFastScrollEnabled(true);
        //设置适配器
        listview.setAdapter(adapter);
        //ListView控件的监听
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position,
                                    long id) {
                List<ContactItemInterface> searchList = inSearchMode ? filterList
                        : contactList;

                Toast.makeText(context_,
                        searchList.get(position).getDisplayInfo(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        //通过findViewById方法找到EditText控件对象
        searchBox = (EditText) findViewById(R.id.input_search_query);
        //为EditText控件设置文本改变监听事件
        searchBox.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        searchString = searchBox.getText().toString().trim().toUpperCase();

        if (curSearchTask != null
                && curSearchTask.getStatus() != AsyncTask.Status.FINISHED) {
            try {
                curSearchTask.cancel(true);
            } catch (Exception e) {
                Log.i(TAG, "Fail to cancel running search task");
            }
        }
        //实例化SearchListTask对象
        curSearchTask = new SearchListTask();
        //执行异步任务
        curSearchTask.execute(searchString);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // do nothing
    }

    private class SearchListTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            filterList.clear();
            String keyword = params[0];
            inSearchMode = (keyword.length() > 0);
            if (inSearchMode) {
                // get all the items matching this
                for (ContactItemInterface item : contactList) {
                    CityItem contact = (CityItem) item;

                    boolean isPinyin = contact.getFullName().toUpperCase().indexOf(keyword) > -1;
                    boolean isChinese = contact.getNickName().indexOf(keyword) > -1;
                    if (isPinyin || isChinese) {
                        filterList.add(item);
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            synchronized (searchLock) {
                if (inSearchMode) {
                    CityAdapter adapter = new CityAdapter(context_, R.layout.city_item, filterList);
                    adapter.setInSearchMode(true);
                    listview.setInSearchMode(true);
                    listview.setAdapter(adapter);
                } else {
                    CityAdapter adapter = new CityAdapter(context_, R.layout.city_item, contactList);
                    adapter.setInSearchMode(false);
                    listview.setInSearchMode(false);
                    listview.setAdapter(adapter);
                }
            }
        }
    }
}
