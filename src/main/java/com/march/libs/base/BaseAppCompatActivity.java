package com.march.libs.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.march.rapiddevelibs.utils.ActAnimUtils;
import com.march.rapiddevelibs.utils.LUtils;

import butterknife.ButterKnife;

/**
 * @author chendong
 * @描述 Activity的基类
 * 1.实现滑动结束当前Activity的操作
 * 2.记录当前Activty是否在前台
 * 3.持有当前Activity的引用
 * 4.使用ButterKnife实现注解控件绑定
 * 5.实现Dialog的安全显示和取消
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {

    protected Intent jumpIntent;//跳转Intent
    protected boolean isForward;//Activity是否在前台
    protected BaseAppCompatActivity self;//当前Activity的引用


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示
        if (isFullScreen()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(getLayoutId());
        //隐藏actionbar
        if (isNoActionBar()) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            if (getActionBar() != null) {
                getActionBar().hide();
            }
        }
        ButterKnife.bind(this);
        self = this;
        initDatas();
        initViews();
        initEvents();
    }

    protected void initDatas() {
    }

    protected void initViews() {
    }

    protected void initEvents() {
    }

    protected abstract int getLayoutId();

    protected boolean isFullScreen() {
        return false;
    }

    protected boolean isNoActionBar() {
        return false;
    }

    protected String str(Object obj) {
        return String.valueOf(obj);
    }

    /**
     * 获取跳换使用的Intent,避免使用过多引用
     *
     * @param cls
     * @return
     */
    protected Intent getJumpIntent(Class cls) {
        jumpIntent = new Intent(this, cls);
        return jumpIntent;
    }

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


    public void finish(ActAnimUtils.AnimStyle style) {
        finish();
        ActAnimUtils.transActivity(this, style);
    }

    public void startActivity(Intent intent, ActAnimUtils.AnimStyle style) {
        startActivity(intent);
        ActAnimUtils.transActivity(this, style);
    }

    public void startActivityForResult(Intent intent, int requestCode, ActAnimUtils.AnimStyle style) {
        startActivityForResult(intent, requestCode);
        ActAnimUtils.transActivity(this, style);
    }

    /**
     * 安全消除对话框
     *
     * @param t
     * @param <T>
     */
    public <T extends BaseAppCompatDialog> void dismissDialog(T t) {
        if (t != null && isForward && t.isShowing()) {
            t.dismiss();
        }
    }

    public <T extends BaseAppCompatDialog> void showDialog(T t) {
        if (t != null && isForward && !t.isShowing()) {
            t.show();
        }
    }


    protected void setVisible(View view) {
        if (view != null && !view.isShown()) {
            view.setVisibility(View.VISIBLE);
        }
    }

    protected void setGone(View view) {
        if (view != null && view.isShown()) {
            view.setVisibility(View.GONE);
        }
    }


}
