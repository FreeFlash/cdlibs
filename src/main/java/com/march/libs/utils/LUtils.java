package com.march.libs.utils;

import android.util.Log;

/**
 * CdLibsTest     com.march.libs.helper
 * Created by 陈栋 on 16/1/2.
 * 功能:日志打印工具类
 */
public final class LUtils {
    //debug开关
    private static final boolean DEBUG = true;
    //默认tag
    private static final String TAG = "chendong";

    public static final void i(String tag, Object msg) {
        if (DEBUG) {
            if (tag == null)
                Log.i(TAG, msg.toString());
            else
                Log.i(tag, msg.toString());
        }
    }

    public static final void i(Object msg) {
        if (DEBUG) {
            Log.i(TAG, msg.toString());
        }
    }

    public static final void w(String tag, Object msg) {
        if (DEBUG) {
            if (tag == null)
                Log.i(TAG, msg.toString());
            else
                Log.i(tag, msg.toString());
        }
    }

    public static final void v(String tag, Object msg) {
        if (DEBUG) {
            if (tag == null)
                Log.i(TAG, msg.toString());
            else
                Log.i(tag, msg.toString());
        }
    }

    public static final void d(String tag, Object msg) {
        if (DEBUG) {
            if (tag == null)
                Log.i(TAG, msg.toString());
            else
                Log.i(tag, msg.toString());
        }
    }

}
