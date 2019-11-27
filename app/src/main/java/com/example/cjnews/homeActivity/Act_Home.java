package com.example.cjnews.homeActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cjnews.newsActivity.Act_News;
import com.example.cjnews.userActivity.Act_User;
import com.example.cjnews.netClient.Bean;
import com.example.cjnews.netClient.Client;
import com.example.cjnews.R;
import com.example.cjnews.newsActivity.TopNewsBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Act_Home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<NewsBean> newsList;//用于recyclerView的新闻item
    private List<TopNewsBean> imageViewList;//用于recyclerView的轮播图
    private static List<String> urlTopList;//用于轮播图的webView&&viewPager
    private static List<List<String>> urlList;//用于新闻item的webView&&viewPager（网址）
    private static List<Integer> sizeList;//用于新闻item的webView&&viewPager（计数）
    private static String msgString;
    private static Context context;
    private static int preDay = 0;//加载几天前的新闻
    private static boolean isComplete;//加载下一天是否完成

    enum Type { //网络请求的种类
        refresh, loadMore
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        context = getApplicationContext();
        initLists();
        initToolBar();
        setGreedText();
        initRefresh();
        loadMore(Type.refresh);//内含initRecyclerView()
    }

    private void initLists() {
        newsList = new ArrayList<>();
        imageViewList = new ArrayList<>();
        urlTopList = new ArrayList<>();
        urlList = new ArrayList<>();
        sizeList = new ArrayList<>();
    }

    private void initRefresh() {
        final SwipeRefreshLayout refreshLayout = findViewById(R.id.home_swipeLayout);
        refreshLayout.setColorSchemeResources(R.color.swipeColor);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMore(Type.refresh);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回到顶部（没有动画）
                recyclerView.scrollToPosition(0);
                Toast.makeText(Act_Home.this, "回到顶部", Toast.LENGTH_SHORT).show();
            }
        });
        ImageButton imageButton = findViewById(R.id.home_tb_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Act_User.activityStart(Act_Home.this);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recy_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(newsList, imageViewList);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalItemCount;
            private int firstVisibleItem;
            private int visibleItemCount;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    totalItemCount = layoutManager.getItemCount();
                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                }
                visibleItemCount = recyclerView.getChildCount();
                if (((totalItemCount - visibleItemCount) <= firstVisibleItem && isComplete)) {
                    loadMore(Type.loadMore);
                }
            }
        });
    }

    private void setGreedText() {
        Calendar calendar = Calendar.getInstance();
        final int month = calendar.get(Calendar.MONTH) + 1;
        String Month = "第" + month + "月";
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        String Day = day + "";
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        TextView dayText = findViewById(R.id.home_tb_day);
        TextView monthText = findViewById(R.id.home_tb_month);
        TextView greedText = findViewById(R.id.home_tb_greet);
        dayText.setText(Day);
        monthText.setText(Month);
        if (hour >= 5 && hour <= 9) {
            greedText.setText("早上好鸭！");
        } else if (hour >= 10 && hour <= 17) {
            greedText.setText("干劲十足！");
        } else if (hour >= 18 && hour <= 21) {
            greedText.setText("别太累了！");
        } else {
            greedText.setText("早点睡鸭！");
        }
    }

    public void loadMore(Type t) {
        switch (t) {
            case refresh: {
                preDay = 0;
                isComplete = true;
                netEnqueue("http://news.at.zhihu.com/api/4/news/latest", true);
                break;
            }
            case loadMore: {
                preDay++;
                isComplete = false;
                newsList.add(new NewsBean(getPreDate()));
                netEnqueue("http://news.at.zhihu.com/api/4/news/before/" + getPreDateUrl(), false);
                break;
            }
        }
    }

    private String getPreDateUrl() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -preDay + 1);
        Date d = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(d) + "";
    }

    private String getPreDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -preDay);
        Date d = calendar.getTime();
        DateFormat df = new SimpleDateFormat("MM月dd日");
        return df.format(d) + "";
    }

    //异步请求：
    public void netEnqueue(String url, final boolean bool) {//true-1-clear & false-0-nonClear
        Client.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String result = response.body().string();
                    Message message = Message.obtain();
                    if (bool) message.what = 1;
                    else message.what = 0;
                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            final Bean bean = gson.fromJson(msg.obj.toString(), Bean.class);
            if (msg.what == 1) {
                clearLists();
                addImageViewList(bean);
                addUrlTopList(bean);
            }
            addNewsList(bean);
            urlList.add(addUrlList(bean));
            sizeList.add(bean.getStories().size());
            if (msg.what == 1) {
                initRecyclerView();
            }
            recyclerAdapter.notifyDataSetChanged();
            isComplete = true;
        }
    };

    private void clearLists() {
        newsList.clear();
        imageViewList.clear();
        urlTopList.clear();
        urlList.clear();
        sizeList.clear();
    }

    private void addUrlTopList(Bean bean) {
        for (int i = 0; i < bean.getTop_stories().size(); i++) {
            urlTopList.add(bean.getTop_stories().get(i).getUrl());
        }
    }

    private List<String> addUrlList(Bean bean) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < bean.getStories().size(); i++) {
            list.add(bean.getStories().get(i).getUrl());
        }
        return list;
    }

    private void addImageViewList(Bean bean) {
        String topImage, topTitle, topHint;
        ImageView imageView;
        for (int i = 0; i < bean.getTop_stories().size(); i++) {
            topTitle = bean.getTop_stories().get(i).getTitle();
            topHint = bean.getTop_stories().get(i).getHint();
            imageView = new ImageView(Act_Home.getContext());
            topImage = bean.getTop_stories().get(i).getImage();
            Glide.with(Act_Home.this).load(topImage).into(imageView);
            imageView.setId(bean.getTop_stories().get(i).getId());
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Act_News.activityStart(context, finalI, 0);
                }
            });
            imageViewList.add(new TopNewsBean(imageView, topTitle, topHint));
        }
    }

    private void addNewsList(Bean bean) {
        String title, hint, image;
        for (int i = 0; i < bean.getStories().size(); i++) {
            title = bean.getStories().get(i).getTitle();
            hint = bean.getStories().get(i).getHint();
            image = bean.getStories().get(i).getImages().get(0);
            newsList.add(new NewsBean(title, hint, image));
        }
    }

    public static Context getContext() {
        return context;
    }

    public static List<String> getUrlTopList() {
        return urlTopList;
    }

    public static List<List<String>> getUrlList() {
        return urlList;
    }

    public static List<Integer> getSizeList() {
        return sizeList;
    }
}