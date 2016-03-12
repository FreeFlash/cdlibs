package com.march.libs.mycamera;

import com.march.libs.base.BaseActivity;

/**
 * CdLibsTest     com.march.libs.mycamera
 * Created by 陈栋 on 16/3/12.
 * 功能:
 */
public abstract class BaseCamActivity extends BaseActivity {

    /******************
     * 固定初始化代码
     *********************************/

    @Override
    protected void onResume() {
        super.onResume();
        CameraNative.getInst().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CameraNative.getInst().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraNative.getInst().onDestory();
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected boolean isNoActionBar() {
        return true;
    }
}
