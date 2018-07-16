package com.roque.app.waylla_app;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roque.app.waylla_app.activities.CrearEventoActivity;
import com.roque.app.waylla_app.activities.IntroSliderActivity;
import com.roque.app.waylla_app.activities.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, IntroSliderActivity.class));
                finish();
            }
        },3000);
    }
}
