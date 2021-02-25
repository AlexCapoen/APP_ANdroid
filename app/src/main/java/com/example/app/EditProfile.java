package com.example.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;


public class EditProfile extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText profileEditFirstName, profileEditName, profileEditBirthDate, profileEditPassword, profileEditWeight, profileEditHeight, profileEditGender, profileEditEmail;
    ImageView profilePicture;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button saveChanges;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Récupération des données de ProfileActivity

        Intent data = getIntent();
        String firstName = data.getStringExtra("Prénom");
        String name = data.getStringExtra("Nom");
        String email = data.getStringExtra("e-mail");
        String password = data.getStringExtra("Mot de passe");
        String birthDate = data.getStringExtra("Date de Naissance");
        String gender = data.getStringExtra("Genre");
        String height = data.getStringExtra("Taille");
        String weight = data.getStringExtra("Poids");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();



        profileEditFirstName = findViewById(R.id.profileEditFirstName);
        profileEditName = findViewById(R.id.profileEditName);
        profileEditEmail = findViewById(R.id.profileEditEmail);
        profileEditPassword = findViewById(R.id.profileEditPassword);
        profileEditBirthDate = findViewById(R.id.profileEditBirthDate);
        profileEditWeight = findViewById(R.id.profileEditWeight);
        profileEditHeight = findViewById(R.id.profileEditHeight);
        profileEditGender = findViewById(R.id.profileEditGender);
        saveChanges = findViewById(R.id.saveChanges);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileEditFirstName.getText().toString().isEmpty() || profileEditName.getText().toString().isEmpty() || profileEditEmail.getText().toString().isEmpty() || profileEditPassword.getText().toString().isEmpty() || profileEditBirthDate.getText().toString().isEmpty() || profileEditGender.getText().toString().isEmpty() || profileEditWeight.getText().toString().isEmpty() || profileEditHeight.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this, "Veuillez remplir les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = profileEditEmail.getText().toString();

                //fonction permettant de modifier des informations dans la base de données
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("Users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("e-mail",email);
                        edited.put("Prénom",profileEditFirstName.getText().toString());
                        edited.put("Nom",profileEditName.getText().toString());
                        edited.put("Date de Naissance",profileEditBirthDate.getText().toString());
                        edited.put("Mot de passe",profileEditPassword.getText().toString());
                        edited.put("Genre",profileEditGender.getText().toString());
                        edited.put("Poids",profileEditWeight.getText().toString());
                        edited.put("Taille",profileEditHeight.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "Données mises à jour", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                                finish();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        profileEditFirstName.setText(firstName);
        profileEditName.setText(name);
        profileEditEmail.setText(email);
        profileEditPassword.setText(password);
        profileEditBirthDate.setText(birthDate);
        profileEditGender.setText(gender);
        profileEditHeight.setText(height);
        profileEditWeight.setText(weight);

        //Initialisation et assignation des variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.profil);

        //perform itemSelected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.course:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profil:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });



    }
}