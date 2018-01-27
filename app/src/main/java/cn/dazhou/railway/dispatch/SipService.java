package cn.dazhou.railway.dispatch;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.doubango.ngn.events.NgnInviteEventArgs;
import org.doubango.ngn.events.NgnMediaPluginEventArgs;
import org.doubango.ngn.events.NgnMessagingEventArgs;
import org.doubango.ngn.events.NgnRegistrationEventArgs;

import cn.dazhou.commonlib.api.HttpApi;
import cn.dazhou.commonlib.util.HttpApiUtil;
import cn.dazhou.sip.ISipSession;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import cn.dazhou.railway.dispatch.R;

/**
 * Created by lenovo on 2017/9/5.
 */

public class SipService extends Service {
    //    public INgnConfigurationService gConfigurationService;
    private final static String TAG = "sipService";

    private PowerManager.WakeLock mWakeLock;
    private BroadcastReceiver mBroadcastReceiver;
    //    private SipEngine mSipEngine;
    private SipReceiverManager.Observer observer;
    private Context mContext;

    public SipService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        mContext = this;
        final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null && mWakeLock == null) {
            mWakeLock = powerManager.newWakeLock(PowerManager.ON_AFTER_RELEASE
                    | PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
        }
    }

    /**
     * 判断是否平板设备
     *
     * @param context
     * @return true:平板,false:手机
     */
    private boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onStart(final Intent intent, final int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart()");

        mBroadcastReceiver = new SipReceiverManager();
        observer = new SipReceiverManager.ObserverSupport() {

            @Override
            public void onAudioIncoming(long sessionId) {
//                Log.e(Constants.SESSION_ID, "onAudioIncoming==" + sessionId);
//                queryCallinUser(sessionId, CallInInfo.AUDIO);
            }

            @Override
            public void onTerminated(long sessionId) {
                super.onTerminated(sessionId);
                // 收到断线事件
                // TODO: 2017/10/24 判断断线事件类型，是否为异常中断
                // TODO: 2017/10/24 如果为异常中断，处理中断事件，细分为服务器中断还是本地中断
            }
        };

        SipReceiverManager.register(observer);

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT);
        intentFilter.addAction(NgnInviteEventArgs.ACTION_INVITE_EVENT);
        intentFilter.addAction(NgnMessagingEventArgs.ACTION_MESSAGING_EVENT);
        intentFilter.addAction(NgnMediaPluginEventArgs.ACTION_MEDIA_PLUGIN_EVENT);
        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
        if (mWakeLock != null) {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
                mWakeLock = null;
            }
        }
//        cn.dazhou.railway.dispatch.SipLauncher.gSipService.stop();
//        cn.dazhou.railway.dispatch.SipLauncher.gEngine.stop();
        // 由于再次进入系统，会引起service的重启，从而导致此时的sip状态几乎必然为notRegister，因此这里应该避免检测
        cn.dazhou.railway.dispatch.SipLauncher.getSipService(false).stop();
        super.onDestroy();
    }

    private void queryCallinUser(final long sessionId, final int callType) {
        ISipSession session = SipUtil.querySessionById(sessionId);
        String projectPath = this.getResources().getString(R.string.BASE_PATH);
        String requestPath = this.getResources().getString(R.string.REQUEST_USERQUERY_PATH);
        HttpApi api = HttpApiUtil.getInstance(projectPath, HttpApi.class);
        Call call = api.queryUsersByCondition(requestPath, "", "", session.getRemotePartyDisplayName());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response == null || response.body() == null) {
                    return;
                }
//                String jsonstr = JSON.toJSONString(response.body());
//                Type type = new TypeReference<BaseModel<List<SysUser>>>() {
//                }.getType();
//                BaseModel<List<SysUser>> sysUserModel = JSON.parseObject(jsonstr, type);
//                if (sysUserModel.isSuccess()) {
//                    final List<SysUser> sysUserList = sysUserModel.getObj();
//                    if (sysUserList.size() > 0) {
//                        onCallin(new CallInInfo(sysUserList.get(0), sessionId, callType), callType);
//                    }
//                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("emergency", "fail");
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}