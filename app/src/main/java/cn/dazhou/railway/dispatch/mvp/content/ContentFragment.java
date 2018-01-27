package cn.dazhou.railway.dispatch.mvp.content;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import cn.dazhou.commonlib.BaseFragment;
import cn.dazhou.railway.dispatch.R;

/**
 * @author Hooyee on 2018/1/27.
 */

public class ContentFragment extends BaseFragment implements ContentContract.View, DatePickerDialog.OnDateSetListener{
    private SearchView mSearchView;
    private SearchSuggestionAdapter mAdapter;
    private ContentContract.Presenter mPresenter;

    private TextView mDateTx;
    private Spinner mSpinner;

    private ArrayAdapter<String> mSpinnerAdapter;

    @Override
    protected void initAdapter() {
        mAdapter = new SearchSuggestionAdapter(getContext(), null, 0);
        mPresenter = new ContentPresenter(getContext(), this);

        mSpinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_text_view);
        mSpinnerAdapter.addAll("站点1", "控制台", "车次");
    }

    @Override
    public View initView(View root) {
        setupSearchView(root);
        mDateTx = root.findViewById(R.id.tx_date);
        mSpinner = root.findViewById(R.id.spinner);
        root.findViewById(R.id.bt_date_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        root.findViewById(R.id.bt_register).setOnClickListener(mPresenter);

        mSpinner.setAdapter(mSpinnerAdapter);
        return root;
    }

    private void showDateDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getContext(), ContentFragment.this, year, month, day).show();
    }

    private void setupSearchView(View root) {
        /*------------------ SearchView有三种默认展开搜索框的设置方式，区别如下： ------------------*/
        // 设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        // mSearchView.setIconified(false);
        // 设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        // mSearchView.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
//        mSearchView.onActionViewExpanded();
//        mSearchView.setSubmitButtonEnabled(true);
//
//        mSearchView.setSuggestionsAdapter(mAdapter);
        SearchManager searchManager =
                (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = root.findViewById(R.id.search_view);
        mSearchView.onActionViewExpanded();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setSuggestionsAdapter(mAdapter);

        mSearchView.setOnQueryTextListener(mPresenter);
        mSearchView.setOnSuggestionListener(mPresenter);
    }


    @Override
    public void swapCursor(Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void setSearchContent(int position) {
        mSearchView.setQuery(mAdapter.getSuggestionText(position), true);
    }

    @Override
    public void setPresenter(ContentContract.Presenter presenter) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String mon;
        String day;
        mon = month < 9 ? "0" + (month + 1) : String.valueOf(month + 1);
        day = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
        mDateTx.setText(year + "-" + mon + "-" + day);
    }
}



