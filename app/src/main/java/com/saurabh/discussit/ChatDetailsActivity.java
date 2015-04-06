package com.saurabh.discussit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class ChatDetailsActivity extends Activity {
    Spinner sub_list;
    EditText question_text;
    EditText participants;
    Button start_disc;
    String question;
    int max_users;
    String subject;
    String userName;
    List<String> participant_list;
    List<String> answers;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        sub_list=(Spinner)findViewById(R.id.subject_spinner3);
        question_text=(EditText)findViewById(R.id.question_txt);
        participants=(EditText)findViewById(R.id.room_cap_text);
        start_disc=(Button)findViewById(R.id.strt_cht_btn);
        answers=new ArrayList<String>();
        participant_list=new ArrayList<String>();
        Intent intent1=getIntent();
        userName=intent1.getExtras().getString("UserName");
        participant_list.add(userName);
        message=userName+"Please help me friends. I nee your help";
        answers.add(message);
        start_disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject=sub_list.getSelectedItem().toString();
                question=question_text.getText().toString();
                max_users=Integer.parseInt(participants.getText().toString());
                ChatDetailsActivity.this.CreateChatRoom();
                Intent intent=new Intent(ChatDetailsActivity.this,DiscussionListActivity.class);
                intent.putExtra("UserName", userName);
                startActivity(intent);
            }
        });

    }
    public void CreateChatRoom()
    {
        final ParseObject discussion=new ParseObject("DiscussionString");
        discussion.put("Admin", userName);
        discussion.put("Question",question);
        discussion.put("Capacity", max_users);
        discussion.put("Participants",participant_list);
        discussion.put("Users", 1);
        discussion.put("Answers",answers);
        discussion.saveInBackground();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_details, menu);
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
