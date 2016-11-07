package com.march.libs.base;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;


/**
 * CdLibsTest     com.march.libs
 * Created by 陈栋 on 16/1/30.
 * 功能:
 */
public class BaseAppCompatDialog extends AppCompatDialog {
    public BaseAppCompatDialog(Context context) {
        super(context);
    }

    public BaseAppCompatDialog(Context context, int theme) {
        super(context, theme);
    }

    public BaseAppCompatDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
