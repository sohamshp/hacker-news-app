package com.example.soham.hacker_news_app.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.soham.hacker_news_app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    WebView wv;

    private String url;
    private String text;
    private JSONObject object;

    public ArticleFragment() {
        // Required empty public constructor
        this.url = "";
    }


    // TODO: Rename and change types and number of parameters
    public static ArticleFragment newInstance(String param1, String param2) {
        ArticleFragment fragment = new ArticleFragment();
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
        View v = inflater.inflate(R.layout.fragment_article, container, false);

        WebView wv = (WebView) v.findViewById(R.id.webView);

        url = this.getArguments().getString("url");
        text = this.getArguments().getString("text");
        try {
            object = new JSONObject(this.getArguments().getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
        System.out.println(url);
        System.out.println(text);*/

        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setBuiltInZoomControls(true);
        ws.setAllowFileAccess(true);
        ws.setAllowContentAccess(true);
        ws.setAllowUniversalAccessFromFileURLs(true);
        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());

        if (url == null || url == ""){
            if (text == null || text == "") {
                String title = "";
                try {
                    title = object.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                wv.loadData(title, "text/html", "utf-8");
            } else {
                wv.loadData(text, "text/html", "utf-8");
            }
        } else {
            wv.loadUrl(url);
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
}
