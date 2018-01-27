package cn.dazhou.railway.dispatch.mvp.work.controllercenter;

import android.content.Context;
import android.view.View;

import cn.dazhou.railway.dispatch.mvp.login.User;

/**
 * Created by Hooyee on 2018/1/27.
 * mail: hooyee_moly@foxmail.com
 */

public class WorkControllerCenterPresenter extends WorkControllerCenterContract.Presenter {
    public WorkControllerCenterPresenter(Context mContext, WorkControllerCenterContract.View view) {
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
