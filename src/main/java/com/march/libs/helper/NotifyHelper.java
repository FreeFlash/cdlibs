package com.march.libs.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;

/**
 * CdLibsTest     com.march.libs.helper
 * Created by 陈栋 on 16/3/28.
 * 功能:
 */
public class NotifyHelper {

    /**
     * 自定义程度较高的通知,相关参数说明;
     * int类型变量若不使用设为-1
     * 对象类型若不是用设为null
     *
     * @param logoRes      logo资源
     * @param id           通知的id号
     * @param context      上下文
     * @param title        标题
     * @param content      通知的内容
     * @param ticker       滚动显示的提示信息
     * @param isOnGoing    是否可以滑动删除
     * @param isAutoCancle 点击之后是否自动删除
     * @param priority     设置通知优先级别，使用
     *                     NotificationCompat.PRIORITY_MAX
     *                     PRIORITY_MIN
     *                     PRIORITY_DEFAULT
     *                     PRIORITY_HIGH
     *                     PRIORITY_LOW
     * @param defaults     设置提示的方式，响铃或者震动，使用参数<br>
     *                     Notification.DEFAULT_VIBRATE
     *                     DEFAULT＿LIGHTS
     *                     DEFAULT_ALL
     *                     DEFAULT_SOUNDS
     * @param bitLarge     设置显示的大图
     * @param activity     设置将要跳转的意图
     * @param broadcast    设置将要发送的广播
     */
    public static void Notify(
            int logoRes,
            int id,
            Context context,
            String title,
            String content,
            String ticker,
            boolean isOnGoing,
            boolean isAutoCancle,
            int priority,
            int defaults,
            Bitmap bitLarge,
            Intent activity,
            Intent broadcast) {
        /* 获取通知管理者，并获得系统通知 的服务 */
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        /* 获得通知的构建器 */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context.getApplicationContext());
        /* 设置小图标,这是必须的用于在通知栏进行显示，默认使用了软件的图标 */
        builder.setSmallIcon(logoRes)
                /* 设置显示的标题 */
                .setContentTitle(title)
                /* 设置内容 */
                .setContentText(content);

		/* 设置滚动显示的提示信息 */
        if (ticker != null)
            builder.setTicker(ticker);
        /* 设置不可滑动删除 */
        builder.setOngoing(isOnGoing);

		/* 设置优先级 */
        if (priority == -1)
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
        else
            builder.setPriority(priority);

		/* 设置通知铃声或者震动 */
        if (defaults == -1)
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        else
            builder.setDefaults(defaults);

		/* 设置大图 */
        if (bitLarge != null)
            builder.setLargeIcon(bitLarge);

		/* 设置跳转到的页面 */
        if (activity != null) {
            PendingIntent i = PendingIntent.getActivity(context, 1, activity,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(i);
        }
        /*广播*/
        if (broadcast != null) {
            PendingIntent i = PendingIntent.getBroadcast(context, 1, broadcast,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(i);
        }

		/* 设置自动删除通知 */
        builder.setAutoCancel(isAutoCancle);
        manager.notify(id, builder.build());
    }

    /**
     * 使用简单地一般化通知,默认使用了震动响铃通知，可以滑动删除
     *
     * @param logoRes logo资源
     * @param id      通知的id号
     * @param context 上下文
     * @param title   标题
     * @param content 通知的内容
     * @param intent  将要跳转的意图
     */
    public static void Notify(int logoRes,
                              int id,
                              Context context,
                              String title,
                              String content,
                              Intent intent) {
        /* 获取通知管理者，并获得系统通知 的服务 */
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
		/* 获得通知的构建器 */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context.getApplicationContext());
		/* 设置小图标,这是必须的用于在通知栏进行显示，默认使用了软件的图标 */
        builder.setSmallIcon(logoRes)
		        /* 设置显示的标题 */
                .setContentTitle(title)
		        /* 设置内容 */
                .setContentText(content)
                /*设置顶部显示*/
                .setTicker(title);
        builder.setDefaults(Notification.DEFAULT_ALL);
		/* 设置跳转到的页面 */
        if (intent != null) {
            PendingIntent i = PendingIntent.getActivity(context, 1, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(i);
        }

		/* 设置自动删除通知 */
        builder.setAutoCancel(true);
        manager.notify(id, builder.build());
    }
}



