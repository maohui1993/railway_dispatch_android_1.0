package cn.dazhou.sip;

import android.content.Context;
import android.view.View;

/**
 * @author Hooyee on 2017/12/5.
 */

public interface ISipSession<T> {
    void hangUpCall();

    void resumeCall();

    void holdCall();

    ISipSession setSession(T t);

    long getId();

    long getStartTime();

    boolean isActive();

    void setSpeakerphoneOn(boolean can);

    ISipSession incRef();

    ISipSession setContext(Context context);

    SipMediaType getMediaType();

    int compensCamRotation(boolean can);

    void sendInfo(String info, String type);

    ISipSession.InviteState getState();

    boolean isSendingVideo();

    void setSendingVideo(boolean start);

    boolean sendDTMF(int code);

    View startVideoProducerPreview();

    void setRotation(int rotation);

    void acceptCall();

    void decRef();

    boolean isSecure();

    View startVideoConsumerPreview();

    String getRemotePartyDisplayName();

    VideoParam getVideoParam();

    T getRealSession();

    void changeCamera();

    enum InviteState{
        NONE,
        INCOMING,
        INPROGRESS,
        REMOTE_RINGING,
        EARLY_MEDIA,
        INCALL,
        TERMINATING,
        TERMINATED,
    }
}
