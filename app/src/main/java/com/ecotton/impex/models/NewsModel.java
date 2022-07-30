package com.ecotton.impex.models;

public class NewsModel {

    String shortnews,time;
    int newsimg;

    public NewsModel(String shortnews, String time, int newsimg) {
        this.shortnews = shortnews;
        this.time = time;
        this.newsimg = newsimg;
    }

    public String getShortnews() {
        return shortnews;
    }

    public void setShortnews(String shortnews) {
        this.shortnews = shortnews;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNewsimg() {
        return newsimg;
    }

    public void setNewsimg(int newsimg) {
        this.newsimg = newsimg;
    }
}
