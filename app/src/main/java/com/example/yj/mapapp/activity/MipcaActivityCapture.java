package com.example.yj.mapapp.activity;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yj.mapapp.R;
import com.example.yj.mapapp.net.handler.HttpUtil;
import com.example.yj.mapapp.net.handler.ResponseHandler;
import com.example.yj.mapapp.util.LogUtil;
import com.example.yj.mapapp.util.ToastUtil;
import com.example.yj.mapapp.zxing.CameraManager;
import com.example.yj.mapapp.zxing.CaptureActivityHandler;
import com.example.yj.mapapp.zxing.InactivityTimer;
import com.example.yj.mapapp.zxing.RGBLuminanceSource;
import com.example.yj.mapapp.zxing.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.json.JSONException;
import org.json.JSONObject;

public class MipcaActivityCapture extends Activity implements Callback, View.OnClickListener {


    private final static String tag = "MipcaActivityCapture-->";

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;


    private static final int REQUEST_CODE = 100;
    private static final int PARSE_BARCODE_SUC = 300;
    private static final int PARSE_BARCODE_FAIL = 303;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;
    private AlertDialog noticeDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mipca_activity_capture);
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        ImageView mImageVIewBack = (ImageView) findViewById(R.id.button_back);
        mImageVIewBack.setOnClickListener(this);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        ImageView mImageView = (ImageView) findViewById(R.id.imageview_function);
        mImageView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                this.finish();
                break;
            case R.id.imageview_function:
//                //打开手机中的相册
//                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
//                innerIntent.setType("image/*");
//                Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
//                this.startActivityForResult(wrapperIntent, REQUEST_CODE);
//                break;

                startActivity(new Intent(MipcaActivityCapture.this, UnderConstructionActivity.class));
        }
    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mProgress.dismiss();
            switch (msg.what) {
                case PARSE_BARCODE_SUC:
//                    onResultHandler((String)msg.obj, scanBitmap);
                    onJumpPageHandler((String) msg.obj);
                    break;
                case PARSE_BARCODE_FAIL:
                    Toast.makeText(MipcaActivityCapture.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;

            }
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    //获取选中图片的路径
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();

                    mProgress = new ProgressDialog(MipcaActivityCapture.this);
                    mProgress.setMessage("正在扫描...");
                    mProgress.setCancelable(false);
                    mProgress.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = scanningImage(photo_path);
                            if (result != null) {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_SUC;
                                m.obj = result.getText();
                                mHandler.sendMessage(m);
                            } else {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_FAIL;
                                m.obj = "Scan failed!";
                                mHandler.sendMessage(m);
                            }
                        }
                    }).start();
                    break;
            }
        }
    }

    /**
     * 扫描二维码图片的方法
     *
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 跳转到指定的页面并显示返回的result值
     *
     * @param resultString
     */
    private void onJumpPageHandler(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
            return;
        }
//        Intent myIntent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putString("result", resultString);
//        myIntent.putExtras(bundle);
//
//        myIntent.setClass(MipcaActivityCapture.this, ScanResultActivity.class);
//        startActivity(myIntent);
//
//        MipcaActivityCapture.this.finish();

        LogUtil.d("tag", "resultString:======" + resultString);
        //输出结果:
        // http://www.51buyjc.com/shop/Company/964
        String[] str = resultString.split("/");
        LogUtil.d("tag", "str:======" + str[5]);

        HttpUtil.specifyEnterpriseInformation(Integer.valueOf(str[5]), specifyEnterpriseInformationHandler);
    }

    private ResponseHandler specifyEnterpriseInformationHandler = new ResponseHandler() {
        @Override
        public void onCacheData(String content) {
            LogUtil.w("========onCacheData=========" + content);
        }

        @Override
        public void success(String data, String resCode, String info) {
            if (resCode.equals("")) {
                LogUtil.d("data===================" + data);
                LogUtil.d("获取指定企业信息接口,成功==============");

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int SI_Id = jsonObject.optInt("SI_Id");
                    String imagesURL = jsonObject.optString("imagesURL");
                    String SI_CompanyName = jsonObject.optString("SI_CompanyName");
                    String SI_Address = jsonObject.optString("SI_Address");
                    String SI_Contacts = jsonObject.optString("SI_Contacts");
                    String SI_Phone = jsonObject.optString("SI_Phone");
                    int SI_YJAuthentication = jsonObject.optInt("SI_YJAuthentication");

                    Intent myIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("SI_CompanyName", SI_CompanyName);
                    bundle.putString("SI_Address", SI_Address);
                    bundle.putString("SI_Contacts", SI_Contacts);
                    bundle.putString("SI_Phone", SI_Phone);

                    myIntent.putExtras(bundle);

                    myIntent.setClass(MipcaActivityCapture.this, ShopsDetailsActivity.class);
                    startActivity(myIntent);

                    MipcaActivityCapture.this.finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtil.shortT(MipcaActivityCapture.this, getText(R.string.buildEnterprises_fail).toString());
//                LogUtil.d("==============获取指定企业信息接口,失败");
            }
        }

        @Override
        public void onFail(int arg0, String arg2, Throwable arg3) {
            ToastUtil.shortT(MipcaActivityCapture.this, getText(R.string.buildEnterprises_fail).toString());
        }
    };

    /**
     * 处理扫描结果
     *
     * @param result
     */
    public void handleDecode(Result result) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        final String resultString = result.getText();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择");
        builder.setMessage("请选择项目?");
        builder.setPositiveButton("进入商铺网站", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                        Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(resultString);
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("查看商铺详情", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onJumpPageHandler(resultString);
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();

        //跳转到商铺的详细
//        onJumpPageHandler(resultString);
        //跳转到商铺的网站
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri content_url = Uri.parse(resultString);
//        intent.setData(content_url);
//        startActivity(intent);
    }

//    public void handleDecode(Result result, Bitmap barcode) {
//        inactivityTimer.onActivity();
//        playBeepSoundAndVibrate();
//        String resultString = result.getText();
////        onResultHandler(resultString, barcode);
//    }

//    /**
//     * 跳转到上一个页面
//     * @param resultString
//     * @param bitmap
//     */
//    private void onResultHandler(String resultString, Bitmap bitmap){
//        if(TextUtils.isEmpty(resultString)){
//            Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Intent resultIntent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putString("result", resultString);
//        bundle.putParcelable("bitmap", bitmap);
//        resultIntent.putExtras(bundle);
//        this.setResult(RESULT_OK, resultIntent);
//        MipcaActivityCapture.this.finish();
//    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


}