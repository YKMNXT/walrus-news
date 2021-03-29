package com.walrus.news.data.article;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.walrus.news.models.topHeadlines.Article;


@Database(entities = Article.class,version = 1,exportSchema = false)
public abstract class ArticleDatabase extends RoomDatabase {
    private static ArticleDatabase instance;

    public abstract ArticleDao articleDao();

    public static synchronized ArticleDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ArticleDatabase.class , "Database_name")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    } //synchronized : only one thread can access this method
    //fallbacktodestructivemigration : to handle versions

    private static Callback roomCallBack = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // new PopulateDb(instance).execute();
        }
    };
}
