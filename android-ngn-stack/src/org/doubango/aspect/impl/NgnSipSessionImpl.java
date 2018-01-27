package org.doubango.aspect.impl;

import android.content.Context;
import android.view.View;

import org.doubango.ngn.media.NgnCameraProducer;
import org.doubango.ngn.media.NgnMediaType;
import org.doubango.ngn.sip.NgnAVSession;
import org.doubango.ngn.utils.NgnContentType;
import org.doubango.tinyWRAP.QoS;

import cn.dazhou.sip.ISipSession;
import cn.dazhou.sip.SipMediaType;
import cn.dazhou.sip.VideoParam;

/**
 * @author Hooyee on 2017/12/5.
 */

public class NgnSipSessionImpl implements ISipSession<NgnAVSession> {
    private NgnAVSession mSession;

    @Override
    public void hangUpCall() {
        if (mSession != null)
            mSession.hangUpCall();
    }

    @Override
    public void resumeCall() {
        if (mSession != null)
            mSession.resumeCall();
    }

    @Override
    public void holdCall() {
        if (mSession != null) {
            mSession.holdCall();
        }
    }

    @Override
    public ISipSession setSession(NgnAVSession ngnAVSession) {
        mSession = ngnAVSession;
        return this;
    }

    @Override
    public long getId() {
        if (mSession != null) {
            return mSession.getId();
        } else {
            return -1;
        }
    }

    @Override
    public long getStartTime() {
        if (mSession != null)
            return mSession.getStartTime();
        else return -1;
    }

    @Override
    public boolean isActive() {
        if (mSession != null) {
            return mSession.isActive();
        } else {
            return false;
        }
    }

    @Override
    public void setSpeakerphoneOn(boolean can) {
        if (mSession != null)
            mSession.setSpeakerphoneOn(can);
    }

    @Override
    public ISipSession incRef() {
        if (mSession != null) {
            mSession.incRef();
        }
        return this;
    }

    @Override
    public ISipSession setContext(Context context) {
        if (mSession != null) {
            mSession.setContext(context);
        }
        return this;
    }

    @Override
    public SipMediaType getMediaType() {
        if (mSession == null) return null;
        return mSession.getMediaType() == NgnMediaType.Audio
                ? SipMediaType.Audio : mSession.getMediaType() == NgnMediaType.AudioVideo
                ? SipMediaType.AudioVideo : SipMediaType.Video;
    }

    @Override
    public int compensCamRotation(boolean can) {
        if (mSession != null) {
            return mSession.compensCamRotation(can);
        } else {
            return -1;
        }
    }

    @Override
    public void sendInfo(String info, String header) {
        if (mSession != null) {
            mSession.sendInfo(info, NgnContentType.DOUBANGO_DEVICE_INFO);
        }
    }

    @Override
    public ISipSession.InviteState getState() {
        if (mSession != null) {
            switch (mSession.getState()) {
                case NONE:
                    return InviteState.NONE;
                case INCALL:
                    return InviteState.INCALL;
                case INCOMING:
                    return InviteState.INCOMING;
                case INPROGRESS:
                    return InviteState.INPROGRESS;
                case TERMINATED:
                    return InviteState.TERMINATED;
                case EARLY_MEDIA:
                    return InviteState.EARLY_MEDIA;
                case TERMINATING:
                    return InviteState.TERMINATING;
                case REMOTE_RINGING:
                    return InviteState.REMOTE_RINGING;
                default:
                    return InviteState.NONE;
            }
        } else {
            return InviteState.NONE;
        }
    }

    @Override
    public boolean isSendingVideo() {
        if (mSession != null) {
            return mSession.isSendingVideo();
        } else {
            return false;
        }
    }

    @Override
    public void setSendingVideo(boolean start) {
        if (mSession != null) {
            mSession.setSendingVideo(start);
        }
    }

    @Override
    public boolean sendDTMF(int code) {
        if (mSession != null) {
            return mSession.sendDTMF(code);
        } else {
            return false;
        }
    }

    @Override
    public View startVideoProducerPreview() {
        if (mSession != null) {
            return mSession.startVideoProducerPreview();
        } else {
            return null;
        }
    }

    @Override
    public void setRotation(int rotation) {
        if (mSession != null) {
            mSession.setRotation(rotation);
        }
    }

    @Override
    public void acceptCall() {
        if (mSession != null) {
            mSession.acceptCall();
        }
    }

    @Override
    public void decRef() {
        if (mSession != null) {
            mSession.decRef();
        }
    }

    @Override
    public boolean isSecure() {
        if (mSession != null) {
            return mSession.isSecure();
        } else {
            return false;
        }
    }

    @Override
    public View startVideoConsumerPreview() {
        if (mSession != null) {
            return mSession.startVideoConsumerPreview();
        } else {
            return null;
        }
    }

    @Override
    public String getRemotePartyDisplayName() {
        if (mSession == null) {
            return "";
        }
        return mSession.getRemotePartyDisplayName();
    }

    @Override
    public VideoParam getVideoParam() {
        if (mSession != null){
            QoS qos = mSession.getQoSVideo();
            return new VideoParam.Builder()
                    .bandwidthDownKbps(qos.getBandwidthDownKbps())
                    .videoDecAvgTime(qos.getVideoDecAvgTime())
                    .videoEncAvgTime(qos.getVideoEncAvgTime())
                    .videoInavgFps(qos.getVideoInAvgFps())
                    .videoInHeight(qos.getVideoInHeight())
                    .videoInWidth(qos.getVideoInWidth())
                    .videoOutHeight(qos.getVideoOutHeight())
                    .videoOutWidth(qos.getVideoOutWidth())
                    .build();
        } else {
            return null;
        }
    }

    @Override
    public NgnAVSession getRealSession() {
        return mSession;
    }

    @Override
    public void changeCamera() {
        if (NgnCameraProducer.isFrontFacingCameraEnabled()) {
            NgnCameraProducer.useRearCamera();
        } else {
            NgnCameraProducer.useFrontFacingCamera();
        }
    }

}
