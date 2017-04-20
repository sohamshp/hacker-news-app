package com.example.soham.hacker_news_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.soham.hacker_news_app.R;
import com.example.soham.hacker_news_app.activity.ArticleActivity;
import com.example.soham.hacker_news_app.activity.CustomAdapter;
import com.example.soham.hacker_news_app.activity.SavedStoryDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SavedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;

    private SavedStoryDB savedStoryDB;

    List<String> jsonStringList;
    List<JSONObject> jsonObjectList;

    public SavedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SavedFragment newInstance(String param1, String param2) {
        SavedFragment fragment = new SavedFragment();
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
        View v = inflater.inflate(R.layout.fragment_saved, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.savedRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        savedStoryDB = new SavedStoryDB(getContext());

        jsonObjectList = new ArrayList<JSONObject>();

        savedStoryDB.open();
        jsonStringList = savedStoryDB.getAllEntries();
        savedStoryDB.close();


        for (int i=0 ; i<jsonStringList.size() ; i++) {
            JSONObject object = null;
            try {
                object = new JSONObject(jsonStringList.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (object != null){
                jsonObjectList.add(object);
            }
        }

        adapter = new CustomAdapter(jsonObjectList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(), "click at "+position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), ArticleActivity.class);
                i.putExtra("data", jsonObjectList.get(position).toString());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Toast.makeText(getContext(), "long click at "+position, Toast.LENGTH_SHORT).show();

                SavedStoryDB savedStoryDB = new SavedStoryDB(getContext());
                savedStoryDB.open();
                //savedStoryDB.removeEntry(position+1);
                savedStoryDB.removeEntry(jsonObjectList.get(position).toString());
                savedStoryDB.close();

                Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();

                onResume();
            }
        }));


        return v;
    }

    public void onResume(){
        super.onResume();
        savedStoryDB = new SavedStoryDB(getContext());

        jsonObjectList = new ArrayList<JSONObject>();

        savedStoryDB.open();
        jsonStringList = savedStoryDB.getAllEntries();
        savedStoryDB.close();


        for (int i=0 ; i<jsonStringList.size() ; i++) {
            JSONObject object = null;
            try {
                object = new JSONObject(jsonStringList.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (object != null){
                jsonObjectList.add(object);
            }
        }

        adapter = new CustomAdapter(jsonObjectList);
        recyclerView.setAdapter(adapter);
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
