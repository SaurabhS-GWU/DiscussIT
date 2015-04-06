package com.saurabh.discussit;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Saurabh on 11/20/2014.
 */
public class DiscussIT extends Application {

    static final String TAG = "MyApp";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this,
                "pmbTlBtcCrRIkjfspWmQ0zHigYGYonNmoQwlEt7v",
                "nv357LaURfA8h8VMjsk224Y8aMmBAKjx2WWCkuFy"
        );

        // Set your Facebook App Id in strings.xml
    //   ParseFacebookUtils.initialize(getString(R.string.app_id));
    }
}
