package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    TextView firstName, name, email, birthDate, password, gender, height, weight;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    ImageView profilePicture,changePictureButton;
    Button changeProfileButton,logOutButton;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId = fAuth.getCurrentUser().getUid();

        setToolBar();


        firstName = findViewById(R.id.profileFirstName);
        name = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        password = findViewById(R.id.profilePassword);
        birthDate = findViewById(R.id.profileBirthDate);
        gender = findViewById(R.id.profileGender);
        height = findViewById(R.id.profileHeight);
        weight = findViewById(R.id.profileWeight);




        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePicture);
            }
        });

        profilePicture = findViewById(R.id.profilePicture);
        changePictureButton = findViewById(R.id.changePictureButton);
        changeProfileButton = findViewById(R.id.changeProfileButton);
        logOutButton = findViewById(R.id.logOutButton);



        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        loadData(userId);

        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditProfile.class);
                i.putExtra("e-mail",email.getText().toString());
                i.putExtra("Prénom",firstName.getText().toString());
                i.putExtra("Nom",name.getText().toString());
                i.putExtra("Mot de passe",password.getText().toString());
                i.putExtra("Date de Naissance",birthDate.getText().toString());
                i.putExtra("Genre",gender.getText().toString());
                i.putExtra("Taille", height.getText().toString());
                i.putExtra("Poids",weight.getText().toString());

                startActivity(i);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToLogOut = new Intent(ProfileActivity.this, LogoutActivity.class);
                startActivity(intToLogOut);
            }
        });

    }

    public void loadData(String userId){
        DocumentReference documentReference = fStore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                firstName.setText(documentSnapshot.getString("Prénom"));
                name.setText(documentSnapshot.getString("Nom"));
                email.setText(documentSnapshot.getString("e-mail"));
                birthDate.setText(documentSnapshot.getString("Date de Naissance"));
                gender.setText(documentSnapshot.getString("Genre"));
                height.setText(documentSnapshot.getString("Taille"));
                weight.setText(documentSnapshot.getString("Poids"));
            }
        });

    }

    public void setToolBar(){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                profilePicture.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to firebase storage
        StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilePicture);
                    }
                });
                Toast.makeText(ProfileActivity.this, "Image mise à jour", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Problème avec l'image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
