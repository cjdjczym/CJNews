package com.example.cjnews.userActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.cjnews.R;

public class Act_User extends AppCompatActivity {

    private static boolean isLight=true;

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, Act_User.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user);
        initButtons();
    }

    private void initButtons() {
        ImageButton imageButton = findViewById(R.id.user_tb_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final ImageButton themeButton = findViewById(R.id.user_theme);
        themeButton.setBackgroundResource(R.drawable.daynight);
        themeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLight) {
                    isLight = false;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    isLight = true;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }
}
