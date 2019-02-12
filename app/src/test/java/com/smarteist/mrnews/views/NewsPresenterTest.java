package com.smarteist.mrnews.views;

import com.smarteist.mrnews.data.models.News;
import com.smarteist.mrnews.data.source.NewsDataSource;
import com.smarteist.mrnews.data.source.NewsRepository;
import com.smarteist.mrnews.util.ChromeTabsUtils.ChromeTabsWrapper;
import com.smarteist.mrnews.util.Constants;
import com.smarteist.mrnews.views.fragments.NewsContracts;
import com.smarteist.mrnews.views.fragments.NewsPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests
 * SUT - {@link NewsPresenter}
 */
public class NewsPresenterTest {
    @Mock
    private NewsRepository mNewsRepository;

    @Mock
    private ChromeTabsWrapper mChromeTabsWrapper;

    @Mock
    private NewsContracts.View mNewsView;

    @Captor
    private ArgumentCaptor<NewsDataSource.LoadNewsCallback> mLoadNewsCallbackCaptor;

    private NewsPresenter mNewsPresenter;

    @Before
    public void setupNewsPresenter() {
        // inject the mocks
        MockitoAnnotations.initMocks(this);

        mNewsPresenter = new NewsPresenter(mNewsRepository, new CompositeDisposable(), mChromeTabsWrapper);
        mNewsPresenter.subscribe(mNewsView);
    }

    /**
     * Test scenario states:
     * Upon presenter call, check if repository conforms
     * Also should check if {@link NewsContracts.View} conforms
     */
    @Test
    public void loadHeadlinesNewsFromRepositoryAndLoadIntoView_Scenario(){
        mNewsPresenter.loadNews(Constants.NEWS_CATEGORY_LATEST);

        verify(mNewsRepository).getNews(eq(Constants.NEWS_CATEGORY_LATEST), mLoadNewsCallbackCaptor.capture());
        mLoadNewsCallbackCaptor.getValue().onNewsLoaded(Constants.TEST_NEWS_3);

        ArgumentCaptor<List> showNewsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mNewsView).showNews(showNewsArgumentCaptor.capture());

        assertTrue(showNewsArgumentCaptor.getValue().size() == 3);
    }

    /**
     * Test scenario states:
     * Upon presenter call, check if {@link ChromeTabsWrapper} conforms
     */
    @Test
    public void clickOnNewsAndShowDetails_Scenario(){
        // Given a stubbed news item
        News requestedNews = new News("Title", "Author", false, "");

        // upon request
        mNewsPresenter.showNewsDetail(requestedNews);

        //check if TabsWrapper conforms
        verify(mChromeTabsWrapper).openCustomtab(requestedNews.getUrl());
    }

    /**
     * Test scenario states:
     * Upon presenter call, check if {@link NewsContracts.View} conforms
     */
    @Test
    public void onUnavailableNewsShowErrorUi_Scenario(){
        // after loading data from repository
        mNewsPresenter.loadNews(Constants.NEWS_CATEGORY_LATEST);

        verify(mNewsRepository).getNews(eq(Constants.NEWS_CATEGORY_LATEST), mLoadNewsCallbackCaptor.capture());

        // and having an error response
        mLoadNewsCallbackCaptor.getValue().onDataNotAvailable();

        //check if the View acts accordingly
        verify(mNewsView).showNoNews();
    }

}
