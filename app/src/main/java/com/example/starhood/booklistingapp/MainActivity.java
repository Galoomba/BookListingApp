package com.example.starhood.booklistingapp;

import android.app.LoaderManager;
import android.content.*;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.starhood.booklistingapp.RecycleView.DividerItemDecoration;
import com.example.starhood.booklistingapp.RecycleView.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookData>> {


    private static final String URL = "www.googleapis.com";
    private String BOOK = "";
    private View loadingIndicator;
    private DataAdapter adapter;
    private EditText mBookName;
    private TextView mEmptyStateTextView;
    private RecyclerView recyclerView;

    private int ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView= (RecyclerView) findViewById(R.id.RecycleView);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        loadingIndicator = findViewById(R.id.loading_indicator);

        recyclerView.setVisibility(View.GONE);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setText(R.string.PBookName);
        loadingIndicator.setVisibility(View.GONE);


        initListView(new ArrayList<BookData>());

        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingIndicator.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setVisibility(View.GONE);

                mBookName = (EditText) findViewById(R.id.bookName);
                BOOK = mBookName.getText().toString().trim().toLowerCase();

                LoaderManager loaderManager = null;

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    LoaderManager loaderManagerr = getLoaderManager();
                    loaderManagerr.initLoader(ID++, null, MainActivity.this);
                    mEmptyStateTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                } else {
                    loadingIndicator.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "NoInternertConection", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    void initListView(ArrayList<BookData> data) {

        adapter = new DataAdapter(data, this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);

        //the divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        //onClick Event
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public Loader<List<BookData>> onCreateLoader(int id, Bundle args) {
        return new com.example.starhood.booklistingapp.Loader(this, URL, BOOK);
    }

    @Override
    public void onLoadFinished(Loader<List<BookData>> loader, List<BookData> data) {


        loadingIndicator.setVisibility(View.GONE);
        adapter.clear();


        if (data != null && !data.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setVisibility(View.GONE);
            adapter.addAll(data);
        }
        else {
            mEmptyStateTextView.setText("No DATA");
            recyclerView.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "No Data to be displayed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BookData>> loader) {

        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id =item.getItemId();
        if(id==R.id.action_setting)
        {
            Intent settingIntent =new Intent(this,Settings.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
