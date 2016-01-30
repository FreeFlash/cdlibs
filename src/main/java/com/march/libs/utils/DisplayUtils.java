package com.march.libs.utils;

import android.content.Context;
import android.view.View;

/**
 * CdLibsTest     com.march.libs.utils
 * Created by 陈栋 on 16/1/18.
 * 功能:尺寸转换,想在不同手机显示相同的尺寸,使用xx2px
 */
public class DisplayUtils {

    public static int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int px2dp(Context context, float pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }


    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 3.获取屏幕宽像素
     *
     * @param context
     * @return
     */
    public static final int getWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 4. 获取屏幕高像素
     *
     * @param context
     * @return
     */
    public static final int getHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 5.将view的左上角坐标存入数组中, 若是普通activity,则y坐标为可见的状态栏高度+可见的标题栏高度+view左上角到标题栏底部的距离.在隐藏了状态栏标题栏的情况下 ,它们的高度以0计算 若是对话框式的activity,则y坐标为可见的标题栏高度+view到标题栏底部的距离.此时是无视状态栏的有无的.
     *
     * @param view
     * @return
     */
    public static final int[] getInWindowLocation(View view) {
        int[] position = new int[2];
        view.getLocationInWindow(position);
        return position;

    }

    /**
     * 6.获得view左上角在整个屏幕中的坐标，从0,0开始的正数(一般情况下和getLocationInWindow返回相同，特殊情况在dialog中)， 非全屏的dialog中，getLocationOnScreen的值要大 getLeft, getRight, getTop, getBottom获得的是在父视图中左上角的xy坐标
     *
     * @param view
     * @return
     */
    public static final int[] getInScreenLocation(View view) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        return position;

    }

}

