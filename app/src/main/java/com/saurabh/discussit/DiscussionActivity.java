package com.saurabh.discussit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.internal.ImageDownloader;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class DiscussionActivity extends Activity {
    ListView listView;
    List<String> responses;
    String question;
    String username;
    EditText msg_txt;
    TextView title;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;
    ImageButton send;
    String newMsg;
    ParseObject current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        listView=(ListView)findViewById(R.id.dis_string);
        title=(TextView)findViewById(R.id.topic);
        responses=new ArrayList<String>();
        msg_txt=(EditText)findViewById(R.id.messageInput);
        Intent intent=getIntent();
        question=intent.getExtras().getString("Question");
        title.setText(question);
        username=intent.getExtras().getString("UserName");
        send=(ImageButton)findViewById(R.id.sendButton);
        new GetDiscussion().execute();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg();
            }
        });

    }
    public void populateList()
    {
        ParseQuery query=new ParseQuery("DiscussionString");
        query.whereEqualTo("Question",question);
        query.getFirstInBackground(new GetCallback() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(parseObject!=null) {
                    current=parseObject;
                    responses = (ArrayList<String>) parseObject.get("Answers");

                                    }
                else
                {
                    Toast.makeText(getApplicationContext(),"Sorry. But no Discussion available currently",Toast.LENGTH_SHORT).show();
                }
                adapter=new ArrayAdapter<String>(DiscussionActivity.this,android.R.layout.simple_list_item_1,responses);
                listView.setAdapter(adapter);

            }
        });
    }
    public void sendMsg()
    {
        newMsg=msg_txt.getText().toString();
        newMsg=username+":" +newMsg;
        responses.add(newMsg);
        current.put("Answers",responses);
        current.saveInBackground();
        adapter=new ArrayAdapter<String>(DiscussionActivity.this,android.R.layout.simple_list_item_1,responses);
        listView.setAdapter(adapter);
        msg_txt.setText("");
    }
    class GetDiscussion extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = mProgressDialog.show(DiscussionActivity.this, "Loading...", "Please wait...", false);
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
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent=new Intent(this,MainMenuActivity.class);
            startActivity(intent);
        }

        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.discussion, menu);
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
            Intent intent=new Intent(this,DiscussionActivity.class);
            intent.putExtra("UserName", username);
            intent.putExtra("Question", question);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
