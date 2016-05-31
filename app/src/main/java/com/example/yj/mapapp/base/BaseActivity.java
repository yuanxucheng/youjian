package com.example.yj.mapapp.base;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.example.yj.mapapp.model.Update;
import com.example.yj.mapapp.model.UserInfo;
import com.example.yj.mapapp.util.AppManager;
import com.example.yj.mapapp.util.CheckUtil;
import com.example.yj.mapapp.util.JsonUtil;
import com.example.yj.mapapp.util.Screen;
import com.example.yj.mapapp.util.ShareRefrenceUtil;
import com.example.yj.mapapp.util.UpdateManager;
import com.example.yj.mapapp.R;

import java.util.Locale;

public abstract class BaseActivity extends FragmentActivity implements IBaseActivity {
    /**
     * 当前Activity渲染的视图View
     **/
    private View mContextView = null;
    /**
     * 动画类型
     **/
    private int mAnimationType = NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppManager.getInstance().addActivity(this);
//        LogUtil.d("更换语言==="+ShareRefrenceUtil.getString("language", "cn"));
        switchLanguage(ShareRefrenceUtil.getString("language", "cn"));

        initScreen();
        //设置渲染视图View
        View mView = bindView();
        if (null == mView) {
            mContextView = LayoutInflater.from(this).inflate(bindLayout(), null);
        } else {
            mContextView = mView;
        }
        setContentView(mContextView);

        //初始化控件
        initView(mContextView);

        //业务操作
        doBusiness(this);
    }

    /**
     * 自动登录设置
     */
    public UserInfo getUser() {
        UserInfo info = MApplication.getApplication().getUserInfo();
        if (info == null) {
            String userJson = (String) ShareRefrenceUtil.get(ShareRefrenceUtil.USER_INFO);
            if (CheckUtil.isEmpty(userJson)) {
                return null;
            } else {
                try {
                    info = (UserInfo) JsonUtil.jsonToObject(userJson, UserInfo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return info;
            }
        } else {
            return info;
        }
    }

    public void initScreen() {
        Screen.initScreen(this);
        Screen.setScale();
    }

    public void toPage(Class<?> c) {
        hideInputFromwindow();
        Intent intent = new Intent(this, c);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void toPage(Class<?> c, Bundle extras) {
        hideInputFromwindow();
        Intent intent = new Intent(this, c);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void hideInputFromwindow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    /**
     * 检测更新
     */
    public void checkUpdate(Update update) {
        UpdateManager.getUpdateManager().checkAppUpdate(this, false, update);
    }


    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
        resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void switchLanguage(String language) {
        //设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("en")) {
            config.locale = Locale.ENGLISH;
        } else if (language.equals("cn")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equals("ft")) {
            config.locale = Locale.TAIWAN;
        }
        resources.updateConfiguration(config, dm);

        //保存设置语言的类型
        ShareRefrenceUtil.save("language", language);
    }

    public void logouts_all() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(getText(R.string.main_logout_all))
                .setPositiveButton(getText(R.string.input_search_logout_ok),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                new AlertDialog.Builder(BaseActivity.this)
                                        .setTitle(getText(R.string.main_logout_clear_all))
                                        .setPositiveButton(
                                                getText(R.string.input_search_logout_ok),
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(
                                                            DialogInterface arg0,
                                                            int arg1) {
//                                                        HKApplication
//                                                                .getApplication()
//                                                                .setUserInfo(
//                                                                        null);
//                                                        ShareRefrenceUtil
//                                                                .save(ShareRefrenceUtil.USER_INFO,
//                                                                        "");

                                                        AppManager.getInstance().killAllActivity();
                                                        System.exit(0);
                                                    }
                                                })
                                        .setNegativeButton(
                                                getText(R.string.input_search_logout_cancle),
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(
                                                            DialogInterface arg0,
                                                            int arg1) {
                                                        AppManager.getInstance().killAllActivity();
                                                        System.exit(0);
                                                    }
                                                }).show();
                            }
                        }).setNegativeButton(getText(R.string.input_search_logout_cancle), null).show();
    }
}
