package cn.dazhou.railway.dispatch;

import android.content.Context;
import android.support.multidex.MultiDex;

import org.doubango.ngn.NgnApplication;

import cn.dazhou.railway.dispatch.mvp.login.RegisterInfo;

/**
 * @author Hooyee on 2018/1/26.
 */

public class AppApplication extends NgnApplication {
    private static AppApplication singleton;
    public RegisterInfo info;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        singleton = this;
    }

    public static AppApplication getInstance() {
        return singleton;
    }
}
