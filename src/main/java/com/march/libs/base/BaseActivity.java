package com.march.libs.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.march.libs.utils.ActAnimUtils;
import com.march.libs.utils.LUtils;

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
public abstract class BaseActivity extends AppCompatActivity {

    protected Intent jumpIntent;
    protected boolean isForward;//Activity是否在前台
    protected BaseActivity self;//当前Activity的引用
    protected GestureDetector gestureDetector;//手势操作
    private boolean isFlingCatch = true;//是否允许截断滑动事件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        self = this;
        gestureDetector = new GestureDetector(self, new DiyGestureListener(), null, true);
    }

    public abstract int getLayoutId();


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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
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


    /**
     * 自定义滑动监听
     */
    class DiyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //是否允许截断滑动事件
            if (!isFlingCatch)
                return false;
            if (Math.abs(velocityX) > 1000 || Math.abs(velocityY) > 1000) {
                //空白区域触发
                float minMove = 120;         //最小滑动距离
                float minVelocity = 1000;      //最小滑动速度
                float beginX = e1.getX();
                float endX = e2.getX();
                float beginY = e1.getY();
                float endY = e2.getY();

                if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) {   //上滑
                    FlingTop();
                    return true;
                } else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) {   //下滑
                    FlingBottom();
                    return true;
                } else if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) {   //左滑
                    FlingLeft();
                    return true;
                } else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) {   //右滑
                    FlingRight();
                    return true;
                }
                return true;
            }

            return false;
        }
    }

    protected void FlingLeft() {
        LUtils.i("界面左滑");
    }

    /**
     * 向右滑动触发事件
     */
    protected void FlingRight() {
        LUtils.i("界面右滑");
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    protected void FlingTop() {
        LUtils.i("界面上滑");
    }

    protected void FlingBottom() {
        LUtils.i("界面下滑");
    }

    //设置是否允许截断滑动事件
    protected void setFlingCatch(boolean is) {
        this.isFlingCatch = is;
    }


    protected void setVisible(View view) {
        if (!view.isShown()) {
            view.setVisibility(View.VISIBLE);
        }
    }

    protected void setGone(View view) {
        if (view.isShown()) {
            view.setVisibility(View.GONE);
        }
    }


}
