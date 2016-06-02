package com.example.yj.mapapp.net.handler;

import android.util.SparseArray;

import com.example.yj.mapapp.model.IndustryClassification;
import com.example.yj.mapapp.util.JsonParser;
import com.example.yj.mapapp.util.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

public class SubListHandler extends ResponseHandler {
    private int position;
    private SparseArray<LinkedList<IndustryClassification>> children;

    public SubListHandler(int position, SparseArray<LinkedList<IndustryClassification>> children) {
        this.position = position;
        this.children = children;
    }

    @Override
    public void onFail(int arg0, String arg2, Throwable arg3) {

    }

    @Override
    public void success(String data, String resCode, String info) {
        LinkedList<IndustryClassification> tItem = new LinkedList<IndustryClassification>();
        IndustryClassification ic = new IndustryClassification();
        ic.setP_Name("全部");
        tItem.add(ic);
        if (resCode.equals("")) {
            LogUtil.d("data===================" + data);
            LogUtil.d("获取指定父类节点的所有类别,成功==============");

            //后台返回的数据进行过处理的,使用GSON解析
            List<IndustryClassification> Classification = JsonParser.deserializeFromJson(data, new TypeToken<List<IndustryClassification>>() {
            }.getType());
            ic.setP_Id(Classification.get(0).getP_ParentId());
            for (IndustryClassification classification : Classification) {
                tItem.add(classification);
            }
        } else {
//                ToastUtil.shortT(BuildEnterprisesActivity.this, "获取指定父类节点的所有类别失败");
            LogUtil.d("==============获取指定父类节点的所有类别,失败");
        }

        children.put(position, tItem);
    }

    @Override
    public void onCacheData(String content) {

    }
}
