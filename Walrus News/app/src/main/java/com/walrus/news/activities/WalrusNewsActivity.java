package com.walrus.news.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.walrus.news.R;
import com.walrus.news.adapters.WalrusNewsAdapter;
import com.walrus.news.models.topHeadlines.Article;
import com.walrus.news.databinding.ActivityWalrusNewsBinding;
import com.walrus.news.models.sources.Source;
import com.walrus.news.utility.Utility;
import com.walrus.news.viewModel.ArticleViewModel;
import com.walrus.news.viewModel.ArticleViewModelFactory;
import com.walrus.news.viewModel.WalrusNewsViewModel;

import java.util.ArrayList;
import java.util.List;

public class WalrusNewsActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityWalrusNewsBinding binding;
    private List<Article> articleList = new ArrayList<>();
    private WalrusNewsAdapter adapter;
    private List<Source> sourceList = new ArrayList<>();
    private int count = 0;
    private ArticleViewModel articleViewModel;
    private boolean refreshClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_walrus_news);
        initUi();
        getOfflineArticles();
        getSourcesApi();
    }

    private void initUi() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/dagerotypos.ttf");
        binding.headerTitle.setTypeface(typeface);
        binding.refreshArticles.setOnClickListener(this);
        binding.closeBtn.setOnClickListener(this);
        binding.refreshButton.setOnClickListener(this);
        articleViewModel = new ViewModelProvider(this, new ArticleViewModelFactory(this.getApplication())).get(ArticleViewModel.class); //init
    }

    private void getSourcesApi() {
        WalrusNewsViewModel model = ViewModelProviders.of(this).get(WalrusNewsViewModel.class);
        Utility.showProgressDialog("Please wait...", this);
        model.initSourcesApi();
        model.getSources().observe(this, response -> {
            if (response != null && response.getSources() != null && !response.getSources().isEmpty()) {
                sourceList.clear();
                sourceList = response.getSources();
                count = 0;
                getNewsApi(count, sourceList.get(count).getId());
            }
        });
    }

    private void getNewsApi(final int sourceCount, final String source) {
        WalrusNewsViewModel model = ViewModelProviders.of(this).get(WalrusNewsViewModel.class);
        model.initTopHeadlines(source);
        model.getTopHeadlines().observe(this, response -> {
            Utility.dismissProgressDialog();
            if (response != null && response.getArticles() != null) {
                if (!response.getArticles().isEmpty()) {
                    if (sourceCount == 0) {
                        if(isNewsUpdated(response.getArticles().get(0))) {
                            if (articleList.isEmpty() || refreshClicked) {
                                if(refreshClicked) {
                                    articleList.clear();
                                    refreshClicked = false;
                                }
                                binding.refreshArticles.setVisibility(View.GONE);
                                articleList.addAll(response.getArticles());
                                adapter = new WalrusNewsAdapter(this, articleList);
                                LinearLayoutManager lm = new LinearLayoutManager(this);
                                binding.newsRecycler.setHasFixedSize(true);
                                binding.newsRecycler.setLayoutManager(lm);
                                binding.newsRecycler.setAdapter(adapter);
                                count++;
                                getNewsApi(count, sourceList.get(count).getId());
                            } else {
                                binding.refreshArticles.setVisibility(View.VISIBLE);
                            }
                        }
                    } else if (!sourceList.isEmpty() && sourceCount == sourceList.size() - 1) {
                        articleList.addAll(response.getArticles());
                        adapter.setArticleList(articleList);
                    } else {
                        articleList.addAll(response.getArticles());
                        adapter.setArticleList(articleList);
                        count++;
                        getNewsApi(count, sourceList.get(count).getId());
                    }
                    saveArticlesOffline(response.getArticles());
                } else {
                    count++;
                    getNewsApi(count, sourceList.get(count).getId());
                }
            } else {
                Toast.makeText(this, "Oops! Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNewsUpdated(Article article) {
        return articleList != null && !articleList.isEmpty() && Utility.dateToMillis(article.getPublishedAt(), "yyyy-MM-dd'T'HH:mm:ssZ") >
                Utility.dateToMillis(articleList.get(0).getPublishedAt(), "yyyy-MM-dd'T'HH:mm:ssZ");
    }

    public void saveArticlesOffline(List<Article> articleList) {
            for (int i = 0; i < articleList.size(); i++) {
                articleViewModel.insert(articleList.get(i)); //insert
            }
//        } else {
//            for (int i = 0; i < articleList.size(); i++) {
//                articleViewModel.update(articleList.get(i)); //update
//            }
//        }
    }

    public void getOfflineArticles() {
        articleViewModel.getAllArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (articles != null && !articles.isEmpty()) {
                    articleList = articles;
                    adapter = new WalrusNewsAdapter(WalrusNewsActivity.this, articleList);
                    LinearLayoutManager lm = new LinearLayoutManager(WalrusNewsActivity.this);
                    binding.newsRecycler.setHasFixedSize(true);
                    binding.newsRecycler.setLayoutManager(lm);
                    binding.newsRecycler.setAdapter(adapter);
                } else {
                    binding.refreshArticles.setVisibility(View.VISIBLE);
                }
            }
        }); //get all data
    }

    public void refereshArticles() {
        if(Utility.isNetworkOnline(this)) {
            articleViewModel.deleteAllArticles(); //delete
            refreshClicked = true;
            getSourcesApi();
            binding.refreshArticles.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.refresh_articles:
            case R.id.refresh_button:
                refereshArticles();
                break;
            case R.id.close_btn:
                onBackPressed();
                break;
        }
    }
}
