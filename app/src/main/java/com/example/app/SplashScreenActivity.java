package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //redirection vers MainActivity après 3 sec
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //demarage de la page
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        };

        //delay with handler
        new Handler().postDelayed(runnable, 3000);

    }
}