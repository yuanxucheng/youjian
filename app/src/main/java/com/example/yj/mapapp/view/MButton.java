package com.example.yj.mapapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.yj.mapapp.R;

public class MButton extends Button {
    private Context Mcontext;

    public MButton(Context context) {
        super(context);
        this.Mcontext = context;
        init();
    }


    public MButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.Mcontext = context;
        init();
    }

    public MButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.Mcontext = context;
        init();
    }

    private void init() {
//        this.setBackgroundResource(R.mipmap.icon_home_rzdk);
    }

    /**
     * 设置删除该模块
     */
    public void setDelete() {
        this.setBackgroundResource(R.mipmap.icon_home_esxx);
    }
}
