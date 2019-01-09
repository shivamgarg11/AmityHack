package com.shivam.amity_hack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {

   // private static final String path ="https://www.facebook.com/100022552814398/videos/371317356963355/?id=100022552814398";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String path1="https://www.demonuts.com/Demonuts/smallvideo.mp4";

        Uri uri=Uri.parse("android.resource://" + getPackageName() + "/raw/" + "splash");

        VideoView video=findViewById(R.id.VideoView);
        video.setVideoURI(uri);
        video.start();


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                        SharedPreferences sharedPref = SplashScreen.this.getSharedPreferences(
                "LOGIN", Context.MODE_PRIVATE);

        if(sharedPref.getBoolean("loggedIn",false))
            startActivity(new Intent(SplashScreen.this,MainActivity.class));
        else
        startActivity(new Intent(SplashScreen.this,LoginActivity.class));

                // close this activity
                finish();
            }
        }, 4050);




    }
}
