package com.smarteist.mrnews.data.source.remote;



import com.google.common.collect.Lists;
import com.smarteist.mrnews.data.models.News;
import com.smarteist.mrnews.rules.RxSchedulersOverrideRule;
import com.smarteist.mrnews.data.models.NewsResponse;
import com.smarteist.mrnews.data.source.NewsDataSource;
import com.smarteist.mrnews.util.Constants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;
import java.util.Observable;

import io.reactivex.Single;
import io.reactivex.exceptions.Exceptions;
import retrofit2.HttpException;

import static io.reactivex.Observable.error;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test
 * SUT - {@link NewsRemoteDataSource}
 */
public class NewsRemoteDataSourceTest {

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    NewsService newsService;

    private NewsRemoteDataSource mRemoteDataSource;

    @Before
    public void setup() throws Exception {
        // init mocks
        MockitoAnnotations.initMocks(this);

        // get reference to the class in test
        mRemoteDataSource = new NewsRemoteDataSource(newsService);

    }


    @Test
    public void testPreConditions() {
        assertNotNull(mRemoteDataSource);
    }

    /**
     * Test scenario states:
     * Remote Source should get the correct callbacks in both success and failure scenarios
     */
    @Test
    public void testRemoteApiResponse() throws Exception{
        // set up mock callback
        NewsDataSource.LoadNewsCallback newsCallback = mock(NewsDataSource.LoadNewsCallback.class);

        // set up mock response
        NewsResponse mockNewsResponse = new NewsResponse();
        mockNewsResponse.setArticles(Constants.TEST_NEWS);

        // prepare fake response
        when(newsService.getNews(Constants.NEWS_API_COUNTRY_STRING, Constants.TEST_NO_CATEGORY_STRING))
                .thenReturn(Single.just(mockNewsResponse));

        // assume the repository calls the remote DataSource
        mRemoteDataSource.getNews(Constants.TEST_NO_CATEGORY_STRING, newsCallback);

        // check if correct callback has been made
        verify(newsCallback).onNewsLoaded(Constants.TEST_NEWS);

        // prepare fake exception
        Throwable exception = new IOException();

        // prepare fake response
        when(newsService.getNews(Constants.NEWS_API_COUNTRY_STRING, Constants.TEST_NO_CATEGORY_STRING))
                .thenReturn(Single.<NewsResponse>error(exception));

        // assume the repository calls the remote DataSource
        mRemoteDataSource.getNews(Constants.TEST_NO_CATEGORY_STRING, newsCallback);

        verify(newsCallback).onDataNotAvailable();
    }
}
