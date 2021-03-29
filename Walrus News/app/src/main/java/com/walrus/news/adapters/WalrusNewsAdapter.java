package com.walrus.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.walrus.news.R;
import com.walrus.news.activities.NewsWebViewActivity;
import com.walrus.news.activities.WalrusNewsActivity;
import com.walrus.news.databinding.NewsContentListItemBinding;
import com.walrus.news.models.topHeadlines.Article;
import com.walrus.news.utility.AppConstants;

import java.util.List;

public class WalrusNewsAdapter extends RecyclerView.Adapter<WalrusNewsAdapter.ViewHolder> {

    private List<Article> marticleList;
    private Context mcontext;

    public WalrusNewsAdapter(Context context, List<Article> articleList) {
        this.marticleList = articleList;
        this.mcontext = context;
    }

    public void setArticleList(List<Article> articleList) {
        this.marticleList = articleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewsContentListItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_content_list_item, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = marticleList.get(position);
        holder.binding.setArticle(article);
        Log.d("My_Log_Tag", "photoUrl: " + article.getUrlToImage() + " imgView: " + holder.binding.imageView);
        Glide.with(mcontext)
                .load(article.getUrlToImage())
                .into(holder.binding.imageView);
        holder.binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WalrusNewsActivity) mcontext).startActivity(new Intent(mcontext, NewsWebViewActivity.class)
                        .putExtra(AppConstants.EXTRA_URL_TO_LOAD, article.getUrl())
                        .putExtra(AppConstants.EXTRA_SCREEN_TITLE, article.getTitle()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return marticleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private NewsContentListItemBinding binding;

        public ViewHolder(@NonNull NewsContentListItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
