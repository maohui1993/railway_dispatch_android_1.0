/*
 * Copyright (c) 2017. Hooyee@dazhou
 *
 * Last modified 17-12-21 下午3:30.
 *
 * author                             bug                             date                            comment
 * Hooyee............................................................................. 2017/12/22..............................init
 */

package cn.dazhou.commonlib.widget.stickrecyclerview;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * @author Hooyee on 2017/12/21.
 */

public abstract class BaseViewHolder<M> extends RecyclerView.ViewHolder {

    public BaseViewHolder(ViewGroup parent) {
        super(parent);
    }

    public BaseViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
    }

    public void setData(M t) {

    }
}