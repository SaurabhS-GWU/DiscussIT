package com.saurabh.discussit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class PublishActivity extends Activity {
    private Spinner article_sub;
    private    EditText write_article;
    private  EditText article_title;
    private ArrayAdapter<CharSequence> m_adapterForSpinner;
    private Button publish_btn;
    String article_text;
    String subject;
    String title;
    String author;
    String currentUser;
    Boolean published;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        Intent intent = getIntent();
        currentUser=intent.getExtras().getString("UserName");
        article_sub = (Spinner)findViewById(R.id.subject_spinner);
        write_article = (EditText)findViewById(R.id.article_write);
        article_title=(EditText)findViewById(R.id.title_field);
        publish_btn= (Button) findViewById(R.id.pub_work);
        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               published=true;
                SaveInDatabase(published);
                Intent intent1=new Intent(PublishActivity.this,PublishedActivity.class);
                intent1.putExtra("UserName",currentUser);
                startActivity(intent1);
            }
        });

    }
    public void saveArticle(View view)
    {
        published=false;
        this.SaveInDatabase(published);
        Intent  i =new Intent(this, MyArticlesActivity.class);
        i.putExtra("UserName",currentUser);
        startActivity(i);
    }
    public void SaveInDatabase(Boolean published)
    {
        subject=article_sub.getSelectedItem().toString();
        article_text=write_article.getText().toString();
        title=article_title.getText().toString();
        byte[] data=article_text.getBytes();
        ParseFile file=new ParseFile("article.txt",data);
        final ParseObject article_ob=new ParseObject("article");
        article_ob.put("Author",currentUser);
        article_ob.put("Subject",subject);
        article_ob.put("Article",file);
        article_ob.put("Title", title);
        article_ob.put("Published",published);
        article_ob.saveInBackground();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.publish, menu);
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
