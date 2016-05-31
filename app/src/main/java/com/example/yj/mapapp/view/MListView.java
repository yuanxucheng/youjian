package com.example.yj.mapapp.view;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.adapter.LabourAgencyAdapter;

public class MListView extends ListView implements OnScrollListener {

	/** 下拉过程的状态值 */
	private final static int PULLING = 0;
	/** 从下拉返回到不刷新的状态值 */
	private final static int PULL_TO_REFRESH = 1;
	/** 正在刷新的状态值 */
	private final static int REFRESHING = 2;
	/** 刷新完毕 */
	private final static int DONE = 3;
	/** 更新中 */
	private final static int LOADING = 4;

	/** 偏移比例 */
	private final static int RATIO = 3;
	private LayoutInflater inflater;

	private LinearLayout headerView;
	private TextView tv_head;
	private TextView tv_lastupdate;
	private ImageView iv_arrow;
	private ProgressBar pb;

	/** 头部布局高度 */
	private int headerViewHeight;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// 起始Y左边 作用 判断拖动前坐标与拖动后坐标的Y值是否大于头布局的高度 从而判断是否该刷新
	private int startY;
	private int state;
	/** 是否拉回 */
	private boolean isBack;
	/** 是否完成了一次完整刷新 */
	private boolean isRecored;

	private OnRefreshListener refreshListener;
	/** 是否可刷新 */
	private boolean isRefreshable;

	public MListView(Context context) {
		super(context);
		init(context);
	}

	public MListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		inflater = LayoutInflater.from(context);
		headerView = (LinearLayout) inflater.inflate(R.layout.lv_header, null);
		tv_head = (TextView) headerView.findViewById(R.id.tv_head);
		tv_lastupdate = (TextView) headerView.findViewById(R.id.tv_lastupdate);
		iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
		// 设置下拉刷新图标的最小高度和宽度
		iv_arrow.setMinimumWidth(70);
		iv_arrow.setMinimumHeight(50);

		pb = (ProgressBar) headerView.findViewById(R.id.pb);
		measureView(headerView);
		headerViewHeight = headerView.getMeasuredHeight();
		// 设置内边距
		headerView.setPadding(0, -1 * headerViewHeight, 0, 0);
		// 重绘
		headerView.invalidate();
		// 将头布局加入ListView的顶部
		addHeaderView(headerView, null, false);
		setOnScrollListener(this);

		// 设置旋转动画事件
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		// 设置动画的变化速度 LinearInterpolator代表匀速
		animation.setInterpolator(new LinearInterpolator());
		// 设置动画时长
		animation.setDuration(250);
		// 设置为true代表当动画结束时 图案的状态停留在最后一帧
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		// 设置动画变化速度
		reverseAnimation.setInterpolator(new LinearInterpolator());
		// 设置时长
		reverseAnimation.setDuration(200);
		// 设置true代表动画停止在最后一帧
		reverseAnimation.setFillAfter(true);

		// 一开始的状态就是下拉刷新完的状态，所以为DONE
		state = DONE;
		// 是否正在刷新
		isRefreshable = false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	/*
	 * 	参数：
	 * 2.第一个可见布局的的ID
	 * 3.可见布局的总数
	 * 4.ListView中item的总数
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem == 0) {
			isRefreshable = true;
		} else {
			isRefreshable = false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isRefreshable) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!isRecored) {
					isRecored = true;
					startY = (int) ev.getY();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING && state != LOADING) {
					if (state == PULL_TO_REFRESH) {
						state = DONE;
						changeHeaderView();
					}
					if (state == PULLING) {
						state = REFRESHING;
						changeHeaderView();
						refreshAdapter();
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) ev.getY();
				if (!isRecored) {
					isRecored = true;
					startY = tempY;
				}
				if (state != REFRESHING && isRecored && state != LOADING) {
					if (state == PULLING) {
						setSelection(0);
						if (((tempY - startY) / RATIO < headerViewHeight)
								&& (tempY - startY) > 0) {
							state = PULL_TO_REFRESH;
							changeHeaderView();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderView();
						}
					}
					if (state == PULL_TO_REFRESH) {
						setSelection(0);
						if ((tempY - startY) / RATIO >= headerViewHeight) {
							state = PULLING;
							isBack = true;
							changeHeaderView();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderView();
						}
					}
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_TO_REFRESH;
							changeHeaderView();
						}
					}
					if (state == PULL_TO_REFRESH) {
						headerView.setPadding(0, -1 * headerViewHeight
								+ (tempY - startY) / RATIO, 0, 0);
					}
					if (state == PULLING) {
						headerView.setPadding(0, (tempY - startY) / RATIO
								- headerViewHeight, 0, 0);
					}
				}
				break;
			default:
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	private void changeHeaderView() {
		switch (state) {
			case PULLING:
				iv_arrow.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE);
				tv_head.setVisibility(View.VISIBLE);
				tv_lastupdate.setVisibility(View.VISIBLE);

				iv_arrow.clearAnimation();
				iv_arrow.startAnimation(animation);
				tv_head.setText("松开刷新");
				break;
			case PULL_TO_REFRESH:
				pb.setVisibility(View.GONE);
				tv_head.setVisibility(View.VISIBLE);
				tv_lastupdate.setVisibility(View.VISIBLE);
				iv_arrow.clearAnimation();
				iv_arrow.setVisibility(View.VISIBLE);
				if (isBack) {
					isBack = false;
					iv_arrow.clearAnimation();
					iv_arrow.startAnimation(reverseAnimation);
					tv_head.setText("下拉刷新");
				} else {
					tv_head.setText("下拉刷新");
				}
				break;
			case REFRESHING:
				headerView.setPadding(0, 0, 0, 0);
				pb.setVisibility(View.VISIBLE);
				iv_arrow.clearAnimation();
				iv_arrow.setVisibility(View.GONE);
				tv_head.setText("正在刷新...");
				tv_lastupdate.setVisibility(View.VISIBLE);
				break;
			case DONE:
				headerView.setPadding(0, -1 * headerViewHeight, 0, 0);
				pb.setVisibility(View.GONE);
				iv_arrow.clearAnimation();
				iv_arrow.setImageResource(R.mipmap.xlistview_arrow);
				tv_head.setText("下拉刷新");
				tv_lastupdate.setVisibility(View.VISIBLE);
				break;
		}
	}

	//估算参数布局的宽高     ObserverTree.addGolbeListener
	private void measureView(View child) {
		ViewGroup.LayoutParams params = child.getLayoutParams();
		if (params == null) {
			params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,
				params.width);
		int lpHeight = params.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public void onRefreshComplete() {
		state = DONE;
		tv_lastupdate.setText("最近更新:" + new Date().toLocaleString());
		changeHeaderView();
	}

	private void refreshAdapter() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	public void setAdapter(LabourAgencyAdapter adapter) {
		tv_lastupdate.setText("最近更新:" + new Date().toLocaleString());
		super.setAdapter(adapter);
	}

}
