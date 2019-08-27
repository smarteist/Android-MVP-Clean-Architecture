package com.smarteist.mrnews.util;

import com.google.common.collect.Lists;
import com.smarteist.mrnews.data.models.News;

import java.util.List;

public class Constants {

    public final static String NEWS_CATEGORY_LATEST = "";
    public final static String NEWS_CATEGORY_TECHNOLOGY = "technology";
    public final static String NEWS_CATEGORY_BUSINESS = "business";
    public static final String NEWS_API_BASE_URL = "https://newsapi.org/v2/";
    public static final String NEWS_API_KEY_STRING = "apiKey";
    public static final String NEWS_API_COUNTRY_STRING = "gb";
    public static final String NEWS_ROOM_DB_STRING = "News.db";

    // for tests only
    public static final String TEST_NO_CATEGORY_STRING = "";
    public static final List<News> TEST_NEWS = Lists.newArrayList(
            new News("Title1", "author1", false, TEST_NO_CATEGORY_STRING),
            new News("Title2", "author2", false, TEST_NO_CATEGORY_STRING),
            new News("Title3", "author3", false, TEST_NO_CATEGORY_STRING));

    public static List<News> TEST_NEWS_2 = Lists.newArrayList(
            new News(1, "Title1", "author1", false, TEST_NO_CATEGORY_STRING),
            new News(2, "Title2", "author2", false, TEST_NO_CATEGORY_STRING),
            new News(3, "Title3", "author3", true, TEST_NO_CATEGORY_STRING));

    public static List<News> TEST_NEWS_3 = Lists.newArrayList(
            new News("Title1", "author1", false, "", "2018-07-10T08:10:00Z"),
            new News("Title2", "author2", false, "", "2018-07-10T09:10:00Z"),
            new News("Title3", "author3", false, "", "2018-07-10T04:10:00Z"));
}
