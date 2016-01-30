package com.march.libs;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

/**
 * CdLibsTest     com.march.libs
 * Created by 陈栋 on 16/1/30.
 * 功能:
 */
public class BaseDialog extends AppCompatDialog {
    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
