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
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import javax.security.auth.login.LoginException;

import io.fabric.sdk.android.Fabric;
import retrofit.http.Field;

public class Main3Activity extends ActionBarActivity {//this activity is responsible for logging the user in

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "EICNjGv4ZlcsUXCsfu33sgs2u";//these are actually not needed here
    private static final String TWITTER_SECRET = "nEpg11VJbJQ1DDsTiA7cyZwLEeMXalESSKBhWsZqsxSZYiGZug";
    private TwitterLoginButton loginButton;
    String input_tweet;//this line just for testing



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        //Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
        Fabric.with(this, new Twitter(authConfig), new Answers());
        setContentView(R.layout.activity_main3);//sets the screen to activity_main3.xml
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);//define the login button
        loginButton.setCallback(new Callback<TwitterSession>() {//define the login function, call for a twitter session
            @Override
            public void success(Result<TwitterSession> result) {//if successfully got a twitter session(successfully logged in)
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;//get back the session(I guess)
                SharedPreferences loggedin_pref=getSharedPreferences("PREFS_NAME",0);//get the sharedpreference defined in MainActivity
                SharedPreferences.Editor editor=loggedin_pref.edit();//edit the sharepreference file
                editor.clear();
                editor.putBoolean("Loggedin",true);//set the loggedin value to "true"
                editor.commit();//make the change
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user Bmodel
                Intent act2=new Intent(getApplicationContext(),Main2Activity.class);//start activity 2 (send a tweet)
                startActivity(act2);//start activity 2
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";//prints out a message saying the user has logged in
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure(TwitterException exception) {//if loggedin unsuccessfully
                Log.d("TwitterKit", "Login with Twitter failure", exception);//throw an exception
            }
        });
        //the following is just for testing
        final RelativeLayout background_color = (RelativeLayout) findViewById(R.id.relativeLayout);

        //Button send_button=(Button) findViewById(R.id.send_button);
        Button convenient_button = (Button) findViewById(R.id.convenient_button);
        final EditText txt = (EditText)findViewById(R.id.txtInput);








    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
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
