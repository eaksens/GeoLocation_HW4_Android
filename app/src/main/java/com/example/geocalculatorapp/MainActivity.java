package com.example.geocalculatorapp;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static final int UNITS_SELECTION = 1;
    //unit variables
    String disUnit = "Miles";
    String bearingUnit = "degrees";

    //Unit Conversion: 1 kilometer = 0.621371 miles. 1 degree = 17.777777777778 mil.
    final Double kmToMiles = 0.621371;
    final Double degreeToMils = 17.777777777778;

    private  void clearValues() {
        final EditText lat1 = findViewById(R.id.lat1);
        final EditText lat2 = findViewById(R.id.lat2);
        final EditText long1 = findViewById(R.id.long1);
        final EditText long2 = findViewById(R.id.long2);
        lat1.setText("43.077366");
        long1.setText("-85.994053");
        lat2.setText("43.077303");
        long2.setText("-85.993860");
    }

    private void computeValues() {
        final TextView calDistance = findViewById(R.id.calDistance);
        final TextView calBearing = findViewById(R.id.calBearing);

        //reference to the UI use the findViewById
        final EditText lat1 = findViewById(R.id.lat1);
        final EditText lat2 = findViewById(R.id.lat2);
        final EditText long1 = findViewById(R.id.long1);
        final EditText long2 = findViewById(R.id.long2);

        Double lat1value = Double.parseDouble(lat1.getText().toString());
        Double long1value = Double.parseDouble(long1.getText().toString());
        Double lat2value = Double.parseDouble(lat2.getText().toString());
        Double long2value = Double.parseDouble(long2.getText().toString());


        Location startPoint = new Location("a");
        Location endPoint = new Location("b");

        startPoint.setLatitude(lat1value);
        startPoint.setLongitude(long1value);
        endPoint.setLatitude(lat2value);
        endPoint.setLongitude(long2value);

        float distance = (startPoint.distanceTo(endPoint))/1000;
        float bearing = (startPoint.bearingTo(endPoint));

        if (disUnit.equals("Miles")) {
            distance *= kmToMiles;
        }
        if (bearingUnit.equals("Mils")) {
            bearing *= degreeToMils;
        }

        String distanceString = String.format("%.2f "+ disUnit, distance);
        String bearingString = String.format("%.2f "+ bearingUnit, bearing);
        calDistance.setText(distanceString);
        calBearing.setText(bearingString);
    }

    //on create Android Life Cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); //where to get UI from -in this case, it's under layout folder on main_activity.xml
        //toolbar
        Toolbar myToolBar = findViewById(R.id.mainToolbar);
        setSupportActionBar(myToolBar);


        Button calBtn = findViewById(R.id.calculate);
        Button clearBtn = findViewById(R.id.clearBtn);


        //Press Clear button the all values are clear
        clearBtn.setOnClickListener(v -> {
            clearValues();
        });

        //Press Calculate button
        calBtn.setOnClickListener(v -> {
            computeValues();
        });
    }


    //setup the setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem settingMenuItem = menu.findItem(R.id.actionSetting);

        settingMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, unitSettingActivity.class);
                startActivityForResult(intent,UNITS_SELECTION);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == UNITS_SELECTION) {
            String[] units = data.getStringArrayExtra("units");
            disUnit = units[0];
            bearingUnit = units[1];
            computeValues();
        }
    }

}
