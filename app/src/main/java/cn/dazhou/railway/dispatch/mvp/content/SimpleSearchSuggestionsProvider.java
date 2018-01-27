package cn.dazhou.railway.dispatch.mvp.content;

import android.content.SearchRecentSuggestionsProvider;


public class SimpleSearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
    private static final String LOG_TAG = SimpleSearchSuggestionsProvider.class.getSimpleName();

    public final static String AUTHORITY_CONTENT = "content";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SimpleSearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY_CONTENT, MODE);
    }

}