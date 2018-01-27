package cn.dazhou.railway.dispatch;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.WeakHashMap;

import cn.dazhou.sip.ISipService;
import cn.dazhou.sip.ISipSession;

/**
 * @author Hooyee on 2017/12/5.
 */

public class SipLauncher {
    private static ISipService sSipService;
    public static WeakHashMap<String, ISipSession> sSessionMap = new WeakHashMap();   // key : 真实的groupCode，非name
//    public static SipPhoneState sState;//对讲状态
    public static long sBroadcastId = -1;//所属对讲id
    public static long sCallInId = -1;

    private static Context sContext;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    /**
     * 儅不在使用doubango的sip實現時，替換"org.doubango.aspect.impl.NgnSipServiceImpl"即可
     *
     * @return
     */
    public synchronized static ISipService getSipService() {
        if (sSipService == null) {
            synchronized (SipLauncher.class) {
                if (sSipService == null) {
                    try {
                        Class clazz = Class.forName("org.doubango.aspect.impl.NgnSipServiceImpl");
                        sSipService = (ISipService) clazz.newInstance();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            checkSelf();
        }
        return sSipService;
    }

    /**
     * 儅不在使用doubango的sip實現時，替換"org.doubango.aspect.impl.NgnSipServiceImpl"即可
     *
     * @return
     */
    public synchronized static ISipService getSipService(boolean needCheckSelf) {
        if (sSipService == null) {
            synchronized (SipLauncher.class) {
                if (sSipService == null) {
                    try {
                        Class clazz = Class.forName("org.doubango.aspect.impl.NgnSipServiceImpl");
                        sSipService = (ISipService) clazz.newInstance();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sSipService;
    }

    public static void context(Context context) {
        sContext = context;
    }

    public static void restart() {
        SipLauncher.getSipService()
                .stop()
                .start();
    }

    public static void checkSelf() {
        Log.i("checkSelf", "self");
        if (sSipService.isRegistered()) {
            sSipService.needInterrupt(false);
        } else {
            sSipService.needInterrupt(true);
            if (sContext != null) {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                       Log.e("checkSelf", "Sip服务器歇菜了");
                    }
                });
            }
        }
    }
}
