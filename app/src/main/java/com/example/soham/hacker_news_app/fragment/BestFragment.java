package com.example.soham.hacker_news_app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soham.hacker_news_app.R;
import com.example.soham.hacker_news_app.activity.ArticleActivity;
import com.example.soham.hacker_news_app.activity.CustomAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class BestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    JSONArray jarr;
    List<JSONObject> objs;

    static int startPos = 0;
    static int endPos = 20;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    JSONObject template;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public BestFragment() {
        try {
            template = new JSONObject();
            template.put("id", -1);
            template.put("deleted", "...");
            template.put("type", "...");
            template.put("by", "...");
            template.put("time", -1);
            template.put("text", "...");
            template.put("dead", "...");
            template.put("parent", -1);
            template.put("kids", new int[]{-1});
            template.put("url", "...");
            template.put("score", -1);
            template.put("title", "...");
            template.put("parts", new int[]{-1});
            template.put("descendants", -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Rename and change types and number of parameters
    public static BestFragment newInstance(String param1, String param2) {
        BestFragment fragment = new BestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_best, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.bestRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        objs = new ArrayList<JSONObject>();

        adapter = new CustomAdapter(objs);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(), "click at "+position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), ArticleActivity.class);
                i.putExtra("data", objs.get(position).toString());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "long click at "+position, Toast.LENGTH_SHORT).show();
            }
        }));

        try {
            new getBestStories(getActivity(), jarr).execute("https://hacker-news.firebaseio.com/v0/beststories.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    class getBestStories extends AsyncTask<String, Void, JSONArray> {

        Activity context;
        JSONArray result;

        public getBestStories(){}

        public getBestStories(Activity context, JSONArray result) {
            this.context = context;
            this.result = result;
        }

        @Override
        protected JSONArray doInBackground(String... urls) {
            HttpURLConnection connection = null;
            try {
                //URL url = new URL("https://hacker-news.firebaseio.com/v0/topstories.json");
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                result = new JSONArray(sb.toString());
                in.close();
            } catch(Exception e) {
                e.printStackTrace();
                connection.disconnect();
            } finally {
                connection.disconnect();
            }

            return result;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            String baseUrl = "https://hacker-news.firebaseio.com/v0/item/";
            String endUrl = ".json";

            for (int i=startPos ; i<endPos ; i++) {
                try {
                    new getBestStory(getActivity(), i).execute(baseUrl +jsonArray.get(i).toString() + endUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    class getBestStory extends AsyncTask<String, Void, JSONObject> {

        Activity context;
        JSONObject obj;
        int index;

        public getBestStory(){}

        public getBestStory(Activity context, int index) {
            this.context = context;
            this.index = index;
        }

        protected JSONObject doInBackground(String... urls) {
            //https://hacker-news.firebaseio.com/v0/item/<<8863>>.json?print=pretty
            HttpURLConnection connection = null;
            try {
                //URL url = new URL("https://hacker-news.firebaseio.com/v0/topstories.json");
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                obj = new JSONObject(sb.toString());
                in.close();
            } catch(Exception e) {
                e.printStackTrace();
                connection.disconnect();
            } finally {
                connection.disconnect();
            }

            return obj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            objs.add(jsonObject);
            CustomAdapter ca = (CustomAdapter) recyclerView.getAdapter();
            ca.notifyDataSetChanged();
        }
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clickListener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView
                , final ClickListener clickListener) {

            this.clickListener = clickListener;
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if ( child != null && clickListener != null ) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if ( child != null && clickListener != null && gestureDetector.onTouchEvent(e) ) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
