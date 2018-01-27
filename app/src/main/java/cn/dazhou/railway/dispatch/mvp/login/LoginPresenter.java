package cn.dazhou.railway.dispatch.mvp.login;

import android.content.Context;
import android.view.View;

/**
 * @author Hooyee on 2018/1/26.
 */

public class LoginPresenter extends LoginContract.Presenter {

    public LoginPresenter(Context mContext, LoginContract.View view) {
        super(mContext, view);
    }

    @Override
    protected void onSuccess(User user, int requestCode) {

    }

    @Override
    protected void onFailed(int code) {

    }

    @Override
    public void onClick(View v) {

    }
}
