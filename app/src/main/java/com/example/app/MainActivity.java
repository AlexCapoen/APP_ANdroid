package com.example.app;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.optimization.v1.MapboxOptimization;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener, MapboxMap.OnMapClickListener {

    private MapView mapView;
    private MapboxMap map;
    private Button startRun;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG = "MainActivity";

    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;

    TextView objectifTemps;
    int objectifTempsEnSecondes;
    Button SaveButton;
    EditText ScoreTempsHeures;
    EditText ScoreTempsMinutes;
    EditText ScoreTempsSecondes;
    String totalTempsEnSecondes;
    String totalTempsEnMinutes;
    String totalTempsEnHeures;
    String userID;

    int tempsRealise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        setToolBar();

        mapView = (MapView) findViewById(R.id.mapView);
        startRun = findViewById(R.id.startRun);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        mFirebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        objectifTemps = findViewById(R.id.ObjectiveTextTime);
        /*try {
            objectifTempsEnSecondes = (int) TextTimetoSeconds(objectifTemps.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        ScoreTempsHeures=findViewById(R.id.HoursText);
        ScoreTempsMinutes=findViewById(R.id.MinutesText);
        ScoreTempsSecondes=findViewById(R.id.SecondsText);
        SaveButton = findViewById(R.id.RegisterButton);


        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .origin(originPosition)
                        .destination(destinationPosition)
                        .shouldSimulateRoute(true)
                        .build();
                NavigationLauncher.startNavigation(MainActivity.this,options);
            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalTempsEnSecondes = ScoreTempsSecondes.getText().toString();
                totalTempsEnMinutes = ScoreTempsMinutes.getText().toString();
                totalTempsEnHeures = ScoreTempsHeures.getText().toString();

                tempsRealise=ScoreTimeToSecond(Integer.parseInt(totalTempsEnHeures),Integer.parseInt(totalTempsEnMinutes),Integer.parseInt(totalTempsEnSecondes));

                storeRun(10, tempsRealise);
            }
        });
    }




    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        map.addOnMapClickListener(this);
        enableLocation();
    }

    //Savoir si le user a accepté la localisation
    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationEngine();
            initializeLocationLayer();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPostion(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer() {
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);

    }

    private void setCameraPostion(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13.0));
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {

        if (destinationMarker != null){
            map.removeMarker(destinationMarker);
        }
        destinationMarker = map.addMarker(new MarkerOptions().position(point));

        destinationPosition = Point.fromLngLat(point.getLongitude(),point.getLatitude());
        originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
        getRoute(originPosition,destinationPosition);

        startRun.setEnabled(true);
        startRun.setBackgroundResource(R.color.orange);

    }



    private void getRoute(Point origin, Point destination){



        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if(response.body()==null){
                            Log.e(TAG,"Pas de routes trouvées");
                            return;
                        }
                        else if (response.body().routes().size() == 0){
                            Log.e(TAG, "Pas de routes trouvées");
                        }

                        DirectionsRoute currentRoute = response.body().routes().get(0);

                        if (navigationMapRoute != null){
                            navigationMapRoute.removeRoute();
                        }
                        else {
                            navigationMapRoute = new NavigationMapRoute(null,mapView,map);
                        }


                        navigationMapRoute.addRoute(currentRoute);

                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "Erreur : "+ t.getMessage());
                    }
                });
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        locationEngine.removeLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
            setCameraPostion(location);
        }
    }

    @Override
    //Quand l'utilisateur refuse l'accès à la localisation
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Accès à la localisation refusée, veuillez accepter pour faire fonctionner couros", Toast.LENGTH_SHORT).show();
    }

    //Quand l'utilisateur a accepté l'accès à la localisation
    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if (locationEngine != null){
            locationEngine.requestLocationUpdates();
        }
        if (locationLayerPlugin != null){
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null){
            locationEngine.removeLocationUpdates();
        }
        if (locationLayerPlugin != null){
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null){
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }



    private void storeRun(int EstimateTime, int RealizedTime) {
        userID = mFirebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Users").document(userID);

        CollectionReference accessRun = documentReference.collection("TimeRun");

        Map<String, Object> user = new HashMap<>();
        user.put("temps_estime", EstimateTime);
        user.put("temps_realise", RealizedTime);

        accessRun.add(user);
    }
    /*
    public int TextTimetoSeconds(String ObjectiveText) throws ParseException {
        String myTime = ObjectiveText;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(myTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //String formattedTime = sdf.format(date);
        int seconds = (int) (date.getTime() / 1000);

        return seconds;
    }*/

    public int ScoreTimeToSecond(int hours, int minutes, int seconds) {
        int HoursToSecond = hours*3600;
        int MinutesToSecondes=minutes*60;
        int result = HoursToSecond+MinutesToSecondes+seconds;

        return result;
    }

    public void setToolBar(){
        //Initialisation et assignation des variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.course);

        //perform itemSelected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profil:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.course:
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






