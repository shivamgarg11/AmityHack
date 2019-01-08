package com.shivam.amity_hack.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.shivam.amity_hack.MainActivity;
import com.shivam.amity_hack.Nearby_places.GetNearbyPlacesData;
import com.shivam.amity_hack.R;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";
    static Menu menu;
    static MenuItem menuItem;

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx)
    {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavView");
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);
        menu = bottomNavigationViewEx.getMenu();
        menuItem = menu.getItem(0);
        menuItem.setChecked(false);
    }


    public static void enableNavigation(final Context context, BottomNavigationViewEx view)
    {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_hospital:
                        menuItem = menu.getItem(0);
                        menuItem.setChecked(true);

                        String Hospital = "hospital";

                        Log.d("onClick", "Button is Clicked");
                        MainActivity.mMap.clear();
                        String urlhospital = getUrl(MainActivity.l.getLatitude(), MainActivity.l.getLongitude(), Hospital);
                        Object[] DataTransferhospital = new Object[2];
                        DataTransferhospital[0] = MainActivity.mMap;
                        DataTransferhospital[1] = urlhospital;
                        Log.d("onClick", urlhospital);
                        GetNearbyPlacesData getNearbyPlacesDatahospital = new GetNearbyPlacesData();
                        getNearbyPlacesDatahospital.execute(DataTransferhospital);
                        break;

                    case R.id.ic_police:
                        menuItem = menu.getItem(1);
                        menuItem.setChecked(true);

                        String Police = "police";

                        Log.d("onClick", "Button is Clicked");
                        MainActivity.mMap.clear();
                        String urlpolice = getUrl(MainActivity.l.getLatitude(), MainActivity.l.getLongitude(), Police);
                        Object[] DataTransferpolice = new Object[2];
                        DataTransferpolice[0] = MainActivity.mMap;
                        DataTransferpolice[1] = urlpolice;
                        Log.d("onClick", urlpolice);
                        GetNearbyPlacesData getNearbyPlacesDatapolice = new GetNearbyPlacesData();
                        getNearbyPlacesDatapolice.execute(DataTransferpolice);

                        break;


                    case R.id.ic_food:
                        menuItem = menu.getItem(2);
                        menuItem.setChecked(true);

                        String Restaurant = "restaurant";

                        Log.d("onClick", "Button is Clicked");
                        MainActivity.mMap.clear();
                        String urlrestaurant = getUrl(MainActivity.l.getLatitude(), MainActivity.l.getLongitude(), Restaurant);
                        Object[] DataTransferrestaurant = new Object[2];
                        DataTransferrestaurant[0] = MainActivity.mMap;
                        DataTransferrestaurant[1] = urlrestaurant;
                        Log.d("onClick", urlrestaurant);
                        GetNearbyPlacesData getNearbyPlacesDatarestaurant = new GetNearbyPlacesData();
                        getNearbyPlacesDatarestaurant.execute(DataTransferrestaurant);

                        break;

                }

                return false;
            }
        });
    }

    private static String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 10000);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }
}

