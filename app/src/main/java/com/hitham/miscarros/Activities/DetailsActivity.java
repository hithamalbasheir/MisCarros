package com.hitham.miscarros.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitham.miscarros.Data.DatabaseHandler;
import com.hitham.miscarros.Model.Cars;
import com.hitham.miscarros.R;
import com.hitham.miscarros.Services.NotifyService;
import com.hitham.miscarros.UI.RecyclerViewAdapter;

import java.util.Date;

public class DetailsActivity extends AppCompatActivity {
    private static final int REQUEST_DATE = 1;
    private static final int REQUEST_CODE = 1;
    private TextView nameDet;
    private TextView manufDet;
    private TextView modelDet;
    private TextView plateDet;
    private ImageView imgDet;
    private Bitmap getImg;
    private TextView mileage;
    private TextView oilChangeDate;
    private Cars car;
    private int id;
    private AlertDialog dialog;
    private DatabaseHandler db;
    private AlertDialog.Builder alertBuilder;
    private Bundle miBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        miBundle = getIntent().getExtras();
        nameDet = findViewById(R.id.carNameDet);
        manufDet = findViewById(R.id.carManufDet);
        modelDet = findViewById(R.id.carDOBDet);
        plateDet = findViewById(R.id.carPlateDet);
        imgDet = findViewById(R.id.carImageDet);
        mileage = findViewById(R.id.mileage);
        oilChangeDate = findViewById(R.id.oilChangeID);
        db = new DatabaseHandler(this);
        if (miBundle != null )
        {
            nameDet.setText(miBundle.getString("name").toUpperCase());
            modelDet.setText(miBundle.getString("model"));
            manufDet.setText(miBundle.getString("manuf"));
            plateDet.setText(miBundle.getString("plate"));
            mileage.setText("Mileage : "+miBundle.getString("mileage"));
            oilChangeDate.setText(miBundle.getString("oilChangeDate"));
            if(miBundle.getString("imagePath") != null)
            {
                getImg = BitmapFactory.decodeFile(miBundle.getString("imagePath"));
                imgDet.setImageBitmap(getImg);
             //   imgDet.setMaxHeight(190);
               // imgDet.setMinimumHeight(180);
            }
            id = miBundle.getInt("position");
            car = new Cars();
            car.setCarID(miBundle.getInt("position"));
            car.setCarName((String) miBundle.get("name"));
            car.setCarManuf((String) miBundle.get("manuf"));
            car.setCarModel((String) miBundle.get("model"));
            car.setCarMileage((String) miBundle.get("mileage"));
            car.setCarImage((String) miBundle.get("imagePath"));
            car.setCarOilDate(miBundle.getString("oilChangeDate"));
            car.setCarPlate(miBundle.getString("plate"));
            car.setCarStatus(miBundle.getInt("status"));
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ListActivity.class));
    }

    public void setMileageBu(View view) {
        alertBuilder = new AlertDialog.Builder(this);
        final View myView = getLayoutInflater().inflate(R.layout.set_value,null);
        final EditText mileageValue = myView.findViewById(R.id.nCarMileage);
        Button setMileage = myView.findViewById(R.id.editMileageID);
        alertBuilder.setView(myView);
        dialog =alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        final Intent intent = new Intent(this,DetailsActivity.class);
        setMileage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car.setCarMileage(mileageValue.getText().toString());
                db.updateCars(car);
                mileage.setText("Mileage : "+mileageValue.getText().toString());
                miBundle.putString("mileage",mileageValue.getText().toString());
                intent.putExtras(miBundle);
                startActivity(intent,miBundle);
                finish();
                dialog.dismiss();
            }
        });
    }
    public void setOilChangeDate(View view) {
        DialogFragment fragment = new com.hitham.miscarros.Fragments.DatePickerFragment();
        fragment.show(getSupportFragmentManager(),"Un puta Date picker");
//        scheduleNotif();
    }
/*    public void scheduleNotif(){
        ComponentName myComponent = new ComponentName(getPackageName(),NotifyService.class.getName());
        JobInfo jobInfo = new JobInfo.Builder(1235,myComponent)
                    .setMinimumLatency(10 * 1000)
                    .setOverrideDeadline(60 * 60 *1000)
                    .setPersisted(true)
                    .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        if(scheduler != null){
//            scheduler.cancelAll();
//            Toast.makeText(this,"Job cancelled",Toast.LENGTH_LONG).show();
//        }
        int resultCode = scheduler.schedule(jobInfo);
        if(resultCode == JobScheduler.RESULT_SUCCESS)
            Log.d("JOB Done","Scheduled successfully");
        else
            Log.d("JOB Done","not Scheduled successfully");
    } */
}