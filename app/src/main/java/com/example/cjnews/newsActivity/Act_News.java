package com.example.cjnews.newsActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.example.cjnews.homeActivity.Act_Home;
import com.example.cjnews.R;

import java.util.ArrayList;
import java.util.List;

public class Act_News extends AppCompatActivity {

    private final static int TYPE_NEWS_TOP = 0;
    private final static int TYPE_NEWS_BODY = 1;
    private List<View> webViewList;
    private List<String> urlTopList;
    private List<List<String>> urlList;
    private List<Integer> sizeList;

    public static void activityStart(Context context, int position, int type) {
        Intent intent = new Intent(context, Act_News.class);
        intent.putExtra("position", position);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_news);
        initLists();
        initButtons();
        initWebViewList();//viewPager
    }

    private void initLists() {
        urlTopList = Act_Home.getUrlTopList();
        urlList = Act_Home.getUrlList();
        sizeList = Act_Home.getSizeList();
        webViewList = new ArrayList<>();
    }

    private void initButtons() {
        ImageButton backButton = findViewById(R.id.web_tb_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initWebViewList() {
        Intent intent = getIntent();
        int newPosition = intent.getIntExtra("position", 0);
        WebView webView;
        switch (intent.getIntExtra("type", TYPE_NEWS_BODY)) {
            case TYPE_NEWS_TOP:
                for (int i = 0; i < urlTopList.size(); i++) {
                    webView = new WebView(this);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient());
                    webView.loadUrl(urlTopList.get(i));
                    webViewList.add(webView);
                }
                break;
            case TYPE_NEWS_BODY://body传进来的position代表当前是第几个文章，所以要最后要减去一！！！
                int index = 0;
                for (int i = 0; i < sizeList.size(); i++) {
                    if (newPosition > sizeList.get(i)) {
                        newPosition -= sizeList.get(i);
                        newPosition--;//去掉分割线
                    } else {
                        index = i;
                        break;
                    }
                }
                newPosition--;
                for (int i = 0; i < sizeList.get(index); i++) {
                    webView = new WebView(this);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient());
                    webView.loadUrl(urlList.get(index).get(i));
                    webViewList.add(webView);
                }
                break;
        }
        ViewPager viewPager = findViewById(R.id.web_vp);
        viewPager.setAdapter(new NewsViewPagerAdapter(webViewList));
        viewPager.setCurrentItem(newPosition);
    }

}
