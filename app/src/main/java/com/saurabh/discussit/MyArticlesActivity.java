package com.saurabh.discussit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MyArticlesActivity extends Activity {
    private ListView lv;
    private List<String> sub_list=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String articleTitle;
    String appUser;
    Intent intent1;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_articles);
        Intent intent=getIntent();
        appUser=intent.getExtras().getString("UserName");
        intent1=new Intent(MyArticlesActivity.this,UserArticleActivity.class);
        setContentView(R.layout.activity_my_articles);
        lv=(ListView)findViewById(R.id.my_articles);
        new LoadList().execute();
       // Parse.initialize(this, "pmbTlBtcCrRIkjfspWmQ0zHigYGYonNmoQwlEt7v", "nv357LaURfA8h8VMjsk224Y8aMmBAKjx2WWCkuFy");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                articleTitle = ((TextView) view).getText().toString();
                intent1.putExtra("title", articleTitle);
                intent1.putExtra("userName",appUser);
                startActivity(intent1);
            }
        });


    }
    public void populateList()
    {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("article");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {

                    for(ParseObject a: objects) {
                        String title=a.getString("Title");
                        if(appUser.equals(a.getString("Author")))
                            sub_list.add(title);
                    }
                    adapter=new ArrayAdapter<String>(MyArticlesActivity.this,android.R.layout.simple_list_item_1,sub_list);
                    lv.setAdapter(adapter);
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
                System.out.print(sub_list);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent=new Intent(this,MainMenuActivity.class);
            startActivity(intent);
        }
        return true;
    }
    class LoadList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = mProgressDialog.show(MyArticlesActivity.this, "Loading...", "Please wait...", false);
        }

        @Override
        protected String doInBackground(String... strings) {
            populateList();
            return  "sucess";
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_articles, menu);
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
        if (id==R.id.action_refresh)
        {
           Intent intent=new Intent(this,MyArticlesActivity.class);
            intent.putExtra("UserName", appUser);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
