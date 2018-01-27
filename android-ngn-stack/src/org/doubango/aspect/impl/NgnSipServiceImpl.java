package org.doubango.aspect.impl;

import android.app.Service;
import android.content.Context;
import android.util.Log;

import org.doubango.ngn.NgnEngine;
import org.doubango.ngn.media.NgnMediaType;
import org.doubango.ngn.services.INgnConfigurationService;
import org.doubango.ngn.services.INgnSipService;
import org.doubango.ngn.sip.NgnAVSession;
import org.doubango.ngn.sip.NgnMessagingSession;
import org.doubango.ngn.sip.NgnSipSession;
import org.doubango.ngn.sip.NgnSipStack;
import org.doubango.ngn.utils.NgnConfigurationEntry;
import org.doubango.ngn.utils.NgnUriUtils;
import org.doubango.utils.SharedPreferenceUtil;

import cn.dazhou.sip.ISipService;
import cn.dazhou.sip.ISipSession;
import cn.dazhou.sip.SipMediaType;

/**
 * @author Hooyee on 2017/12/5.
 */

public class NgnSipServiceImpl implements ISipService<NgnAVSession> {
    private Context mContext;
    private INgnConfigurationService mConfigurationService;
    private INgnSipService mSipService;
    private NgnEngine mEngine;
    private String mUsername;
    private String mIpphone;

    private boolean mInterrupt;

    @Override
    public ISipService setNativeService(Class<? extends Service> clazz) {
        if (mEngine != null && clazz != null) {
            mEngine.setNativeServiceClass(clazz);
        }
        return this;
    }

    @Override
    public ISipService username(String username) {
        mUsername = username;
        return this;
    }

    @Override
    public ISipService init(Context context) {
        mContext = context;
        mEngine = NgnEngine.getInstance();
        mConfigurationService = mEngine.getInstance().getConfigurationService();
        mSipService = mEngine.getSipService();
        return this;
    }

    @Override
    public boolean start() {
        if(!mEngine.isStarted()){
            if(mEngine.start()){
                Log.i("TAG",SipConfigHelper.IDENTITY_IMPU+"启动Sip电话引擎。");
            }
            else{
                Log.i("TAG",SipConfigHelper.IDENTITY_IMPU+"启动Sip电话引擎失败 :(");
            }
        } else {
            mEngine.stop();
            if(mEngine.start()){
                Log.i("TAG",SipConfigHelper.IDENTITY_IMPU+"启动Sip电话引擎 :");
            }
            else{
                Log.i("TAG",SipConfigHelper.IDENTITY_IMPU+"启动Sip电话引擎失败 :(");
            }
        }

        if (mEngine.isStarted()) {
            if (!mSipService.isRegistered()) {
                Log.i("TAG",SipConfigHelper.IDENTITY_IMPU+"重新注册");
                SipConfigHelper.initConfig(mConfigurationService, mContext, mUsername, mIpphone);
                if(mSipService.getRegistrationState() == NgnSipSession.ConnectionState.CONNECTING || mSipService.getRegistrationState() == NgnSipSession.ConnectionState.TERMINATING){
                    mSipService.stopStack();
                }
                else if(mSipService.isRegistered()){
                    mSipService.unRegister();
                }
                else{
                    //启动单独线程执行注册服务
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                //先判断是否能连接内网
                                if(!SipConfigHelper.isConnTcp(SipConfigHelper.NETWORK_PCSCF_HOST_L,SipConfigHelper.NETWORK_PCSCF_PORT_L))
                                {
                                    Log.i("TAG",SipConfigHelper.IDENTITY_IMPU+"切换到外网。");
                                    SipConfigHelper.NETWORK_PCSCF_HOST=SipConfigHelper.NETWORK_PCSCF_HOST_N;
                                    SipConfigHelper.NETWORK_PCSCF_PORT=SipConfigHelper.NETWORK_PCSCF_PORT_N;
                                }else
                                {
                                    Log.i("TAG",SipConfigHelper.IDENTITY_IMPU+"切换到内网。");
                                    SipConfigHelper.NETWORK_PCSCF_HOST=SipConfigHelper.NETWORK_PCSCF_HOST_L;
                                    SipConfigHelper.NETWORK_PCSCF_PORT=SipConfigHelper.NETWORK_PCSCF_PORT_L;
                                }
                                SipConfigHelper.updateConfig(mConfigurationService ,NgnConfigurationEntry.NETWORK_PCSCF_HOST, SipConfigHelper.NETWORK_PCSCF_HOST);
                                SipConfigHelper.updateConfig(mConfigurationService, NgnConfigurationEntry.NETWORK_PCSCF_PORT, SipConfigHelper.NETWORK_PCSCF_PORT);

                                Log.i("TAG",SipConfigHelper.IDENTITY_IMPU+":"+SipConfigHelper.NETWORK_PCSCF_HOST+"启动单独线程执行注册服务！");
                                while (mEngine.getInstance().getNetworkService().getLocalIP(false)==null) {
                                    //判断网络是否已经连接成功，未成功则循环等待。
                                }
                                Thread.sleep(1000);//休眠10秒,保证回掉方法状态已改变
                                if(mSipService.getSipStack()!=null) {
                                    //判断软交换回掉方法是否注册，已注册需等待10秒等状态变化再执行注册
                                    if (mSipService.getSipStack().getState()== NgnSipStack.STACK_STATE.STARTING
                                            || mSipService.getSipStack().getState()== NgnSipStack.STACK_STATE.STOPPING)
                                    {
                                        //保证回掉方法状态已改变，否则继续循环等待
                                    }
                                }
                                boolean isResister = mSipService.register(mContext.getApplicationContext());
//                                if(isResister) {
//                                    Thread.sleep(10000);//休眠10秒,保证网络注册状态已变化
//                                    Toast.makeText(mContext, "登陆成功 :)", Toast.LENGTH_SHORT);
//
//                                }
                            }catch (Exception e)
                            {}
                        }
                    }.start();
                }
            }
        }
        return false;
    }

    @Override
    public ISipService stop() {
        mSipService.stop();
        mEngine.stop();
        return this;
    }

    @Override
    public ISipSession makeCall(String remoteUri, SipMediaType mediaType) throws Exception {
        if (mInterrupt) {
            Log.e("TAG","Sip 掉线了···");
        }
        final INgnConfigurationService configurationService = mEngine
                .getConfigurationService();
        final String validUri = NgnUriUtils.makeValidSipUri(remoteUri);
        if (validUri == null) {
            Log.e("uri", "转换uri失败 '" + remoteUri + "'");
            return null;
        } else {
            remoteUri = validUri;
            if (remoteUri.startsWith("tel:")) {
                // E.164 number => use ENUM protocol
                final NgnSipStack sipStack = mSipService.getSipStack();
                if (sipStack != null) {
                    String phoneNumber = NgnUriUtils
                            .getValidPhoneNumber(remoteUri);
                    if (phoneNumber != null) {
                        String enumDomain = configurationService
                                .getString(
                                        NgnConfigurationEntry.GENERAL_ENUM_DOMAIN,
                                        NgnConfigurationEntry.DEFAULT_GENERAL_ENUM_DOMAIN);
                        String sipUri = sipStack.dnsENUM("E2U+SIP",
                                phoneNumber, enumDomain);
                        if (sipUri != null) {
                            remoteUri = sipUri;
                        }
                    }
                }
            }
        }

        final NgnAVSession avSession = NgnAVSession.createOutgoingSession(
                mSipService.getSipStack(),
                mediaType == SipMediaType.Audio ? NgnMediaType.Audio : mediaType == SipMediaType.AudioVideo ? NgnMediaType.AudioVideo : NgnMediaType.Video);
        avSession.setRemotePartyUri(remoteUri); // HACK

        // Hold the active call
        final NgnAVSession activeCall = NgnAVSession
                .getFirstActiveCallAndNot(avSession.getId());
        if (activeCall != null) {
            activeCall.holdCall();
        }

        avSession.makeCall(remoteUri);
        return new NgnSipSessionImpl().setSession(avSession);
    }

    @Override
    public ISipSession makeVoiceCall(String phoneNumber) throws Exception {
        if (mInterrupt) {
            Log.e("TAG","Sip 掉线了···");
        }
        final String validUri = NgnUriUtils.makeValidSipUri(String.format("sip:%s@%s", phoneNumber,
                SharedPreferenceUtil.getString(mContext, NgnConfigurationEntry.NETWORK_PCSCF_HOST, SipConfigHelper.NETWORK_PCSCF_HOST)));

        if (validUri == null) return null;

        NgnAVSession avSession = NgnAVSession.createOutgoingSession(mSipService.getSipStack(), NgnMediaType.AudioVideo);
        if (avSession.makeCall(validUri)) {
            return new NgnSipSessionImpl().setSession(avSession);
        } else {
            return null;
        }
    }

    @Override
    public long makeCameraCall(String phoneNumber, SipMediaType mediaType) throws Exception {
        if (mInterrupt) {
            Log.e("TAG","Sip 掉线了···");
        }
        final String validUri = NgnUriUtils.makeValidSipUri(String.format("sip:pttservice%s@%s", phoneNumber,
                SharedPreferenceUtil.getString(mContext, NgnConfigurationEntry.NETWORK_PCSCF_HOST, SipConfigHelper.NETWORK_PCSCF_HOST)));
        if (validUri == null) {
            return -1;
        }
        NgnAVSession avSession = NgnAVSession.createOutgoingSession(mSipService.getSipStack(),
                mediaType == SipMediaType.Audio ? NgnMediaType.Audio : mediaType == SipMediaType.AudioVideo ? NgnMediaType.AudioVideo : NgnMediaType.Video);
        if (avSession.makeCall(validUri)) {
            return avSession.getId();
        } else {
            return -1;
        }
    }

    @Override
    public void inviteTalkback(String userCode, String group) throws Exception {
        if (mInterrupt) {
            Log.e("TAG","Sip 掉线了···");
        }
        String validUri = NgnUriUtils.makeValidSipUri(String.format("sip:%s@%s", userCode,
                SharedPreferenceUtil.getString(mContext, NgnConfigurationEntry.NETWORK_PCSCF_HOST, SipConfigHelper.NETWORK_PCSCF_HOST)));

        NgnMessagingSession imSession = NgnMessagingSession.createOutgoingSession(mSipService.getSipStack(), validUri);
//        imSession.sendTextMessage("{\"pttservice\":[{\"status\": 3, \"group\": \"" + group + "\", \"id\": \"" + userCode + "\"}]}");
        imSession.sendTextMessage("{\"bgroup\":\"" + group + "\",\"host\":\""+SipConfigHelper.IDENTITY_IMPU+"\",\"add\":{\""+userCode+"\":\"\"}}");
    }

    @Override
    public ConnectionState getRegistrationState() {
        switch (mSipService.getRegistrationState()) {
            case NONE:
                return ConnectionState.NONE;
            case CONNECTING:
                return ConnectionState.CONNECTING;
            case CONNECTED:
                return ConnectionState.CONNECTED;
            case TERMINATED:
                return ConnectionState.TERMINATED;
            case TERMINATING:
                return ConnectionState.TERMINATING;
            default:
                return ConnectionState.NONE;
        }
    }

    @Override
    public boolean isRegistered() {
        return mSipService.isRegistered();
    }

    @Override
    public ISipService stopRingBackTone() {
        mEngine.getSoundService().stopRingBackTone();
        return this;
    }

    @Override
    public ISipService stopRingTone() {
        mEngine.getSoundService().stopRingTone();
        return this;
    }

    @Override
    public void startRingTone() {
        mEngine.getSoundService().startRingTone();
    }

    @Override
    public void startRingBackTone() {
        mEngine.getSoundService().startRingBackTone();
    }

    @Override
    public boolean sendSipMessage(String user, String content) throws Exception {
        if (mInterrupt) {
            Log.e("TAG","Sip 掉线了···");
        }
        String validUri = NgnUriUtils.makeValidSipUri(String.format("sip:%s@%s", user,
                SharedPreferenceUtil.getString(mContext, NgnConfigurationEntry.NETWORK_PCSCF_HOST, SipConfigHelper.NETWORK_PCSCF_HOST)));
        if (mSipService != null) {
            NgnMessagingSession imSession = NgnMessagingSession.createOutgoingSession(mSipService.getSipStack(), validUri);
            return imSession.sendTextMessage(content);
        } else {
            return false;
        }
    }

    @Override
    public String getParam(String key, String defaultValue) {
        return mConfigurationService.getString(key, defaultValue);
    }

    @Override
    public boolean getParam(String key, boolean defaultValue) {
        return mConfigurationService.getBoolean(key, defaultValue);
    }

    @Override
    public void releaseSession(ISipSession<NgnAVSession> session) {
        NgnAVSession.releaseSession(session.getRealSession());
    }

    @Override
    public ISipSession querySessionById(long id) {
        if (NgnAVSession.getSession(id) == null) return null;
        return new NgnSipSessionImpl().setSession(NgnAVSession.getSession(id));
    }

    @Override
    public void needInterrupt(boolean interrupt) {
        mInterrupt = interrupt;
    }

    @Override
    public ISipService ipphone(String ipphone) {
        mIpphone = ipphone;
        return this;
    }
}
