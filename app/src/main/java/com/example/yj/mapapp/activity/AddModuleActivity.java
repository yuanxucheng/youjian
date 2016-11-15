package com.example.yj.mapapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.AddModuleAdapter;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.Image;
import com.example.yj.mapapp.view.MButton;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 添加模块
 */
public class AddModuleActivity extends BaseActivity {

    @Bind(R.id.id_back)
    ImageView back;

    @Bind(R.id.add_module_btn_jzgd)
    Button jzgd;

    @Bind(R.id.add_module_btn_jcqy)
    Button jcqy;

    @Bind(R.id.add_module_btn_glqy)
    Button glqi;

    @Bind(R.id.add_module_btn_wjcs)
    Button wjcs;

    @Bind(R.id.add_module_btn_lwzj)
    Button lwzj;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

    @OnClick(R.id.add_module_btn_jzgd)
    public void jzgd(View v) {
        finish();
    }

    @OnClick(R.id.add_module_btn_jcqy)
    public void jcqy(View v) {
        finish();
    }

    @OnClick(R.id.add_module_btn_glqy)
    public void glqy(View v) {
        finish();
    }

    @OnClick(R.id.add_module_btn_wjcs)
    public void wlcs(View v) {
        finish();
    }

    @OnClick(R.id.add_module_btn_lwzj)
    public void lwzj(View v) {
        finish();
    }

    //    @Bind(R.id.my_label_grid)
//    GridView my_label_grid;
    @Bind(R.id.add_module_grid)
    GridView add_module_grid;

//    @OnItemClick(R.id.add_module_grid)
//    void onItemClick(int position) {
//        Toast.makeText(this, "Clicked position " + position + "!", Toast.LENGTH_LONG).show();
//    }

    private ArrayList<Image> addModule_list;//我的标签集合
    private AddModuleAdapter addModuleAdapter;//我的标签适配器

//    private ArrayList<Label> recommendLabel_list;//推荐标签集合
//    private RecommendLabelAdapter recommendLabelAdapter;//推荐标签适配器

    @Override
    public int bindLayout() {
        return R.layout.activity_add_module;
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

        initData();

        add_module_grid.setAdapter(addModuleAdapter);//设置我的标签适配器
        add_module_grid.setOnItemClickListener(addModuleGridItemOnclick);//我的标签适配器item点击事件

//        recommend_label_grid.setAdapter(recommendLabelAdapter);//设置我的标签适配器
//        recommend_label_grid.setOnItemClickListener(recommendLabelGridItemOnclick);//我的标签适配器item点击事件
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }

//    /**
//     * 推荐标签item点击事件
//     */
//    private AdapterView.OnItemClickListener recommendLabelGridItemOnclick = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//            //根据我的标签集合长度判断是否可以继续添加标签
//            if (myLabel_list.size() < 6) {
//                Label label = (Label) adapterView.getItemAtPosition(pos);
//                myLabel_list.add(label);//将标签添加到集合中
//                recommendLabel_list.remove(pos);//移除标签
//                myLabelAdapter.notifyDataSetChanged();//刷新我的标签适配器
//                recommendLabelAdapter.notifyDataSetChanged();//刷新推荐标签适配器
//            } else {
//                Toast.makeText(LabelManagerActivity.this, "标签已满,不能添加!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };

    /**
     * 我的标签item点击事件
     */
    private AdapterView.OnItemClickListener addModuleGridItemOnclick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
            //显示泡泡窗口
            ToastUtil.shortT(AddModuleActivity.this, "点击了!");
            LogUtil.d("tag", "输出了");
        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        addModule_list = new ArrayList<Image>();
//        for (int i = 0; i < 6; i++) {
//            MButton button = new MButton(this);
//            button.setBackgroundResource(R.mipmap.desktop_icon);
//            label.setContent("爱运动" + i);
//            myLabel_list.add(label);
//        }

//        recommendLabel_list = new ArrayList<Label>();
//        for (int i = 0; i < 8; i++) {
//            Label label = new Label();
//            label.setHide(false);//默认为不隐藏
//            label.setContent("爱宠物" + i);
//            if (i == 6) {
//                label.setContent("教师");
//            }
//            recommendLabel_list.add(label);
//        }

        addModuleAdapter = new AddModuleAdapter(addModule_list, this);
//        recommendLabelAdapter = new RecommendLabelAdapter(recommendLabel_list, this);
    }

    /**
     * 删除item并刷新适配器
     *
     * @param position
     */
    public void deleteItem(int position) {
        addModule_list.remove(position);
        addModuleAdapter.notifyDataSetChanged();
    }

    /**
     * 隐藏item刷新适配器
     */
    public void hideItem() {
        addModuleAdapter.notifyDataSetChanged();
    }

}
