package com.march.libs.badge;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * com.march.libs.badge
 * CdLibsTest
 * Created by chendong on 16/7/26.
 * Copyright © 2016年 chendong. All rights reserved.
 * Desc : 使用launcher来区分，而不是用手机型号，防止某些手机换了桌面应用。
 * 支持的launcher xiaomi,samsung,lg,htc,sony,adw,apex,asus,solid,nova,huawei(没有测试)
 */
public class BadgeFactory {

    private static String TAG = BadgeFactory.class.getSimpleName();
    //小米
    private static final List<String> XIAOMI = Arrays.asList(
            "com.miui.miuilite",
            "com.miui.home",
            "com.miui.miuihome",
            "com.miui.miuihome2",
            "com.miui.mihome",
            "com.miui.mihome2"
    );
    //三星
    private static final List<String> SAMSUNG = Arrays.asList(
            "com.sec.android.app.launcher",
            "com.sec.android.app.twlauncher"
    );
    //LG
    private static final List<String> LG = Arrays.asList(
            "com.lge.launcher",
            "com.lge.launcher2",
            "com.lge.launcher3"
    );
    //HTC
    private static final List<String> HTC = Collections.singletonList("com.htc.launcher");
    private static final List<String> ADW = Arrays.asList(
            "org.adw.launcher",
            "org.adwfreak.launcher"
    );
    //索尼
    private static final List<String> SONY = Collections.singletonList("com.sonyericsson.home");
    //非国内主流launcher
    private static final List<String> APEX = Collections.singletonList("com.anddoes.launcher");
    private static final List<String> ASUS = Collections.singletonList("com.asus.launcher");
    private static final List<String> NOVA = Collections.singletonList("com.teslacoilsw.launcher");
    private static final List<String> SOLID = Collections.singletonList("com.majeur.launcher");
    private static final List<String> HUAWEI = Collections.singletonList("com.huawei.android.launcher");


    /**
     * 获取badger
     *
     * @param launcherName launcher name
     * @return badger
     */
    public static Badge getBadge(String launcherName) {
        Badge mBadge = new DefaultBadge();
        if (isContains(XIAOMI, launcherName)) {
            mBadge = new XiaomiBadge();
        } else if (isContains(SAMSUNG, launcherName)) {
            mBadge = new SamsungBadge();
        } else if (isContains(HTC, launcherName)) {
            mBadge = new HTCBadge();
        } else if (isContains(LG, launcherName)) {
            mBadge = new LGBadge();
        } else if (isContains(NOVA, launcherName)) {
            mBadge = new NovaBadge();
        } else if (isContains(SONY, launcherName)) {
            mBadge = new SonyBadge();
        } else if (isContains(ADW, launcherName)) {
            mBadge = new AdwBadge();
        } else if (isContains(SOLID, launcherName)) {
            mBadge = new SolidBadge();
        } else if (isContains(APEX, launcherName)) {
            mBadge = new ApexBadge();
        } else if (isContains(ASUS, launcherName)) {
            mBadge = new AsusBadge();
        } else if (isContains(HUAWEI, launcherName)) {
            mBadge = new HuaweiBadge();
        }
        return mBadge;
    }

    private static boolean isContains(List<String> list, String name) {
        return list.contains(name);
    }


    public abstract static class Badge {
        public abstract void executeBadge(Context context, Notification notification, int notificationId, int count);

        /**
         * 获取当前app 的启动页面activity的classname
         */
        protected String getLauncherClassName(Context context) {
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

        protected void setNotification(Notification notification, int notificationId, Context context) {
            if (notification != null) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notificationId, notification);
            }
        }
    }

    public static class HuaweiBadge extends Badge {
        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {
            String launcherClassName = getLauncherClassName(context);
            Bundle localBundle = new Bundle();
            localBundle.putString("package", context.getPackageName());
            localBundle.putString("class", launcherClassName);
            localBundle.putInt("badgenumber", count);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, localBundle);
        }
    }

    //    小米
    public static class XiaomiBadge extends Badge {
        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {

            boolean isUpMIUIV6 = true;
            if (notification == null) {
                Log.e(TAG, "小米的launcher需要用户自己去点击图标或者清除通知才可以清除");
                return;
            }
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
                        context.getPackageName() + "/" + getLauncherClassName(context));
                localIntent.putExtra("android.intent.extra.update_application_message_text", String.valueOf(count));
                context.sendBroadcast(localIntent);
            } finally {
                if (isUpMIUIV6) {
                    //miui6以上版本需要使用通知发送
                    setNotification(notification, notificationId, context);
                }
            }
        }
    }

    //三星
    public static class SamsungBadge extends Badge {
        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {
            setNotification(notification, notificationId, context);
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", count);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);
        }
    }

    //索尼
    public static class SonyBadge extends Badge {

        private static final String INTENT_ACTION = "com.sonyericsson.home.action.UPDATE_BADGE";
        private static final String INTENT_EXTRA_PACKAGE_NAME = "com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME";
        private static final String INTENT_EXTRA_ACTIVITY_NAME = "com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME";
        private static final String INTENT_EXTRA_MESSAGE = "com.sonyericsson.home.intent.extra.badge.MESSAGE";
        private static final String INTENT_EXTRA_SHOW_MESSAGE = "com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE";

        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {
            setNotification(notification, notificationId, context);
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }
            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(INTENT_EXTRA_PACKAGE_NAME, context.getPackageName());
            intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, launcherClassName);
            intent.putExtra(INTENT_EXTRA_MESSAGE, String.valueOf(count));
            intent.putExtra(INTENT_EXTRA_SHOW_MESSAGE, count > 0);
            context.sendBroadcast(intent);
        }
    }

    //adw
    public static class AdwBadge extends Badge {
        public static final String INTENT_UPDATE_COUNTER = "org.adw.launcher.counter.SEND";
        public static final String PACKAGENAME = "PNAME";
        public static final String COUNT = "COUNT";

        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {
            setNotification(notification, notificationId, context);
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }

            Intent intent = new Intent(INTENT_UPDATE_COUNTER);
            intent.putExtra(PACKAGENAME, context.getPackageName());
            intent.putExtra(COUNT, count);
            context.sendBroadcast(intent);
        }
    }

    //apex
    public static class ApexBadge extends Badge {

        private static final String INTENT_UPDATE_COUNTER = "com.anddoes.launcher.COUNTER_CHANGED";
        private static final String PACKAGENAME = "package";
        private static final String COUNT = "count";
        private static final String CLASS = "class";

        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {
            setNotification(notification, notificationId, context);
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }

            Intent intent = new Intent(INTENT_UPDATE_COUNTER);
            intent.putExtra(PACKAGENAME, context.getPackageName());
            intent.putExtra(COUNT, count);
            intent.putExtra(CLASS, launcherClassName);
            context.sendBroadcast(intent);
        }
    }

    //asus
    public static class AsusBadge extends Badge {

        private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
        private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
        private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";
        private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";

        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {
            setNotification(notification, notificationId, context);
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }

            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(INTENT_EXTRA_BADGE_COUNT, count);
            intent.putExtra(INTENT_EXTRA_PACKAGENAME, context.getPackageName());
            intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, launcherClassName);
            intent.putExtra("badge_vip_count", 0);
            context.sendBroadcast(intent);
        }
    }

    //默认
    public static class DefaultBadge extends Badge {
        private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
        private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
        private static final String INTENT_EXTRA_PACKAGE_NAME = "badge_count_package_name";
        private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";

        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {
            setNotification(notification, notificationId, context);
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }

            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(INTENT_EXTRA_BADGE_COUNT, count);
            intent.putExtra(INTENT_EXTRA_PACKAGE_NAME, context.getPackageName());
            intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, launcherClassName);
            context.sendBroadcast(intent);
        }
    }

    //htc
    public static class HTCBadge extends Badge {

        public static final String INTENT_UPDATE_SHORTCUT = "com.htc.launcher.action.UPDATE_SHORTCUT";
        public static final String INTENT_SET_NOTIFICATION = "com.htc.launcher.action.SET_NOTIFICATION";
        public static final String PACKAGENAME = "packagename";
        public static final String COUNT = "count";
        public static final String EXTRA_COMPONENT = "com.htc.launcher.extra.COMPONENT";
        public static final String EXTRA_COUNT = "com.htc.launcher.extra.COUNT";

        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {
            setNotification(notification, notificationId, context);
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }
            ComponentName localComponentName = new ComponentName(context.getPackageName(),
                    getLauncherClassName(context));

            Intent intent1 = new Intent(INTENT_SET_NOTIFICATION);
            intent1.putExtra(EXTRA_COMPONENT, localComponentName.flattenToShortString());
            intent1.putExtra(EXTRA_COUNT, count);
            context.sendBroadcast(intent1);

            Intent intent = new Intent(INTENT_UPDATE_SHORTCUT);
            intent.putExtra(PACKAGENAME, context.getPackageName());
            intent.putExtra(COUNT, count);
            context.sendBroadcast(intent);
        }
    }

    //lg
    public static class LGBadge extends Badge {

        private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
        private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
        private static final String INTENT_EXTRA_PACKAGE_NAME = "badge_count_package_name";
        private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";

        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {
            setNotification(notification, notificationId, context);
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }

            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(INTENT_EXTRA_BADGE_COUNT, count);
            intent.putExtra(INTENT_EXTRA_PACKAGE_NAME, context.getPackageName());
            intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, launcherClassName);
            context.sendBroadcast(intent);
        }
    }

    //nova
    public static class NovaBadge extends Badge {

        private static final String CONTENT_URI = "content://com.teslacoilsw.notifier/unread_count";
        private static final String COUNT = "count";
        private static final String TAG = "tag";

        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {
            setNotification(notification, notificationId, context);

            ContentValues contentValues = new ContentValues();
            contentValues.put(TAG, context.getPackageName() + "/" +
                    getLauncherClassName(context));
            contentValues.put(COUNT, count);
            try {
                context.getContentResolver().insert(Uri.parse(CONTENT_URI),
                        contentValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //solid
    public static class SolidBadge extends Badge {

        private static final String INTENT_ACTION_UPDATE_COUNTER = "com.majeur.launcher.intent.action.UPDATE_BADGE";
        private static final String INTENT_EXTRA_PACKAGENAME = "com.majeur.launcher.intent.extra.BADGE_PACKAGE";
        private static final String INTENT_EXTRA_COUNT = "com.majeur.launcher.intent.extra.BADGE_COUNT";
        private static final String INTENT_EXTRA_CLASS = "com.majeur.launcher.intent.extra.BADGE_CLASS";

        @Override
        public void executeBadge(Context context, Notification notification, int notificationId, int count) {
            setNotification(notification, notificationId, context);
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }

            Intent intent = new Intent(INTENT_ACTION_UPDATE_COUNTER);
            intent.putExtra(INTENT_EXTRA_PACKAGENAME, context.getPackageName());
            intent.putExtra(INTENT_EXTRA_COUNT, count);
            intent.putExtra(INTENT_EXTRA_CLASS, launcherClassName);
            context.sendBroadcast(intent);
        }
    }

}
