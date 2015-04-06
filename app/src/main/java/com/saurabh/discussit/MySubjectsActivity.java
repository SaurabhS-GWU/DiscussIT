package com.saurabh.discussit;

import android.app.Activity;
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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MySubjectsActivity extends Activity {
    ListView mySubjects;
    String userName;
    private String subject;
    private ArrayList<String> subjectlist;
    ArrayList<String> subject_list;
    Intent intent1;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subjects);
        mySubjects=(ListView)findViewById(R.id.subject_list);
        intent1 = new Intent(this,ArticleShowActivity.class);
        final Intent intent=getIntent();
        userName=intent.getExtras().getString("UserName");
//        subjectlist=intent.getExtras().getStringArrayList("SubjectList");
        ParseQuery query = new ParseQuery("subjectlist");
        query.whereEqualTo("user", userName);
        query.getFirstInBackground(new GetCallback() {
            public void done(ParseObject object, ParseException e) {
                ArrayList<String> arrayList;
                if (object == null) {
                    Intent intent1 = new Intent(getApplicationContext(), EditSubjectsActivity.class);
                    startActivity(intent1);
                } else {

                    if (subjectlist==null) {
                        subject_list =  (ArrayList) object.get("subjectlist");
                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, subject_list);
                        mySubjects.setAdapter(adapter);
                    } else {

                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, subjectlist);
                        mySubjects.setAdapter(adapter);
                    }
                }

            }
        });
        Toast.makeText(getApplicationContext(),"Select the Subject which you would like to read",Toast.LENGTH_SHORT).show();

        mySubjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 subject=((TextView)view).getText().toString();
                 intent1.putExtra("subject", subject);
                startActivity(intent1);
                     }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_subjects, menu);
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
