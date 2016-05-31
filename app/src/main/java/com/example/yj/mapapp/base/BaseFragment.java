package com.example.yj.mapapp.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yj.mapapp.model.UserInfo;
import com.example.yj.mapapp.util.ShareRefrenceUtil;

import java.util.Locale;

public abstract class BaseFragment extends Fragment implements IBaseFragment{
    /** 当前Fragment渲染的视图View **/
    private View mContextView = null;

    /**依附的Activity**/
    protected Activity mContext = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //缓存当前依附的activity
        mContext = activity;
    }
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 渲染视图View
		if (null == mContextView) {
			//初始化参数
			initParms(getArguments());

			View mView = bindView();
			if(null == mView){
				mContextView = inflater.inflate(bindLayout(), container, false);
			}else{
				mContextView = mView;
			}
			// 控件初始化
			initView(mContextView);
			// 业务处理
			doBusiness(getActivity());
		}

		return mContextView;
	}
	public void toPage(Class<?> c) {
		((BaseActivity) getActivity()).toPage(c);
	}

	public void toPage(Class<?> c, Bundle extras) {
		((BaseActivity) getActivity()).toPage(c, extras);
	}

    public UserInfo getUser(){
        return  ((BaseActivity) getActivity()).getUser();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mContextView != null && mContextView.getParent() != null) {
            ((ViewGroup) mContextView.getParent()).removeView(mContextView);
        }
    }

    /**
     * 获取当前Fragment依附在的Activity
     *
     * @return
     */
    public Activity getContext() {
        return getActivity();
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
}
