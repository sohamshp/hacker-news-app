package com.example.soham.hacker_news_app.activity;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.soham.hacker_news_app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Soham on 10-04-2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<JSONObject> objList;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public RelativeLayout relativeLayout;
        public TextView title;
        public TextView user;
        public TextView score;

        public ViewHolder(LinearLayout ll) {
            super(ll);
            cv = (CardView) ll.findViewById(R.id.card_view);
            relativeLayout = (RelativeLayout) ll.findViewById(R.id.relCard);
            title = (TextView) ll.findViewById(R.id.titleCard);
            user = (TextView) ll.findViewById(R.id.userCard);
            score = (TextView) ll.findViewById(R.id.scoreCard);
        }

    }

    public CustomAdapter(List<JSONObject> objList) {
        this.objList = objList;

    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_text_view, parent, false);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.card_item1, parent, false);
        ViewHolder vh = new ViewHolder(linearLayout);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject obj = objList.get(position);
        try {
            holder.title.setText(obj.getString("title"));
            holder.user.setText(obj.getString("by"));
            holder.score.setText(obj.getString("score"));
        } catch (JSONException e) {
            //e.printStackTrace();
            holder.title.setText("...");
            holder.user.setText("...");
            holder.score.setText("...");
        }
    }

    public int getItemCount() {
        return objList.size();
    }
}
