package com.example.restauracje.restaurantFragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restauracje.R;
import com.example.restauracje.location.MyLocation;
import com.example.restauracje.location.iMyLocation;
import com.example.restauracje.permission.PermissionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantMap extends Fragment implements OnMapReadyCallback, iMyLocation {

    private Address address;
    private LatLng deviceLatLng;
    Intent locationService;
    TextView distance;
    Bundle data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(!PermissionManager.hasPermissionTo(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            PermissionManager.makeRequest(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, 124);
        }
        data = getArguments();

        if(data.getBoolean("canDisplayMap")) {
            locationService = new Intent(getContext(), MyLocation.class);
            getContext().startService(locationService);
        }else{
            getFragmentManager().popBackStack();
        }



        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                assert extras != null;
                if(extras.getBoolean("showAlert")){
                    showAlert();
                }
                deviceLatLng = new LatLng(extras.getDouble("latitude"),extras.getDouble("longitude"));
                calculateAndShowDistance();
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver((broadcastReceiver), new IntentFilter(MyLocation.SERVICE_RESULT));

        return inflater.inflate(R.layout.fragment_restaurant_map, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        distance = getActivity().findViewById(R.id.distance);

        Geocoder geocoder = new Geocoder(getContext());
        Toast toast = Toast.makeText(getContext(), getText(R.string.location_not_found), Toast.LENGTH_SHORT);
        try {
            address = geocoder.getFromLocationName(data.getString("Address"), 1).get(0);
            toast.cancel();
        } catch (Exception e) {
            toast.show();
            if(getActivity().findViewById(R.id.fragment_map) == null)
                getFragmentManager().popBackStack();
            else
                getFragmentManager().beginTransaction().remove(this).commit();
        }

        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && address != null) {
            LatLng restaurant = new LatLng(address.getLatitude(), address.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(restaurant).title(data.getString("Name")));

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(restaurant, 12f));
        }
        if(locationService != null)
            googleMap.setMyLocationEnabled(true);

        if(distance != null)
            distance.setText(R.string.no_data);
    }


    public void calculateAndShowDistance() {
        Location locationFrom = new Location("From");
        locationFrom.setLongitude(deviceLatLng.longitude);
        locationFrom.setLatitude(deviceLatLng.latitude);
        Location locationTo = new Location("To");
        locationTo.setLatitude(address.getLatitude());
        locationTo.setLongitude(address.getLongitude());
        if(distance != null && deviceLatLng.latitude != 0.0 && deviceLatLng.longitude != 0)
            distance.setText(String.valueOf(locationFrom.distanceTo(locationTo)/1000) + " km");
    }

    @Override
    public void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getText(R.string.enable_location))
                .setMessage(getText(R.string.location_is_off) + "\n" + getText(R.string.please_enable_location))
                .setPositiveButton(getText(R.string.settings), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(locationService != null)
            getContext().stopService(locationService);
    }
}