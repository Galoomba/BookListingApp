package com.example.starhood.booklistingapp;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Starhood on 6/9/17.
 */

public final class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();

    public static List<BookData> fetchBookData(String requestUrl, String BookName)  {

        /**
         * create url just try and catch case
         * reqURLString ret req url
         */
        URL url = createUrl(reqURlString(requestUrl, BookName));

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<BookData> Books = extractFeatureFromJson(jsonResponse);
        return Books;
    }

    private static String reqURlString(String url, String bookName) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(url)
                .appendPath("books")
                .appendPath("v1")
                .appendPath("volumes")
                .appendQueryParameter("q", bookName)
                .appendQueryParameter("maxResults", "30");

        Log.v(LOG_TAG, builder.build().toString());
        return builder.build().toString();
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }



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


    private static List<BookData> extractFeatureFromJson(String JSONString) {

        if (TextUtils.isEmpty(JSONString)) {
            return null;
        }

        List<BookData> books = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(JSONString);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject currentbook = jsonArray.getJSONObject(i);
                JSONObject bookobject = currentbook.getJSONObject("volumeInfo");


                String bookName = bookobject.getString("title");
                String description = "";
                if (bookobject.optString("description") != null)
                    description = bookobject.optString("description");

                String bookWriter = "";
                if (bookobject.optJSONArray("authors") != null) {
                    JSONArray writer = bookobject.getJSONArray("authors");
                    for (int j = 0; j < writer.length(); j++) {
                        String write = writer.getString(j);
                        bookWriter = write + ", ";
                    }
                }

                BookData Book = new BookData(bookName, bookWriter, description);


                books.add(Book);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        return books;
    }
}
