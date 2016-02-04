package com.march.libs.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected boolean isForward;//Activity是否在前台
    protected BaseActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        self = this;
    }

    public abstract int getLayoutId();


    /**
     * restart之后会调用resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        isForward = true;
    }

    /**
     * pause状态下是部分可见的
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isForward = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public <T extends BaseDialog> void dismissDialog(T t) {
        if (t != null && isForward && t.isShowing()) {
            t.dismiss();
        }
    }

    public <T extends BaseDialog> void showDialog(T t) {
        if (t != null && isForward && !t.isShowing()) {
            t.show();
        }
    }
}
