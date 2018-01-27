/*
 * Copyright (c) 2017. Hooyee@dazhou
 *
 * Last modified 17-12-21 下午5:02.
 *
 * author                             bug                             date                            comment
 * Hooyee............................................................................. 2017/12/22..............................init
 */

package cn.dazhou.commonlib.widget.stickrecyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.dazhou.commonlib.R;
import cn.dazhou.commonlib.util.ActivityUtils;

/**
 * @author Hooyee on 2017/12/21.
 */

public class StickRecyclerView extends LinearLayout {
    private RecyclerView mMainRecycler;
    private RecyclerView mSubordinateRecycler;
    private TextView mTitleTv;
    private ImageView mMoreTv;

    public StickRecyclerView(Context context) {
        this(context, null);
    }

    public StickRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("WrongConstant")
    public StickRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setOrientation(HORIZONTAL);

        int orientation = getOrientation();

        mMainRecycler = new RecyclerView(getContext());
        mSubordinateRecycler = new RecyclerView(getContext());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StickRecyclerView);
        int type = typedArray.getInteger(R.styleable.StickRecyclerView_child_layout_manager, 0);
        mMainRecycler.setBackgroundColor(typedArray.getColor(R.styleable.StickRecyclerView_parent_background, getDrawingCacheBackgroundColor()));
        mSubordinateRecycler.setBackgroundColor(typedArray.getColor(R.styleable.StickRecyclerView_child_background, getDrawingCacheBackgroundColor()));

        mMainRecycler.setLayoutManager(new LinearLayoutManager(getContext(), getOrientation() ^ VERTICAL, false));
        mMainRecycler.setItemAnimator(new DefaultItemAnimator());

        if (type == 1) {
            mSubordinateRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            int spanCount = 4;
            if(!ActivityUtils.isTabletDevice(getContext())) {
                spanCount = 3;
            }
            mSubordinateRecycler.setLayoutManager(new GridLayoutManager(getContext(), spanCount, orientation, false));
            mSubordinateRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
            mSubordinateRecycler.setHasFixedSize(true);
        }

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(getOrientation() ^ VERTICAL);
        mTitleTv = new TextView(getContext());
        mTitleTv.setText(typedArray.getString(R.styleable.StickRecyclerView_title));
        mTitleTv.setTextSize(typedArray.getDimensionPixelSize(R.styleable.StickRecyclerView_titleSize, 14));
        mTitleTv.setTextColor(typedArray.getColor(R.styleable.StickRecyclerView_titleColor, Color.BLACK));
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(20, 8, 20, 8);
        layout.addView(mTitleTv, params1);
        layout.setBackgroundColor(typedArray.getColor(R.styleable.StickRecyclerView_parent_background, getDrawingCacheBackgroundColor()));

        if (layout.getOrientation() == HORIZONTAL) {
            layout.setGravity(Gravity.CENTER_VERTICAL);
            LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
            params.weight = getWeightSum() <= 0 ? 1 : getWeightSum();
            layout.addView(mMainRecycler, params);
        } else {
            layout.setGravity(Gravity.CENTER_HORIZONTAL);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
            params.topMargin = 10;
            params.weight = getWeightSum() <= 0 ? 1 : getWeightSum();
            layout.addView(mMainRecycler, params);
        }

        mMoreTv = new ImageView(getContext());
        mMoreTv.setImageResource(typedArray.getResourceId(R.styleable.StickRecyclerView_bottomSrc, 0));
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.rightMargin = 10;
        mMoreTv.setBackgroundColor(typedArray.getColor(R.styleable.StickRecyclerView_parent_background, getDrawingCacheBackgroundColor()));
        layout.addView(mMoreTv, params2);

        if (getOrientation() == HORIZONTAL) {
            addView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

            LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
            params.weight = getWeightSum() <= 0 ? 1 : getWeightSum();
            addView(mSubordinateRecycler, params);
        } else {
            addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
            params.weight = getWeightSum() <= 0 ? 1 : getWeightSum();
            addView(mSubordinateRecycler, params);
        }

//        mMoreTv.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mMainRecycler.getAdapter() instanceof  BaseAdapter) {
//                    final BaseAdapter adapter = (BaseAdapter) mMainRecycler.getAdapter();
//                    final GridDialog<StickModel> dialog = new GridDialog<StickModel>(getContext(), adapter.getAllData());
//                    dialog.setItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(int position) {
//                            Intent intentInTalk = new Intent();
//                            intentInTalk.setAction("action.talkback.in");
//                            intentInTalk.putExtra("group", (String)dialog.getItem(position).parent);
//                            getContext().sendBroadcast(intentInTalk);
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
//                }
//            }
//        });
    }

    public void setAdapter(BaseAdapter adapter) {
        mMainRecycler.setAdapter(adapter);
        mSubordinateRecycler.setAdapter(adapter.getSubordinateAdapter());
    }

    public void setOnMoreClickListener(OnClickListener listener) {
        mMoreTv.setOnClickListener(listener);
    }

    public void setMainContentSize(int width, int height) {
        mMainRecycler.getLayoutParams().height = height;
        mMainRecycler.getLayoutParams().width = width;
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }
}
