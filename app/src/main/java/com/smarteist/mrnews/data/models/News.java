package com.smarteist.mrnews.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "news")
public final class News {

    @PrimaryKey
    @ColumnInfo(name = "entryid")
    private int id;

    @Ignore
    @SerializedName("source")
    @Expose
    private SourceData sourceData;

    @ColumnInfo(name = "source")
    private String sourceDataString;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("author")
    @Expose
    @ColumnInfo(name = "author")
    private String author;

    @SerializedName("url")
    @Expose
    @ColumnInfo(name = "url")
    private String url;

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    private String description;

    @SerializedName("urlToImage")
    @Expose
    @ColumnInfo(name = "urlToImage")
    private String urlToImage;

    @SerializedName("publishedAt")
    @Expose
    @ColumnInfo(name = "publishedAt")
    private String publishedAt;

    @ColumnInfo(name = "archived")
    private boolean archived;

    @ColumnInfo(name = "category")
    private String category;

    public News(String title, String author, String url, String description, String urlToImage, String publishedAt, String category, boolean archived) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.archived = archived;
        this.category = category;
        this.description = description;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    @Ignore
    public News(int id, String title, String author, boolean archived, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.archived = archived;
        this.category = category;
    }

    @Ignore
    public News(String title, String author, boolean archived, String category) {
        this.title = title;
        this.author = author;
        this.archived = archived;
        this.category = category;
    }

    @Ignore
    public News(String title, String author, boolean archived, String category, String publishedAt) {
        this.title = title;
        this.author = author;
        this.archived = archived;
        this.category = category;
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public SourceData getSourceData() {
        return sourceData;
    }

    public void setSourceData(SourceData sourceData) {
        this.sourceData = sourceData;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getSourceDataString() {
        return sourceDataString;
    }

    public void setSourceDataString(String sourceDataString) {
        this.sourceDataString = sourceDataString;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    @Override
    public String toString() {
        return (title + author);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(id, title, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return com.google.common.base.Objects.equal(id, news.id) &&
                com.google.common.base.Objects.equal(title, news.title) &&
                com.google.common.base.Objects.equal(url, news.url) &&
                com.google.common.base.Objects.equal(category, news.category) &&
                com.google.common.base.Objects.equal(description, news.description);
    }

    public class SourceData {
        @SerializedName("name")
        @ColumnInfo(name = "source_name")
        @Expose
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

