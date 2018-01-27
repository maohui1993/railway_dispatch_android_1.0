package cn.dazhou.railway.dispatch.mvp.work.controllercenter;

import android.content.Context;

import cn.dazhou.commonlib.BasePresenterSupport;
import cn.dazhou.commonlib.BaseView;
import cn.dazhou.commonlib.bean.BaseModel;
import cn.dazhou.railway.dispatch.mvp.login.User;

/**
 * Created by Hooyee on 2018/1/27.
 * mail: hooyee_moly@foxmail.com
 */

public interface WorkControllerCenterContract {
    abstract class Presenter extends BasePresenterSupport<BaseModel<User>, User, WorkControllerCenterContract.View> implements android.view.View.OnClickListener {

        public Presenter(Context mContext, WorkControllerCenterContract.View view) {
            super(mContext, view);
        }
    }

    interface View extends BaseView<WorkControllerCenterContract.Presenter> {

    }
}
