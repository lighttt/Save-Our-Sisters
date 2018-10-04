package bootcamp.android.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class SplashScreenActivity extends AppCompatActivity {


    private static final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }

}
