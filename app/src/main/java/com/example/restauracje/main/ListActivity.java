package com.example.restauracje.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.restauracje.R;
import com.example.restauracje.model.Restaurant;
import com.example.restauracje.permission.PermissionManager;

import java.util.List;


public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private List<Restaurant> restaurants;
    private ArrayAdapter<Restaurant> adapter;
    private DatabaseHandler db;
    private final int CONTEXT_MENU_EDIT = 1;
    private final int CONTEXT_MENU_DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if(!PermissionManager.hasPermissionTo(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            PermissionManager.makeRequest(this, Manifest.permission.ACCESS_FINE_LOCATION, 1);
        }

        ListView restaurantsList = findViewById(R.id.restaurantListView);
        db = new DatabaseHandler(this);

        restaurants = db.getRestaurants();
        adapter = getMyArrayAdapter();

        restaurantsList.setOnItemClickListener(this);
        restaurantsList.setAdapter(adapter);
        registerForContextMenu(restaurantsList);

    }

    @Override
    protected void onResume() {
        refreshListView();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission", "Permission has been denied by user");
        } else {
            Log.i("Permission", "Permission has been granted by user");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_restaurant:
                Intent intent = new Intent(getApplicationContext(), RestForm.class);
                intent.putExtra("RESTAURANT_ID", 0L);
                startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getApplicationContext(), RestActivity.class);
        int position = i+1;
        intent.putExtra("RESTAURANT_ID", position);
        startActivity(intent);
    }

    public ArrayAdapter<Restaurant> getMyArrayAdapter(){
        return new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_2, android.R.id.text1, restaurants) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                int MAX_LEN = 70;
                String desc = restaurants.get(position).getDescription();

                if(desc.length() > MAX_LEN)
                    desc = desc.substring(0, MAX_LEN) + "...";

                text1.setText(restaurants.get(position).getName());
                text2.setText(desc);
                return view;
            }
        };
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, CONTEXT_MENU_EDIT, Menu.NONE, "Edit");
        menu.add(Menu.NONE, CONTEXT_MENU_DELETE, Menu.NONE, "Delete");
    }

    public boolean onContextItemSelected (MenuItem item){
        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        long restaurant_id = menuinfo.id+1;
        switch (item.getItemId()) {
            case CONTEXT_MENU_EDIT:
                Intent i = new Intent(this, RestForm.class);
                i.putExtra("RESTAURANT_ID", restaurant_id);
                startActivity(i);
                break;
            case CONTEXT_MENU_DELETE:
                db.deleteRestaurant(restaurant_id);
                refreshListView();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void refreshListView(){
        restaurants.clear();
        restaurants.addAll(db.getRestaurants());
        adapter.notifyDataSetChanged();
    }
}
