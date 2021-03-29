package com.walrus.news.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.walrus.news.models.topHeadlines.Article;
import com.walrus.news.data.article.ArticleDao;
import com.walrus.news.data.article.ArticleDatabase;

import java.util.List;

public class ArticleRepository {

    private ArticleDao articleDao;
    private LiveData<List<Article>> allArticles;

    public ArticleRepository(Application application) { //application is subclass of context
        ArticleDatabase database = ArticleDatabase.getInstance(application);
        articleDao = database.articleDao();
        allArticles = articleDao.getAllArticles();
    }

    //methods for database operations :-


    public void insert(Article article) {
        new InsertArticleAsyncTask(articleDao).execute(article);
    }
    public void update(Article article) {
        new UpdateArticleAsyncTask(articleDao).execute(article);
    }
    public void delete(Article article) {
        new DeleteArticleAsyncTask(articleDao).execute(article);
    }
    public void deleteAllArticles() {
        new DeleteAllArticlesAsyncTask(articleDao).execute();
    }
    public LiveData<List<Article>> getAllArticles() {
        return allArticles;
    }


    private static class InsertArticleAsyncTask extends AsyncTask<Article, Void, Void> { //static : doesnt have reference to the
        // repo itself otherwise it could cause memory leak!
        private ArticleDao articleDao;
        private InsertArticleAsyncTask(ArticleDao articleDao) {
            this.articleDao = articleDao;
        }
        @Override
        protected Void doInBackground(Article... articles) { // ...  is similar to array
            articleDao.Insert(articles[0]); //single article
            return null;
        }
    }
    private static class UpdateArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private ArticleDao articleDao;
        private UpdateArticleAsyncTask(ArticleDao articleDao) { //constructor as the class is static
            this.articleDao = articleDao;
        }
        @Override
        protected Void doInBackground(Article... articles) {
            articleDao.Update(articles[0]);
            return null;
        }
    }
    private static class DeleteArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private ArticleDao articleDao;
        private DeleteArticleAsyncTask(ArticleDao articleDao) {
            this.articleDao = articleDao;
        }
        @Override
        protected Void doInBackground(Article... articles) {
            articleDao.Delete(articles[0]);
            return null;
        }
    }
    private static class DeleteAllArticlesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ArticleDao articleDao;
        private DeleteAllArticlesAsyncTask(ArticleDao articleDao) {
            this.articleDao = articleDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            articleDao.DeleteAllArticles();
            return null;
        }
    }
}
