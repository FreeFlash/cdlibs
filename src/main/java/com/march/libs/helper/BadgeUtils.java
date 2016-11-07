package com.march.libs.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * com.march.libs.helper
 * CdLibsTest
 * Created by chendong on 16/7/26.
 * Copyright © 2016年 chendong. All rights reserved.
 * Desc :角标帮助类，支持MIUI，Sony，SAMSUNG，LG，HTC，Nova
 */
public class BadgeUtils {

    private Context context;
    private static BadgeUtils mInst;

    private BadgeUtils(Context context) {
        this.context = context;
    }

    public static BadgeUtils getInst(Context context) {
        synchronized (BadgeUtils.class) {
            if (mInst == null) {
                mInst = new BadgeUtils(context);
            }
        }
        return mInst;
    }

    private static final String TAG = BadgeUtils.class.getSimpleName();
    private static final String XIAOMI = "xiaomi";
    private static final String SONY = "sony";
    private static final String SAMSUNG = "samsung";
    private static final String LG = "lg";
    private static final String HTC = "htc";
    private static final String NOVA = "nova";

    /**
     * 设置Badge分流
     *
     * @param count     count
     * @param iconResId 小米不能单纯的加角标，通过通知的方式，通知小图标
     */
    public void setBadgeCount(int count, int iconResId) {
        if (count <= 0 || count > 99) {
            Log.e(TAG, "number is to large or too small");
            return;
        }
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer.equalsIgnoreCase(XIAOMI)) {
            //小米
            setBadgeOfMIUI(count, iconResId);
        } else if (manufacturer.equalsIgnoreCase(SONY)) {
            //索尼
            setBadgeOfSony(count);
        } else if (manufacturer.toLowerCase().contains(SAMSUNG) ||
                manufacturer.toLowerCase().contains(LG)) {
            //LG和三星好基友
            setBadgeOfSumsung(count);
        } else if (manufacturer.toLowerCase().contains(HTC)) {
            //HTC
            setBadgeOfHTC(count);
        } else if (manufacturer.toLowerCase().contains(NOVA)) {
            //使用nova launcher
            setBadgeOfNova(count);
        } else {
            Log.e(TAG, "当前不支持该launcher添加角标" + manufacturer);
        }
    }


    /**
     * 获取launcher class name
     *
     * @return launcher class name
     */
    private String getLauncherClassName() {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        return info.activityInfo.name;
    }


    /**
     * 索尼添加角标
     * <uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" />
     *
     * @param number 角标数
     */
    private void setBadgeOfSony(int number) {
        Intent localIntent = new Intent();
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", true);//是否显示
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", getLauncherClassName());//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", number);//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());//包名
        context.sendBroadcast(localIntent);
    }

    /**
     * 三星和LG添加角标
     *
     * @param number 角标数
     */
    private void setBadgeOfSumsung(int number) {
        Intent localIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        localIntent.putExtra("badge_count", number);//数字
        localIntent.putExtra("badge_count_package_name", context.getPackageName());//包名
        localIntent.putExtra("badge_count_class_name", getLauncherClassName()); //启动页
        context.sendBroadcast(localIntent);
    }

    /**
     * 设置MIUI的Badge
     *
     * @param count count
     */
    private void setBadgeOfMIUI(int count, int iconResId) {
        boolean isUpMIUIV6 = true;
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("title").setContentText("content").setSmallIcon(iconResId);
        Notification notification = builder.build();
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, count);
        } catch (Exception e) {
            e.printStackTrace();
            // miui 6之前的版本
            isUpMIUIV6 = false;
            Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra("android.intent.extra.update_application_component_name",
                    context.getPackageName() + "/" + getLauncherClassName());
            localIntent.putExtra("android.intent.extra.update_application_message_text", String.valueOf(count == 0 ? "" : count));
            context.sendBroadcast(localIntent);
        } finally {
            if (notification != null && isUpMIUIV6) {
                //miui6以上版本需要使用通知发送
                mNotificationManager.notify(101010, notification);
            }
        }
    }

    /**
     * 设置HTC的Badge
     *
     * @param count   count
     */
    private void setBadgeOfHTC(int count) {
        Intent intentNotification = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
        ComponentName localComponentName = new ComponentName(context.getPackageName(), getLauncherClassName());

        intentNotification.putExtra("com.htc.launcher.extra.COMPONENT", localComponentName.flattenToShortString());
        intentNotification.putExtra("com.htc.launcher.extra.COUNT", count);
        context.sendBroadcast(intentNotification);

        Intent intentShortcut = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
        intentShortcut.putExtra("packagename", context.getPackageName());
        intentShortcut.putExtra("count", count);
        context.sendBroadcast(intentShortcut);
    }

    /**
     * 设置Nova的Badge
     *
     * @param count   count
     */
    private void setBadgeOfNova(int count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tag", context.getPackageName() + "/" +
                getLauncherClassName());
        contentValues.put("count", count);
        context.getContentResolver().insert(Uri.parse("content://com.teslacoilsw.notifier/unread_count"),
                contentValues);
    }

    public static void setBadgeOfMadMode(Context context, int count, String packageName, String className) {
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", packageName);
        intent.putExtra("badge_count_class_name", className);
        context.sendBroadcast(intent);
    }
}
