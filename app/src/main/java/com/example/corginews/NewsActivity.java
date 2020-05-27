package com.example.corginews;


import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderCallbacks<List<NewsItem>> {

private static final String LOG_TAG = NewsActivity.class.getName();

/** URL for corgi-related news story from the guardian */
private static final String GUARDIAN_REQUEST_URL =
        "https://content.guardianapis.com/search?q=corgi&api-key=test&show-tags";

    /**
     * Constant value for the news item loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWSITEM_LOADER_ID = 1;

    /** Adapter for the list of news items */
    private NewsAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;


@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    // Find a reference to the {@link ListView} in the layout
    ListView newsListView = (ListView) findViewById(R.id.list);

    mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
    newsListView.setEmptyView(mEmptyStateTextView);

    // Create a new adapter that takes an empty list of news items as input
    mAdapter = new NewsAdapter(this, new ArrayList<NewsItem>());

    // Set the adapter on the {@link ListView}
    // so the list can be populated in the user interface
    newsListView.setAdapter(mAdapter);

    // Set an item click listener on the ListView, which sends an intent to a web browser
    // to open a website with more information about the selected news item.
    newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            // Find the current news item that was clicked on
            NewsItem currentNewsItem = mAdapter.getItem(position);

            // Convert the String URL into a URI object (to pass into the Intent constructor)
            Uri newsItemUri = Uri.parse(currentNewsItem.getUrl());

            // Create a new intent to view the news item URI
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsItemUri);

            // Send the intent to launch a new activity
            startActivity(websiteIntent);
        }
    });

    // Get a reference to the ConnectivityManager to check state of network connectivity
    ConnectivityManager connMgr = (ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);

    // Get details on the currently active default data network
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

    // If there is a network connection, fetch data
    if (networkInfo != null && networkInfo.isConnected()) {
        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(NEWSITEM_LOADER_ID, null, this);
    } else {
        // Otherwise, display error
        // First, hide loading indicator so error message will be visible
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Update empty state with no connection error message
        mEmptyStateTextView.setText("NO INTERNET CONNECTION - Change to strings");
    }
}
    public Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("limit", "10");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No news items found."
        mEmptyStateTextView.setText("NO NEWS ITEMS FOUND HARDCODED");

        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of NewsItems, then add them to the adapter's
        // data set. This will trigger the ListView to update.

        if (newsItems != null && !newsItems.isEmpty()) {
            mAdapter.addAll(newsItems);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
