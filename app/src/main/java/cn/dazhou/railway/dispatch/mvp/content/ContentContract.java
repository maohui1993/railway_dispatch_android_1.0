package cn.dazhou.railway.dispatch.mvp.content;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.SearchView;

import cn.dazhou.commonlib.BasePresenterSupport;
import cn.dazhou.commonlib.BaseView;
import cn.dazhou.commonlib.bean.BaseModel;
import cn.dazhou.railway.dispatch.mvp.login.RegisterInfo;
import cn.dazhou.railway.dispatch.mvp.login.User;

/**
 * @author Hooyee on 2018/1/27.
 */

public interface ContentContract {
    abstract class Presenter extends BasePresenterSupport<BaseModel<User>, User, View> implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener,
            android.view.View.OnClickListener{

        public Presenter(Context mContext, View view) {
            super(mContext, view);
        }

        abstract void getInfoFromServer();
    }

    interface View extends BaseView<Presenter> {
        void swapCursor(Cursor cursor);

        void setSearchContent(int position);

        RegisterInfo getRegisterInfo();
    }
}
