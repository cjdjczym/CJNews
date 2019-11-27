package com.example.cjnews.homeActivity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private final static int SLEEP_TIME = 3000;//轮播图间隔时间
    private int previousPosition = 0;
    private List<View> mDots;

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
        Handler vpHandler = new Handler();
        ViewPager viewPager;
        final TextView titleView;
        final TextView hintView;

        PicVH(@NonNull final View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.home_recy_vp_title);
            hintView = itemView.findViewById(R.id.home_recy_vp_hint);
            viewPager = itemView.findViewById(R.id.home_recy_vp);
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
            autoScroll(viewPager, SLEEP_TIME);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void viewPagerOnTouch(final ViewPager viewPager, final int pauseTime) {
            //通过viewPager去设置触摸滑动的点击事件
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            vpHandler.removeMessages(0);
                            //移除回调函数和消息
                        case MotionEvent.ACTION_DOWN:
                            vpHandler.removeCallbacksAndMessages(null);
                            break;
                        //当你触摸时停止自动滑动
                        case MotionEvent.ACTION_UP:
                            PicVH.this.autoScroll(viewPager, pauseTime);
                            break;
                    }
                    return false;
                }
            });
        }

        private void autoScroll(final ViewPager viewPager, final int pauseTime) {
            vpHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //获取当前的轮播的位置,通过currentItem+1实现切换到下一张
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    //通过vpHandler请求延迟3秒
                    vpHandler.postDelayed(this, pauseTime);
                    //调用触摸滑动事件方法
                    viewPagerOnTouch(viewPager, pauseTime);
                }
            }, pauseTime);
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