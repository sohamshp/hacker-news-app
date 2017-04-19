package com.example.soham.hacker_news_app.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.TextView;

import com.example.soham.hacker_news_app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private TextView headerTitle;
    private TextView headerUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScroll);

        headerTitle = (TextView) findViewById(R.id.headerTitle);
        headerUser = (TextView) findViewById(R.id.headerUser);

        //nestedScrollView.setFillViewport(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Article"));
        tabLayout.addTab(tabLayout.newTab().setText("Comments"));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);

        Intent i = getIntent();
        JSONObject json = null;
        String url = "";
        try {
            json = new JSONObject(i.getExtras().getString("data"));
            String username = json.getString("by");
            username = "by - ".concat(username);
            headerUser.setText(username);
            headerTitle.setText(json.getString("title"));
            url = json.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), 2, json);
        viewPager.setAdapter(adapter);
    }
}