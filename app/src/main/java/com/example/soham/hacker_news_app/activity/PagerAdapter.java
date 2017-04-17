package com.example.soham.hacker_news_app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.soham.hacker_news_app.fragment.ArticleFragment;
import com.example.soham.hacker_news_app.fragment.CommentFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soham on 15-04-2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;
    String url;
    JSONObject object;

    public PagerAdapter(FragmentManager fm, int tabCount, JSONObject json) {
        super(fm);
        this.tabCount = tabCount;
        this.object = json;
        try {
            this.url = json.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                ArticleFragment tab1 = new ArticleFragment();
                Bundle b = new Bundle();
                b.putString("url", url);
                tab1.setArguments(b);
                return tab1;
            case 1:
                CommentFragment tab2 = new CommentFragment();
                Bundle b2 = new Bundle();
                try {
                    JSONArray objs2 = object.getJSONArray("kids");
                    ArrayList<String> objectIds = new ArrayList<String>();
                    for (int i=0 ; i<objs2.length() ; i++) {
                        String id = objs2.get(i).toString();
                        objectIds.add(id);
                    }

                    b2.putStringArrayList("objectIds", objectIds);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tab2.setArguments(b2);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Article";
            case 1:
                return "Comments";
        }
        return null;
    }
}
