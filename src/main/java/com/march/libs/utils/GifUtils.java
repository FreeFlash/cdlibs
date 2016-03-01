package com.march.libs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.march.libs.gif.AnimatedGifEncoder;


/**
 * babyphoto_app     com.babypat.util
 * Created by 陈栋 on 16/2/17.
 * 功能:将jpg图片合成gif
 */
public class GifUtils {
    /**
     * 把多张jpg图片合成一张
     *
     * @param pic    String[] 多个jpg文件名 包含路径
     * @param newPic String 生成的gif文件名 包含路径
     */
    public static void jpgToGif(String pic[],
                                String newPic, int duration) {
        try {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(1);
            e.start(newPic);

            for (int i = 0; i < pic.length; i++) {
                e.setDelay(duration); // 设置播放的延迟时间
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inSampleSize = 2;
                Bitmap src = BitmapFactory.decodeFile(pic[i], op);
                e.addFrame(src); // 添加到帧中
            }
            e.finish();//刷新任何未决的数据，并关闭输出文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void jpgToGif(Bitmap pic[],
                                String newPic, int duration) {
        try {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(1);
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
}