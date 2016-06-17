package com.example.yj.mapapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yj.mapapp.banner.ADInfo;
import com.example.yj.mapapp.banner.CycleViewPager;
import com.example.yj.mapapp.banner.ViewFactory;
import com.example.yj.mapapp.base.BaseActivity;
import com.example.yj.mapapp.net.handler.HttpConfig;
import com.example.yj.mapapp.net.handler.HttpUtil;
import com.example.yj.mapapp.net.handler.ResponseHandler;
import com.example.yj.mapapp.util.DipUtil;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.yj.mapapp.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuildEnterprisesDetailActivity extends BaseActivity {

    private Handler handler;

    @Bind(R.id.id_back)
    ImageView back;

    @OnClick(R.id.id_back)
    public void back(View v) {
        finish();
    }

    @Bind(R.id.tv_build_enterprise_companyName)
    TextView build_enterprise_companyName;

    @Bind(R.id.tv_build_enterprise_address)
    TextView build_enterprise_address;

    @Bind(R.id.tv_build_enterprise_contacts)
    TextView build_enterprise_contacts;

    @Bind(R.id.tv_build_enterprise_phone)
    TextView build_enterprise_phone;

    @Bind(R.id.iv_build_enterprise_callPhone)
    ImageView build_enterprise_callPhone;

    @Bind(R.id.tv_build_enterprise_YJAuthentication)
    TextView build_enterprise_YJAuthentication;

    @Bind(R.id.id_buildEnterprise_isShowYJAuthentication)
    TextView buildEnterprise_isShowYJAuthentication;

    @Bind(R.id.id_buildEnterprise_isShowPhone)
    TextView buildEnterprise_isShowPhone;

    @Bind(R.id.shop_details_advert_layout)
    FrameLayout shop_details_advert_layout;

    @Bind(R.id.close_button)
    ImageButton imageButton;

    @Bind(R.id.view)
    View view;


    /**
     * 轮播图
     */
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPager cycleViewPager;

//    private String[] imageUrls = {"http://www.51buyjc.com/Content/image/shopCommodity_1.jpg",
//            "http://www.51buyjc.com/Content/image/shopCommodity_2.jpg",
//            "http://www.51buyjc.com/Content/image/shopCommodity_3.jpg",};

//    private String imageUrls = "http://www.51buyjc.com/Content/image/shopCommodity_1.jpg";

    private ResponseHandler specifyEnterpriseInformationHandler = new ResponseHandler() {
        @Override
        public void onCacheData(String content) {
            LogUtil.w("========onCacheData=========" + content);
        }

        @Override
        public void success(String data, String resCode, String info) {
            if (resCode.equals("")) {
                LogUtil.d("data===================" + data);
                LogUtil.d("获取指定企业信息接口,成功==============");

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int SI_Id = jsonObject.optInt("SI_Id");
                    String imagesURL = jsonObject.optString("imagesURL");
                    String SI_CompanyName = jsonObject.optString("SI_CompanyName");
                    String SI_Address = jsonObject.optString("SI_Address");
                    String SI_Contacts = jsonObject.optString("SI_Contacts");
                    String SI_Phone = jsonObject.optString("SI_Phone");
                    int SI_YJAuthentication = jsonObject.optInt("SI_YJAuthentication");

                    LogUtil.d("imagesURL==========" + ":" + imagesURL);
                    LogUtil.d("SI_Id==========" + ":" + SI_Id);
                    LogUtil.d("SI_YJAuthentication==========" + ":" + SI_YJAuthentication);

                    build_enterprise_companyName.setText(getString(R.string.buildEnterprises_companyName) + SI_CompanyName);
                    build_enterprise_address.setText(getString(R.string.buildEnterprises_address) + SI_Address);
                    build_enterprise_contacts.setText(getString(R.string.buildEnterprises_contacts) + SI_Contacts);

                    //截取URL地址
                    int i = 0;
                    String[] str = imagesURL.split("../../");
                    StringBuffer buffer = new StringBuffer();
                    for (i = 0; i < str.length; i++) {
                        if (str[i].endsWith("|")) {
                            String ss = str[i].substring(0, str[i].length() - 1);
                            LogUtil.d(ss);
                            buffer.append(ss + " ");
                        } else {
                            LogUtil.d(str[i]);
                            buffer.append(str[i] + " ");
                        }
                    }
                    initialize(buffer.toString());

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
                    build_enterprise_callPhone.setVisibility(View.VISIBLE);
                    buildEnterprise_isShowPhone.setVisibility(View.VISIBLE);
                    build_enterprise_phone.setVisibility(View.GONE);
                    build_enterprise_callPhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);//指定意图动作
                            intent.setData(Uri.parse("tel:" + build_enterprise_phone.getText()));//指定电话号码
                            startActivity(intent);
                        }
                    });
                    if (SI_YJAuthentication == 0) {
                        buildEnterprise_isShowYJAuthentication.setVisibility(View.VISIBLE);
                        build_enterprise_YJAuthentication.setText("未认证");
                        build_enterprise_YJAuthentication.setTextColor(getResources().getColor(R.color.SI_YJAuthentication_blue));
                    } else if (SI_YJAuthentication == 1) {
                        buildEnterprise_isShowYJAuthentication.setVisibility(View.VISIBLE);
                        build_enterprise_YJAuthentication.setText("已认证");
                        build_enterprise_YJAuthentication.setTextColor(getResources().getColor(R.color.SI_YJAuthentication_red));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtil.shortT(BuildEnterprisesDetailActivity.this, getText(R.string.buildEnterprises_fail).toString());
//                LogUtil.d("==============获取指定企业信息接口,失败");
            }
        }

        @Override
        public void onFail(int arg0, String arg2, Throwable arg3) {
            ToastUtil.shortT(BuildEnterprisesDetailActivity.this, getText(R.string.buildEnterprises_fail).toString());
        }
    };

    /**
     * JSON
     *
     * @param data
     */
    private void ParserJson(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            int SI_Id = jsonObject.optInt("SI_Id");
            String imagesURL = jsonObject.optString("imagesURL");
            String SI_CompanyName = jsonObject.optString("SI_CompanyName");
            String SI_Address = jsonObject.optString("SI_Address");
            String SI_Contacts = jsonObject.optString("SI_Contacts");
            String SI_Phone = jsonObject.optString("SI_Phone");
            int SI_YJAuthentication = jsonObject.optInt("SI_YJAuthentication");

            LogUtil.d("tag", "SI_id:" + SI_Id + "========ImagesURL:" + imagesURL
                    + "==========SI_CompanyName:" + SI_CompanyName + "==========SI_Address:"
                    + SI_Address + "==========SI_Contacts" + SI_Contacts
                    + "===========SI_Phone" + SI_Phone + "==========SI_YJAuthentication" + SI_YJAuthentication);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_build_enterprises_detail;
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

        handler = new Handler();

        //界面跳转带过来的数据
        int buildEnterprise_id = getIntent().getIntExtra("buildEnterprise_id", 0);

        HttpUtil.specifyEnterpriseInformation(buildEnterprise_id, specifyEnterpriseInformationHandler);

        //轮播图
        configImageLoader();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //开启线程
                handler.post(run);
            }
        }, 10000);

    }

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
                        ToastUtil.shortT(BuildEnterprisesDetailActivity.this, "建设中,敬请期待...");
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
                shop_details_advert_layout.setVisibility(View.GONE);
            }
        }
    };

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

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
                Toast.makeText(BuildEnterprisesDetailActivity.this,
                        "position-->" + info.getContent(), Toast.LENGTH_SHORT)
                        .show();
            }
        }

    };

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//				 .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
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
}
