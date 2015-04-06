package com.saurabh.discussit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class ArticlePagerActivity extends FragmentActivity {
    ViewPager viewPager;
    ArticlePagerAdapter adapter;
    ArrayList<String> links;
    String link;
    Fragment fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_pager);
        Intent intent=getIntent();
        links=intent.getStringArrayListExtra("ArticleList");
        link=intent.getStringExtra("link");
        viewPager=(ViewPager)findViewById(R.id.pager);
        adapter=new ArticlePagerAdapter(getSupportFragmentManager(),links,link);
        viewPager.setAdapter(adapter);

        Bundle bundle=new Bundle();
        bundle.putString("currentLink",link);
        bundle.putStringArrayList("ArticleList",links);
        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
     //   adapter=new ArticlePagerAdapter(fm);
//        viewPager.setAdapter(adapter);
        android.support.v4.app.Fragment fragment=fm.findFragmentById(R.id.pager);
        if (fragment == null) {
            fragment = new ArticleFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.pager, fragment)
                    .commit();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article_pager, menu);
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
