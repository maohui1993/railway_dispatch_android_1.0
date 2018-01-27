/*
 * Copyright (c) 2017. Hooyee@dazhou
 *
 * Last modified 17-12-22 上午11:15.
 *
 * author                             bug                             date                            comment
 * Hooyee.......................................................... 2017/12/22........................init
 * Hooyee.......................................................... 2018/01/13.......................修正再次点击组选中状态消失的问题
 */

package cn.dazhou.commonlib.widget.stickrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Hooyee on 2017/12/21.
 */

public abstract class BaseAdapter<T extends StickModel> extends RecyclerView.Adapter<BaseViewHolder> {
    protected List<T> data;
    private OnItemClickListener itemClickListener;
    protected BaseSubordinateAdapter subordinateAdapter;

    private View lastSelectedView;
    private T lastSelectedItem;

    private int defaultSelectedPosition = -1;

    public BaseAdapter(List<T> data) {
        this.data = data;
        initSubAdapter();
    }

    protected abstract void initSubAdapter();

    public T getItem(int position) {
        return data == null ? null : data.get(position);
    }

    public T getItem(T t) {
        if (data.contains(t)) {
            int index = data.indexOf(t);
            return data.get(index);
        }
        return null;
    }

    public T getLastSelectedItem() {
        return lastSelectedItem;
    }

    public List<T> getAllData() {
        return data;
    }

    /**
     * 只能使用一次，用于初始化设置被选择的position
     * @param position
     */
    public void setDefaultSelectedPosition(int position) {
        if (defaultSelectedPosition == -101) return;
        defaultSelectedPosition = position;
        subordinateAdapter.addAllData(data.get(position).children);
        notifyItemChanged(position);
    }

    public void setDefaultSelectedItem(T t) {
        setDefaultSelectedPosition(data.indexOf(t));
    }

    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.itemClickListener = mItemClickListener;
    }


    public BaseSubordinateAdapter getSubordinateAdapter() {
        return subordinateAdapter;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseViewHolder holder = OnCreateViewHolder(parent, viewType);

        //itemView 的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 更新上次点击选中的item的UI
                if (lastSelectedView != null && lastSelectedView != v) setSelect(lastSelectedView, false);

                if (lastSelectedView == v) return;
//                notifyItemChanged(getPosition(lastSelectedItem));

                // 更新当前点击选中的Item的UI
                if (!v.isSelected()) {
                    subordinateAdapter.addAllData(data.get(holder.getAdapterPosition()).children);
                    setSelect(v, true);
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(holder.getAdapterPosition());
                    }
                }

                // 调用onItemClick后涉及到异步，因此在更新UI之前 lastSelectedView被改变了，导致UI达不到预期效果，因此需要执行以下操作
                v.post(new Runnable() {
                    @Override
                    public void run() {
//                        notifyItemChanged(holder.getAdapterPosition());
                    }
                });

                lastSelectedView = v;
                lastSelectedItem = data.get(holder.getAdapterPosition());
        }
    });
        return holder;
}

    private void setSelect(View view, boolean select) {
        view.setSelected(select);
        ViewGroup parent;
        if (view instanceof ViewGroup) {
            parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                parent.getChildAt(i).setSelected(select);
            }
        }
    }

    protected abstract BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        // 针对默认被选中的Item的处理
        if (defaultSelectedPosition >= 0 && defaultSelectedPosition == position) {
            if (lastSelectedView != holder.itemView) {
                if (lastSelectedView != null) setSelect(lastSelectedView, false);
                setSelect(holder.itemView, true);
                lastSelectedView = holder.itemView;
                lastSelectedItem = data.get(position);
            }
//            defaultSelectedPosition = -101;
            defaultSelectedPosition = -1;
        }
        holder.setData(data.get(position).parent);
    }

    /**
     * add和update都用这个
     *
     * @param data
     */
    public void updateData(T data) {
        if (data == null) return;
        int index = this.data.indexOf(data);
        if (index >= 0) {
            for (Object o : data.children) {
                if (!this.data.get(index).children.contains(o)) {
                    this.data.get(index).children.add(o);
                }
            }
//            notifyItemChanged(index);
        } else {
            this.data.add(0, data);
            notifyItemInserted(0);
            if (this.data.size() > 1) {
                notifyItemChanged(1);
            }
        }
    }


    public void removeSelectedItem() {
        if(lastSelectedItem == null) return;
        int index = data.indexOf(lastSelectedItem);
        data.remove(lastSelectedItem);
        notifyItemRemoved(index);
        subordinateAdapter.mData.removeAll(lastSelectedItem.children);
        if (data.size() > 0) {
            lastSelectedItem = data.get(0);
        }
        subordinateAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addHeader(View view) {

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
