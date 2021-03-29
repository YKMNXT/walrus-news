package com.walrus.news.viewModel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ArticleViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;



    public ArticleViewModelFactory(Application application) {
        mApplication = application;

    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ArticleViewModel(mApplication);
    }

}
