package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    //Version du Home en commentaire en cours de développement non terminée par manque de temps


    /*
    Button logOutButton;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseFirestore fStore;
    RecyclerView recyclerView;
    RecyclerView.Adapter CourseAdapter;
    private List<Course> coursesList;
    private String userId, docId;
    String tpsC, calorie;
    boolean reussi;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        coursesList = new ArrayList<>();

        logOutButton = findViewById(R.id.logOutButton);
        fStore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        //userId = mFirebaseAuth.getCurrentUser().getUid();

        setRecyclerView();
        //loadDataToCourseAdapter();
        getDataCourse();

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intToLogin);
            }
        });

        setToolBar();
    }




    public void setToolBar(){
        //Initialisation et assignation des variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //perform itemSelected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profil:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.course:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }


    public void setRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CourseAdapter = new CourseAdapter(this, coursesList);
        recyclerView.setAdapter(CourseAdapter);
    }


    public void loadDataToCourseAdapter(){

        for (int i = 0; i<=5; i++){
            Course course = new Course(reussi ,Integer.parseInt(tpsC),Integer.parseInt(calorie),"Temps de Course","Calorie","objectif atteint");
            coursesList.add(course);
        }
    }


    public void getDataCourse(){
        DocumentReference documentReference = fStore.collection("Users").document(userId).collection("Run").document();
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                tpsC = documentSnapshot.getString("kms");
                calorie = documentSnapshot.getString("calorie");
                reussi = documentSnapshot.getBoolean("objectifAtteint");

            }
        });

        for (int i = 0; i<=5; i++){
            Course course = new Course(reussi ,Integer.parseInt(tpsC),Integer.parseInt(calorie),"Temps de Course","Calorie","objectif atteint");
            coursesList.add(course);
        }

    }

     */



    private RecyclerView recyclerView;
    private RecyclerView.Adapter CourseAdapter;
    private List<StructureItem> coursesList;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private boolean objectifAtteint;
    private EditText tempsDeCourseNB, calorieBurleNB;
    private TextView tvTempsCourse, tvCalorieBrule, tvObjectifAtteint;
    private Button saveButton, backButton, test;
    private CourseAdapter.RecyclerViewClickListener listener;
    private CheckBox checkBox;


    FirebaseFirestore fStore;
    String tpsC, calorie;
    boolean reussi;
    FirebaseAuth mFirebaseAuth;
    private String userId;
    public static final String TAG = "TAG";

    public ArrayList<String> listIdDoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mFirebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        coursesList = new ArrayList<>();

        test = (Button) findViewById(R.id.buttontest);
        setRecyclerView();
        setToolBar();
    }



    public void setRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.reyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        for (int i = 1; i<=4; i++){
            StructureItem course = new StructureItem("Course numéro " + i,"détail de la course");
            coursesList.add(course);
        }

        CourseAdapter = new CourseAdapter(this, coursesList, listener);
        recyclerView.setAdapter(CourseAdapter);
        setOnClickListener();

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWindowCourseDialog();
            }
        });
    }


    private void setOnClickListener() {
        listener = new CourseAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                createWindowCourseDialog();
            }
        };
    }

    //création d'une popup permettant d'afficher les informations
    public void  createWindowCourseDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View coursePopUpView = getLayoutInflater().inflate(R.layout.popup_course, null);

        tvTempsCourse = (TextView) coursePopUpView.findViewById(R.id.tempsCourseTV);
        tvCalorieBrule = (TextView) coursePopUpView.findViewById(R.id.calorieTV);
        tvObjectifAtteint = (TextView) coursePopUpView.findViewById(R.id.objectifatteintTV);
        tempsDeCourseNB = (EditText) coursePopUpView.findViewById(R.id.tempsCourseNB);
        calorieBurleNB = (EditText) coursePopUpView.findViewById(R.id.calorieNB);
        checkBox = (CheckBox) coursePopUpView.findViewById(R.id.checkBox);
        saveButton = (Button) coursePopUpView.findViewById(R.id.saveButton);
        backButton = (Button) coursePopUpView.findViewById(R.id.backButton);



        //setRun(true,155,200);
        getDataCourse();

        dialogBuilder.setView(coursePopUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save donne dans la bdd
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void setRun(boolean objectifAtteint, String tempsCourse, String caloriBrule){
        checkBox.setChecked(objectifAtteint);
        calorieBurleNB.setText(caloriBrule+"");
        tempsDeCourseNB.setText(tempsCourse+"");
    }

    //génération des données de course depuis la base de données
    public void getDataCourse() {
        userId = mFirebaseAuth.getCurrentUser().getUid();
        fStore.collection("Users").document(userId).collection("Run")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : task.getResult()) {

                                dataId(document2.getId());

                                listIdDoc.add(document2.getId());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }


                    }
                });

    }

    //récupération de données par ID
    public void dataId(String id){
        userId = mFirebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Users").document(userId).collection("Run").document(id);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                tpsC = documentSnapshot.getString("kms");
                calorie = documentSnapshot.getString("calorie");
                reussi = documentSnapshot.getBoolean("objectifAtteint");


                setRun(reussi,tpsC,calorie);

            }
        });
    }

    //initialisation de la toolbar
    public void setToolBar(){
        //Initialisation et assignation des variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //perform itemSelected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profil:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.course:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }







}