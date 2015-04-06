package com.saurabh.discussit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DiscussionListActivity extends Activity {
    List<String> al;
    private ListView disc_list;
    String username;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;
    Button new_disc;
    String question;
    Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_list);
        disc_list=(ListView)findViewById(R.id.avail_disc_list);
        new_disc=(Button)findViewById(R.id.new_disc_btn);
        final Intent intent=getIntent();
        username=intent.getExtras().getString("UserName");
        intent2=new Intent(this,DiscussionActivity.class);
        al=new ArrayList<String>();
       new DownloadArticles().execute();
        new_disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(DiscussionListActivity.this,ChatDetailsActivity.class);
                intent1.putExtra("UserName",username);
                startActivity(intent1);
            }
        });

        disc_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                question = ((TextView) view).getText().toString();
                intent2.putExtra("Question", question);
                intent2.putExtra("UserName", username);
                startActivity(intent2);
            }
        });

    }
    public void populateList()
    {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("DiscussionString");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    for(ParseObject a: objects) {
                        String question=a.getString("Question");
                        int users=a.getInt("Users");
                        int capacity=a.getInt("Capacity");
                        if(users<capacity)
                            al.add(question);
                    }
                    adapter=new ArrayAdapter<String>(DiscussionListActivity.this,android.R.layout.simple_list_item_1,al);
                    disc_list.setAdapter(adapter);
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

    }
    class DownloadArticles extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = mProgressDialog.show(DiscussionListActivity.this, "Loading...", "Please wait...", false);
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
        getMenuInflater().inflate(R.menu.discussion_list, menu);
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


