package com.example.yj.mapapp.activity;

import android.os.Bundle;

import com.example.yj.mapapp.R;

import android.app.Activity;
import android.widget.TextView;

/**
 * 二维码扫描
 */
public class ScanResultActivity extends Activity {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    /**
     * 显示扫描结果
     */
    private TextView mTextView;

    /**
     * 显示扫描拍的图片
     */
//    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

//      mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);

        mTextView = (TextView) findViewById(R.id.result);

        Bundle myBundle = ScanResultActivity.this.getIntent().getExtras();
        String myText = myBundle.getString("result");
        mTextView.setText(myText);

  /*      //点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
        //扫描完了之后调到该界面
        Button mButton = (Button) findViewById(R.id.button1);
        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ScanResultActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });*/
    }


 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    mTextView.setText(bundle.getString("result"));
                    Log.d("ScanResultActivity", bundle.getString("result"+"================="));
                    //显示
//                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }*/

}
