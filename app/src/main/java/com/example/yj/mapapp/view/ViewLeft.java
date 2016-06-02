package com.example.yj.mapapp.view;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.activity.BuildEnterprisesActivity;
import com.example.yj.mapapp.activity.HardSuperActivity;
import com.example.yj.mapapp.adapter.TextAdapter;
import com.example.yj.mapapp.model.HardSuper;
import com.example.yj.mapapp.model.IndustryClassification;
import com.example.yj.mapapp.model.NextClassification;
import com.example.yj.mapapp.net.handler.HTTPTool;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.net.handler.HttpUtil;
import com.example.yj.mapapp.net.handler.ResponseHandler;
import com.example.yj.mapapp.net.handler.SubListHandler;
import com.example.yj.mapapp.util.JsonParser;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewLeft extends LinearLayout implements ViewBaseAction {

    private ListView regionListView;
    private ListView plateListView;
    private LinkedList<IndustryClassification> childrenItem = new LinkedList<IndustryClassification>();
    private SparseArray<LinkedList<IndustryClassification>> children = new SparseArray<LinkedList<IndustryClassification>>();
    private TextAdapter plateListViewAdapter;
    private TextAdapter earaListViewAdapter;
    private OnSelectListener mOnSelectListener;
    private int tEaraPosition = 0;
    private int tBlockPosition = 0;
    private String showString = "不限";

    //    private List<IndustryClassification> mClassificationList;
    private List<IndustryClassification> mNameList = new ArrayList<>();

    public ViewLeft(Context context) {
        super(context);
        init(context);
    }

    public ViewLeft(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private ResponseHandler allCategoriesHandler = new ResponseHandler() {
        @Override
        public void onCacheData(String content) {
            LogUtil.w("========onCacheData=========" + content);
        }

        @Override
        public void success(String data, String resCode, String info) {
            if (resCode.equals("")) {
                LogUtil.d("data===================" + data);
                LogUtil.d("获取指定父类节点的所有类别,成功==============");

                //后台返回的数据进行过处理的,使用GSON解析
                List<IndustryClassification> Classification = JsonParser.deserializeFromJson(data, new TypeToken<List<IndustryClassification>>() {
                }.getType());
                IndustryClassification classification = new IndustryClassification();
                classification.setP_Name("全部分类");
                mNameList.add(classification);
                LinkedList<IndustryClassification> tItem = new LinkedList<IndustryClassification>();
                children.put(0, tItem);
                for (IndustryClassification ic : Classification) {
                    LogUtil.d("tag", "id:" + ic.getP_Id() + "========name:" + ic.getP_Name() + "==========P_ParentId:" + ic.getP_ParentId() + "==========P_Desc:" + ic.getP_Desc());

                    IndustryClassification industryC = new IndustryClassification();
                    industryC.setP_Id(ic.getP_Id());
                    industryC.setP_Name(ic.getP_Name());
                    industryC.setP_ParentId(ic.getP_ParentId());
                    industryC.setP_Desc(ic.getP_Desc());

                    LogUtil.d("p_id:==================" + ic.getP_Id());

//                    mClassificationList.add(industryC);

//                    mNameList.add(ic.getP_Name());
//                    mNameList.add(ic.getP_Id());

                    mNameList.add(ic);

                    HttpUtil.allCategories(ic.getP_Id(), new SubListHandler(mNameList.size() - 1, children));
                }
            } else {
//                ToastUtil.shortT(BuildEnterprisesActivity.this, "获取指定父类节点的所有类别失败");
                LogUtil.d("==============获取指定父类节点的所有类别,失败");
            }
        }

        @Override
        public void onFail(int arg0, String arg2, Throwable arg3) {
//            ToastUtil.shortT(BuildEnterprisesActivity.this, "获取指定父类节点的所有类别失败");
            LogUtil.d("==============获取指定父类节点的所有类别,失败");
        }
    };

    public void updateShowText(String showArea, String showBlock) {
        if (showArea == null || showBlock == null) {
            return;
        }
        for (int i = 0; i < mNameList.size(); i++) {
            if (mNameList.get(i).equals(showArea)) {
                earaListViewAdapter.setSelectedPosition(i);
                childrenItem.clear();
                if (i < children.size()) {
                    childrenItem.addAll(children.get(i));
                }
                tEaraPosition = i;
                break;
            }
        }
        for (int j = 0; j < childrenItem.size(); j++) {
            if (childrenItem.get(j).getP_Name().replace("不限", "").equals(showBlock.trim())) {
                plateListViewAdapter.setSelectedPosition(j);
                tBlockPosition = j;
                break;
            }
        }
        setDefaultSelect();
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_region, this, true);
        regionListView = (ListView) findViewById(R.id.listView);
        plateListView = (ListView) findViewById(R.id.listView2);
        setBackgroundDrawable(getResources().getDrawable(
                R.drawable.choosearea_bg_left));

//        mClassificationList = new ArrayList<>();

        HttpUtil.allCategories(0, allCategoriesHandler);

        earaListViewAdapter = new TextAdapter(context, mNameList,
                R.mipmap.choose_item_selected,
                R.drawable.choose_eara_item_selector);
        earaListViewAdapter.setTextSize(17);
        earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
        regionListView.setAdapter(earaListViewAdapter);
        earaListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (position < children.size()) {
                            childrenItem.clear();
                            childrenItem.addAll(children.get(position));
                            plateListViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
        if (tEaraPosition < children.size())
            childrenItem.addAll(children.get(tEaraPosition));
        plateListViewAdapter = new TextAdapter(context, childrenItem,
                R.mipmap.choose_item_right,
                R.drawable.choose_plate_item_selector);
        plateListViewAdapter.setTextSize(15);
        plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
        plateListView.setAdapter(plateListViewAdapter);
        plateListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, final int position) {

                        view.setId(childrenItem.get(position).getP_Id());

                        IndustryClassification classification = childrenItem.get(position);

                        if (mOnSelectListener != null) {

                            mOnSelectListener.getValue(classification);
                        }
                        // 加气泡
                    }
                });
        if (tBlockPosition < childrenItem.size())
            showString = childrenItem.get(tBlockPosition).getP_Name();
        if (showString.contains("不限")) {
            showString = showString.replace("不限", "");
        }
        setDefaultSelect();

    }

    public void setDefaultSelect() {
        regionListView.setSelection(tEaraPosition);
        plateListView.setSelection(tBlockPosition);
    }

    public String getShowText() {
        return showString;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(IndustryClassification showText);
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }
}
