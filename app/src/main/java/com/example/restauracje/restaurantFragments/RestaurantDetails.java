package com.example.restauracje.restaurantFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.restauracje.R;

public class RestaurantDetails extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView restaurantName = getView().findViewById(R.id.restaurantNameTV);
        TextView restaurantDescription = getView().findViewById(R.id.restaurantDescription);
        TextView restaurantLocation = getView().findViewById(R.id.restaurantLocationTV);
        ImageButton showOnMap = getView().findViewById(R.id.showOnMap);


        restaurantName.setText(getArguments().getString("Name"));
        restaurantDescription.setText(getArguments().getString("Description"));
        restaurantLocation.setText(getArguments().getString("Address"));

        showOnMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        iFragmentChange fc = (iFragmentChange) getActivity();
        fc.showMapFragment();
    }
}