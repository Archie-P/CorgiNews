package com.example.corginews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from The Guardian.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian dataset and return a list of NewsItem objects.
     */
    public static List<NewsItem> fetchNewsItemData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of NewsItems
        List<NewsItem> newsItems = extractFeatureFromJson(jsonResponse);

        // Return the list of NewsItems
        return newsItems;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of NewsItem objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<NewsItem> extractFeatureFromJson(String newsItemJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsItemJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news items to
        List<NewsItem> newsItems = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsItemJSON);

            // Extract the JSONObject with the key "response"
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");
            // Extract the JSONArray associated with the key called "results",
            // which represents a list of results of the request.
            JSONArray newsItemArray = responseObject.getJSONArray("results");


            // For each result in the newsItemArray, create a NewsItem object
            for (int i = 0; i < newsItemArray.length(); i++) {

                // Get a single news item at position i within the list of news items
                JSONObject currentNewsItem = newsItemArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String headline = currentNewsItem.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String section = currentNewsItem.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                String date = currentNewsItem.getString("webPublicationDate");

                // Extract the value for the key called "webUrl"
                String url = currentNewsItem.getString("webUrl");

                // Extract the JSONArray associated with the key called "tags",
                // which represents a list of tags in the newsItemArray.
                // Used question/answer posted by Anju in the knowledge base:
                // https://knowledge.udacity.com/questions/196705, to get the code right.
                String author ="";
                if (currentNewsItem.has("tags")) {
                    JSONArray authorArray = currentNewsItem.getJSONArray("tags");
                if (authorArray != null && authorArray.length() >= 0) {
                    for (int j = 0; j < authorArray.length(); j++) {
                        JSONObject object = authorArray.getJSONObject(j);
                        author = object.getString("webTitle");
                    }
                    }
                }
                else author = "Author NA";

                // Create a new NewsItem object with headline,
                // from the JSON response.
                NewsItem newsItem = new NewsItem(headline, section, date, author, url);

                // Add the new NewsItem to the list of news items.
                newsItems.add(newsItem);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news items
        return newsItems;
    }

}
