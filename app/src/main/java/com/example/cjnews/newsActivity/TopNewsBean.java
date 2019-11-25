package com.example.cjnews.newsActivity;

import android.widget.ImageView;

public class TopNewsBean {
    private ImageView imageView;
    private String title;
    private String hint;

    public TopNewsBean(ImageView imageView, String title, String hint) {
        this.imageView = imageView;
        this.title = title;
        this.hint = hint;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getTitle() {
        return title;
    }

    public String getHint() {
        return hint;
    }
}
