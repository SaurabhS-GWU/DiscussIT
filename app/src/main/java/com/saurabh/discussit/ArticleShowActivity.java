package com.saurabh.discussit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;


public class ArticleShowActivity extends FragmentActivity {
    String subname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_show);
        Intent intent1=getIntent();
        subname =  intent1.getExtras().getString("subject");
        Bundle bundle = new Bundle();
        bundle.putString("subject",subname);
// set Fragmentclass Arguments
        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
        android.support.v4.app.Fragment fragment=fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new ArticleListFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
           }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article_show, menu);
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
