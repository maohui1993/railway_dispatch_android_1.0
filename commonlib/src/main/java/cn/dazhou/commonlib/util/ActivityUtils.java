package cn.dazhou.commonlib.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    /**
     * 判断是否平板设备
     *
     * @param context
     * @return true:平板,false:手机
     */
    public static boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void replaceFragmentOfActivity (@NonNull FragmentManager fragmentManager,
                                                  @NonNull Fragment from, @NonNull Fragment to, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(from);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (to.isAdded()) {
            transaction.hide(from).show(to).commit();
        } else {
            transaction.add(frameId, to).hide(from).show(to).commit();
        }
//        transaction.replace(frameId, fragment);
//        transaction.commit();
    }

    public static void hideFragment (@NonNull Fragment fragment) {
        checkNotNull(fragment);
        if(fragment.getFragmentManager() == null) {
            return;
        }
        FragmentTransaction transaction = fragment.getFragmentManager().beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

    public static void showFragment (@NonNull Fragment fragment) {
        checkNotNull(fragment);
        if(fragment.getFragmentManager() == null) {
            return;
        }
        FragmentTransaction transaction = fragment.getFragmentManager().beginTransaction();
        transaction.show(fragment);
        transaction.commit();
    }

    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new RuntimeException();
        }
        return obj;
    }

    public static int getSupportSoftInputHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        Rect r = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight(activity);
        }
        if (softInputHeight < 0) {
            Log.w("EmotionInputDetector", "Warning: value of softInputHeight is below zero!");
        }

        return softInputHeight;
    }

    public static int getScreenHeight(Activity activity) {
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        return screenHeight;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    public static void callPhone(Activity context, String phone) {
        if (phone == null) {
            Toast.makeText(context, "该联系人没有手机信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phone.startsWith("tel")) {
            phone = "tel:" + phone;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(context,
                    new String[]{
                            Manifest.permission.CALL_PHONE,},
                    1);
        } else {
            context.startActivity(intent);
        }
    }

    public static long getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }
}
