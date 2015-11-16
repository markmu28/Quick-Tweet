package teamnull.test;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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


public class MainActivity extends ActionBarActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "EICNjGv4ZlcsUXCsfu33sgs2u";
    private static final String TWITTER_SECRET = "nEpg11VJbJQ1DDsTiA7cyZwLEeMXalESSKBhWsZqsxSZYiGZug";
    private TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
        Fabric.with(this, new Twitter(authConfig), new Answers());
        setContentView(R.layout.activity_main);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        final TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("just setting up my Fabric.");
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user Bmodel
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        final RelativeLayout background_color = (RelativeLayout) findViewById(R.id.relativeLayout);
        Button playButton = (Button) findViewById(R.id.button);
        final MediaPlayer mp3=MediaPlayer.create(this, R.raw.boundless);
        Button send_button=(Button) findViewById(R.id.send_button);
        Button convenient_button = (Button) findViewById(R.id.convenient_button);

        View.OnClickListener listener1 = new View.OnClickListener(){
            public void onClick (View view){
                if(mp3.isPlaying()){
                    mp3.pause();
                    background_color.setBackgroundColor(Color.LTGRAY);
                }
                else{
                    mp3.start();

                    background_color.setBackgroundColor(Color.CYAN);
                }
            }
        };
        playButton.setOnClickListener(listener1);

        View.OnClickListener listener2 = new View.OnClickListener(){
            public void onClick(View view){

                builder.show();
            }
        };
        send_button.setOnClickListener(listener2);

        View.OnClickListener listener3 = new View.OnClickListener(){
            public void onClick (View view){

                StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();
                statusesService.update("Sending Tweet without using Twitter client composer!! LOL", null, null, null, null, null, null, null, null, new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        String msg="Sent successfullly";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(TwitterException e) {
                        String msg="Sent failure";
                        
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        convenient_button.setOnClickListener(listener3);

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
