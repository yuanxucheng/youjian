package com.example.yj.mapapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;

import com.example.yj.mapapp.base.MApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtil {

    public static int sampleSize = 1;

    /**
     * 获取指定宽度的bitmap
     */
    public static Bitmap loadScaledBitmap(int resId, Context context, int dstWidth) {
        Bitmap bitmap = loadBitmap(resId, context);
        if (bitmap == null)
            return null;
        if (dstWidth == bitmap.getWidth())
            return bitmap;
        int height = (int) (bitmap.getHeight() * ((double) dstWidth / bitmap.getWidth()));
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, height, true);
        return newBitmap;
    }

    /**
     * 获取指定宽度的bitmap
     */
    public static Bitmap loadScaledBitmap(String path, int dstWidth) {
        Bitmap bitmap = loadBitmap(path);
        if (bitmap == null)
            return null;
        if (dstWidth == bitmap.getWidth())
            return bitmap;
        int height = (int) (bitmap.getHeight() * ((double) dstWidth / bitmap.getWidth()));
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, height, true);
        return newBitmap;
    }

    /**
     * 获取指定宽度的bitmap
     */
    public static Bitmap loadScaledBitmap(byte[] arr, int dstWidth) {
        Bitmap bitmap = byteToBitmap(arr);
        if (bitmap == null)
            return null;
        if (dstWidth == bitmap.getWidth())
            return bitmap;
        int height = (int) (bitmap.getHeight() * ((double) dstWidth / bitmap.getWidth()));
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, height, true);
        return newBitmap;
    }

    public static void remove(String path, int sampleSize) {
        BitmapPool.getInstance().remove(path + "samplesize:" + sampleSize);
    }

    public static void remove(String path) {
        remove(path, sampleSize);
    }

    public static Bitmap loadBitmap(int resId, Context context) {
        return loadBitmap(resId, context, sampleSize);
    }

    public static Bitmap loadBitmap(int resId, Context context, int sampleSize) {
        Bitmap bitmap = null;
        bitmap = BitmapPool.getInstance().getBitmapFromCatche(resId + "samplesize:" + sampleSize);
        if (bitmap != null) {
            return bitmap;
        }

        Options options = new Options();
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        options.inInputShareable = true;
        options.inPurgeable = true;
        bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        if (bitmap != null) {
            BitmapPool.getInstance().addBitmapToCatche(resId + "samplesize:" + sampleSize, bitmap);
        }
        return bitmap;
    }

    public static Bitmap loadBitmap(String path) {
        return loadBitmap(path, sampleSize);
    }

    public static Bitmap loadBitmap(String path, int sampleSize) {
        Bitmap bitmap = null;
        bitmap = BitmapPool.getInstance().getBitmapFromCatche(path + "samplesize:" + sampleSize);
        if (bitmap != null) {
            return bitmap;
        }
        Options options = new Options();
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        options.inInputShareable = true;
        options.inPurgeable = true;
        if (path == null)
            return null;
        if (path.startsWith("assets:")) {
            InputStream in = null;
            try {
                in = MApplication.getApplication().getAssets().open(path.substring(7));
                bitmap = BitmapFactory.decodeStream(in, null, options);
            } catch (IOException e) {
                LogUtil.w(e);
            } finally {
                CommonUtil.close(in);
            }
        } else {
            bitmap = BitmapFactory.decodeFile(path, options);
        }
        if (bitmap != null) {
            BitmapPool.getInstance().addBitmapToCatche(path + "samplesize:" + sampleSize, bitmap);
        }
        return bitmap;
    }

    /**
     * Bitmap转byte[]
     *
     * @param
     * @return byte[]
     */
    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }

    /**
     * byte[] 转Bitmap
     *
     * @param b
     * @return Bitmap
     */
    public static Bitmap byteToBitmap(byte[] b) {
        if (b != null && b.length > 0)
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        return null;
    }

    /**
     * View截图
     *
     * @param view
     * @return Bitmap
     */
    public static Bitmap ViewToBitmap(View view) {
        view.clearFocus();
        view.setPressed(false);
        boolean willNotCatche = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int color = view.getDrawingCacheBackgroundColor();
        if (color != 0)
            view.destroyDrawingCache();
        view.setDrawingCacheBackgroundColor(0);
        view.buildDrawingCache();
        Bitmap cachedBitmap = view.getDrawingCache();
        if (cachedBitmap == null) {
            LogUtil.d("error", "view → bitmap failed...");
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cachedBitmap);
        view.destroyDrawingCache();
        view.setWillNotDraw(willNotCatche);
        view.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    /**
     * 截取整个WebView
     *
     * @param webView
     * @return Bitmap
     */
    public static Bitmap WebViewToBitmap(WebView webView) {
        Picture snapShot = webView.capturePicture();
        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }

    /**
     * 彩色图转为灰度图
     *
     * @param bitmap
     * @return bitmap
     */
    public static Bitmap coverGrayBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth(); // 获取位图的宽
        int height = bitmap.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    /**
     * Bitmap->Base64String
     *
     * @param bitmap
     * @return String
     */
    public static String getBitmapStrBase64(Bitmap bitmap) {

        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            return Base64.encodeToString(bytes, 0);
        }
        return null;
    }

    /**
     * Base64String->Bitmap
     *
     * @param iconBase64
     * @return Bitmap
     */
    public static Bitmap getBitmap(String iconBase64) {
        byte[] bitmapArray;
        bitmapArray = Base64.decode(iconBase64, 0);
        Options options = new Options();
        options.inSampleSize = 10;
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    public static Bitmap getSmallBiMap(byte[] data, int width, int height) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static int calculateInSampleSize(Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 10;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;//图片压缩质量参数
        opt.inPurgeable = true;// 允许可清除
        opt.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果
//        opt.inJustDecodeBounds = false;
//        opt.inSampleSize = 2;   // width，hight设为原来的十分一
        //  获取资源图片
        InputStream is = context.getResources().openRawResource(resId);

        //获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;
        LogUtil.d("picWidth", "picWidth:========" + picWidth);
        LogUtil.d("picHeight", "picHeight:========" + picHeight);

        return BitmapFactory.decodeStream(is, null, opt);
    }
}
