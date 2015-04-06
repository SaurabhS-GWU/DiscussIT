package com.saurabh.discussit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.AppEventsLogger;
import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends Activity {
    private LoginButton fblogin;
    private Button login;
    private EditText muserName;
    private EditText mpassword;
    private static final String TAG="discussit";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fblogin=(LoginButton)findViewById(R.id.fb_lgn);
        login=(Button)findViewById(R.id.login_btn);
        muserName=(EditText)findViewById(R.id.uname);
        mpassword=(EditText)findViewById(R.id.pword);
        fblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();
            }
        });
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            showUserDetailsActivity();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validateerror=false;
                StringBuilder errorMessage=new StringBuilder("Login Failed. Please enter");
                if(isEmpty(muserName))
                {
                    validateerror=true;
                    errorMessage.append(" UserName");
                }
                if(isEmpty(mpassword))
                {
                    if(validateerror)
                    {errorMessage.append("and");}
                    errorMessage.append("password");
                    validateerror=true;
                }
                if(validateerror){
                    Toast.makeText(LoginActivity.this, errorMessage.toString(),Toast.LENGTH_LONG).show();}

                final ProgressDialog dialog=new ProgressDialog(LoginActivity.this);
                dialog.setTitle("Login progress");
                dialog.setMessage("Please wait while we log you in");
                dialog.show();

                ParseUser.logInInBackground(muserName.getText().toString(),mpassword.getText().toString(),new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(e!=null)
                        {
                            Toast.makeText(LoginActivity.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Intent intent=new Intent(LoginActivity.this,MainMenuActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("UserName",muserName.getText().toString());
                            startActivity(intent);
                        }
                    }
                });

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
    private void onLoginButtonClicked() {
        this.progressDialog =
                ProgressDialog.show(this, "", "Logging in...", true);

        List<String> permissions = Arrays.asList("public_profile", "email");
        // NOTE: for extended permissions, like "user_about_me", your app must be reviewed by the Facebook team
        // (https://developers.facebook.com/docs/facebook-login/permissions/)

        ParseFacebookUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                // UserRegistrationActivity.this.progressDialog.dismiss();
                if (user == null) {
                    Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d(TAG, "User signed up and logged in through Facebook!");
                    showUserDetailsActivity();
                } else {
                    Log.d(TAG, "User logged in through Facebook!");
                    showUserDetailsActivity();
                }
            }
        });
    }
    public void  showUserDetailsActivity(){
        Intent intent=new Intent(this,MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
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
    public boolean isEmpty(EditText field)
    {
        if(field.getText().toString().length()>0)
            return false;
        else
            return true;
    }
}
