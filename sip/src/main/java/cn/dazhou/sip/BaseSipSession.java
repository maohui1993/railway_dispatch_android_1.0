package cn.dazhou.sip;

/**
 * @author Hooyee on 2017/12/5.
 */

public abstract class BaseSipSession<T> implements ISipSession<T> {
    protected static OnQuerySessionListener mOnQuerySessionListener;

    public static ISipSession querySessionById(OnQuerySessionListener listener, long sessionId){
        if (mOnQuerySessionListener == null) {
            return null;
        }
        return mOnQuerySessionListener.querySession(sessionId);
    }

    protected interface OnQuerySessionListener {
        ISipSession querySession(long id);
    }
}
