package com.example.corginews;

/**
 * NewsItem represents a news item.
 */
public class NewsItem {

    /** Headline of the news item */
    private String mHeadline;

    /**
     * Constructs a new NewsItem.
     *
     * headline is the headline of the news item
     */
    public NewsItem(String newsHeadline) {
        mHeadline = newsHeadline;
    }

    /**
     * Returns the headline.
     */
    public String getHeadline() {
        return mHeadline;
    }
}
