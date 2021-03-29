package com.walrus.news.data.article;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.walrus.news.models.topHeadlines.Article;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert
    void Insert(Article article);

    @Update
        //(onConflict = OnConflictStrategy.REPLACE)
    void Update(Article article);

    @Delete
    void Delete(Article article);

    @Query("DELETE FROM table_name")
    void DeleteAllArticles();

    @Query("SELECT * FROM table_name")
    LiveData<List<Article>> getAllArticles();


}
