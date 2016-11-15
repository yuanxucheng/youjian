package com.example.yj.mapapp.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class BActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去除通知栏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public void jumpActivity(Class<?> cls) {
		startActivity(new Intent(getApplicationContext(), cls));
	}
}
