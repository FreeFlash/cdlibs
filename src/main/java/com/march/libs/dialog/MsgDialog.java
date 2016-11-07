package com.march.libs.dialog;

import android.content.Context;
import android.os.Bundle;

import com.march.libs.R;
import com.march.libs.base.BaseAppCompatDialog;

/**
 * CdLibsTest     com.march.libs
 * Created by 陈栋 on 16/2/4.
 * 功能:
 */
public class MsgDialog extends BaseAppCompatDialog {

    public MsgDialog(Context context) {
        super(context, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_msg);
    }
}
