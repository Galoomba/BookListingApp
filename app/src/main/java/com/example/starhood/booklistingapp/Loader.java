package com.example.starhood.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Starhood on 6/9/17.
 */


public class Loader extends AsyncTaskLoader<List<BookData>> {


    private static final String LOG_TAG = Loader.class.getName();


    private String mUrl;
    private String mBookName;


    public Loader(Context context, String url, String bookName) {
        super(context);
        mUrl = url;
        mBookName = bookName;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<BookData> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<BookData> Books = Utils.fetchBookData(mUrl, mBookName);
        return Books;
    }
}
