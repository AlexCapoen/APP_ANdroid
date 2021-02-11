package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById((R.id.textView));

        executor.execute(() -> {
            StarWarsDatabase db = Room.databaseBuilder(this, StarWarsDatabase.class, "database.db").build();

            //add a person
            Person p = new Person();
            p.setName("Luke");
            p.setHairColor(("Blond"));
            p.setGender("Male");
            db.personDao().insert(p);

            List<Person> personList = db.personDao().getAll();
            String text = "";
            for (Person person : personList){
                Log.i("ROOM_S2",person.toString());
                text += person.toString() + "\n";
            }
            String finalText = text;
            runOnUiThread(() -> {
                textView.setText((finalText));
            });
        });

    }
}