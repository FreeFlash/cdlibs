package com.march.libs.utils;

import com.march.libs.base.BaseActivity;

/**
 * CdLibsTest     com.march.libs.utils
 * Created by 陈栋 on 16/2/14.
 * 功能:界面跳转动画显示
 */
public class ActAnimUtils {


    public enum AnimStyle {
        Fade, SlideInLeft, SlideInRight
    }


    public static void transActivity(BaseActivity activity, AnimStyle style) {
        switch (style) {
            case Fade:
                fadeIn(activity);
                break;
            case SlideInLeft:
                slideInLeft(activity);
                break;
            case SlideInRight:
                slideInRight(activity);
                break;
        }
    }


    /**
     * 新的界面淡入,启动界面时常用
     *
     * @param activity
     */
    private static void fadeIn(BaseActivity activity) {
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    /**
     * 从左边划入,返回上一个界面时常用
     *
     * @param activity
     */
    private static void slideInLeft(BaseActivity activity) {
        activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    /**
     * 从右边划入,启动界面时常用
     *
     * @param activity
     */
    private static void slideInRight(BaseActivity activity) {
        activity.overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
    }

}
