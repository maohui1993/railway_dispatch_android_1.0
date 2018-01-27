package cn.dazhou.railway.dispatch;

import android.content.Context;
import android.support.multidex.MultiDex;

import org.doubango.ngn.NgnApplication;

/**
 * @author Hooyee on 2018/1/26.
 */

public class AppApplication extends NgnApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
