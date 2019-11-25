package com.example.cjnews.newsActivity;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class NewsViewPagerAdapter extends PagerAdapter {

    private List<View> webViews;

    NewsViewPagerAdapter(List<View> webViews) {
        this.webViews = webViews;
    }

    @Override
    public int getCount() {
        return webViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View webView = webViews.get(position);
        container.addView(webView);
        return webView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(webViews.get(position));
    }
}
