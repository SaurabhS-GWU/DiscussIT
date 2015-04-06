package com.saurabh.discussit;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.facebook.AppEventsLogger;
import com.facebook.LoginActivity;
import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Arrays;
import java.util.List;


public class UserRegistrationActivity extends Activity {
    private LoginButton mloginbutton;
    private Button mregLogin;
    private EditText muserName;
    private EditText mpassword;
    private EditText mpassAgain;
    private Dialog progressDialog;
    private static final String TAG="discussit";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

     //  Parse.initialize(this, "", "");
       //ParseFacebookUtils.initialize("305572152973952");
        mloginbutton=(LoginButton)findViewById(R.id.login_button);
        mregLogin=(Button)findViewById(R.id.login_regular);
        muserName=(EditText)findViewById(R.id.user_name);
        mpassword=(EditText)findViewById(R.id.password);
        mpassAgain=(EditText)findViewById(R.id.password_again);
        mloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();
            }
        });

        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            showUserDetailsActivity();
        }
        mregLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean validateerror=false;
                StringBuilder errorMessage=new StringBuilder("Invalid Input. Please Enter");
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
                if(!isMatching(mpassword,mpassAgain))
                {
                    if(validateerror){errorMessage.append("and");}
                    errorMessage.append("Passwords do not match");
                    validateerror=true;
                }
                if(validateerror)
                {
                    Toast.makeText(UserRegistrationActivity.this,errorMessage.toString(),Toast.LENGTH_LONG).show();
                }

                final ProgressDialog dialog=new ProgressDialog(UserRegistrationActivity.this);
                dialog.setTitle("Signing Up");
                dialog.setMessage("Please wait while we sign you up");
                dialog.show();

                ParseUser user=new ParseUser();
                user.setUsername(muserName.getText().toString());
                user.setPassword(mpassword.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        dialog.dismiss();
                        if(e!=null)
                        {
                            Toast.makeText(UserRegistrationActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                         Intent intent=new Intent(UserRegistrationActivity.this,MainMenuActivity.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_registration, menu);
        return true;
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

        ParseFacebookUtils.logIn(this,new LogInCallback() {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.logout1){ logout();
        return true;}
        return super.onOptionsItemSelected(item);
    }
    public void logout(){ParseUser.logOut();}
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
    public boolean isEmpty(EditText field)
    {
        if(field.getText().toString().length()>0)
        return false;
        else
            return true;
    }
    public boolean isMatching(EditText field1, EditText field2)
    {
        String p1=field1.getText().toString();
        String p2=field2.getText().toString();
        if(p1.equals(p2))
            return true;
        else
            return false;
    }
}
