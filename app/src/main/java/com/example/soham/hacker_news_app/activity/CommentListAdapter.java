package com.example.soham.hacker_news_app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.soham.hacker_news_app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Soham on 16-04-2017.
 */

public class CommentListAdapter extends BaseAdapter {

    int itemCount;
    List<JSONObject> jsonObjects;
    Context context;
    LayoutInflater inflater;

    public CommentListAdapter(Context context, List<JSONObject> list) {
        this.context = context;
        this.jsonObjects = list;
        this.itemCount = jsonObjects.size();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return jsonObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;

        row = inflater.inflate(R.layout.comment_item, null);

        holder.commentText = (TextView) row.findViewById(R.id.commentTextView);
        holder.commentUser = (TextView) row.findViewById(R.id.commentUserTextView);

        JSONObject object = jsonObjects.get(position);

        try {
            //if (object.getString("type").equals("comment")) {
            String user = object.getString("by");
            String text = object.getString("text");
                holder.commentUser.setText(user);
                holder.commentText.setText(text);
            //}
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return row;
    }

    class Holder {
        TextView commentText;
        TextView commentUser;
    }
}
