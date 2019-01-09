package com.shivam.amity_hack;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.shivam.amity_hack.util.BottomNavigationViewHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {

    public static GoogleMap mMap;
    boolean flag = false;
    PlaceAutocompleteFragment placeAutoComplete;
    private LocationManager mLocationManager = null;
    private String provider = null;
    Circle c;
    private Marker mCurrentPosition = null;
    Marker marker;
    CircleOptions mOptions;
    public static Location l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int perm = ContextCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (perm == PackageManager.PERMISSION_GRANTED) {
            start();
            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
            }


        } else {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    44
            );
        }



    }


    public void start()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);


        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (marker != null)
                    marker.remove();

                flag = true;
                Log.d("Maps", "Place selected: " + place.getLatLng());
                marker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()).zIndex(800));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

            }


            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = false;
                locateCurrentPosition();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setupBottomNavigationView();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 44) { //write request
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                start();

            }
        }
        else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
            Toast.makeText(MainActivity.this, "Go to Settings and Grant the permission to use this feature.", Toast.LENGTH_SHORT).show();
        }
    }



    //region NavDrawer Activity
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.emergency){
            //Added by Shivam
            List<String> HelpLineNumbers = new ArrayList<>();
            HelpLineNumbers.add("Women's Helpline");
            HelpLineNumbers.add("Police");
            HelpLineNumbers.add("Hospital");
            HelpLineNumbers.add("Fire Department");
            HelpLineNumbers.add("Ambulance");
            HelpLineNumbers.add("Men's Helpline");

            final CharSequence[] helpLine = HelpLineNumbers.toArray(new String[HelpLineNumbers.size()]);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            mBuilder.setTitle("Helpline Numbers");

            mBuilder.setItems(helpLine, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    char selectedText = helpLine[i].toString().charAt(0);

                    switch(selectedText){

                        case 'W' :startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "1091", null)));
                            break;

                        case 'P' :startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "100", null)));
                            break;

                        case 'H' :startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "102", null)));
                            break;

                        case 'F' :startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "101", null)));
                            break;

                        case 'A' :startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "102", null)));
                            break;

                        case 'M' :startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "9278978978", null)));
                            break;
                    }


                }
            });

            AlertDialog alertDialogObject = mBuilder.create();
            //Show the dialog
            alertDialogObject.show();

        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void setupBottomNavigationView()
    {
        Log.d("BottomNv", "setupBottomNavigationView: setting up botNavView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bnve);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(this,bottomNavigationViewEx);
    }
    //endregion

    //region Maps Methods
    public void onMapReady(GoogleMap googleMap) {
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            Log.e("Error", "onMapReady: ");
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            Log.e("Error", "onMapReady: " );
        }

        if(!gps_enabled && !network_enabled)
        {
            buildAlertMessageNoGps();
        }
        mMap = googleMap;

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (isProviderAvailable() && (provider != null))
        {
            locateCurrentPosition();
        }

    }

    public void locateCurrentPosition() {

        int status = getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                getPackageName());

        if (status == PackageManager.PERMISSION_GRANTED) {
            Location location = mLocationManager.getLastKnownLocation(provider);
            updateWithNewLocation(location);
            //  mLocationManager.addGpsStatusListener(this);
            long minTime = 5000;// ms
            float minDist = 5.0f;// meter
            mLocationManager.requestLocationUpdates(provider, minTime, minDist, this);
            l=location;
            if(l != null)
                placeAutoComplete.setBoundsBias(new LatLngBounds(new LatLng(l.getLatitude(),l.getLongitude()),new LatLng(l.getLatitude()+2,l.getLongitude()+2)));
        }
    }


    private boolean isProviderAvailable() {
        mLocationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        provider = mLocationManager.getBestProvider(criteria, true);
        if (mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;

            return true;
        }

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            return true;
        }

        if (provider != null) {
            return true;
        }
        return false;
    }

    private void updateWithNewLocation(Location location) {

        if (location != null && provider != null) {
            double lng = location.getLongitude();
            double lat = location.getLatitude();
            if(!flag)
                addBoundaryToCurrentPosition(lat, lng);

            CameraPosition camPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng)).zoom(12f).build();

            if (mMap != null && !flag)
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(camPosition));
        } else {
            Log.d("Location error", "Something went wrong");
        }
    }


    private void addBoundaryToCurrentPosition(double lat, double lang) {
        Geocoder myLocation = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> myList = myLocation.getFromLocation(lat,lang, 1);
            Address address = (Address) myList.get(0);
            String addressStr = "";

            Log.d("LOCC", address.getAddressLine(0));

            addressStr += address.getAddressLine(0) ;
//            placeAutoComplete.setText(addressStr);
        } catch (IOException e) {
            e.printStackTrace();
        }


        MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.position(new LatLng(lat, lang));
        mMarkerOptions.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.ic_location));

        mMarkerOptions.anchor(0.5f, 0.5f);

        if( mOptions == null)
        {
            mOptions = new CircleOptions()
                    .center(new LatLng(lat, lang)).radius(5000)
                    .strokeColor(0x110000FF).strokeWidth(1).fillColor(0x110000FF);

            c = mMap.addCircle(mOptions);
        }
        else {
            c.remove();

            mOptions = new CircleOptions()
                    .center(new LatLng(lat, lang)).radius(5000)
                    .strokeColor(0x110000FF).strokeWidth(1).fillColor(0x110000FF);

            c = mMap.addCircle(mOptions);
        }

        if (mCurrentPosition != null)
            mCurrentPosition.remove();
        mCurrentPosition = mMap.addMarker(mMarkerOptions);
    }


    @Override
    public void onLocationChanged(Location location) {
        updateWithNewLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {

        updateWithNewLocation(null);
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                break;
            case LocationProvider.AVAILABLE:
                break;
        }
    }

    public void buildAlertMessageNoGps()
    {
        new MaterialDialog.Builder(this)
                .title("Location")
                .content("Enable Location")
                .positiveText("Enable GPS!")
                .negativeText("No, Thanks!")
                .cancelable(false)
                .positiveColor(Color.rgb(232,42,42))
                .negativeColor(Color.rgb(232,42,42))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(MainActivity.this, "Location Access Required!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //endregion


}
