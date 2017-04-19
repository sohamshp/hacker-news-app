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
import android.widget.ListView;

import com.example.soham.hacker_news_app.R;
import com.example.soham.hacker_news_app.activity.CommentAdapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarOutputStream;

public class CommentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<JSONObject> objectList;
    private List<String> objectIds;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public CommentFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
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
        View v = inflater.inflate(R.layout.fragment_comment, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.commentRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        objectList = new ArrayList<JSONObject>();

        adapter = new CommentAdapter(objectList);
        recyclerView.setAdapter(adapter);

        Bundle b = getArguments();
        objectIds = b.getStringArrayList("objectIds");

        for (int i=0 ; i<objectIds.size()-1 ; i++) {
            try {
                new GetCommentTask(getActivity(), i).execute("https://hacker-news.firebaseio.com/v0/item/"+objectIds.get(i)+".json");
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    class GetCommentTask extends AsyncTask<String, Void, JSONObject> {

        Activity context;
        int index;
        JSONObject obj;
        List<JSONObject> objList = objectList;

        public GetCommentTask(Activity context, int index) {
            this.context = context;
            this.index = index;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject obj = new JSONObject(sb.toString());
                System.out.println(obj.getString("text"));
                in.close();
                return obj;
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
            objList.add(jsonObject);
            CommentAdapter ca = (CommentAdapter) recyclerView.getAdapter();
            ca.notifyDataSetChanged();
        }
    }
}
