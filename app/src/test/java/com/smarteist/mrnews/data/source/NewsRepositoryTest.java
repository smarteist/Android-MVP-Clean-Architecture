package com.smarteist.mrnews.data.source;

import com.smarteist.mrnews.data.models.News;
import com.smarteist.mrnews.util.ConnectivityUtils.OnlineChecker;
import com.smarteist.mrnews.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests
 * SUT - {@link NewsRepository}
 */
public class NewsRepositoryTest {

    private NewsRepository mNewsRepository;

    @Mock
    private NewsDataSource mNewsRemoteDataSource;

    @Mock
    private NewsDataSource mNewsLocalDataSource;

    @Mock
    private OnlineChecker mOnlineChecker;

    @Mock
    private NewsDataSource.LoadNewsCallback mLoadNewsCallback;

    @Mock
    private NewsDataSource.LoadSavedNewsCallback mLoadSavedNewsCallback;

    @Captor
    private ArgumentCaptor<NewsDataSource.LoadNewsCallback> mNewsCallbackCaptor;

    @Captor
    private ArgumentCaptor<NewsDataSource.LoadSavedNewsCallback> mSavedNewsCallbackCaptor;

    @Before
    public void setupNewsRepository(){
        // init mocks
        MockitoAnnotations.initMocks(this);

        // get a reference to the class under test
        mNewsRepository = new NewsRepository(mNewsRemoteDataSource, mNewsLocalDataSource, mOnlineChecker);
    }

    /**
     * Offline test scenario states:
     * upon no internet connection, simply try getting unsaved news(items) from LocalDataSource
     */
    @Test
    public void getNews_OfflineScenario(){
        // establish a fake internet connection status
        when(mOnlineChecker.isOnline()).thenReturn(false);

        mNewsRepository.getNews("", mLoadNewsCallback);

        // the only call should be from LocalDataSource
        verify(mNewsLocalDataSource).getNews(eq(Constants.TEST_NO_CATEGORY_STRING),
                mNewsCallbackCaptor.capture());
    }

    /**
     * Online test scenario states:
     * upon internet connection, first try getting the data from RemoteDataSource
     * if RemoteDataSource fails providing the data, then LocalDataSource should be queried
     */
    @Test
    public void getNews_OnlineScenario(){
        // establish a fake internet connection status
        when(mOnlineChecker.isOnline()).thenReturn(true);

        mNewsRepository.getNews(Constants.TEST_NO_CATEGORY_STRING, mLoadNewsCallback);

        // first call from RemoteDataSource
        verify(mNewsRemoteDataSource).getNews(eq(Constants.TEST_NO_CATEGORY_STRING),
                mNewsCallbackCaptor.capture());

        // RemoteDataSource fails
        mNewsCallbackCaptor.getValue().onDataNotAvailable();

        // we should then get a call on the LocalDataSource
        verify(mNewsLocalDataSource).getNews(eq(Constants.TEST_NO_CATEGORY_STRING),
                mNewsCallbackCaptor.capture());
    }

    /**
     * Offline test scenario states:
     * upon no internet connection, simply try getting saved news(items) from LocalDataSource
     * if that fails, we will query the remote DataSource with an online state
     */
    @Test
    public void getSavedNews_OfflineScenario(){
        // establish a fake internet connection status
        when(mOnlineChecker.isOnline()).thenReturn(false);

        mNewsRepository.getArchivedNews(mLoadSavedNewsCallback);

        // first call should be from LocalDataSource
        verify(mNewsLocalDataSource).getArchivedNews(mSavedNewsCallbackCaptor.capture());

        // let's go online
        when(mOnlineChecker.isOnline()).thenReturn(true);

        // let's assume that the LocalDataSource failed
        mSavedNewsCallbackCaptor.getValue().onDataNotAvailable();

        // second call should be from RemoteDataSource
        verify(mNewsRemoteDataSource).getArchivedNews(mSavedNewsCallbackCaptor.capture());
    }

    /**
     * Online test scenario states:
     * upon internet connection, first try getting the data from RemoteDataSource
     * if RemoteDataSource succeeds providing the data, then both LocalDataSource
     * and RemoteDataSource should be refreshed with the new data
     */
    @Test
    public void refreshAndSaveNews_OnlineScenario(){
        // establish a fake internet connection status
        when(mOnlineChecker.isOnline()).thenReturn(true);

        mNewsRepository.getNews(Constants.TEST_NO_CATEGORY_STRING, mLoadNewsCallback);

        // first call from RemoteDataSource
        verify(mNewsRemoteDataSource).getNews(eq(Constants.TEST_NO_CATEGORY_STRING), mNewsCallbackCaptor
                .capture());

        // RemoteDataSource succeeds
        mNewsCallbackCaptor.getValue().onNewsLoaded(Constants.TEST_NEWS);

        // make sure that all items are being stored
        verify(mNewsLocalDataSource, times(Constants.TEST_NEWS.size())).insertNews(any(News.class));
        verify(mNewsRemoteDataSource, times(Constants.TEST_NEWS.size())).insertNews(any(News.class));
    }

    /**
     * Test scenario states:
     * Upon update command , both LocalDataSource and RemoteDataSource should update
     *  the corresponding item taken as parameter
     */
    @Test
    public void updateNews_Scenario(){
        // first save some news to the repository

        mNewsRepository.insertNews(Constants.TEST_NEWS.get(0));

        Constants.TEST_NEWS.get(0).setArchived(true);

        mNewsRepository.updateNews(Constants.TEST_NEWS.get(0));

        // upon update command, check if both data sources are being called
        verify(mNewsLocalDataSource).updateNews(Constants.TEST_NEWS.get(0));
        verify(mNewsRemoteDataSource).updateNews(Constants.TEST_NEWS.get(0));

        // make sure no other tests are being affected
        Constants.TEST_NEWS.get(0).setArchived(false);
    }

    /**
     * Test scenario states:
     * Upon save command , both LocalDataSource and RemoteDataSource should save
     *  the corresponding item taken as parameter
     */
    @Test
    public void saveNews_Scenario(){
        mNewsRepository.insertNews(Constants.TEST_NEWS.get(0));

        // upon save command, check if both data sources are being called
        verify(mNewsLocalDataSource).insertNews(Constants.TEST_NEWS.get(0));
        verify(mNewsRemoteDataSource).insertNews(Constants.TEST_NEWS.get(0));
    }

    /**
     * Test scenario states:
     * Upon refresh command , both LocalDataSource and RemoteDataSource should refresh their records
     */
    @Test
    public void refreshNews_Scenario(){
        mNewsRepository.refreshNews(Constants.TEST_NO_CATEGORY_STRING);

        // upon refresh command, check if both data sources are being called
        verify(mNewsLocalDataSource).refreshNews(Constants.TEST_NO_CATEGORY_STRING);
        verify(mNewsRemoteDataSource).refreshNews(Constants.TEST_NO_CATEGORY_STRING);
    }

    /**
     * Test scenario states:
     * Upon deletion command , both LocalDataSource and RemoteDataSource should delete
     *  their records of any data
     */
    @Test
    public void deleteAllNews_Scenario(){
        // first save some news to the repository
        for(News newsItem: Constants.TEST_NEWS)
            mNewsRepository.insertNews(newsItem);

        mNewsRepository.deleteNews();

        // upon deletion command, check if both data sources are being called
        verify(mNewsLocalDataSource).deleteNews();
        verify(mNewsRemoteDataSource).deleteNews();
    }
}
