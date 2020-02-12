package com.example.newsfeed.Recycleview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newsfeed.Items.Article;
import com.example.newsfeed.R;

public class NewsItemViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageArticleView = null;

    private TextView titleArticleView = null;

    private Article article;


    public NewsItemViewHolder(View cardArticleView) {
        super(cardArticleView);
        imageArticleView = cardArticleView.findViewById(R.id.card_view_image);
        titleArticleView = cardArticleView.findViewById(R.id.card_view_image_title);
    }

    public TextView getArticleTitleView() {
        return titleArticleView;
    }

    public ImageView getArticleImageView() {
        return imageArticleView;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;

        titleArticleView.setText(article.get(Article.TAG_TITLE));
    }
}
