package com.saurabh.discussit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Saurabh on 11/25/2014.
 */
public class ArticlePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> al;
    String[] links;
    String link;

    public ArticlePagerAdapter(FragmentManager fm,ArrayList<String> al, String link) {
        super(fm);
        this.al=al;
        this.link=link;
        links=al.toArray(new String[al.size()]);
       // links[0]=link;
        for(int i=0;i<al.size();i++)
        {
            if(links[i].equals(link))
            {
                String temp= links[0];
                links[0]=links[i];
                links[i]=temp;
            }
        }
    }

    @Override
    public Fragment getItem(int position) {

        return ArticleFragment.newInstance(position,links[position],link);
    }

    @Override
    public int getCount() {
        return links.length;
    }
}
