package cn.dazhou.commonlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

public class NetworkReceiver extends BroadcastReceiver {
    private static List<IObserver> sObservers = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null) {
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        }
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
            mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        }
        if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState) {
            // 手机网络连接成功
            //      IMUtil.login(context);
            for (IObserver o : sObservers) {
                o.onNetOn();
            }
        } else if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED != mobileState) {
            // 手机没有任何的网络
            for (IObserver o : sObservers) {
                o.onNetOff();
            }
        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
            // 无线网络连接成功
            //     IMUtil.login(context);
            for (IObserver o : sObservers) {
                o.onNetOn();
            }
        }
    }

    public static void register(IObserver observer) {
        sObservers.add(observer);
    }

    public static void unRegister(IObserver observer) {
        sObservers.remove(observer);
    }

    public interface IObserver {
        void onNetOn();

        void onNetOff();
    }

}
