/*
 * Copyright (c) 2017. Hooyee@DaZhou
 * author                             bug                              date                               comment
 * Hooyee - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -2017/09/04 - - - - - - - - - - - - - - -init
 * Hooyee - - - - - - - - - - -拉入组呼会抛出空指针异常- - - - - - - 2017/11/22 - - - - - - - - - - - - - 该问题是由于部分Fragment新建的时候没有展示到Activity,
 *                                                                                                        因此没有执行onCreateView，
 *                                                                                                        然后直接调用了他的adapter，此时adapter还未初始化
 */
package cn.dazhou.commonlib;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lenovo on 2017/9/4.
 */

public abstract class BaseFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private int mLayoutId;
    protected ViewGroup mRootView;
    protected String TAG = getClass().getSimpleName();

    public static BaseFragment newInstance(@NonNull @LayoutRes int layoutId, @NonNull Class clazz) {
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, layoutId);
        BaseFragment fragment = null;
        try {
            Object obj =  clazz.newInstance();
            if (obj instanceof BaseFragment) {
                fragment = (BaseFragment) obj;
                fragment.setArguments(args);
            } else {
                throw new IllegalArgumentException("传入类型错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLayoutId = getArguments().getInt(ARG_PARAM1);
        }
        prepare();
        initAdapter();
    }

    protected void prepare() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = (ViewGroup) inflater.inflate(mLayoutId, container, false);
//            initAdapter();
            initView(mRootView);
        } else {
            // 同一个parent不能添加相同的view，因此要先移除
            ViewGroup parent = (ViewGroup) mRootView.getChildAt(0).getParent();
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    protected void initAdapter() {

    }

    /**
     * 初始化mLayoutId布局文件中的 view
     * @param root
     * @return
     */
    public abstract View initView(View root);

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
