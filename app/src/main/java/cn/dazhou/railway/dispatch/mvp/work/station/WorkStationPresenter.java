package cn.dazhou.railway.dispatch.mvp.work.station;

import android.content.Context;
import android.view.View;

import cn.dazhou.railway.dispatch.mvp.login.User;

/**
 * Created by Hooyee on 2018/1/27.
 * mail: hooyee_moly@foxmail.com
 */

public class WorkStationPresenter extends WorkStationContract.Presenter {
    public WorkStationPresenter(Context mContext, WorkStationContract.View view) {
        super(mContext, view);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onSuccess(User user, int requestCode) {

    }

    @Override
    protected void onFailed(int code) {

    }
}
