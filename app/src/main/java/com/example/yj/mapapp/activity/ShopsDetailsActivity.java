package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.banner.ADInfo;
import com.example.yj.mapapp.banner.CycleViewPager;
import com.example.yj.mapapp.banner.ViewFactory;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商铺详情
 */
public class ShopsDetailsActivity extends BaseActivity {

    private final static String tag = "ShopsDetailsActivity-->";

    /**
     * 轮播图
     */
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPager cycleViewPager;

    private Handler handler;

    @Bind(R.id.id_back)
    ImageView back;

    @Bind(R.id.tv_build_enterprise_companyName)
    TextView build_enterprise_companyName;

    @Bind(R.id.tv_build_enterprise_address)
    TextView build_enterprise_address;

    @Bind(R.id.tv_build_enterprise_contacts)
    TextView build_enterprise_contacts;

    @Bind(R.id.tv_build_enterprise_phone)
    TextView build_enterprise_phone;

    @Bind(R.id.id_buildEnterprise_isShowPhone)
    TextView buildEnterprise_isShowPhone;

    @Bind(R.id.iv_build_enterprise_callPhone)
    ImageView build_enterprise_callPhone;

    @Bind(R.id.tv_build_enterprise_YJAuthentication)
    TextView build_enterprise_YJAuthentication;

    @Bind(R.id.id_buildEnterprise_isShowYJAuthentication)
    TextView buildEnterprise_isShowYJAuthentication;

    @Bind(R.id.shop_details_advert_layout)
    FrameLayout shop_details_advert_layout;

    @Bind(R.id.close_button)
    ImageButton imageButton;

    @Bind(R.id.view)
    View view;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_shops_details;
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

        handler = new Handler();//实例化Handler对象

        //获取上一个界面携带的数据
        String SI_CompanyName = getIntent().getStringExtra("SI_CompanyName");
        String SI_Address = getIntent().getStringExtra("SI_Address");
        String SI_Contacts = getIntent().getStringExtra("SI_Contacts");
        String SI_Phone = getIntent().getStringExtra("SI_Phone");
        int SI_Id = getIntent().getIntExtra("SI_Id", -1);
        String imagesURL = getIntent().getStringExtra("imagesURL");
        int SI_YJAuthentication = getIntent().getIntExtra("SI_YJAuthentication", -1);
        String buffer = getIntent().getStringExtra("buffer");

        initialize(buffer);

        //设置数据
        build_enterprise_companyName.setText(getString(R.string.buildEnterprises_companyName) + SI_CompanyName);
        build_enterprise_address.setText(getString(R.string.buildEnterprises_address) + SI_Address);
        build_enterprise_contacts.setText(getString(R.string.buildEnterprises_contacts) + SI_Contacts);

        //判断认证状态
        if (SI_YJAuthentication == 0) {
            buildEnterprise_isShowYJAuthentication.setVisibility(View.VISIBLE);
            build_enterprise_YJAuthentication.setText("未认证");
            build_enterprise_YJAuthentication.setTextColor(getResources().getColor(R.color.SI_YJAuthentication_blue));
        } else if (SI_YJAuthentication == 1) {
            buildEnterprise_isShowYJAuthentication.setVisibility(View.VISIBLE);
            build_enterprise_YJAuthentication.setText("已认证");
            build_enterprise_YJAuthentication.setTextColor(getResources().getColor(R.color.SI_YJAuthentication_red));
        }

        //截取电话号码
        if (SI_Phone.contains("、")) {//判断是否包含两个号码
            String[] s = SI_Phone.split("、");//使用split()方法截取
            String phone = s[0];//默认截取第一个
            LogUtil.d("phone=========" + phone);
            build_enterprise_phone.setText(phone);
        } else {
            LogUtil.d("SI_Phone=========" + SI_Phone);
            build_enterprise_phone.setText(SI_Phone);
        }
        //设置控件的显示与隐藏
        build_enterprise_callPhone.setVisibility(View.VISIBLE);
        buildEnterprise_isShowPhone.setVisibility(View.VISIBLE);
        build_enterprise_phone.setVisibility(View.GONE);
        //点击事件监听
        build_enterprise_callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拨打电话
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);//指定意图动作
                intent.setData(Uri.parse("tel:" + build_enterprise_phone.getText()));//指定电话号码
                startActivity(intent);
            }
        });
        //线程延迟
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //开启线程
                handler.post(run);
            }
        }, 10000);
    }

    /**
     * 轮播图初始化
     * @param imageUrl
     */
    private void initialize(String imageUrl) {
        cycleViewPager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);
        String str1 = "";
        String str2 = "";
        String str3 = "";
        LogUtil.d("imageUrl:" + imageUrl + "----------------");

        String[] str = imageUrl.split(" ");
        for (int i = 0; i < str.length; i++) {
            str1 = str[1];
            str2 = str[2];
            str3 = str[3];
            LogUtil.d("str1----" + str1 + "==============");
            LogUtil.d("str2---" + str2 + "==============");
            LogUtil.d("str3---" + str3 + "==============");
        }
        LogUtil.d("-------" + str1 + "----------");
        LogUtil.d("-------" + str2 + "-------------");
        LogUtil.d("-------" + str3 + "---------------");

        String[] imageUrls = {HttpConfig.BUILD_BANNER_URL + "/" + str1, HttpConfig.BUILD_BANNER_URL + "/" + str2, HttpConfig.BUILD_BANNER_URL + "/" + str3};

//         String[] imageUrls = {"http://www.51buyjc.com/Content/image/shopCommodity_1.jpg",
//                "http://www.51buyjc.com/Content/image/shopCommodity_2.jpg",
//                "http://www.51buyjc.com/Content/image/shopCommodity_3.jpg",};

        for (int i = 0; i < imageUrls.length; i++) {
            ADInfo info = new ADInfo();
            info.setUrl(imageUrls[i]);
            info.setContent("图片-->" + i);
            infos.add(info);
        }
//        ADInfo info = new ADInfo();
//        info.setUrl(imageUrls);
//        info.setContent("图片-->");
//        infos.add(info);

        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));
        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);
        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);
        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    /**
     * 轮播图滑动点击事件
     */
    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
                Toast.makeText(ShopsDetailsActivity.this,
                        "position-->" + info.getContent(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    /**
     * 线程
     */
    private Runnable run = new Runnable() {
        int i = 0;
        public void run() {
            if (i < 6) {
                i++;
                view.setBackgroundResource(R.mipmap.login_logo);
                shop_details_advert_layout.setVisibility(View.VISIBLE);
                shop_details_advert_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.shortT(ShopsDetailsActivity.this, "建设中,敬请期待...");
                    }
                });
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shop_details_advert_layout.setVisibility(View.GONE);
                        i = 6;
                    }
                });
                //延迟加载
                handler.postDelayed(run, 1000);
            } else {
                //设置控件不可见
                shop_details_advert_layout.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }
}
