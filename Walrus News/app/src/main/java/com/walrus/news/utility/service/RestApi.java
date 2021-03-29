package com.walrus.news.utility.service;

import com.walrus.news.models.sources.SourcesResponse;
import com.walrus.news.utility.AppConstants;
import com.walrus.news.models.topHeadlines.TopHeadlinesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RestApi {

    @GET(AppConstants.ENDPOINT_EVERYTING)
    Call<TopHeadlinesResponse> getEverything(@Header(AppConstants.REQ_AUTHORIZATION) String apiKey,
                                             @Query(AppConstants.REQ_Q) String queryString);

    @GET(AppConstants.ENDPOINT_TOP_HEADLINES)
    Call<TopHeadlinesResponse> topHeadlinesApi(@Header(AppConstants.REQ_AUTHORIZATION) String apiKey,
                                               @Query(AppConstants.REQ_SOURCES) String sources);

    @GET(AppConstants.ENDPOINT_SOURCES)
    Call<SourcesResponse> sourcesApi(@Header(AppConstants.REQ_AUTHORIZATION) String apiKey,
                                     @Query(AppConstants.REQ_LANGUAGE) String language,
                                     @Query(AppConstants.REQ_COUNTRY) String country);

}
