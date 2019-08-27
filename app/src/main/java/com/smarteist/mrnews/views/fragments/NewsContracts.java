package com.smarteist.mrnews.views.fragments;

import com.smarteist.mrnews.BaseContracts;
import com.smarteist.mrnews.data.models.News;
import com.smarteist.mrnews.views.fragments.NewsFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Contract that is responsible for News Screen operations
 */

public interface NewsContracts {

    interface View extends BaseContracts.View<NewsPresenter> {

        void showNews(List<News> news);

        void showNoNews();

        void showSuccessfullyArchivedNews();

        void getImageLoaderService(Picasso picasso);
    }

    abstract class Presenter implements BaseContracts.Presenter<NewsFragment> {

        abstract void loadNews(String category);

        abstract void showNewsDetail(News newsItem);

        abstract void loadSavedNews();

        abstract void archiveNews(News newsItem);

    }
}
