package com.saurabh.discussit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Saurabh on 11/25/2014.
 */

public class ArticleFragment extends Fragment {
    String currentLink;
    ArrayList<String> links=new ArrayList<String>();
    WebView webView;
    String url;
    static  int count=0;

    public ArticleFragment()
    {

    }
    public static ArticleFragment newInstance(int position, String url,String currentLink)
    {
        ArticleFragment fragment=new ArticleFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("urlposition",position);
        bundle.putString("url",url);
        bundle.putString("currentLink",currentLink);
        fragment.setArguments(bundle);
        return  fragment;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article, parent, false);
       // Toast.makeText(getActivity(),"Swipe left or right to read more articles",Toast.LENGTH_SHORT);
        currentLink=this.getArguments().getString("currentLink");
        url=getArguments().getString("url");
        webView=(WebView)v.findViewById(R.id.online_article);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        if(count==0) {
            Toast.makeText(getActivity(), "Swipe LEFT or RIGHT to read more articles", Toast.LENGTH_SHORT).show();
            count++;
        }
        return v;

    }
}


