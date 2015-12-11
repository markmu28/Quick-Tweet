package teamnull.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.*;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;


import javax.security.auth.login.LoginException;

import io.fabric.sdk.android.Fabric;
import retrofit.http.Field;
import twitter4j.auth.AccessToken;


public class MainActivity extends ActionBarActivity {//this acticity determins whether the user has logged in and sends the user to different activities based on that

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "EICNjGv4ZlcsUXCsfu33sgs2u";//the key and secret I got by being a Twitter developer
    private static final String TWITTER_SECRET = "nEpg11VJbJQ1DDsTiA7cyZwLEeMXalESSKBhWsZqsxSZYiGZug";
    private TwitterLoginButton loginButton;
    String input_tweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);//register with twitter using the key and secret
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());//set up a new twitter instance
        Fabric.with(this, new Twitter(authConfig), new Answers());
        SharedPreferences loggedin_pref=getSharedPreferences("PREFS_NAME", 0);//a sharedpreference value shared among all activities to determine whether the user has logged in
        Boolean test_existence=loggedin_pref.getBoolean("Loggedin", false);
        if(test_existence==null) {
            SharedPreferences.Editor editor = loggedin_pref.edit();//edit the sharedpreference
            editor.clear();
            editor.putBoolean("Loggedin", false);//default value set to "false"
            editor.commit();//make the change
        }
        boolean loggedin=loggedin_pref.getBoolean("Loggedin",false);//set up a boolean value called loggedin, the value of which will be determined by the value in sharedpreference



        //setContentView(R.layout.activity_main);//set the screen to activity_main
        if(!loggedin) {//if the user is logged in
            this.finish();
            Intent act3 = new Intent(getApplicationContext(), Main3Activity.class);//start activity 3 (send a tweet)
            startActivity(act3);//start activity 3

        }
        else{//if the user is not logged in
            this.finish();
            Intent act2 = new Intent(getApplicationContext(), Main2Activity.class);//start activity 2 (prompt the user to log in)
            startActivity(act2);//start activity 2

        }


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
