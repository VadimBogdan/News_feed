package com.example.newsfeed.AsyncTasks;

import android.os.AsyncTask;
import android.text.Html;

import com.example.newsfeed.Items.Article;
import com.example.newsfeed.Listeners.ParseJsonListener;
import com.example.newsfeed.Network.JSONFromUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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
        try {
            // instantiate our json parser
            JSONFromUrl jsonFromUrl = new JSONFromUrl();
            ArrayList<Article> articles = new ArrayList<>();

            // get json string from url then json object
            JSONObject json = new JSONObject(jsonFromUrl.getJSONFromUrl(new URL(params[0])));
            JSONArray articlesJSON = json.getJSONArray("articles");
            for (int i = 0; i < articlesJSON.length() && i < 20; i++) {
                JSONObject article = (JSONObject) articlesJSON.get(i);
                HashMap<String, String> articleData = new HashMap<>();
                String name = article.getJSONObject(Article.TAG_SOURCE).getString(Article.TAG_NAME);
                String author = article.getString(Article.TAG_AUTHOR).trim();
                String title = article.getString(Article.TAG_TITLE);
                String description = article.getString(Article.TAG_DESCRIPTION);
                String url = article.getString(Article.TAG_URL);
                String urlToImage = article.getString(Article.TAG_URL_TO_IMAGE);


                description = Html.fromHtml(description).toString();

                if (author.length() > 25 || author.equals("null")) author = "";
                if (description.trim().equals("null")) description = "";

                articleData.put(Article.TAG_NAME, name);
                articleData.put(Article.TAG_AUTHOR, author);
                articleData.put(Article.TAG_TITLE, title);
                articleData.put(Article.TAG_DESCRIPTION, description);
                articleData.put(Article.TAG_URL, url);
                articleData.put(Article.TAG_URL_TO_IMAGE, urlToImage);

                articles.add(new Article(articleData));
            }

            return articles;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
