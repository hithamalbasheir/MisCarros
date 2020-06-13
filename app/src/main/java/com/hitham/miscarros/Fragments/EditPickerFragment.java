package com.hitham.miscarros.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hitham.miscarros.Activities.ListActivity;
import com.hitham.miscarros.Data.DatabaseHandler;
import com.hitham.miscarros.Model.Cars;
import com.hitham.miscarros.R;
import com.hitham.miscarros.UI.RecyclerViewAdapter;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class EditPickerFragment extends DialogFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 7;
    private Button imageBu;
    private Button submitBu;
    private ImageView carImage;
    private EditText carManuf;
    private EditText carName;
    private EditText carModel;
    private EditText carPlate;
    private String imagePath;
    private TextView carDetails;
    static private Cars car;
    private Context context;
    private RadioGroup radioGroup;
    private int status =1;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View myView = LayoutInflater.from(getActivity()).inflate(R.layout.new_car_popup,null);
        context = getActivity();
        setStyle(STYLE_NO_FRAME,getTheme()) ;
        bundle = getArguments();
        setDialog(myView);
        setCar();
        getImage();
        theChoice(myView);
        submitBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(context);
                if(!carManuf.getText().toString().isEmpty())
                    car.setCarManuf(carManuf.getText().toString());
                if(!carName.getText().toString().isEmpty())
                    car.setCarName(carName.getText().toString());
                if(!carModel.getText().toString().isEmpty())
                    car.setCarModel(carModel.getText().toString());
                if(!carPlate.getText().toString().isEmpty())
                    car.setCarPlate(carPlate.getText().toString());
                if(imagePath != null)
                    car.setCarImage(imagePath);
                if(status == 0 || status == 2)
                    car.setCarStatus(status);
                db.updateCars(car);
                Intent myIntent = new Intent(context, ListActivity.class);
                context.startActivity(myIntent);
                ((Activity)context).finish();
//                RecyclerViewAdapter adapter = new RecyclerViewAdapter(context,misCarros);
//                adapter.notifyItemChanged(car.getCarID(), car);
            }
        });
//        myView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        return myView;
    }


    private void getImage() {
        imageBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(), new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_IMAGE_CAPTURE);
                    }else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images
                                .Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent,REQUEST_IMAGE_CAPTURE);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setCar() {
        car = new Cars();
        car.setCarID(bundle.getInt("position"));
        car.setCarName(bundle.getString("name"));
        car.setCarModel(bundle.getString("model"));
        car.setCarManuf(bundle.getString("manuf"));
        car.setCarImage(bundle.getString("imagePath"));
        car.setCarMileage(bundle.getString("mileage"));
        car.setCarOilDate(bundle.getString("oilChangeDate"));
        car.setCarStatus(bundle.getInt("status"));
        car.setCarPlate(bundle.getString("plate"));
    }

    private void setDialog(View myView) {
        carManuf = myView.findViewById(R.id.nCarManuf);
        carName = myView.findViewById(R.id.nCarName);
        carModel = myView.findViewById(R.id.nCarDOB);
        carImage = myView.findViewById(R.id.laImagenaPuta);
        carDetails = myView.findViewById(R.id.carDetails);
        carPlate = myView.findViewById(R.id.nCarPlate);
        imageBu = myView.findViewById(R.id.addImageID);
        submitBu = myView.findViewById(R.id.addCarID);
        imageBu.setText(R.string.editImageText);
        submitBu.setText(R.string.editCarBu);
        carDetails.setText(R.string.editCarText);
        carManuf.setHint(R.string.manufHint);
        carName.setHint(R.string.nameHint);
        carModel.setHint(R.string.DOBHint);
        carPlate.setHint(R.string.plateHint);
    }

    private void theChoice(final View myView) {
        radioGroup = myView.findViewById(R.id.radioGroupID);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case (R.id.available):
                        status = 2;
                        break;
                    case (R.id.notAvailable):
                        status = 0;
                        break;
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK)
                {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor miCursor = getActivity().getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                    miCursor.moveToFirst();
                    int columnIndex = miCursor.getColumnIndex(filePathColumn[0]);
                    imagePath = miCursor.getString(columnIndex);
                    miCursor.close();
                    Bitmap laImagenaPuta = BitmapFactory.decodeFile(imagePath);
                    carImage.setImageBitmap(laImagenaPuta);
                }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_IMAGE_CAPTURE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent,REQUEST_IMAGE_CAPTURE);
                }else{
                    Toast.makeText(getActivity(),R.string.uAintGotPermit,Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }
}