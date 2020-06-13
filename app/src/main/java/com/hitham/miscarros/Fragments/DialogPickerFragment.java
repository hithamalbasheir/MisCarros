package com.hitham.miscarros.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hitham.miscarros.Activities.ListActivity;
import com.hitham.miscarros.Model.Cars;
import com.hitham.miscarros.R;

import static android.app.Activity.RESULT_OK;

public class DialogPickerFragment extends DialogFragment {
    //this is the interface which is used to transfer the data from this class to the activity
    public interface OnInputListener {
        void sendData(String carManuf,String carName,String carModel,String carImage,String carPlate,int carStatus,View v);
    }
    public OnInputListener onInputListener;//the object of the interface
    //Variables
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button imageBu;
    private Button submitBu;
    private ImageView carImage;
    private EditText carManuf;
    private EditText carName;
    private EditText carModel;
    private EditText carPlate;
    private String imagePath;
    private Cars car;
    private RadioGroup radioGroup;
    private int status = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //here is the view which is used to access the layout to use it ;)
        final View myView = LayoutInflater.from(getActivity()).inflate(R.layout.new_car_popup,null);
        //the layout widgets of course
        carManuf = myView.findViewById(R.id.nCarManuf);
        carName = myView.findViewById(R.id.nCarName);
        carModel = myView.findViewById(R.id.nCarDOB);
        carPlate = myView.findViewById(R.id.nCarPlate);
        carImage = myView.findViewById(R.id.laImagenaPuta);
        imageBu = myView.findViewById(R.id.addImageID);
        submitBu = myView.findViewById(R.id.addCarID);
        //the method to get the image from the user by invoking startActivityForResult method
        getImage();
        //getting the user's choice on the car status (Available/Not)
        theChoice(myView);
        submitBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String manuf = carManuf.getText().toString();
                String name = carName.getText().toString();
                String model = carModel.getText().toString();
                String plate = carPlate.getText().toString();
                //invoking the interface method to send the data to the activity in order to save it
                onInputListener.sendData(manuf,name,model,imagePath,plate,status,v);
            }
        });
        ConstraintLayout.LayoutParams layoutParam = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        myView.setLayoutParams(layoutParam);
        return myView;
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
    private void getImage() {
        imageBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //Requesting to access the device storage to get the image
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
                    //Storing the image path to save it in the database
                    imagePath = miCursor.getString(columnIndex);
                    miCursor.close();
                    Bitmap laImagenaPuta = BitmapFactory.decodeFile(imagePath);
                    //Previewing the image to the user to ensure he picked the right image
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e("maTag","on Attach : class cast exception");
        }
    }

}
