package com.saurabh.discussit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class EditSubjectsActivity extends Activity {
    ListView msubjectSelect;
    String subjectChoices[]={"Sports","travel","money", "weather", "tech","books", "people", "nation", "religion","world"};
    String userName;
    ArrayList<String> subjects;
    ArrayAdapter<String> adapter;
    String aroma="aroma";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subjects);
        Intent intent=getIntent();
        userName=intent.getExtras().getString("UserName");
        msubjectSelect=(ListView)findViewById(R.id.subject_select_list);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, subjectChoices);
        msubjectSelect.setChoiceMode(msubjectSelect.CHOICE_MODE_MULTIPLE);
        msubjectSelect.setAdapter(adapter);

    }
    public void subjectSubmit(View view)
    {
        SparseBooleanArray checked = msubjectSelect.getCheckedItemPositions();
        subjects = new ArrayList<String>();
        for(int i = 0; i <= 9; i++){
            if(checked.get(i))
                subjects.add(subjectChoices[i]);
        }
        if(subjects.size()!=0) {
            ParseQuery query = new ParseQuery("subjectlist");
            query.whereEqualTo("user", userName);
            query.getFirstInBackground(new GetCallback() {
                public void done(ParseObject object, ParseException e) {
                    if (object == null) {
                        final ParseObject mysubjects = new ParseObject("subjectlist");
                        mysubjects.put("user", userName);
                        mysubjects.put("subjectlist", subjects);
                        mysubjects.put("roman", 1);
                        mysubjects.saveInBackground();
                    } else {
                        try {
                            object.delete();
                            final ParseObject mysubjects = new ParseObject("subjectlist");
                            mysubjects.put("user", userName);
                            mysubjects.put("subjectlist", subjects);
                            mysubjects.put("roman", 1);
                            mysubjects.saveInBackground();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
        }
        if(subjects.size()==0) {
            Toast.makeText(getApplicationContext(), "Please select atleast one subject", Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(this, MySubjectsActivity.class);
            intent.putExtra("UserName", userName);
            intent.putStringArrayListExtra("SubjectList", subjects);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_subjects, menu);
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
