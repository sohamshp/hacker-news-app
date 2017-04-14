package com.example.soham.hacker_news_app.fragment;

import android.view.View;

/**
 * Created by Soham on 14-04-2017.
 */

public interface ClickListener {
    public void onClick(View view, int position);
    public void onLongClick(View view, int position);
}
