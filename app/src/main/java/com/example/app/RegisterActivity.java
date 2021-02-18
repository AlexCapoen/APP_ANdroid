package com.example.app;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    public static final String TAG = "TAG";
    public static final String MAIL = "MAIL";
    public EditText emailId, password, password2, mName, mFirstName, mBirthDate, mWeight, mHeight;
    public Spinner mGender;
    Button nextButton;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;
    String userID;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_part_1);


        mFirebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        emailId = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword1);
        password2 = findViewById(R.id.registerPassword2);
        nextButton = findViewById(R.id.nextButton);
        mName = findViewById(R.id.name);
        mFirstName = findViewById(R.id.firstName);
        mBirthDate = findViewById(R.id.birthDate);
        mGender = (Spinner) findViewById(R.id.spinner);
        mWeight = findViewById(R.id.editTextNumberDecimal2);
        mHeight = findViewById(R.id.height);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {

                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String pwd2 = password2.getText().toString();
                String name = mName.getText().toString();
                String firstName = mFirstName.getText().toString();
                String birthDate = mBirthDate.getText().toString();
                String gender = mGender.getSelectedItem().toString();

                int weight;
                if (mWeight.getText().toString().isEmpty()) {
                    weight = 0;
                } else {
                    weight = Integer.valueOf(mWeight.getText().toString());
                }

                int height;
                if (mHeight.getText().toString().isEmpty()) {
                    height = 0;
                } else {
                    height = Integer.valueOf(mHeight.getText().toString());
                }


                if (email.isEmpty()) {
                    emailId.setError("Veuillez entrer un e-mail");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Veuillez entrer un mot de passe");
                    password.requestFocus();
                }
                else if (pwd2.isEmpty()) {
                    password2.setError("Veuillez entrer un mot de passe");
                    password2.requestFocus();
                }
                else if (!(pwd.equals(pwd2))) {
                    password2.setError("Veuillez entrer des mots de passe identiques");
                    password2.requestFocus();
                }
                else if (name.isEmpty()) {
                    mName.setError("Veuillez entrer un nom");
                    mName.requestFocus();
                }
                else if (firstName.isEmpty()) {
                    mFirstName.setError("Veuillez entrer un prénom");
                    mFirstName.requestFocus();
                }

                else if (weight == 0) {
                    mWeight.setError("Veuillez entrer un poids");
                    mWeight.requestFocus();
                }
                else if (birthDate.isEmpty()) {
                    mBirthDate.setError("Veuillez entrer une date de naissance");
                    mBirthDate.requestFocus();

                }
                else if (!(isValidDate(birthDate))) {
                    mBirthDate.setError("Veuillez rentrer une date au format jj/mm/aaaa");
                    mBirthDate.requestFocus();
                }
                else if (height == 0) {
                    mHeight.setError("Veuillez entrer une taille");
                    mHeight.requestFocus();
                }
                else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Les champs sont vides", Toast.LENGTH_SHORT);
                }
                 else if (!(isFieldEmpty(email, pwd, pwd2, name, firstName, birthDate, gender, height, weight))) {

                    if (pwd.equals(pwd2)) {
                        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Sign In Unsuccessful ", Toast.LENGTH_SHORT);
                                } else {

                                    storeNewUsersData(email, pwd, name, firstName, birthDate, gender, height, weight);
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Mots de passe non identiques", Toast.LENGTH_SHORT);
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void storeNewUsersData(String email, String pwd, String name, String firstName, String birthDate, String gender, int height, int weight) {
        userID = mFirebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Users").document(userID);


        Map<String, Object> user = new HashMap<>();
        user.put("e-mail", email);
        user.put("Mot de passe", pwd);
        user.put("Nom", name);
        user.put("Prénom", firstName);
        user.put("Date de Naissance", birthDate);
        user.put("Genre", gender);
        user.put("Taille", height);
        user.put("Poids", weight);

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Compte créé pour l'ID : " + userID);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error", e);
                    }
                });
    }


    public boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public boolean isFieldEmpty(String email, String pwd, String pwd2, String name, String firstName, String birthDate, String gender, int weight, int height) {
        if (email.isEmpty() || pwd.isEmpty() || pwd2.isEmpty() || name.isEmpty() || firstName.isEmpty() || birthDate.isEmpty() || gender.isEmpty() || weight == 0 || height == 0) {
            return true;
        } else {
            return false;
        }


    }
}

