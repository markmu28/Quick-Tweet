package teamnull.test;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.AccountService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

public class Main2Activity extends ActionBarActivity {//this activity sends out the tweet
    String input_tweet;//this tring stores the content of the tweet
    private static final String TWITTER_KEY = "EICNjGv4ZlcsUXCsfu33sgs2u";//the key and secret I got by being a Twitter developer
    private static final String TWITTER_SECRET = "nEpg11VJbJQ1DDsTiA7cyZwLEeMXalESSKBhWsZqsxSZYiGZug";
    private ListView mDrawerList;//define a listview for drawer menu
    private ArrayAdapter<String> mAdapter;//define the array adapter for drawer menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Answers());//set up a new twitter instance

        setContentView(R.layout.activity_main2);//sets the screen to activity_main2.xml
        Button convenient_button = (Button) findViewById(R.id.convenient_button);//define a send-tweet button

        final EditText txt = (EditText)findViewById(R.id.txtInput);//define the text input box
        View.OnClickListener listener3 = new View.OnClickListener(){
            public void onClick (View view){//define the send-tweet function performed by the button
                input_tweet = txt.getText().toString();//store the text input into the string
                StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();//get a twitter StatusesService session
                statusesService.update(input_tweet, null, null, null, null, null, null, null, null, new Callback<Tweet>() {//send out the tweet
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void success(Result<Tweet> result) {//if sent successfully
                        String msg="Sent successfully";//print out the success message
                        txt.setText("");
                        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.msg_sent);//play the message sent sound effect
                        mediaPlayer.start();//start play the sound effect
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();//print out the success message
                        finishAffinity();//close the app
                    }

                    @Override
                    public void failure(TwitterException e) {//if sent unsuccessfully
                        String msg="Sent failure";//print out the failure message
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Intent act1=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(act1);//prompt the user to login again
                    }
                });
            }
        };
        convenient_button.setOnClickListener(listener3);


        mDrawerList = (ListView)findViewById(R.id.navList);//make the drawer menu
        addDrawerItems();//add items into the drawer
    }
    private void addDrawerItems() {
        String[] osArray = { "About this App","Logout" };//the options in the drawer
        mAdapter = new ArrayAdapter<String>(this, R.layout.mytextview, osArray);
        mDrawerList.setAdapter(mAdapter);//make the complete drawer menu

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//define what happens if options in the drawer menu is taped
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected=(String)mDrawerList.getItemAtPosition(position);
                if(selected=="About this App"){//if About this App is taped
                    Intent act1=new Intent(getApplicationContext(),Main4Activity.class);
                    startActivity(act1);//show the app information screen
                }
                else if(selected=="Logout"){//if logout is taped
                    Intent act1=new Intent(getApplicationContext(),Main3Activity.class);
                    startActivity(act1);//prompt the user to login again
                    SharedPreferences loggedin_pref=getSharedPreferences("PREFS_NAME",0);//get the sharedpreference defined in MainActivity
                    SharedPreferences.Editor editor=loggedin_pref.edit();//edit the sharepreference file
                    editor.clear();
                    editor.putBoolean("Loggedin", false);//set the loggedin value to "false"
                    editor.commit();
                    String msg="Logged Out";//print out the logout message
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
