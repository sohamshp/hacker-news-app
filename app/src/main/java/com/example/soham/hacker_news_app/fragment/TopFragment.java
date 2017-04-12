package com.example.soham.hacker_news_app.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.soham.hacker_news_app.R;
import com.example.soham.hacker_news_app.activity.CustomAdapter;
import com.example.soham.hacker_news_app.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TopFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<JSONObject> objs;
    JSONArray jarr;

    JSONObject template ;

    static int startPos = 0;
    static int endPos = 20;

    static String baseUrl = "https://hacker-news.firebaseio.com/v0/item/";
    static String endUrl = ".json";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public TopFragment() {
        // Required empty public constructor
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
    public static TopFragment newInstance(String param1, String param2) {
        TopFragment fragment = new TopFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.topRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        objs = new ArrayList<JSONObject>();

        for (int i=0 ; i<25 ; i++) {
            objs.add(template);
        }

        adapter = new CustomAdapter(objs);
        recyclerView.setAdapter(adapter);

        try {
            new getTopStories(getActivity(), jarr).execute("https://hacker-news.firebaseio.com/v0/topstories.json");
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

    @Override
    public void onResume() {
        super.onResume();
    }

    class getTopStories extends AsyncTask<String, Void, JSONArray> {

        Activity context;
        JSONArray result;
        //AsyncHandler asyncHandler = null;

        public getTopStories(){}

        public getTopStories(Activity context, JSONArray result) {
            this.context = context;
            this.result = result;
            //this.asyncHandler = (AsyncHandler) TopFragment.this;
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
                System.out.println(sb.toString());
                result = new JSONArray(sb.toString());
                in.close();
                return result;
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
            //super.onPostExecute(jsonArray);
            String baseUrl = "https://hacker-news.firebaseio.com/v0/item/";
            String endUrl = ".json";

            //adapter = new CustomAdapter(objs);
            //recyclerView.setAdapter(adapter);

            for (int i=startPos ; i<endPos ; i++) {
                try {
                    new getTopStory(getActivity(), i).execute(baseUrl +jsonArray.get(i).toString() + endUrl);
                    System.out.println(jsonArray.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            adapter = new CustomAdapter(objs);
            recyclerView.setAdapter(adapter);
        }
    }

    class getTopStory extends AsyncTask<String, Void, JSONObject> {

        Activity context;
        JSONObject obj;
        int insertIndex;

        public getTopStory(){}

        public getTopStory(Activity context, int insertIndex) {
            this.context = context;
            this.insertIndex = insertIndex;
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
                System.out.println(sb);
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
            //objs.add(this.insertIndex, jsonObject);
            objs.set(this.insertIndex, jsonObject);
            adapter = new CustomAdapter(objs);
            recyclerView.setAdapter(adapter);

            //RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.relLay);

            //adapter.bindViewHolder(new CustomAdapter.ViewHolder(r), this.insertIndex);

        }
    }
}
