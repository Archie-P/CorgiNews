package com.example.corginews;

/**
 * NewsItem represents a news item.
 */
public class NewsItem {

    /** Headline of the news item */
    private String mHeadline;

    /** Section of the news item */
    private String mSection;

    /** Date of the news item */
    private String mDate;

    /** Author of the news item */
    private String mAuthor;

    /** URL of the news item */
    private String mUrl;

    /**
     * Constructs a new NewsItem.
     *
     * headline is the headline of the news item
     */
    public NewsItem(String newsHeadline, String newsSection, String newsDate, String newsAuthor, String url) {
        mHeadline = newsHeadline;
        mSection = newsSection;
        mDate = newsDate;
        mAuthor = newsAuthor;
        mUrl = url;
    }

    /**
     * Returns the headline.
     */
    public String getHeadline() {
        return mHeadline;
    }

    /**
     * Returns the section.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Returns the date.
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Returns the author.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the URL.
     */
    public String getUrl() {
        return mUrl;
    }

}
