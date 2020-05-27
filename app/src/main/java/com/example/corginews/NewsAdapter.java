package com.example.corginews;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A NewsAdapter knows how to create a list item layout for each news item
 * in the data source (a list of NewsItem objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsAdapter extends ArrayAdapter<NewsItem> {

    /**
     * Constructs a new NewsAdapter.
     *
     * context = context of the app
     * newsItems is the list of news items, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    /**
     * Returns a list item view that displays information about the news item at the given position
     * in the list of news items.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the news item at the given position in the list of news items
        NewsItem currentNewsItem = getItem(position);

        // Get the headline string from the NewsItem object
        String headline = currentNewsItem.getHeadline();

        // Find the headline TextView
        TextView headlineTextView = (TextView) listItemView.findViewById(R.id.headline);
        // Display the headline of the current news item in that TextView
        headlineTextView.setText(headline);

        // Get the section string from the NewsItem object
        String section = currentNewsItem.getSection();

        // Find the section TextView
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section);
        // Display the section of the current news item in that TextView
        sectionTextView.setText(section);

        // Get the date string from the NewsItem object
        String date = currentNewsItem.getDate();

        // Find the date TextView
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        // Display the date of the current news item in that TextView
        dateTextView.setText(date);

        // Get the author string from the NewsItem object
        String author = currentNewsItem.getAuthor();

        // Find the author TextView
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);
        // Display the author of the current news item in that TextView
        authorTextView.setText(author);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
