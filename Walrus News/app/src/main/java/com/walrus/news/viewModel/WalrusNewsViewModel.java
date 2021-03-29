package com.walrus.news.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.walrus.news.models.sources.SourcesResponse;
import com.walrus.news.models.topHeadlines.TopHeadlinesResponse;
import com.walrus.news.repository.WalrusNewsRepository;

public class WalrusNewsViewModel extends ViewModel {

    private WalrusNewsRepository repository;

    private MutableLiveData<TopHeadlinesResponse> topHeadlinesResponse;
    private MutableLiveData<SourcesResponse> sourcesResponse;

    public void initTopHeadlines(final String source) {
        repository = WalrusNewsRepository.getInstance();
        topHeadlinesResponse = repository.topHeadlinesApiCall(source);
    }

    public LiveData<TopHeadlinesResponse> getTopHeadlines() {
        return topHeadlinesResponse;
    }

    public void initSourcesApi() {
        repository = WalrusNewsRepository.getInstance();
        sourcesResponse = repository.sourcesApiCall();
    }

    public LiveData<SourcesResponse> getSources() {
        return sourcesResponse;
    }

}
