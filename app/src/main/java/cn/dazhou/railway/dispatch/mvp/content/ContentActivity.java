package cn.dazhou.railway.dispatch.mvp.content;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;

import cn.dazhou.commonlib.BaseActivity;
import cn.dazhou.commonlib.BaseFragment;
import cn.dazhou.commonlib.util.ActivityUtils;
import cn.dazhou.railway.dispatch.MainActivity;
import cn.dazhou.railway.dispatch.R;

public class ContentActivity extends BaseActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mFragment == null) {
            mFragment = ContentFragment.newInstance(R.layout.fragment_content, ContentFragment.class);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            Log.v(LOG_TAG, "handleIntent(): query = " + query);
//
//            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
//                    SimpleSearchSuggestionsProvider.AUTHORITY, SimpleSearchSuggestionsProvider.MODE);
//            suggestions.saveRecentQuery(query, null);
//        }
//    }
}
