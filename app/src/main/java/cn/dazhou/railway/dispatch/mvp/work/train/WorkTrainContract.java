package cn.dazhou.railway.dispatch.mvp.work.train;

import android.content.Context;

import cn.dazhou.commonlib.BasePresenterSupport;
import cn.dazhou.commonlib.BaseView;
import cn.dazhou.commonlib.bean.BaseModel;
import cn.dazhou.railway.dispatch.mvp.login.User;

/**
 * Created by Hooyee on 2018/1/27.
 * mail: hooyee_moly@foxmail.com
 */

public interface WorkTrainContract {
    abstract class Presenter extends BasePresenterSupport<BaseModel<User>, User, WorkTrainContract.View> implements android.view.View.OnClickListener {

        public Presenter(Context mContext, WorkTrainContract.View view) {
            super(mContext, view);
        }
    }

    interface View extends BaseView<Presenter> {

    }
}
