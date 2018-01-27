package cn.dazhou.commonlib.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RemoteViews;

import cn.dazhou.commonlib.R;


/**
 * Created by Hooyee on 2017/10/27.
 */

public class WidgetUtil {
    public static View v;
    public static final String NOTIFICATION_ACTION_CANCEL = "action.notification.BUTTON_CLICKED.cancel";
    public static final String NOTIFICATION_ACTION_PAUSE = "action.notification.BUTTON_CLICKED.pause";

    public static Notification sendNotification(Context context, Class activityClass) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.down)
                .setContent(new RemoteViews(context.getPackageName(), R.layout.view_notification))
                .setDefaults(Notification.DEFAULT_ALL)
                .setOngoing(true);
        builder.setTicker("软件更新");//第一次提示消息的时候显示在通知栏上
        builder.setAutoCancel(true);//自己维护通知的消失
        Intent intent = new Intent(context, activityClass);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pIntent);
        Notification notification = builder.build();

        Intent cancelIntent = new Intent(NOTIFICATION_ACTION_CANCEL);
        Intent pauseIntent = new Intent(NOTIFICATION_ACTION_PAUSE);

        PendingIntent cancelPending = PendingIntent.getBroadcast(context,0,cancelIntent,0);
        PendingIntent pausePending = PendingIntent.getBroadcast(context,0,pauseIntent,0);
        notification.contentView.setOnClickPendingIntent(R.id.bt_cancel, cancelPending);
        notification.contentView.setOnClickPendingIntent(R.id.bt_pause, pausePending);
        notification.contentView.setProgressBar(R.id.progress, 100, 0, false);

        notificationManager.notify(1, notification);
        return notification;
    }

    public static AlertDialog.Builder showDialog(Context context, String content) {
        return new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.version_update))
                .setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage(content);
    }

    public static View createProgressBar(Context context, ViewGroup root, boolean interceptClick) {
        View v = LayoutInflater.from(context).inflate(R.layout.progress, null);
        if (root != null) {
            root.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        v.setVisibility(View.GONE);
        v.setClickable(interceptClick);
        return v;
    }

    public static View windowFloat(Context context, View view, int type, int width, int height, int gravity) {
        if (view.getParent()!= null && view.getParent() instanceof ViewGroup) {
            ViewGroup root = (ViewGroup) view.getParent();
            root.removeView(view);
        }

        // 获取WindowManager
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        //参数设置：
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.type = type;
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
//        int flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = width;
        params.height = height;
        params.gravity = gravity;

        try {
            windowManager.addView(view, params);
        } catch (Exception e) {
            windowManager.removeView(view);
            windowManager.addView(view, params);
        }
        return view;
    }

    public static void updateFloatView(Context context, View view, WindowManager.LayoutParams layoutParams) {
        // 获取WindowManager
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.updateViewLayout(view, layoutParams);
    }

    public static void removeFloatView(Context context, View view) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        try {
            windowManager.removeView(view);
        } catch (Exception e) {

        }
    }
}
