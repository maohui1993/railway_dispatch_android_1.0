package cn.dazhou.commonlib.util;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.Toast;

/**
 * Created by hooyee on 2017/9/4.
 */

public class Util {
    /**
     * dpתpx
     *
     */
    public static int dip2px(Context ctx,float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     *	pxתdp
     */
    public static int px2dip(Context ctx,float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void sendBroadcast(Context context, String broadcaseName, String key, Parcelable data) {
        Intent intent = new Intent();
        intent.setAction(broadcaseName);
        intent.putExtra(key, data);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String broadcaseName, String key, String data) {
        Intent intent = new Intent();
        intent.setAction(broadcaseName);
        intent.putExtra(key, data);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String broadcastName) {
        Intent intent = new Intent();
        intent.setAction(broadcastName);
        context.sendBroadcast(intent);
    }

}
