package com.hitham.miscarros.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hitham.miscarros.Data.DatabaseHandler;
import com.hitham.miscarros.Fragments.DialogPickerFragment;
import com.hitham.miscarros.Model.Cars;
import com.hitham.miscarros.R;
import com.hitham.miscarros.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements DialogPickerFragment.OnInputListener {
    @Override
    public void sendData(String manuf, String name, String model, String carImage,String carPlate,int carStatus,View myView) {
        saveCarToDB(myView,manuf, name, model, carImage, carStatus, carPlate);
    }

    //Variables
    private RecyclerViewAdapter miAdapteroDeRicicler;
    private RecyclerView miRicicler;
    private List <Cars>listaDeCarros;
    private List <Cars>listaDeItems;
    private DatabaseHandler db;
    private DialogFragment fragment;
    private Toolbar toolbar;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                fragment = new com.hitham.miscarros.Fragments.DialogPickerFragment();
                fragment.show(getSupportFragmentManager(),"new car picker fragment");
            }
        });
        db = new DatabaseHandler(this);
        setRecyclerView(fab);
    }

    private void setRecyclerView(final FloatingActionButton fab) {
        miRicicler = findViewById(R.id.recyclerViewID);
        miRicicler.setHasFixedSize(true);
        miRicicler.setLayoutManager(new LinearLayoutManager(this));
        listaDeCarros = new ArrayList<>();
        listaDeItems = new ArrayList<>();
        listaDeCarros = db.getAllCars();
        for (Cars x : listaDeCarros)
        {
            Cars car = new Cars();
            String carManufText = x.getCarManuf();
            String carModelText = x.getCarModel();
            String carPlateText = x.getCarPlate();
            if (carManufText.contains("Manufacturer : "))
                car.setCarManuf(x.getCarManuf());
            else{
                car.setCarManuf("Manufacturer : "+x.getCarManuf().toUpperCase());
            }
            car.setCarName(x.getCarName());
            if (carModelText.contains("Model : "))
                car.setCarModel(x.getCarModel());
            else{
                car.setCarModel("Model : " + x.getCarModel());
            }
            if (carPlateText.contains("Plate : "))
                car.setCarPlate(x.getCarPlate());
            else{
                car.setCarPlate("Plate : " + x.getCarPlate());
            }
            car.setCarID(x.getCarID());
            car.setCarImage(x.getCarImage());
            car.setCarMileage(x.getCarMileage());
            car.setCarOilDate(x.getCarOilDate());
            car.setCarStatus(x.getCarStatus());
            listaDeItems.add(car);
        }
        miAdapteroDeRicicler = new RecyclerViewAdapter(ListActivity.this,listaDeItems);
        miRicicler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_INDICATOR_TOP)
                    fab.show();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && fab.isShown())
                    fab.hide();
            }
        });
        miRicicler.setAdapter(miAdapteroDeRicicler);
        miAdapteroDeRicicler.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
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
        if (!carName.isEmpty() && !carManuf.isEmpty() && !carModel.isEmpty() && !carPlate.isEmpty())
        {
            db.addCar(miCarro);
            Snackbar.make(v, "Fucking Shit saved", Snackbar.LENGTH_LONG).show();
            Log.e("ID:", String.valueOf(db.getCarsCount()));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragment.dismiss();
                    //Refresh activity
                    startActivity(new Intent(ListActivity.this, ListActivity.class));
                    finish();
                }}, 1273);
        }
        else
            Toast.makeText(v.getContext(), "There are empty fields U peace of crap", Toast.LENGTH_LONG).show();
    }

}
