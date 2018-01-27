package cn.dazhou.commonlib;

import android.view.View;

/**
 * Created by lenovo on 2017/9/4.
 */

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T t);

    View getView();
}
