package com.saurabh.discussit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Saurabh on 11/20/2014.
 */
public class ArticleListFragment extends Fragment {
   private ListView mArticleList;
    ProgressDialog mProgressDialog;
    List<OnlineArticles> articleList= new ArrayList<OnlineArticles>();
   // String passurl1="http://api.usatoday.com/open/articles/topnews/sports?count=10&days=0&page=0&encoding=json&api_key=e6ha83n39qyn38vwmwg65pjp";
    String subject;
    ArrayList<String> titleList= new ArrayList<String>();
    ArrayList<String> links= new ArrayList<String>();
    String toAppend="?count=10&days=0&page=0&encoding=json&api_key=e6ha83n39qyn38vwmwg65pjp";
    StringBuilder passurl=new StringBuilder("http://api.usatoday.com/open/articles/topnews/");
    String topass;
    String articleTitle;
    ArrayAdapter<String> adapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        subject=this.getArguments().getString("subject");
        String subject1=subject;
        passurl.append(subject);
        passurl.append(toAppend);
        topass=passurl.toString();
        View v = inflater.inflate(R.layout.fragment_article_list, parent, false);


        mArticleList=(ListView)v.findViewById(R.id.list_online_articles);
        mArticleList.setClickable(true);
        mArticleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                articleTitle=((TextView)view).getText().toString();

                OnlineArticles a=new OnlineArticles();

                String current=a.getLink(articleTitle,(ArrayList)articleList);
               if(current==null)
                   Toast.makeText(getActivity(),"This is not good",Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(getActivity(),ArticlePagerActivity.class);
                intent1.putExtra("link",current);
                intent1.putStringArrayListExtra("ArticleList",links);
                ArticleList.alist=links;
                startActivity(intent1);

                }
        });
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        loadFromApi();

    }
    private void loadFromApi() {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(topass, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                mProgressDialog = mProgressDialog.show(getActivity(), "Loading...", "Please wait...", false);
            }

            @Override
            public void onFinish() {
                mProgressDialog.dismiss();

            }

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT);

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {

                JSONObject json;
                try {
                    json = new JSONObject(s);

                    parseAndDisplayJson(json);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT);
                }

            }
        });
    }
    class DownloadArticleInfoAsyncTask extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = mProgressDialog.show(getActivity(), "Loading...", "Please wait...", false);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = parser.getJSONFromUrl(strings[0]);

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            mProgressDialog.dismiss();

            if (json != null) {
                parseAndDisplayJson(json);
            } else {
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT);
            }
        }
    }


    private void parseAndDisplayJson(JSONObject json) {
        try {
                JSONArray stories = json.getJSONArray("stories");
                 for(int i=0;i<stories.length();i++)
                     {   if(adapter==null) {
                         JSONObject article = stories.getJSONObject(i);
                         String title = article.getString("title");
                         String link = article.getString("link");
                         OnlineArticles onlineArticles = new OnlineArticles(title, link);
                         articleList.add(onlineArticles);
                         titleList.add(title);
                         links.add(link);
                     }
                     }

            } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1, titleList);
        mArticleList.setAdapter(adapter);
    }
  }





