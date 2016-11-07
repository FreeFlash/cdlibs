package com.march.libs.badge;

import android.app.Activity;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.util.Log;

/**
 * com.march.libs.badge
 * CdLibsTest
 * Created by chendong on 16/7/26.
 * Copyright © 2016年 chendong. All rights reserved.
 * Desc : 添加角标的帮助类，需要注意的是，不同的launcher
 * launcher类型  点击图标  清除通知  不同notifyId
 * 三星,点击图标数字不会消失,清除通知数字不会消失,不同的notifyId不会叠加，新的会把旧的数字替换掉,相同的notifyId不会叠加，新的会把旧的数字替换掉
 * xiaomi,当应用位于前台时,添加角标是不生效的,点击图标进入或者清除通知之后角标会自动消失，如果发送了两条通知（id不同），那么通知的count会叠加，不同的id会替换掉,没有清除角标的方法，需要用户自己点击图标和清除通知
 *
 * 乐视：乐视代表的是default类型，每次发一条不同的通知，数目会加1，点击之后消失
 */
public class BadgeHelper {

    private Context context;
    private static BadgeHelper mInst;

    private BadgeHelper(Context context) {
        this.context = context;
    }

    public static BadgeHelper newInst(Context context) {
        synchronized (BadgeHelper.class) {
            if (mInst == null) {
                mInst = new BadgeHelper(context);
            }
        }
        return mInst;
    }

    public static BadgeHelper get() {
        return mInst;
    }

    private static final String TAG = BadgeHelper.class.getSimpleName();
    private static BadgeFactory.Badge badger;

    /**
     * @param notification 更新角标一般都是和发送notification并行的。如果不想发notification只是更新角标，这里传null
     * @param notifyID     notification id
     * @param count        整个app所有的未读数量（其他的是使用这个count）
     */
    public void sendBadgeNotification(Notification notification, int notifyID, int count) {
        if (count <= 0) {
            count = 0;
        } else {
            count = Math.max(0, Math.min(count, 99));
        }
        String currentHomePackage = getLauncherName(context);
        Log.d(TAG, "currentHomePackage:" + currentHomePackage);
        if (badger == null)
            badger = BadgeFactory.getBadge(currentHomePackage);
        badger.executeBadge(context, notification, notifyID, count);
    }

    /**
     * 重置、清除Badge未读显示
     */
    public void removeBadgeCount(Notification notification) {
        sendBadgeNotification(notification, 0, 0);
    }

    private String getLauncherName(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null)
            return resolveInfo.activityInfo.packageName;
        else
            return "";
    }


    public static void createShortCut(Activity act, int iconResId,
                                      int appnameResId) {

        // com.android.launcher.permission.INSTALL_SHORTCUT
        BitmapDrawable iconBitmapDrawabel = null;
        // 获取应用基本信息
        String label = act.getPackageName();
        PackageManager packageManager = act.getPackageManager();
        try {
            iconBitmapDrawabel = (BitmapDrawable) packageManager.getApplicationIcon(label);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Intent shortcutintent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutintent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, label);
        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(
                act.getApplicationContext(), iconResId);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
//        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,iconBitmapDrawabel.getBitmap());

        // 点击快捷图片，运行的程序主入口
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(act.getApplicationContext(), act.getClass()));

        ComponentName comp = new ComponentName(label, "." + act.getLocalClassName());
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
        // 发送广播
        act.sendBroadcast(shortcutintent);
    }
}
