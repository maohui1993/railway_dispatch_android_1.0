package cn.dazhou.sip;

import android.app.Service;
import android.content.Context;

/**
 * @author Hooyee on 2017/12/5.
 */

public interface ISipService<T> {

    ISipService setNativeService(Class<? extends Service> clazz);

    ISipService init(Context context);

    ISipService username(String username);

    boolean start();

    ISipService stop();

    ISipSession makeCall(String remoteUri, SipMediaType mediaType) throws Exception;

    ISipSession makeVoiceCall(String phoneNumber) throws Exception;

    long makeCameraCall(String phoneNumber, SipMediaType mediaType) throws Exception;

    void inviteTalkback(String userCode, String group) throws Exception;

    ConnectionState getRegistrationState();

    boolean isRegistered();

    ISipService stopRingBackTone();

    ISipService stopRingTone();

    void startRingTone();

    void startRingBackTone();

    boolean sendSipMessage(String user, String content) throws Exception;

    String getParam(String key, String defaultValue);

    boolean getParam(String key, boolean defaultValue);

    void releaseSession(ISipSession<T> session);

    ISipSession querySessionById(long id);

    void needInterrupt(boolean interrupt);

    ISipService ipphone(String ipphone);

    enum ConnectionState{
        NONE,
        CONNECTING,
        CONNECTED,
        TERMINATING,
        TERMINATED,
    }


}
