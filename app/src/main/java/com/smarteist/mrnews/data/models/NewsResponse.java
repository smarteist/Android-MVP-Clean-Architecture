package com.smarteist.mrnews.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class NewsResponse {

    @SerializedName("articles")
    @Expose
    public List<News> articles;

    @SerializedName("status")
    @Expose
    public String status;


    @SerializedName("totalResults")
    @Expose
    public int totalResults;

    public List<News> getArticles() {
        return articles;
    }

    public void setArticles(List<News> articles) {
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public NewsResponse() {
        articles = new ArrayList<News>();
    }
}