package com.example.yj.mapapp.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.view.ExitDialog;

public class DialogUtils {
    private Context context;

    public static void versionUpdateDialog(final Context context) {
        final ExitDialog myDialog = new ExitDialog(context,
                context.getText(R.string.personal_center_version_updating).toString(), context.getText(R.string.personal_center_later_updating).toString(), context.getText(R.string.personal_center_immediate_updating).toString());
        myDialog.show();
        myDialog.setDialogROnClickListener(new ExitDialog.MyDialogROnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_l:
                        myDialog.dismiss();
                        break;
                    case R.id.tv_r:
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("http://www.51buyjc.com/Download_ios_android.html");
                        intent.setData(content_url);
                        context.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
