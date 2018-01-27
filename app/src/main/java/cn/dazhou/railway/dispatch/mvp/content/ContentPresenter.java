package cn.dazhou.railway.dispatch.mvp.content;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.SearchRecentSuggestions;
import android.view.View;

import cn.dazhou.railway.dispatch.AppApplication;
import cn.dazhou.railway.dispatch.R;
import cn.dazhou.railway.dispatch.SipLauncher;
import cn.dazhou.railway.dispatch.mvp.WorkActivity;
import cn.dazhou.railway.dispatch.mvp.login.RegisterInfo;
import cn.dazhou.railway.dispatch.mvp.login.User;

/**
 * @author Hooyee on 2018/1/27.
 */

public class ContentPresenter extends ContentContract.Presenter {

    public ContentPresenter(Context mContext, ContentContract.View view) {
        super(mContext, view);
        getInfoFromServer();
    }

    public void getInfoFromServer() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(mContext,
                SimpleSearchSuggestionsProvider.AUTHORITY_CONTENT, SimpleSearchSuggestionsProvider.MODE);
        suggestions.saveRecentQuery("q2", null);
        suggestions.saveRecentQuery("asdf", null);
        suggestions.saveRecentQuery("qsdd", null);
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
            Cursor cursor = getRecentSuggestions(newText);
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

    public Cursor getRecentSuggestions(String query) {
        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(SimpleSearchSuggestionsProvider.AUTHORITY_CONTENT);

        uriBuilder.appendPath(SearchManager.SUGGEST_URI_PATH_QUERY);

        String selection = " ?";
        String[] selArgs = new String[] { query };

        Uri uri = uriBuilder.build();

        return mContext.getContentResolver().query(uri, null, selection, selArgs, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register:
                RegisterInfo info = mView.getRegisterInfo();
                AppApplication.getInstance().info = info;
//                SipLauncher.getSipService(false)
//                        .init(mContext)
//                        .username(loginUserInfo.getUsername())
//                        .ipphone(loginUserInfo.getIpphone())
//                        .setNativeService(SipService.class)
//                        .start();
                WorkActivity.startActivity(mContext, WorkActivity.class);
                break;
        }
    }
}
