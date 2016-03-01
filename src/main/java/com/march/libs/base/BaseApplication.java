package com.march.libs.base;

import android.app.Application;
import android.util.DisplayMetrics;

/**
 * CdLibsTest     com.march.libs.base
 * Created by 陈栋 on 16/1/30.
 * 功能:
 */
public class BaseApplication extends Application {

    private static BaseApplication mInstApp;
    public int mScreenWidth, mScreenHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstApp = this;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
    }

    public static BaseApplication getInst() {
        return mInstApp;
    }


}
