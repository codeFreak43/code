package com.artback.bth.timeplanner;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.artback.bth.timeplanner.Geofence.GeofenceLocation;
import com.artback.bth.timeplanner.Geofence.GeofenceLocationProvider;
import com.artback.bth.timeplanner.Geofence.GeolocationService;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private static final int REQUEST_STORAGE  =1;
    private static final int REQUEST_NETWORK =2;
    private static final int REQUEST_LOCATION =3;

    private RecyclerView locationView;
    private RecyclerView.Adapter locAdapter;
    private RecyclerView.LayoutManager locationLayoutManager;
    static public boolean geofencesAlreadyRegistered = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        checkPermissons();
        init();
    }
    private int checkPermissons(){
        ArrayList<String> permissons = new ArrayList<String>();
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED ) {
            permissons.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED ) {
            permissons.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)!=
                PackageManager.PERMISSION_GRANTED ) {
            permissons.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        requestPermissions(permissons,);
        return 0;
    }
    private void init(){
        locationView = (RecyclerView) findViewById(R.id.location_list);
        locationView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        locationView.setLayoutManager(llm);
        //start geolocation
        startService(new Intent(this, GeolocationService.class));
        locationLayoutManager= new LinearLayoutManager(this);
        locationView.setLayoutManager(locationLayoutManager);
        List<GeofenceLocation> myGeofenceSet = new ArrayList<GeofenceLocation>
                (GeofenceLocationProvider.getInstance().getGeofencesLocations().values());
        locAdapter = new locationAdapter(myGeofenceSet);
        locationView.setAdapter(locAdapter);
    }
    public void openaddpage(View view){
        Intent intent = new Intent(this, AddLocationActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

}
