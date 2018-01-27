package cn.dazhou.railway.dispatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.doubango.ngn.events.NgnEventArgs;
import org.doubango.ngn.events.NgnInviteEventArgs;
import org.doubango.ngn.events.NgnRegistrationEventArgs;
import org.doubango.ngn.events.NgnRegistrationEventTypes;
import org.doubango.ngn.media.NgnMediaType;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.commonlib.util.ActivityUtils;
import cn.dazhou.sip.ISipSession;

public class SipReceiverManager extends BroadcastReceiver {

    private static final String TAG = "sipReceiver";

    private static List<Observer> observers;
    private long sessionId;
    private String group;
    private String mGroupName;
    private String mHost;

    public static void register(SipReceiverManager.Observer o) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(o);
        Log.d(TAG, "register:" + o);
    }

    public static void unregister(SipReceiverManager.Observer o) {
        ActivityUtils.checkNotNull(o);
        observers.remove(o);
        Log.d(TAG, "unregister:" + o);
    }

    private void dispatchEvent(Type type) {
        if (observers == null) return;
        switch (type) {
            case HANGUP:
                for (Observer o : observers) {
                    o.onHangup();
                }
                break;
            case INCOMING_AUDIO:
                for (Observer o : observers) {
                    o.onAudioIncoming(sessionId);
                }
                break;
            case CONNECTED:
                for (Observer o : observers) {
                    o.onCallConnected();
                }
                break;
            case TERMINATED:
                for (Observer o : observers) {
                    o.onTerminated(sessionId);
                }
                break;
        }
    }

    private void dispatchEvent(Type type, Object obj) {
        if (observers == null) return;
        switch (type) {
            case HANGUP:
                for (Observer o : observers) {
                    o.onHangup();
                }
                break;
            case INCOMING_AUDIO:
                for (Observer o : observers) {
                    o.onAudioIncoming(sessionId);
                }
                break;
            case CONNECTED:
                for (Observer o : observers) {
                    o.onCallConnected();
                }
                break;
            case TERMINATED:
                for (Observer o : observers) {
                    o.onTerminated((Long) obj);
                }
                break;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT.equals(action)) {  // start
            NgnRegistrationEventArgs args = intent.getParcelableExtra(NgnEventArgs.EXTRA_EMBEDDED);
            final NgnRegistrationEventTypes type;
            if (args == null) {
                return;
            }

            Log.i(TAG, " type = " + args.getEventType());
            switch ((type = args.getEventType())) {
                case REGISTRATION_OK:
                    break;
                case REGISTRATION_NOK:
                    break;
                case REGISTRATION_INPROGRESS:
                    break;
                case UNREGISTRATION_INPROGRESS:
                    break;
                case UNREGISTRATION_OK:
                    break;
                case UNREGISTRATION_NOK:
                    break;
                default:
                    final boolean bTrying = (type == NgnRegistrationEventTypes.REGISTRATION_INPROGRESS || type == NgnRegistrationEventTypes.UNREGISTRATION_INPROGRESS);
                    if (cn.dazhou.railway.dispatch.SipLauncher.getSipService().isRegistered()) {
//                        AppApplication.acquirePowerLock();
                    } else {
//                        AppApplication.releasePowerLock();
                    }
                    break;
            }
        } // end: NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT

        // Invite Events
        else if (NgnInviteEventArgs.ACTION_INVITE_EVENT.equals(action)) {
            NgnInviteEventArgs args = intent.getParcelableExtra(NgnEventArgs.EXTRA_EMBEDDED);
            if (args == null) {
                Log.e(TAG, "Invalid event args");
                return;
            }
            final NgnMediaType mediaType = args.getMediaType();
            dispatchEvent(Type.INVITE_EVENT, args.getSessionId());
            switch (args.getEventType()) {
                case TERMWAIT:break;
                case TERMINATED:
                    Log.e(TAG, "通话断开..." + args.getSessionId());
                    dispatchEvent(Type.HANGUP);
                    dispatchEvent(Type.TERMINATED, args.getSessionId());
                    if (NgnMediaType.isAudioVideoType(mediaType)) {
                        cn.dazhou.railway.dispatch.SipLauncher.getSipService()
                                .stopRingBackTone()
                                .stopRingTone();
                    }
                    break;
                case INCOMING:
                    if (NgnMediaType.isAudioType(mediaType)) {
//                        ISipSession session = SipUtil.querySessionById(args.getSessionId());
                        sessionId = args.getSessionId();
                        dispatchEvent(Type.INCOMING_AUDIO);

                    }
                    break;
                case RINGING:
                    if (NgnMediaType.isAudioVideoType(mediaType)) {
                        cn.dazhou.railway.dispatch.SipLauncher.getSipService().startRingBackTone();
                    }
                    break;
                case CONNECTED:
                    if (cn.dazhou.railway.dispatch.SipLauncher.sBroadcastId == args.getSessionId()) {
                        ISipSession s = cn.dazhou.railway.dispatch.SipLauncher.getSipService().querySessionById(cn.dazhou.railway.dispatch.SipLauncher.sBroadcastId);
                        if (s != null && s.isActive()) {
//                            cn.dazhou.railway.dispatch.SipLauncher.sState = SipPhoneState.PS_BROADCAST;
                            s.setSpeakerphoneOn(true);
                        } else if (s != null) {
                            Log.w(TAG, "Get Connected but the Call is not active");
                            cn.dazhou.railway.dispatch.SipLauncher.getSipService().querySessionById(cn.dazhou.railway.dispatch.SipLauncher.sBroadcastId).setSpeakerphoneOn(false);
                        }
                    }
                    Log.e(TAG, "已接听来电..." +  args.getSessionId());
                    if (NgnMediaType.isAudioVideoType(mediaType)) {
                    cn.dazhou.railway.dispatch.SipLauncher.getSipService()
                            .stopRingBackTone()
                            .stopRingTone();
                    }
                    dispatchEvent(Type.CONNECTED);
                    break;
                case EARLY_MEDIA:
                    break;
                default:
                    break;
            }
        }
    }

    private enum Type {
        INCOMING_AUDIO,
        HANGUP,
        CONNECTED,
        INVITE_EVENT, TERMINATED,
    }

    public interface Observer {
        boolean onHangup();

        boolean onCallConnected();

        void onAudioIncoming(long sessionId);

        void onTerminated(long sessionId);

    }

    public static class ObserverSupport implements Observer{

        @Override
        public boolean onHangup() {
            return false;
        }

        @Override
        public boolean onCallConnected() {
            return false;
        }


        @Override
        public void onAudioIncoming(long sessionId) {

        }

        @Override
        public void onTerminated(long sessionId) {

        }

    }
}
