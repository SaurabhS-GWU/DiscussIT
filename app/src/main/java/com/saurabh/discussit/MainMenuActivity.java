package com.saurabh.discussit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MainMenuActivity extends Activity {
    private Button read_online;
    private Button sub_select;
    private Button discuss;
    private Button publish_button;
    private Button mlogout;
    String userName;
    private static final String TAG="discussit";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        read_online=(Button)findViewById(R.id.Read_Button);
        sub_select=(Button)findViewById(R.id.Subject_Select);
        discuss=(Button)findViewById(R.id.Discuss);
        publish_button=(Button)findViewById(R.id.Publish_Article);
        mlogout=(Button)findViewById(R.id.logoutfrmmain);
        ParseUser currentuser=ParseUser.getCurrentUser();
        userName=currentuser.getUsername().toString();

    }
    public void editSubjectList(View view)
    {
        Intent intent=new Intent(this,EditSubjectsActivity.class);
        intent.putExtra("UserName",userName);
        startActivity(intent);
    }
    public void showArticles(View view)
    {
        Intent myArticles=new Intent(this,MyArticlesActivity.class);
        myArticles.putExtra("UserName",userName);
        startActivity(myArticles);

    }
    public void showUserArticles(View view)
    {
        Intent userArticles=new Intent(this,PublishedActivity.class);
        userArticles.putExtra("UserName", "common");
        startActivity(userArticles);
    }
    public void gotoArticleShow(View view)
    {
        ParseQuery query = new ParseQuery("subjectlist");
        query.whereEqualTo("user", userName);
        query.getFirstInBackground(new GetCallback() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Intent intent1 = new Intent(getApplicationContext(), EditSubjectsActivity.class);
                    intent1.putExtra("UserName",userName);
                    startActivity(intent1);
                } else {
                    Intent intent=new Intent(getApplicationContext(),MySubjectsActivity.class);
                    intent.putExtra("UserName",userName);
                    startActivity(intent);
                }

            }
        });

    }


    private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            // Create a JSON object to hold the profile info
                            JSONObject userProfile = new JSONObject();
                            try {
                                // Populate the JSON object
                                userProfile.put("facebookId", user.getId());
                                userProfile.put("name", user.getName());
                                if (user.getProperty("gender") != null) {
                                    userProfile.put("gender", (String) user.getProperty("gender"));
                                }
                                if (user.getProperty("email") != null) {
                                    userProfile.put("email", (String) user.getProperty("email"));
                                }

                                // Save the user profile info in a user property
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.put("profile", userProfile);
                                currentUser.saveInBackground();

                                // Show the user info
                             //   updateViewsWithProfileInfo();
                            } catch (JSONException e) {
                                Log.d(TAG, "Error parsing returned user data. " + e);
                            }

                        } else if (response.getError() != null) {
                            if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY) ||
                                    (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
                                Log.d(TAG, "The facebook session was invalidated." + response.getError());
                                gotoFirst();
                            } else {
                                Log.d(TAG,
                                        "Some other error: " + response.getError());
                            }
                        }
                    }
                }
        );
        request.executeAsync();
    }
    public void publishArticle(View view)
    {
        Intent intent;
        intent = new Intent(this,PublishActivity.class);
        intent.putExtra("UserName",userName);
        startActivity(intent);
    }

    public void gotoFirst(){
        ParseUser.logOut();
        com.facebook.Session fbs = com.facebook.Session.getActiveSession();
        if (fbs == null) {
            fbs = new com.facebook.Session(MainMenuActivity.this);
            com.facebook.Session.setActiveSession(fbs);
        }
        fbs.closeAndClearTokenInformation();

        Intent intent=new Intent(this, WelcomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

    }
    public void startChat(View view)
    {
        Intent intent=new Intent(this,DiscussionListActivity.class);
        intent.putExtra("UserName", userName);
        startActivity(intent);
    }
    public void logoutfrommain(View view){
        ParseUser.logOut();
        com.facebook.Session fbs = com.facebook.Session.getActiveSession();
        if (fbs == null) {
            fbs = new com.facebook.Session(MainMenuActivity.this);
            com.facebook.Session.setActiveSession(fbs);
        }
        fbs.closeAndClearTokenInformation();
        Intent intent=new Intent(this, WelcomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
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

        switch (item.getItemId()) {
            case R.id.logout:
                gotoFirst();
                return true;
            // All other buttons here, each in it's own case
        }
        return super.onOptionsItemSelected(item);

    }
}
