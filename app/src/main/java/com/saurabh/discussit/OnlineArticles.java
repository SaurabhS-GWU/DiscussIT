package com.saurabh.discussit;

import java.util.ArrayList;

/**
 * Created by Saurabh on 11/25/2014.
 */
public class OnlineArticles {
    public String title;
    public String link;

    public OnlineArticles()
    {
        title="Nothing to Display";
        link="Nothing to display";
    }
    public OnlineArticles(String title, String link)
    {
        this.title=title;
        this.link=link;
    }
    public String getTitle()
    {
        return title;
    }
    public String getLink(String title, ArrayList<OnlineArticles> ol)
    {
        for(OnlineArticles a: ol) {
            String current=a.getTitle();
            if(current.equals(title))
            return a.link;
        }
        return null;
    }

}
