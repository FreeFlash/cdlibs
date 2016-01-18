package com.march.libs.helper;

import android.content.Context;
import android.widget.Toast;

/**
 * CdLibsTest     com.march.libs.helper
 * Created by 陈栋 on 16/1/2.
 * 功能:显示toast工具类
 */
public final class TT {

    public static final int USE_RES_STR = 0;
    /**
     * 系统风格短Toast
     *
     * @param context
     * @param msg
     */
    public static final void Short(Context context, int strRes, String msg) {
        if (strRes != USE_RES_STR)
            Toast.makeText(context, context.getText(strRes), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 系统风格长Toast
     *
     * @param context
     * @param msg
     */
    public static final void Long(Context context, int strRes, String msg) {
        if (strRes != USE_RES_STR)
            Toast.makeText(context, context.getText(strRes), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
