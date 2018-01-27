package cn.dazhou.railway.dispatch.mvp.work.station;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.SearchRecentSuggestions;
import android.view.View;

import cn.dazhou.railway.dispatch.mvp.content.SimpleSearchSuggestionsProvider;
import cn.dazhou.railway.dispatch.mvp.login.User;

/**
 * Created by Hooyee on 2018/1/27.
 * mail: hooyee_moly@foxmail.com
 */

public class WorkStationPresenter extends WorkStationContract.Presenter {
    public WorkStationPresenter(Context mContext, WorkStationContract.View view) {
        super(mContext, view);
    }

    public void getInfoFromServer() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(mContext,
                SimpleSearchSuggestionsProvider.AUTHORITY_CONTENT, SimpleSearchSuggestionsProvider.MODE);
        suggestions.saveRecentQuery("www", null);
        suggestions.saveRecentQuery("sss", null);
        suggestions.saveRecentQuery("wwssww", null);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onSuccess(User user, int requestCode) {

    }

    @Override
    protected void onFailed(int code) {

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText != null && !"".equals(newText)) {
            Cursor cursor = getSuggestions(newText);
            mView.swapCursor(cursor);
        } else {
            mView.swapCursor(null);
        }
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return true;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        mView.setSearchContent(position);
        return true;
    }

    public Cursor getSuggestions(String query) {
        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(SimpleSearchSuggestionsProvider.AUTHORITY_CONTENT);

        uriBuilder.appendPath(SearchManager.SUGGEST_URI_PATH_QUERY);

        String selection = " ?";
        String[] selArgs = new String[] { query };

        Uri uri = uriBuilder.build();

        return mContext.getContentResolver().query(uri, null, selection, selArgs, null);
    }

}
