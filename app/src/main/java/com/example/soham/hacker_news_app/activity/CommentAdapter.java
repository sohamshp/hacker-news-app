package com.example.soham.hacker_news_app.activity;

import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.soham.hacker_news_app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Soham on 19-04-2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<JSONObject> objectList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public RelativeLayout relativeLayout;
        public TextView text;
        public TextView user;

        public ViewHolder(LinearLayout ll) {
            super(ll);
            cv = (CardView) ll.findViewById(R.id.comment_card_view);
            relativeLayout = (RelativeLayout) ll.findViewById(R.id.commentRelCard);
            text = (TextView) ll.findViewById(R.id.commentTextCard);
            user = (TextView) ll.findViewById(R.id.commentUserCard);
        }
    }

    public CommentAdapter(List<JSONObject> objList){
        this.objectList = objList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        ViewHolder vh = new ViewHolder(linearLayout);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject obj = objectList.get(position);
        try {
            String html = obj.getString("text");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                html = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
            }
            else {
                html = Html.fromHtml(html).toString();
            }
            holder.text.setText(html);
            holder.user.setText(obj.getString("by"));
        } catch (Exception e) {
            //e.printStackTrace();
            holder.text.setText("...");
            holder.user.setText("...");
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }


}
