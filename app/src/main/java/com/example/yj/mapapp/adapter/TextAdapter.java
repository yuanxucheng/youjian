package com.example.yj.mapapp.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.activity.BuildEnterprisesActivity;
import com.example.yj.mapapp.model.IndustryClassification;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.view.ExpandTabView;
import com.example.yj.mapapp.view.ViewBaseAction;

public class TextAdapter extends ArrayAdapter<IndustryClassification> implements ViewBaseAction {

    private Context mContext;
    private List<IndustryClassification> mListData;
    //    private String[] mArrayData;
    private IndustryClassification[] mArrayData;
    private int selectedPos = -1;
    private String selectedText = "";
    private int normalDrawbleId;
    private Drawable selectedDrawble;
    private float textSize;
    private OnClickListener onClickListener;
    private OnItemClickListener mOnItemClickListener;
    private ExpandTabView expandTabView;
    private View view;

    public TextAdapter(Context context, List<IndustryClassification> listData, int sId, int nId) {
        super(context, R.string.no_data, listData);
        mContext = context;
        mListData = listData;
        selectedDrawble = mContext.getResources().getDrawable(sId);
        normalDrawbleId = nId;
        expandTabView = new ExpandTabView(getContext());
        init();
    }

    private void init() {
        onClickListener = new OnClickListener() {

            @Override
            public void onClick(View view) {

                selectedPos = (Integer) view.getTag();
                setSelectedPosition(selectedPos);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, selectedPos);
                }
            }
        };
    }

    public TextAdapter(Context context, IndustryClassification[] arrayData, int sId, int nId) {
        super(context, R.string.no_data, arrayData);
        mContext = context;
        mArrayData = arrayData;
        selectedDrawble = mContext.getResources().getDrawable(sId);
        normalDrawbleId = nId;
        init();
    }

    /**
     * 设置选中的position,并通知列表刷新
     */
    public void setSelectedPosition(int pos) {
//        ToastUtil.shortT(getContext(),"aaaaaaaaaaaaaaaaaaa");
        LogUtil.d("pos:============" + pos);

        if (pos == 0) {
            expandTabView.getExpandTabView(expandTabView);
        }
        if (mListData != null && pos < mListData.size()) {
            selectedPos = pos;
            selectedText = mListData.get(pos).getP_Name();
            notifyDataSetChanged();
        } else if (mArrayData != null && pos < mArrayData.length) {
            selectedPos = pos;
            selectedText = mArrayData[pos].getP_Name();
            notifyDataSetChanged();
        }
        if (selectedText.equals("全部分类")) {
            LogUtil.d("bbbbbbbbbbbbbbbbbb");
            //隐藏菜单
            expandTabView.onPressBack();
            notifyDataSetChanged();
        }
    }

    /**
     * 设置选中的position,但不通知刷新
     */
    public void setSelectedPositionNoNotify(int pos) {
        selectedPos = pos;
        if (mListData != null && pos < mListData.size()) {
            selectedText = mListData.get(pos).getP_Name();
        } else if (mArrayData != null && pos < mArrayData.length) {
            selectedText = mArrayData[pos].getP_Name();
        }
    }

    /**
     * 获取选中的position
     */
    public int getSelectedPosition() {
        if (mArrayData != null && selectedPos < mArrayData.length) {
            return selectedPos;
        }
        if (mListData != null && selectedPos < mListData.size()) {
            return selectedPos;
        }

        return -1;
    }

    /**
     * 设置列表字体大小
     */
    public void setTextSize(float tSize) {
        textSize = tSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.view = convertView;
        TextView view;
        if (convertView == null) {
            view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.choose_item, parent, false);
        } else {
            view = (TextView) convertView;
        }
        view.setTag(position);
        String mString = "";
        if (mListData != null) {
            if (position < mListData.size()) {
                mString = mListData.get(position).getP_Name();
            }
        } else if (mArrayData != null) {
            if (position < mArrayData.length) {
                mString = mArrayData[position].getP_Name();
            }
        }
        if (mString.contains("不限"))
            view.setText("不限");
        else
            view.setText(mString);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        if (selectedText != null && selectedText.equals(mString)) {
            view.setBackgroundDrawable(selectedDrawble);//设置选中的背景图片
//            ToastUtil.shortT(getContext(),"aaaa");
        } else {
            view.setBackgroundDrawable(mContext.getResources().getDrawable(normalDrawbleId));//设置未选中状态背景图片
        }

        view.setPadding(20, 0, 0, 0);
        view.setOnClickListener(onClickListener);
        return view;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    /**
     * 重新定义菜单选项单击接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

}

