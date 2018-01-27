/*
 * Copyright (c) 2017. Hooyee@dazhou
 *
 * Last modified 17-12-21 下午4:26.
 *
 * author                             bug                             date                            comment
 * Hooyee............................................................................. 2017/12/22..............................init
 */

package cn.dazhou.commonlib.widget.stickrecyclerview;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hooyee on 2017/12/21.
 */

public abstract class BaseSubordinateAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected List<T> mData;

    public BaseSubordinateAdapter(List<T> data) {
        this.mData = data;
    }

    public void addAllData(List<T> data) {
        if (mData == null) {
            mData = new ArrayList();
        }
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
