package com.example.newsfeed.AsyncTasks;

import android.os.AsyncTask;

import com.example.newsfeed.Items.Article;
import com.example.newsfeed.Listeners.ParseJsonListener;

import java.util.ArrayList;

public class JSONParse extends AsyncTask<String, Void, ArrayList<Article>> {
    private ParseJsonListener<ArrayList<Article>> mParseListener;

    public void setOnParsedListener(ParseJsonListener<ArrayList<Article>> listener) {
        mParseListener = listener;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> result) {
        mParseListener.task(result);
    }

    @Override
    protected ArrayList<Article> doInBackground(String... params) {
        return null;
    }
}
