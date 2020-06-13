package com.hitham.miscarros.Fragments;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;
import com.hitham.miscarros.Activities.DetailsActivity;
import com.hitham.miscarros.Data.DatabaseHandler;
import com.hitham.miscarros.Model.Cars;
import com.hitham.miscarros.R;
import java.util.Calendar;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static int year;
    private static int month;
    private static int day;
    private Cars car;
    private DatabaseHandler db;
    private Bundle miBundle;
    private static final String channel_id = "myChannel";
    private static final int notification_id = 69;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Declaring Car object to use it to update the car's Oil change Date
        db =  new DatabaseHandler(getActivity().getApplicationContext());
        miBundle = getActivity().getIntent().getExtras();
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
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dp =new DatePickerDialog(getActivity(),this,year,month,day);
        dp.setTitle(R.string.date_picker_title);
        return dp;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView tv = Objects.requireNonNull(getActivity()).findViewById(R.id.oilChangeID);
        String formattedDate = getResources().getString(R.string.oilChangeText)+" : "+dayOfMonth+"/"+ (month+2)+"/" +year;
        tv.setText(formattedDate);
        car.setCarOilDate(formattedDate);
        miBundle.putString("oilChangeDate",formattedDate);
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("oilChangeDate",formattedDate);
        db.updateCars(car);
        intent.putExtras(miBundle);
        Calendar c = Calendar.getInstance();
        AlarmManager myManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),1,intent,0);
        myManager.setExact(AlarmManager.RTC_WAKEUP,10000,pendingIntent);
//        scheduleNotif();
        getActivity().finish();
        getActivity().startActivity(intent);
    }


  /*  public void scheduleNotif(){
        ComponentName myComponent = new ComponentName(getActivity().getPackageName(), NotifyService.class.getName());
        JobInfo jobInfo = new JobInfo.Builder(1235,myComponent)
                .setMinimumLatency(24 * 60 * 60 * 1000)
                .setOverrideDeadline(60 * 60 * 1000)
                .setPersisted(true)
                .build();

        JobScheduler scheduler = (JobScheduler) getActivity().getSystemService(JOB_SCHEDULER_SERVICE);
//        if(scheduler != null){
//            scheduler.cancelAll();
//            Toast.makeText(this,"Job cancelled",Toast.LENGTH_LONG).show();
//        }
        int resultCode = scheduler.schedule(jobInfo);
        if(resultCode == JobScheduler.RESULT_SUCCESS)
            Log.d("JOB Done","Scheduled successfully");
        else
            Log.d("JOB Done","not Scheduled successfully");
    }
    */


}
