package cn.dazhou.railway.dispatch.mvp.work.station;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.SearchView;
import android.view.View;

import cn.dazhou.commonlib.BaseFragment;
import cn.dazhou.railway.dispatch.R;
import cn.dazhou.railway.dispatch.mvp.content.SearchSuggestionAdapter;

/**
 * Created by Hooyee on 2018/1/27.
 * mail: hooyee_moly@foxmail.com
 */

public class WorkStationFragment extends BaseFragment implements WorkStationContract.View {
    private WorkStationContract.Presenter mPresenter;


    private SearchView mSearchView;
    private SearchSuggestionAdapter mAdapter;

    @Override
    protected void prepare() {
        mPresenter = new WorkStationPresenter(getContext(), this);
    }

    @Override
    protected void initAdapter() {
        mAdapter = new SearchSuggestionAdapter(getContext(), null, 0);
    }

    @Override
    public View initView(View root) {
        setupSearchView(root);
        return root;
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

}
