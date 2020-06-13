package com.hitham.miscarros.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hitham.miscarros.Data.DatabaseHandler;
import com.hitham.miscarros.Fragments.DialogPickerFragment;
import com.hitham.miscarros.Model.Cars;
import com.hitham.miscarros.R;

public class MainActivity extends AppCompatActivity implements DialogPickerFragment.OnInputListener {
    @Override
    public void sendData(String carManuf, String carName, String carModel, String carImage,String carPlate, int carStatus, View v) {
        saveCarToDB(v,carManuf, carName, carModel, carImage, carStatus, carPlate);
    }
    //Variables
    private DatabaseHandler db;
    private DialogFragment fragment;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        //Bypassing the current activity in case there is data stored
        byPassActivity();
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to fill up the car details
                fragment = new com.hitham.miscarros.Fragments.DialogPickerFragment();
                fragment.show(getSupportFragmentManager(),"new car picker fragment");
            }
        });

    }

    private void byPassActivity() {
        if(db.getCarsCount() > 0)
        {
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }
    boolean doubleBackToExitPressedOnce = false;
    // to confirm quitting the app
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.onBackPressed, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2690);
    }

    private void saveCarToDB(View v, String carManuf, String carName, String carModel, String carImage, int carStatus, String carPlate){
        Cars miCarro = new Cars();
        miCarro.setCarManuf(carManuf);
        miCarro.setCarModel(carModel);
        miCarro.setCarName(carName);
        miCarro.setCarImage(carImage);
        miCarro.setCarStatus(carStatus);
        miCarro.setCarPlate(carPlate);
        if (carName != null && carManuf != null && carModel != null && carPlate != null)
        {
            db.addCar(miCarro);
            Snackbar.make(v, "Fucking Shit saved", Snackbar.LENGTH_LONG).show();
            Log.e("ID:", String.valueOf(db.getCarsCount()));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragment.dismiss();
                    //Switch Activities
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                    finish();
                }}, 1273);
        }
        else
            Toast.makeText(v.getContext(), "There are empty fields U peace of crap", Toast.LENGTH_LONG).show();
    }

    //Menu Stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"U have just clicked the settings button \n yaaay",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}