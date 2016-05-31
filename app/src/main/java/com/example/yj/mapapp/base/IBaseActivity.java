package com.example.yj.mapapp.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public interface IBaseActivity {
	/**
	 * 用于 intent 传输动画类型数据
	 */
	public static final String ANIMATION_TYPE = "AnimationType";
	/**
	 * 无动画
	 */
	public static final int NONE = 0;
	/**
	 * 左右动画
	 */
	public static final int LEFT_RIGHT = 1;
	/**
	 * 上下动画
	 */
	public static final int TOP_BOTTOM = 2;
	/**
	 * 淡入淡出
	 */
	public static final int FADE_IN_OUT = 3;
	
	/**
	 * 绑定渲染视图的布局文件
	 * @return 布局文件资源id
	 */
	public int bindLayout();
	
	/**
	 * 绑定渲染View
	 * @return
	 */
	public View bindView();
	
	/**
	 * 初始化界面参数
	 * @param parms
	 */
	public void initParms(Bundle parms);
	
	/**
	 * 初始化控件
	 */
	public void initView(final View view);
	
	/**
	 * 业务处理操作（onCreate方法中调用）
	 * @param mContext  当前Activity对象
	 */
	public void doBusiness(Context mContext);
	
	/**
	 * 暂停恢复刷新相关操作（onResume方法中调用）
	 */
	public void resume();
	
	/**
	 * 销毁、释放资源相关操作（onDestroy方法中调用）
	 */
	public void destroy();
	
}
