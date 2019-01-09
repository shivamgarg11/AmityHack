package com.shivam.amity_hack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class CreditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        TextView currentCredits = findViewById(R.id.credits);
        currentCredits.setText("Current Credits: 0");

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://m.p-y.tm/requestPayment?recipient=7838460370&amount=1&comment=RedeemingCredits";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

//        SharedPreferences sharedPref = CreditActivity.this.getSharedPreferences(
//                "LOGIN", Context.MODE_PRIVATE);
//        int email = sharedPref.getString("email","null");

//        Context myContext = getApplicationContext().createPackageContext
//                ("com.cr.sharedprefexampleone", Context.MODE_WORLD_WRITEABLE);
//        SharedPreferences testPrefs = myContext.getSharedPreferences
//                ("sharedprefone", Context.MODE_WORLD_READABLE);
//        String prefString = testPrefs.getString("time", "Couldn't find");
    }
}
