package com.march.libs.utils;

import android.widget.Toast;

import com.march.libs.base.BaseApplication;

/**
 * CdLibsTest     com.march.libs.helper
 * Created by 陈栋 on 16/1/2.
 * 功能:显示toast工具类
 */
public final class TUtils {

    public static final int NO_RES_STR = 0;

    /**
     * @param strRes
     * @param msg
     */
    public static final void Short(int strRes, String msg) {
        if (strRes != NO_RES_STR)
            Toast.makeText(BaseApplication.getInst(), BaseApplication.getInst().getText(strRes), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(BaseApplication.getInst(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param strRes
     * @param msg
     */
    public static final void Long(int strRes, String msg) {
        if (strRes != NO_RES_STR)
            Toast.makeText(BaseApplication.getInst(), BaseApplication.getInst().getText(strRes), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(BaseApplication.getInst(), msg, Toast.LENGTH_SHORT).show();
    }
}
