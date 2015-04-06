package com.saurabh.discussit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.widget.FacebookDialog;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class UserArticleActivity extends Activity {
    TextView myArticle;
    String userName;
    String title;
    String text;
    TextView titleText;
    private Button pub;
    private Button del;
    private Button share;
    ParseObject currArticle;
    ParseFacebookUtils pf;
    Facebook fb;
    String caption="Please check this app. I just wrote an article here named: ";
    String description="DiscussIT is a great lightweight app for knowledge seekers, where you can Read, Discuss and Share your ideas";
    private UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_article);
        myArticle=(TextView)findViewById(R.id.articleText);
        final Intent intent=getIntent();
        userName=intent.getExtras().getString("userName");
        title=intent.getExtras().getString("title");
        caption=caption+title;
        myArticle.setMovementMethod(new ScrollingMovementMethod());
        share=(Button)findViewById(R.id.share_article);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser) && !userName.equals("common")) {
           share.setVisibility(Button.VISIBLE);
        }
        else
        share.setVisibility(share.GONE);

        titleText=(TextView)findViewById(R.id.title_text);
        del= (Button) findViewById(R.id.del_saved_btn);
        pub=(Button)findViewById(R.id.pub_saved_btn);

        ParseQuery query = new ParseQuery("article");
        if(userName.equals("common")) {
         pub.setVisibility(pub.GONE);
         del.setVisibility(del.GONE);

        }
        else {
            query.whereEqualTo("Author", userName);
        }
        query.whereEqualTo( "Title", title);
        query.getFirstInBackground(new GetCallback() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Toast.makeText(getApplicationContext(),"this  article no longer exists",Toast.LENGTH_SHORT).show();
                } else {
                    currArticle=object;
                    ParseFile pf=object.getParseFile("Article");
                    Boolean s=(Boolean)object.get("Published");
                    if(s){pub.setVisibility(pub.GONE); }
                    try {
                        byte[]a=  pf.getData();
                        text=new String(a);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    myArticle.setText(text);
                    titleText.setText(title);

                    }

            }
        });
        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currArticle.put("Published",true);
                currArticle.saveInBackground();
                Toast.makeText(getApplicationContext(),"Your Article Has Now been published",Toast.LENGTH_LONG).show();
                Intent intent1=new Intent(getApplicationContext(),PublishedActivity.class);
                intent1.putExtra("UserName",userName);
                startActivity(intent1);
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    currArticle.delete();
                    Intent intent1=new Intent(getApplicationContext(),MyArticlesActivity.class);
                    intent1.putExtra("UserName",userName);
                    startActivity(intent1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });
    }
    public void share(View view)
    {
        FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                .setLink("https://developers.facebook.com/android")
                .setCaption(caption)
                .setName("DiscussIT")
                .setDescription(description)
                .build();
        uiHelper.trackPendingDialogCall(shareDialog.present());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_article, menu);
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
    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
}
