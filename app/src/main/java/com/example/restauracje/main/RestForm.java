package com.example.restauracje.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.restauracje.R;
import com.example.restauracje.model.Restaurant;

public class RestForm extends AppCompatActivity implements View.OnClickListener {

    private TextView name, street, city, description;
    private ButtonAction action;
    private DatabaseHandler db;
    private Restaurant restaurant;
    private int restaurant_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        name = findViewById(R.id.restaurantName);
        street = findViewById(R.id.restaurantStreet);
        city = findViewById(R.id.restaurantCity);
        description = findViewById(R.id.restaurantDescription);
        db = new DatabaseHandler(this);

        Intent intent = getIntent();
        if(intent != null)
            restaurant_id = (int) intent.getExtras().getLong("RESTAURANT_ID");

        if(restaurant_id == 0){
            action = ButtonAction.SAVE;
            restaurant = new Restaurant();
        }
        else {
            action = ButtonAction.UPDATE;
            restaurant = db.getRestaurant(restaurant_id);
            setUpTextViewsValues();
        }

        setButtonAction(action);
    }


    @Override
    public void onClick(View view) {
        String mName = name.getText().toString();
        String mStreet = street.getText().toString();
        String mCity = city.getText().toString();
        String mDescription = description.getText().toString();

        restaurant.setName(mName);
        restaurant.setStreet(mStreet);
        restaurant.setCity(mCity);
        restaurant.setDescription(mDescription);


        if(mStreet.isEmpty()){
            street.setError(getText(R.string.restaurant_street_required));
        }else if(mCity.isEmpty()){
            city.setError(getText(R.string.restaurant_city_required));
        }else{
            switch(action){
                case SAVE:
                    db.addRestaurant(restaurant);
                    finish();
                    break;
                case UPDATE:
                    db.updateRestaurant(restaurant);
                    finish();
                    break;
            }
        }
    }

    public void setButtonAction(ButtonAction action){
        this.action = action;
        Button actionButton = findViewById(R.id.restaurantBtn);
        setTitle(action == ButtonAction.SAVE ? getText(R.string.new_restaurant) : getText(R.string.update_restaurant));
        actionButton.setText(action == ButtonAction.SAVE ? getString(R.string.save_restaurant) : getString(R.string.update_restaurant));
        actionButton.setOnClickListener(this);
    }

    public void setUpTextViewsValues(){
        restaurant = db.getRestaurant(restaurant_id);
        name.setText(restaurant.getName());
        street.setText(restaurant.getStreet());
        city.setText(restaurant.getCity());
        description.setText(restaurant.getDescription());
    }
}
