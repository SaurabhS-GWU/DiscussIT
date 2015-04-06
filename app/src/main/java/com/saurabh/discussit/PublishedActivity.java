package com.saurabh.discussit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class PublishedActivity extends Activity {
    private ListView pub_listView;
    private String appUser;
    private List<String> title_list;
    String articleTitle;
    ArrayAdapter<String> adapter;
    Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_published);
        pub_listView=(ListView)findViewById(R.id.published_articles);
        title_list=new ArrayList<String>();
        Intent intent=getIntent();
        appUser=intent.getExtras().getString("UserName");
        intent1=new Intent(this,UserArticleActivity.class);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("article");
        query.whereEqualTo("Published", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {

                    for (ParseObject a : objects) {
                        String title = a.getString("Title");
                        if (!appUser.equals("common")) {
                            if (appUser.equals(a.getString("Author")))
                                title_list.add(title);
                        }
                        else title_list.add(title);
                    }
                    adapter = new ArrayAdapter<String>(PublishedActivity.this, android.R.layout.simple_list_item_1, title_list);
                    pub_listView.setAdapter(adapter);
                } else {
                    //   objectRetrievalFailed();
                    Context context = getApplicationContext();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            }
        });
        pub_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                articleTitle = ((TextView) view).getText().toString();
                intent1.putExtra("title", articleTitle);
                intent1.putExtra("userName", appUser);
                startActivity(intent1);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.published, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
