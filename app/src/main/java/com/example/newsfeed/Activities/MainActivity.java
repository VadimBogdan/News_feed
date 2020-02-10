package com.example.newsfeed.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsfeed.AsyncTasks.JSONParse;
import com.example.newsfeed.Items.Article;
import com.example.newsfeed.Listeners.ParseJsonListener;
import com.example.newsfeed.R;
import com.example.newsfeed.Recycleview.NewsRecycleViewAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;

    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mNewsRecyclerView;
    private NewsRecycleViewAdapter mNewsDataAdapter;

    private JSONParse mJSONParse = null;
    private String mApiLink;
    private String mApiLinkBase;
    private String mApiKey;

    @Override
    protected void onStart() {
        super.onStart();
        getNews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mApiLinkBase = getString(R.string.api_link_base);
        mApiKey = getString(R.string.api_key);

        updateApiLink();
        initializeTask();
    }

    private boolean updateApiLink() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String oldValue = mApiLink;
        String country = mSharedPreferences.getString("country", "ua");
        mApiLink = mApiLinkBase + country + mApiKey;

        return !mApiLink.equals(oldValue);
    }

    private void initializeTask() {
        mJSONParse = new JSONParse();
        mJSONParse.setOnParsedListener(new ParseJsonListener<ArrayList<Article>>() {
            @Override
            public void task(ArrayList<Article> res) {
                if (res == null) {
                    return;
                }
                mNewsRecyclerView = findViewById(R.id.card_view_recycler_list);
                // Create the grid layout manager with 2 columns.
                mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                // Set layout manager.
                mNewsRecyclerView.setLayoutManager(mGridLayoutManager);
                mNewsDataAdapter = new NewsRecycleViewAdapter(res, MainActivity.this);
                // Set data adapter.
                mNewsRecyclerView.setAdapter(mNewsDataAdapter);
            }
        });
        mJSONParse.execute(mApiLink);
    }

    private void updateTask() {
        mJSONParse = new JSONParse();
        mJSONParse.setOnParsedListener(new ParseJsonListener<ArrayList<Article>>() {
            @Override
            public void task(ArrayList<Article> res) {
                if (res == null) {
                    return;
                }
                mNewsDataAdapter = new NewsRecycleViewAdapter(res, MainActivity.this);
                mNewsRecyclerView.setAdapter(mNewsDataAdapter);
            }
        });
        mJSONParse.execute(mApiLink);
    }

    private void getNews() {
        if (updateApiLink()) {
            updateTask();
        }
    }
}
