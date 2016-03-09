package com.march.libs.utils;

import android.graphics.Bitmap;

import com.march.libs.gif.AnimatedGifEncoder;
import com.xingye.gif.GifUtil;


/**
 * babyphoto_app     com.babypat.util
 * Created by 陈栋 on 16/2/17.
 * 功能:将jpg图片合成gif
 */
public class GifUtils {

    public static final String TAG = "chendong";

    private static GifUtils instance;

    public synchronized static GifUtils getInstance() {
        if (null == instance) {
            instance = new GifUtils();
        }
        return instance;
    }

    public void jpgToGif(boolean isC, Bitmap pic[],
                         String newPic, int duration) {
        if (pic == null) {
            LUtils.e("gif", " pic is null ");
            return;
        }

        if (isC)
            GifUtil.getInstance().encode(newPic, pic, duration);
        else {
            encodeJava(pic, newPic, duration);
        }
    }

    /**
     * 使用java合成gif
     *
     * @param pic
     * @param newPic
     * @param duration
     */
    private void encodeJava(Bitmap[] pic, String newPic, int duration) {
        try {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(-1);//无限轮播
            e.start(newPic);
            // e.setFrameRate(30f);
            for (int i = 0; i < pic.length; i++) {
                e.setDelay(duration); // 设置播放的延迟时间
                e.addFrame(pic[i]); // 添加到帧中
            }
            e.finish();//刷新任何未决的数据，并关闭输出文件

            for (Bitmap bit : pic) {
                if (null != bit && !bit.isRecycled())
                    bit.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    /**
//     * 使用C语言合成Gif图片
//     *
//     * @param fileName 待合成Gif图片路径
//     * @param bitmaps  图片集合
//     * @param delay    Gif图片播放延迟时间
//     * @return true 合成成功<br>
//     * false 合成失败
//     */
//    public boolean encode(String fileName, Bitmap[] bitmaps, int delay) {
//        if (bitmaps == null || bitmaps.length == 0) {
//            throw new NullPointerException("Bitmaps should have content!!!");
//        }
//        try {
//            int width = bitmaps[0].getWidth();
//            int height = bitmaps[0].getHeight();
//
//            if (Init(fileName, width, height, 256, 100, delay) != 0) {
//                Log.e(TAG, "GifUtil init failed");
//                return false;
//            }
//
//            for (Bitmap bp : bitmaps) {
//                int pixels[] = new int[width * height];
//                bp.getPixels(pixels, 0, width, 0, 0, width, height);
//                AddFrame(pixels);
//            }
//            Close();
//            return true;
//        } catch (Exception e) {
//            Log.e(TAG, "Encode error", e);
//        }
//        return false;
//    }
//
//    /**
//     * 初始化
//     *
//     * @param gifName    gif图片路径
//     * @param w          生成Gif图片默认宽
//     * @param h          生成Gif图片默认高
//     * @param numColors  色彩,默认256
//     * @param quality    图片质量,默认100
//     * @param frameDelay 播放间隔
//     * @return
//     */
//    public native int Init(String gifName, int w, int h, int numColors, int quality, int frameDelay);
//
//    public native int AddFrame(int[] pixels);
//
//    public native void Close();
//
//    static {
//        System.loadLibrary("gifflen");
//    }


}