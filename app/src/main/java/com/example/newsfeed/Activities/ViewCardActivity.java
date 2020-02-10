package com.example.newsfeed.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.newsfeed.Items.Article;
import com.example.newsfeed.R;

public class ViewCardActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_viewcard);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        Article article = getIntent().getParcelableExtra(Article.TAG_ARTICLE);
        TextView authorView = findViewById(R.id.news_author);
        TextView contentView = findViewById(R.id.news_content);
        TextView linkView = findViewById(R.id.news_link);
        TextView nameView = findViewById(R.id.news_name);


        if (article != null) {
            linkView.setText(Html.fromHtml("<a href=\"" + article.get(Article.TAG_URL) + "\">to Source</a>"));
            linkView.setMovementMethod(LinkMovementMethod.getInstance());

            authorView.setText(article.get(Article.TAG_AUTHOR));
            contentView.setText(article.get(Article.TAG_DESCRIPTION));
            nameView.setText(article.get(Article.TAG_NAME));
        }
    }
}
