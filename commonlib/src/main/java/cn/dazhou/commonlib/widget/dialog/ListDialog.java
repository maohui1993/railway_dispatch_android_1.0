/*
 * Copyright (c) 2017. Hooyee@dazhou
 *
 * Last modified 17-12-22 下午12:05.
 *
 * author                             bug                             date                            comment
 * Hooyee............................................................................. 2017/12/22..............................init
 */

package cn.dazhou.commonlib.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import cn.dazhou.commonlib.R;
import cn.dazhou.commonlib.util.Util;

/**
 * @author Hooyee on 2017/12/22.
 */

public class ListDialog extends AlertDialog {
    private ListView mListView;
    private EditText mEditView;
    private ArrayAdapter<String> mAdapter;
    private List mData;
    private OnViewClickListener mOnViewClickListener;
    private OnCreateGroupListener mCreateListener;

    private String mPreShowText;

    public ListDialog(@NonNull Context context, List data, OnViewClickListener onItemClickListener) {
        super(context);
        mData = data;
        mOnViewClickListener = onItemClickListener;
    }

    public ListDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setPreEditContent(String content) {
        mPreShowText = content;
        if (mEditView != null) {
            mEditView.setText(content);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = Util.dip2px(getContext(), 250);
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        mListView = findViewById(R.id.listview);
        mEditView = findViewById(R.id.edit);
        mAdapter = new ArrayAdapter<>(getContext(), R.layout.item_text_view, R.id.item_text, mData);
        mListView.setAdapter(mAdapter);
        mListView.requestFocus();

        mEditView.setText(mPreShowText);
        mEditView.setSelection(mPreShowText.length());
        findViewById(R.id.iv_create_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = mEditView.getText().toString();
                mAdapter.insert(groupName, 0);
                if (mCreateListener != null) {
                    mCreateListener.onCreate(groupName);
                }
                ListDialog.this.dismiss();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListDialog.this.dismiss();
                mOnViewClickListener.onClick(position, mAdapter.getItem(position));
            }
        });

        mEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditView.setFocusable(true);
                mEditView.requestFocus();
            }
        });
    }

    public void setOnCreateGroupClick(OnCreateGroupListener listener) {
        mCreateListener = listener;
    }

    public interface OnCreateGroupListener {
        void onCreate(String name);
    }

    public interface OnViewClickListener {
        void onClick(int position, String groupName);
    }
}
