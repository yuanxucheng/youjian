package com.example.yj.mapapp.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.yj.mapapp.R;

public class ExitDialog extends Dialog implements View.OnClickListener {
    private String titles;
    private String tv1;
    private String tv2;
    private TextView dialog_title;
    private TextView tv_l;
    public TextView tv_r;
    public MyDialogROnClickListener dialogROnClickListener;

    public ExitDialog(Context context, String titles, String tv_l, String tv_r) {
        super(context);
        this.titles = titles;
        this.tv1 = tv_l;
        this.tv2 = tv_r;
        this.setCanceledOnTouchOutside(false);
    }

    public ExitDialog(Context context, String tv_l, String tv_r) {
        super(context);
        this.titles = "";
        this.tv1 = tv_l;
        this.tv2 = tv_r;
        this.setCanceledOnTouchOutside(false);
    }

    public void setTitles(String titles) {
        this.titles = titles;
        dialog_title.setText(titles);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_dialog);

        tv_l = (TextView) findViewById(R.id.tv_l);
        tv_r = (TextView) findViewById(R.id.tv_r);
        dialog_title = (TextView) findViewById(R.id.dialog_title);
        dialog_title.setText(titles);
        tv_l.setText(tv1);
        tv_r.setText(tv2);
        tv_l.setOnClickListener(this);
        tv_r.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_l:
                this.dismiss();
                break;
            case R.id.tv_r:
                dialogROnClickListener.onClick(tv_r);
                break;
            default:
                break;
        }

    }

    public MyDialogROnClickListener getDialogROnClickListener() {
        return dialogROnClickListener;
    }

    public void setDialogROnClickListener(
            MyDialogROnClickListener dialogROnClickListener) {
        this.dialogROnClickListener = dialogROnClickListener;
    }

    public interface MyDialogROnClickListener {
        void onClick(View v);
    }
}
