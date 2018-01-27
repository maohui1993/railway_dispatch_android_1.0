package cn.dazhou.railway.dispatch.mvp.login;

import android.content.Context;

import cn.dazhou.commonlib.BasePresenterSupport;
import cn.dazhou.commonlib.BaseView;
import cn.dazhou.commonlib.bean.BaseModel;

/**
 * @author Hooyee on 2018/1/26.
 */

public interface LoginContract {
    abstract class Presenter extends BasePresenterSupport<BaseModel<User>, User, View> implements android.view.View.OnClickListener {

        public Presenter(Context mContext, View view) {
            super(mContext, view);
        }
    }

    interface View extends BaseView<Presenter> {

    }
}
