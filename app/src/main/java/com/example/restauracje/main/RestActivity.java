package com.example.restauracje.main;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.restauracje.R;
import com.example.restauracje.model.Restaurant;
import com.example.restauracje.permission.PermissionManager;
import com.example.restauracje.restaurantFragments.RestaurantDetails;
import com.example.restauracje.restaurantFragments.RestaurantMap;
import com.example.restauracje.restaurantFragments.iFragmentChange;


public class RestActivity extends AppCompatActivity implements iFragmentChange {

    private int restaurant_id = 0;
    private Bundle restaurant_data;
    private boolean canDisplayMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Bundle extras = getIntent().getExtras();

        if (extras != null)
            restaurant_id = extras.getInt("RESTAURANT_ID");

        Restaurant restaurant = new DatabaseHandler(this).getRestaurant(restaurant_id);
        setTitle(restaurant.getName());

        RestaurantDetails restaurantDetails = new RestaurantDetails();

        restaurant_data = new Bundle();
        restaurant_data.putString("Name", restaurant.getName());
        restaurant_data.putString("Description", restaurant.getDescription());
        restaurant_data.putString("Address", restaurant.getAddress());

        restaurantDetails.setArguments(restaurant_data);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_placeholder, restaurantDetails, "details").commit();

        if (PermissionManager.hasPermissionTo(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            canDisplayMap = true;
            showMapFragment();
        }

    }


    @Override
    public void showMapFragment() {
        RestaurantMap restaurantMap = new RestaurantMap();
        restaurant_data.putBoolean("canDisplayMap", canDisplayMap);
        restaurantMap.setArguments(restaurant_data);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fragment_map) != null) {
            restaurantMap.setArguments(restaurant_data);
            fragmentManager.beginTransaction().add(R.id.fragment_map, restaurantMap, "map").commit();
        }
        if (findViewById(R.id.fragment_map) == null) {
            fragmentManager.beginTransaction().replace(R.id.fragment_placeholder, restaurantMap, "map").addToBackStack("map").commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == 0) {
            canDisplayMap = true;
            showMapFragment();
        } else {
            Toast.makeText(this, getText(R.string.location_permission_disable) + " "
                    + getText(R.string.unable_to_show_map) + " "
                    + getText(R.string.enable_location_permission), Toast.LENGTH_LONG).show();

        }
    }

}
