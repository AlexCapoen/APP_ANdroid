package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    Button yesLogOut, noLogOut;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        fAuth = FirebaseAuth.getInstance();

        yesLogOut = findViewById(R.id.yesLogOut);
        noLogOut = findViewById(R.id.noLogOut);

        noLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        yesLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(LogoutActivity.this, LoginActivity.class);
                startActivity(intToLogin);
            }
        });



    }
}