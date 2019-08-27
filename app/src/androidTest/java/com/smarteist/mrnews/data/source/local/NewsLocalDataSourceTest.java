package com.smarteist.mrnews.data.source.local;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.smarteist.mrnews.data.models.News;
import com.smarteist.mrnews.data.source.NewsDataSource;
import com.smarteist.mrnews.util.Constants;
import com.smarteist.mrnews.utils.SingleExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.disposables.Disposable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test
 * SUT - {@link NewsLocalDataSource}
 */
@RunWith(AndroidJUnit4.class)
public class NewsLocalDataSourceTest {
    private NewsLocalDataSource mLocalDataSource;

    private NewsDatabase mDatabase;

    @Before
    public void setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                NewsDatabase.class)
                .build();
        NewsDao tasksDao = mDatabase.newsDao();

        // Make sure that we're not keeping a reference to the wrong instance.
        mLocalDataSource = new NewsLocalDataSource(new SingleExecutors(), tasksDao);
    }

    @After
    public void cleanUp() {
        mDatabase.newsDao().deleteNews();
        mDatabase.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }


    /**
     * Test scenario states:
     * Local DataSource should retrieve correct records from Database
     */
    @Test
    public void getNews_Scenario() {
        // Given 2 news (items) in the persistent local repository
        mLocalDataSource.insertNews(Constants.TEST_NEWS_2.get(0));
        mLocalDataSource.insertNews(Constants.TEST_NEWS_2.get(1));

        // Then the tasks can be retrieved from the persistent repository
        mLocalDataSource.getNews(Constants.NEWS_CATEGORY_LATEST, new NewsDataSource.LoadNewsCallback() {
            @Override
            public void onDisposableAcquired(Disposable disposable) {
            }

            @Override
            public void onNewsLoaded(List<News> news) {
                assertNotNull(news);
                assertTrue(news.size() == 2);

                boolean newsItem1Found = false;
                boolean newsItem2Found = false;
                for (News newsItem : news) {
                    if (newsItem.getId() == Constants.TEST_NEWS_2.get(0).getId()) {
                        newsItem1Found = true;
                    }
                    if (newsItem.getId() == Constants.TEST_NEWS_2.get(1).getId()) {
                        newsItem2Found = true;
                    }
                }
                assertTrue(newsItem1Found);
                assertTrue(newsItem2Found);
            }

            @Override
            public void onDataNotAvailable() {
                fail();
            }
        });
    }


    /**
     * Test scenario states:
     * Upon retrieval of Unarchived and Archived items from Local DataSource,
     * correct callbacks should be made
     */
    @Test
    public void retrieveArchivedUnarchivedNews_Scenario() {
        // Initialize mocks for the callbacks.
        NewsDataSource.LoadNewsCallback callbackUnarchived = mock(NewsDataSource.LoadNewsCallback.class);
        NewsDataSource.LoadSavedNewsCallback callbackArchived = mock(NewsDataSource.LoadSavedNewsCallback.class);

        // insert news (items)
        for(News newsItem : Constants.TEST_NEWS_2) mLocalDataSource.insertNews(newsItem);


        mLocalDataSource.getNews(Constants.NEWS_CATEGORY_LATEST, callbackUnarchived);

        // check if correct callbacks were made
        verify(callbackUnarchived, never()).onDataNotAvailable();
        verify(callbackUnarchived).onNewsLoaded(anyList());

        mLocalDataSource.getArchivedNews(callbackArchived);

        // check if correct callbacks were made
        verify(callbackArchived, never()).onDataNotAvailable();
        verify(callbackArchived).onNewsLoaded(anyList());

    }

    /**
     * Test scenario states:
     * Upon deletion of all records of Local DataSource,
     * correct callbacks should be made
     */
    @Test
    public void deleteAllNewsAndRetrieve_Scenario() {
        mLocalDataSource.insertNews(Constants.TEST_NEWS_2.get(0));
        NewsDataSource.LoadNewsCallback callback = mock(NewsDataSource.LoadNewsCallback.class);

        // When all news (items) are deleted
        mLocalDataSource.deleteNews();

        // Then the retrieved news content is actually an empty list
        mLocalDataSource.getNews(Constants.NEWS_CATEGORY_LATEST, callback);

        verify(callback).onDataNotAvailable();
        verify(callback, never()).onNewsLoaded(anyList());
    }
}
