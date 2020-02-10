package com.example.newsfeed.Recycleview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newsfeed.Activities.ViewCardActivity;
import com.example.newsfeed.Items.Article;
import com.example.newsfeed.Network.ImageDownloader;
import com.example.newsfeed.R;

import java.util.ArrayList;

public class NewsRecycleViewAdapter extends RecyclerView.Adapter<NewsItemViewHolder> {

    private ArrayList<Article> articles;
    private ImageDownloader imageDownloader = new ImageDownloader();
    private Context activityContext;


    public NewsRecycleViewAdapter(ArrayList<Article> articles, Context context) {
        this.articles = articles;
        activityContext = context;
    }

    public void onItemClick(int position) {
    }

    @Override
    public NewsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        final NewsItemViewHolder vh = new NewsItemViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityContext, ViewCardActivity.class);
                intent.putExtra(Article.TAG_ARTICLE, vh.getArticle());

                activityContext.startActivity(intent);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(NewsItemViewHolder holder, final int articleIndex) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Article article = articles.get(articleIndex);
        String urlToImg = articles.get(articleIndex).get(Article.TAG_URL_TO_IMAGE);
        holder.setArticle(article);
        holder.getArticleTitleView().setText(article.get(Article.TAG_TITLE));

        if (!urlToImg.equals("null")) {
            imageDownloader.download(urlToImg, holder.getArticleImageView());
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
