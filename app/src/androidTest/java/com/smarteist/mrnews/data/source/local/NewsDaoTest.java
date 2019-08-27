package com.smarteist.mrnews.data.source.local;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.smarteist.mrnews.data.models.News;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test
 * SUT - {@link NewsDao}
 */

@RunWith(AndroidJUnit4.class)
public class NewsDaoTest {
    private NewsDatabase mDatabase;
    private static String mMainCategory = "business";
    private static String mDiffCategory = "technology";
    private static final News mNewsItem1 = new News(56, "title1", "author1", false, mMainCategory);
    private static final News mNewsItem2 = new News(1, "title22", "author133", false, mMainCategory);
    private static final News mNewsItem3 = new News(33, "title33", "author333", false, mMainCategory);
    private static final News mNewsItem4 = new News(44, "title44", "author44", false, mDiffCategory);
    private static final News mNewsItem5 = new News(43, "title33", "author233", true, mMainCategory);

    @Before
    public void initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                NewsDatabase.class).build();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    /**
     * ALL BELOW TEST SCENARIOS EITHER GET, DELETE, REFRESH etc. NEWS (ITEMS) FROM
     * WITHIN A SPECIFIC CATEGORY (in this case {@link mMainCategory}
     *
     * There will be a last test conducted to check that items from within a specific
     * category are correctly retrieved
     */

    /**
     * Test scenario states:
     * Upon insertion of a news (item), the correct news (item) is retrieved
     */
    @Test
    public void insertNewsAndGetById_Scenario() {
        // insert news (item)
        mDatabase.newsDao().insertNews(mNewsItem1);

        // getting the news (item) by id from the database
        News loaded = mDatabase.newsDao().getNewsById(mNewsItem1.getId());

        // the loaded data contains the expected values
        assertNews(loaded, 56, "title1", "author1", false, mMainCategory);
    }

    /**
     * Test scenario states:
     * Upon initial insertion of 3 news (items), we should retrieve 3 items
     * As one of the items has been archived by the user, another retrieval should
     * provide only 2 items, as only unarchived items are retrieved
     */
    @Test
    public void receiveNewsAndSaveSomeThenRetrieve_Scenario() {
        // assume a refresh occurs
        mDatabase.newsDao().insertNews(mNewsItem1);
        mDatabase.newsDao().insertNews(mNewsItem2);
        mDatabase.newsDao().insertNews(mNewsItem3);

        List<News> newsRetrieved = mDatabase.newsDao().getNews(mMainCategory);

        // we should now get 3 items
        assertThat(newsRetrieved.size(), is(3));

        // archive a news item
        mNewsItem1.setArchived(true);
        mDatabase.newsDao().updateNews(mNewsItem1);

        //  assume another refresh occurs  we should now get only 2 items
        newsRetrieved = mDatabase.newsDao().getNews(mMainCategory);
        // we should now get only 2 items
        assertThat(newsRetrieved.size(), is(2));

        mNewsItem1.setArchived(false);
    }

    /**
     * Test scenario states:
     * Upon insertion of 2 news (items) that have the same id,
     * the transaction should be ignored, and the old object should remain
     */
    @Test
    public void insertNewsReplacesOnConflict() {
        // insert news (item)
        mDatabase.newsDao().insertNews(mNewsItem1);

        News newsItem = new News(56, "title2", "author2", false, mMainCategory);
        // insert conflictual news (item)
        mDatabase.newsDao().insertNews(newsItem);

        // getting the news (item) by id from the database
        News loaded = mDatabase.newsDao().getNewsById(mNewsItem1.getId());

        // the loaded data contains the expected values
        assertNews(loaded, 56, "title1", "author1", false, mMainCategory);
    }

    /**
     * Test scenario states:
     * Upon insertion of 2 news (items), we should be able to retrieve all unsaved items
     */
    @Test
    public void insertAndGetNews_Scenario() {
        // insert news (items)
        mDatabase.newsDao().insertNews(mNewsItem1);
        mDatabase.newsDao().insertNews(mNewsItem2);

        // getting the news (items) from the database
        List<News> newsRetrieved = mDatabase.newsDao().getNews(mMainCategory);

        // checking that retrieved list contain 2 unsaved items
        assertThat(newsRetrieved.size(), is(2));

        // checking consistency of the items
        assertNews(newsRetrieved.get(0), 1, "title22", "author133", false, mMainCategory);
        assertNews(newsRetrieved.get(1), 56, "title1", "author1", false, mMainCategory);
    }

    /**
     * Test scenario states:
     * Upon insertion of 1 news (item) and deletion of all records, we should retrieve an empty list
     * fom the database
     */
    @Test
    public void deleteNewsAndGetNews_Scenario() {
        // given a news (item) inserted
        mDatabase.newsDao().insertNews(mNewsItem1);

        // deleting all data
        mDatabase.newsDao().deleteNews();

        // getting the data
        List<News> news = mDatabase.newsDao().getNews(mMainCategory);

        // the list should be empty
        assertThat(news.size(), is(0));
    }

    /**
     * Test scenario states:
     * Upon insertion of 2 news (items), one saved and one unsaved,
     * we refresh database records deleting all unsaved news (items)
     * simply retrieving items should now provide am empty list
     * if we retrieve only saved news we should get 1 item
     */
    @Test
    public void refreshNewsAndGetNews_Scenario() {
        // insert news (items)
        mDatabase.newsDao().insertNews(mNewsItem1);
        mDatabase.newsDao().insertNews(mNewsItem5);

        // refresh data
        mDatabase.newsDao().refreshNews(mMainCategory);

        // getting data directly should provide all unsaved news
        List<News> news = mDatabase.newsDao().getNews(mMainCategory);

        // the list should be empty of unsaved news
        assertThat(news.size(), is(0));

        news = mDatabase.newsDao().getArchivedNews();

        // the list should have 1 item
        assertThat(news.size(), is(1));

        //now let's check the item's consistency
        assertNews(news.get(0), 43, "title33", "author233", true, mMainCategory);
    }

    /**
     * LAST TEST
     * Test scenario states:
     * Upon insertion of 2 news (items), one within a category and another within
     * another category, retrieving news (items) within a category from database
     * should result in only 1 item
     */
    @Test
    public void insertNewsAndGetNewsByDiffCategory_Scenario() {
        // insert news (items)
        mDatabase.newsDao().insertNews(mNewsItem1);
        mDatabase.newsDao().insertNews(mNewsItem4);

        // getting data directly should provide all unsaved news
        List<News> news = mDatabase.newsDao().getNews(mDiffCategory);

        // the list should have 1 item from within its category
        assertThat(news.size(), is(1));

        //now let's check the item's consistency
        assertNews(news.get(0), 44, "title44", "author44", false, mDiffCategory);
    }


    /**
     * Simple method to check a {@link News} item consistency
     */
    private void assertNews(News news, int id, String title, String author, boolean saved, String category) {
        assertThat(news, notNullValue());
        assertThat(news.getId(), is(id));
        assertThat(news.getTitle(), is(title));
        assertThat(news.getAuthor(), is(author));
        assertThat(news.isArchived(), is(saved));
        assertThat(news.getCategory(), is(category));
    }
}
