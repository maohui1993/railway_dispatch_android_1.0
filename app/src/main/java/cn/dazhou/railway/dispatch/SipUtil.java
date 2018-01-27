package cn.dazhou.railway.dispatch;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.doubango.aspect.impl.SipConfigHelper;

import cn.dazhou.railway.dispatch.mvp.login.User;
import cn.dazhou.sip.ISipSession;
import cn.dazhou.sip.SipMediaType;


/**
 * Created by Hooyee on 2017/9/14.
 */

public class SipUtil {


    /**
     * 呼叫会议，用于对讲
     *
     * @param phoneNumber 分组号
     * @return
     */
    public static ISipSession makeVoiceCall(String phoneNumber) {
        try {
            return SipLauncher.getSipService().makeVoiceCall(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ISipSession makeCall(String remoteUri, SipMediaType mediaType) {
        try {
            return SipLauncher.getSipService().makeCall(remoteUri, mediaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void audioCallSpecifiedUser(Context context, User user) {
//        SipAudioCallActivity.startItself(context, user);
    }

    public static void sessionHangupCall(final ISipSession session, boolean needDecRef) {
        if (session != null) {
            session.setContext(null);
            if (needDecRef) {
                session.decRef();
            }
            session.hangUpCall();
        }
    }

    public static void sessionHangupCall(final ISipSession session) {
        sessionHangupCall(session, null);
    }

    public static void sessionHangupCall(final ISipSession session, Handler handler) {
        sessionHangupCall(session, handler, 0);
    }

    public static void sessionHangupCall(ISipSession session, Handler handler, long delay) {
        sessionHangupCall(session, handler, -1, delay);
    }

    public static void sessionHangupCall(final ISipSession session, Handler handler, int sendDTMF, long delay) {
        if (!checkSipServiceSate()) return;

        if (sendDTMF >= 0) {
            session.sendDTMF(sendDTMF);
        }

        if (delay == 0 && session != null) {
            session.setContext(null);
            session.decRef();
            session.hangUpCall();
            return;
        }

        if (handler != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    session.setContext(null);
                    session.hangUpCall();
                }
            }, delay);
        }
    }


    /**
     * @return true为service操作已完成, false为操作中不能做其他操作
     */
    public static boolean checkSipServiceSate() {
//        if(sipService.getRegistrationState() == NgnSipSession.ConnectionState.CONNECTING || sipService.getRegistrationState() == NgnSipSession.ConnectionState.TERMINATING){
//            sipService.stopStack();
//            // todo tip
//            Util.toast(SipLauncher, "sevicestate : 错误");
//            return false;
//        }
        return true;
    }

    /***
     * 向服务器发送操作
     * @param content
     */
    public static boolean sendSipMessageToServer(String content) {
//        return false;
        return sendSipMessage(SipConfigHelper.SIPSERVER_IMPU, content);
    }


    /**
     * 发送消息
     *
     * @param user    接收者
     * @param content 内容
     */
    public static boolean sendSipMessage(String user, String content) {
        try {
            Log.i("SipUtil", content);
            return SipLauncher.getSipService().sendSipMessage(user, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void stop() {
        if (SipLauncher.getSipService() != null) {
            SipLauncher.getSipService().stop();
        }
    }

    public static ISipSession querySessionById(long sessionId) {
        return SipLauncher.getSipService().querySessionById(sessionId);
    }
}
