package com.example.smart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewsWebViewActivity extends AppCompatActivity {

    private TextView news_title, news_author, news_time, news_content, news_like, news_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        Intent intent = getIntent();

        news_title = findViewById(R.id.news_title);
        news_author = findViewById(R.id.news_author);
        news_time = findViewById(R.id.news_time);
        news_content = findViewById(R.id.news_content);
//        news_like = findViewById(R.id.news_like);
//        news_view = findViewById(R.id.news_view);
        news_title.setText(intent.getStringExtra("title"));
        news_content.setText(intent.getStringExtra("content"));
        news_time.setText(intent.getStringExtra("time"));
        news_author.setText(intent.getStringExtra("author"));
    }
}
