package com.smarteist.mrnews.views.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.smarteist.mrnews.BaseContracts;
import com.smarteist.mrnews.BaseFragment;
import com.smarteist.mrnews.data.models.News;
import com.smarteist.mrnews.di.scopes.ActivityScoped;
import com.smarteist.mrnews.R;
import com.smarteist.mrnews.util.ConnectivityUtils.OnlineChecker;
import com.smarteist.mrnews.util.Constants;
import com.smarteist.mrnews.views.NewsAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.DaggerFragment;


/**
 * News Screen {@link NewsContracts.View}
 */
@ActivityScoped
public class NewsFragment extends BaseFragment implements NewsContracts.View {
    private NewsAdapter adapter;
    private TextView noNewsTv;
    private ListView listView;

    @Inject
    NewsPresenter mPresenter;

    @Inject
    OnlineChecker onlineChecker;

    @Inject
    public NewsFragment() {
        // Required empty public constructor
    }

    NewsItemListener mNewsItemListener = new NewsItemListener() {
        @Override
        public void onNewsClick(News clickedNews) {
            mPresenter.showNewsDetail(clickedNews);
        }

        @Override
        public void onArchiveNewsClick(News toSaveNews) {
            mPresenter.archiveNews(toSaveNews);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialise adapter
        adapter = new NewsAdapter(new ArrayList<News>(0), mNewsItemListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        listView = root.findViewById(R.id.list_news);
        noNewsTv = root.findViewById(R.id.no_news_tv);
        noNewsTv.setVisibility(View.GONE);

        listView.setAdapter(adapter);

        mPresenter.loadNews(Constants.NEWS_CATEGORY_LATEST);

        return root;
    }

    @Override
    public void showNews(List<News> news) {
        adapter.replaceData(news);
        listView.setSelectionAfterHeaderView();
        noNewsTv.setVisibility(View.GONE);
    }

    @Override
    public void showNoNews() {
        adapter.notifyDataSetInvalidated();
        adapter.clearContent();

        noNewsTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void getImageLoaderService(Picasso picasso) {
        adapter.setImageLoaderService(picasso);
    }

    @Override
    public void initViews(View parentRoot) {

    }

    @Override
    public NewsPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.attach(this);
        mPresenter.onViewResume();
        adapter.setInternetAccessState(onlineChecker.isOnline());

        if (!onlineChecker.isOnline()) showOfflineStateMessage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onViewDestroyed();
        mPresenter.detach();  //prevent leaking activity in
        // case presenter is orchestrating a long running task
    }

    /**
     * Through this interface we establish a communication channel between
     * view (fragment) and its adapter
     */
    public interface NewsItemListener {
        void onNewsClick(News clickedNews);

        void onArchiveNewsClick(News toSaveNews);
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessfullyArchivedNews() {
        showMessage(getString(R.string.successfully_archived_news_message));
    }

    public void showOfflineStateMessage() {
        showMessage(getString(R.string.offline_state_message));
    }
}
