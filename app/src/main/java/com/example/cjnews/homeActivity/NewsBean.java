package com.example.cjnews.homeActivity;

class NewsBean {
    private String title;
    private String hint;
    private String netImage; //图片的http网址
    private int type; //种类: 0新闻 1日期分割

    NewsBean(String title, String hint, String netImage) {
        this.title = title;
        this.hint = hint;
        this.netImage = netImage;
        this.type = 0;
    }

    NewsBean(String title) {
        this.title = title;
        this.type = 1;
    }

    String getNetImage() {
        return netImage;
    }

    int getType() {
        return type;
    }

    String getTitle() {
        return title;
    }

    String getHint() {
        return hint;
    }
}
