package com.example.cjnews.homeActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.cjnews.newsActivity.TopNewsBean;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private List<TopNewsBean> topNews;

    ViewPagerAdapter(List<TopNewsBean> topNews) {
        this.topNews = topNews;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = topNews.get(position % topNews.size()).getImageView();
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(topNews.get(position % topNews.size()).getImageView());
    }
}
