package com.example.yj.mapapp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.example.yj.mapapp.R;

public class MProgressDialog extends ProgressDialog {
    public MProgressDialog(Context context) {
        super(context);
    }

    public MProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.progressdialog_input);
    }
}
