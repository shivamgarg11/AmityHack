package com.shivam.amity_hack;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
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

    }
}
