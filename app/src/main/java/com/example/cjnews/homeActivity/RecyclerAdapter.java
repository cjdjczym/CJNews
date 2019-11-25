package com.example.cjnews.homeActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.cjnews.newsActivity.Act_News;
import com.example.cjnews.R;
import com.example.cjnews.newsActivity.TopNewsBean;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter {

    private final static int ITEM_TYPE_IMAGE = 0;
    private final static int ITEM_TYPE_NEWS = 1;
    private final static int ITEM_TYPE_LINE = 2;
    private List<NewsBean> newsList;
    private static List<TopNewsBean> imageViewList;
    private static boolean isScroll = false;
    private final static int SLEEP_TIME = 4000;//轮播图间隔时间
//    private int previousPosition = 0;
//    private List<View> mDots;

    RecyclerAdapter(List<NewsBean> newsList, List<TopNewsBean> imageViewList) {
        this.newsList = newsList;
        RecyclerAdapter.imageViewList = imageViewList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recy_vp, parent, false);
            return new PicVH(view);
        } else if (viewType == ITEM_TYPE_NEWS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recy_item, parent, false);
            final NewsVH holder = new NewsVH(view);
            holder.newsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Act_News.activityStart(Act_Home.getContext(), holder.getAdapterPosition(), 1);
                }
            });
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recy_dateline, parent, false);
            return new LineVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PicVH) {
            final PicVH hold = (PicVH) holder;
            hold.viewPager.setAdapter(new ViewPagerAdapter(imageViewList));
            hold.titleView.setText(imageViewList.get(0).getTitle());
            hold.hintView.setText(imageViewList.get(0).getHint());
            int m = (Integer.MAX_VALUE / 2) % imageViewList.size();
            int currentPosition = Integer.MAX_VALUE / 2 - m;
            hold.viewPager.setCurrentItem(currentPosition);
        } else if (holder instanceof NewsVH) {
            final NewsVH hold = (NewsVH) holder;
            hold.titleView.setText(newsList.get(position - 1).getTitle());
            hold.hintView.setText(newsList.get(position - 1).getHint());
            if (newsList.get(0).getType() == 0) {
                Glide.with(Act_Home.getContext()).load(newsList.get(position - 1).getNetImage()).into(hold.imageView);
            }
        } else if (holder instanceof LineVH) {
            final LineVH hold = (LineVH) holder;
            hold.textView.setText(newsList.get(position - 1).getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return ITEM_TYPE_IMAGE;
        if (newsList.get(position - 1).getType() == 1) return ITEM_TYPE_LINE;
        else return ITEM_TYPE_NEWS;
    }

    @Override
    public int getItemCount() {
        return newsList.size() + 1;
    }

    static class NewsVH extends RecyclerView.ViewHolder {
        View newsView;
        final TextView titleView;
        final TextView hintView;
        final ImageView imageView;

        NewsVH(@NonNull final View itemView) {
            super(itemView);
            newsView = itemView;
            titleView = itemView.findViewById(R.id.recy_card_text);
            hintView = itemView.findViewById(R.id.recy_card_hint);
            imageView = itemView.findViewById(R.id.recy_card_image);
        }
    }

    static class PicVH extends RecyclerView.ViewHolder {
        final ViewPager viewPager;
        final TextView titleView;
        final TextView hintView;

        PicVH(@NonNull final View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.home_recy_vp_title);
            hintView = itemView.findViewById(R.id.home_recy_vp_hint);
            viewPager = itemView.findViewById(R.id.home_recy_vp);
            new Thread() {
                public void run() {
                    isScroll=false;
                    while (!isScroll) {
                        try {
                            Thread.sleep(SLEEP_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        viewPager.post(new Runnable() {
                            @Override
                            public void run() {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            }
                        });
                    }
                }
            }.start();
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    int newPosition = position % imageViewList.size();
                    titleView.setText(imageViewList.get(newPosition).getTitle());
                    hintView.setText(imageViewList.get(newPosition).getHint());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    static class LineVH extends RecyclerView.ViewHolder {
        final TextView textView;

        LineVH(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.home_recy_date_line);
        }
    }
}