package com.walrus.news.repository;

import androidx.lifecycle.MutableLiveData;

import com.walrus.news.models.sources.SourcesResponse;
import com.walrus.news.utility.AppConstants;
import com.walrus.news.models.topHeadlines.TopHeadlinesResponse;
import com.walrus.news.utility.Utility;
import com.walrus.news.utility.service.RestApi;
import com.walrus.news.utility.service.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalrusNewsRepository {

    private static WalrusNewsRepository repository;

    public static WalrusNewsRepository getInstance() {
        if (repository == null) {
            repository = new WalrusNewsRepository();
        }
        return repository;
    }
    private RestApi restApi;

    public WalrusNewsRepository() {
        restApi = RestClient.createService(RestApi.class);
    }

    public MutableLiveData<TopHeadlinesResponse> topHeadlinesApiCall(final String source) {

        final MutableLiveData<TopHeadlinesResponse> getTopHeadlinesData = new MutableLiveData<>();

        restApi.topHeadlinesApi(AppConstants.API_KEY, source).
                enqueue(new Callback<TopHeadlinesResponse>() {
                    @Override
                    public void onResponse(Call<TopHeadlinesResponse> call, Response<TopHeadlinesResponse> response) {
                        Utility.dismissProgressDialog();
                        if (response != null && response.body() != null) {
                            getTopHeadlinesData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<TopHeadlinesResponse> call, Throwable t) {
                        Utility.dismissProgressDialog();
                    }
                });

        return getTopHeadlinesData;
    }

    public MutableLiveData<SourcesResponse> sourcesApiCall() {

        final MutableLiveData<SourcesResponse> getSourcesData = new MutableLiveData<>();

        restApi.sourcesApi(AppConstants.API_KEY, "en", "in").
                enqueue(new Callback<SourcesResponse>() {
                    @Override
                    public void onResponse(Call<SourcesResponse> call, Response<SourcesResponse> response) {
                        Utility.dismissProgressDialog();
                        if (response != null && response.body() != null) {
                            getSourcesData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<SourcesResponse> call, Throwable t) {
                        Utility.dismissProgressDialog();
                    }
                });

        return getSourcesData;
    }

}
