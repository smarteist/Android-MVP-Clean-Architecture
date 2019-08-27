package com.smarteist.mrnews.data.source.remote;


import android.support.annotation.NonNull;

import com.smarteist.mrnews.data.models.News;
import com.smarteist.mrnews.data.models.NewsResponse;
import com.smarteist.mrnews.data.source.NewsDataSource;
import com.smarteist.mrnews.di.scopes.ApplicationScoped;
import com.smarteist.mrnews.util.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Remote Data Source implementation
 */
@ApplicationScoped
public class NewsRemoteDataSource implements NewsDataSource {

    private NewsService mNewsService;

    @Inject
    public NewsRemoteDataSource(NewsService newsService) {
        mNewsService = newsService;
    }

    /**
     * We get fresh news (items) from Remote API
     */
    @Override
    public void getNews(String category, @NonNull final LoadNewsCallback callback) {
        mNewsService.getNews(Constants.NEWS_API_COUNTRY_STRING, category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<NewsResponse, List<News>>() {
                    @Override
                    public List<News> apply(NewsResponse newsResponse) throws Exception {
                        return newsResponse.articles;
                    }
                })
                .subscribe(new SingleObserver<List<News>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        callback.onDisposableAcquired(d);
                    }

                    @Override
                    public void onSuccess(List<News> news) {
                        callback.onNewsLoaded(news);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                    }
                });

    }

    /**
     * These methods should be implemented when required
     * (e.g. when a cloud service is integrated)
     */
    @Override
    public void getArchivedNews(@NonNull LoadSavedNewsCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void insertNews(News news) {
    }

    @Override
    public void updateNews(News news) {

    }

    @Override
    public void refreshNews(String category) {
    }

    @Override
    public void deleteNews() {
    }
}
