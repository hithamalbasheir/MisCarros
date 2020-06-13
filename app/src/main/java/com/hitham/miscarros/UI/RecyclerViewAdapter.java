package com.hitham.miscarros.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitham.miscarros.Activities.DetailsActivity;
import com.hitham.miscarros.Activities.ListActivity;
import com.hitham.miscarros.Activities.MainActivity;
import com.hitham.miscarros.Data.DatabaseHandler;
import com.hitham.miscarros.Fragments.DialogPickerFragment;
import com.hitham.miscarros.Fragments.EditPickerFragment;
import com.hitham.miscarros.Model.Cars;
import com.hitham.miscarros.R;

import java.io.Serializable;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<Cars> misCarros;
    private AlertDialog dialog;
    private AlertDialog.Builder alertBuilder;
    private ViewHolder holder;
    private View view;
    private CardView cardView;
    public RecyclerViewAdapter(Context context, List<Cars> misCarros) {
        this.context = context;
        this.misCarros = misCarros;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row,parent,false);
        cardView = view.findViewById(R.id.cardView);
        holder = new ViewHolder(view,context);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Cars carro = misCarros.get(position);
        Bitmap pic;
        if(carro.getCarImage() != null) {
            pic = BitmapFactory.decodeFile(carro.getCarImage());
            holder.ImagenaDelCarro.setImageBitmap(pic); }
        holder.ManufacturarDelCarro.setText(carro.getCarManuf());
        holder.NombreDelCarro.setText(carro.getCarName());
        holder.NombreDelCarro.setAllCaps(true);
        holder.ModelDelCarro.setText(carro.getCarModel());
        holder.PlateDelCarro.setText(carro.getCarPlate());
        switch (carro.getCarStatus())
        {
            case 2:
                cardView.setCardBackgroundColor(view.getResources().getColor(R.color.green));
                holder.ManufacturarDelCarro.setTextColor(view.getResources().getColor(R.color.white));
                holder.NombreDelCarro.setTextColor(view.getResources().getColor(R.color.white));
                holder.ModelDelCarro.setTextColor(view.getResources().getColor(R.color.white));
                holder.PlateDelCarro.setTextColor(view.getResources().getColor(R.color.white));
                break;
            case 0:
                cardView.setCardBackgroundColor(view.getResources().getColor(R.color.black));
                holder.ManufacturarDelCarro.setTextColor(view.getResources().getColor(R.color.white));
                holder.NombreDelCarro.setTextColor(view.getResources().getColor(R.color.white));
                holder.ModelDelCarro.setTextColor(view.getResources().getColor(R.color.white));
                holder.PlateDelCarro.setTextColor(view.getResources().getColor(R.color.white));
                break;
            default:
                cardView.setCardBackgroundColor(view.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return misCarros.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ManufacturarDelCarro;
        public TextView NombreDelCarro;
        public TextView ModelDelCarro;
        public TextView PlateDelCarro;
        public ImageView ImagenaDelCarro;
        public Button editarElCarro;
        public Button deletarElCarro;
        public int id;
        View myView;
        public ViewHolder(View view, final Context ctx) {
            super(view);
            context = ctx;
            myView = view;
            ManufacturarDelCarro = view.findViewById(R.id.carManuf);
            NombreDelCarro = view.findViewById(R.id.carName);
            ModelDelCarro = view.findViewById(R.id.carDOB);
            PlateDelCarro = view.findViewById(R.id.carPlate);
            ImagenaDelCarro = view.findViewById(R.id.carImage);
            editarElCarro = view.findViewById(R.id.editCarID);
            deletarElCarro = view.findViewById(R.id.deleteCarID);
            editarElCarro.setOnClickListener(this);
            deletarElCarro.setOnClickListener(this);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Cars car = misCarros.get(position);
                    Intent miIntento = new Intent(ctx, DetailsActivity.class);
                    miIntento.putExtra("name", car.getCarName());
                    miIntento.putExtra("model", car.getCarModel());
                    miIntento.putExtra("manuf", car.getCarManuf());
                    miIntento.putExtra("position", car.getCarID());
                    miIntento.putExtra("imagePath",car.getCarImage());
                    miIntento.putExtra("mileage",car.getCarMileage());
                    miIntento.putExtra("oilChangeDate",car.getCarOilDate());
                    miIntento.putExtra("status",car.getCarStatus());
                    miIntento.putExtra("plate",car.getCarPlate());
                    ctx.startActivity(miIntento);

                }
            });

        }
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.editCarID:
                    Cars unCarro = misCarros.get(getAdapterPosition());
                    editCar(unCarro);
                    break;
                case R.id.deleteCarID:
                    int position = getAdapterPosition();
                    unCarro = misCarros.get(position);
                    deleteCar(unCarro.getCarID());
                    break;
            }

        }
        public void deleteCar (final int id)
        {
            final DatabaseHandler db = new DatabaseHandler(context);
            alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setTitle(R.string.confTitle);
           // alertBuilder.setIcon(android.R.drawable.alert_dark_frame);
            alertBuilder.setMessage(R.string.confMsg);
            alertBuilder.setNegativeButton(R.string.noBu, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertBuilder.setPositiveButton(R.string.yesBu, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.deleteCars(id);
                    misCarros.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                    if (db.getCarsCount() <= 0)
                    {
                        context.startActivity(new Intent(context,MainActivity.class));
                        ((Activity)context).finish();
                    }
                }
            });
            dialog = alertBuilder.create();
            dialog.show();

        }
        public void editCar(Cars car) {
            DialogFragment fragment = new EditPickerFragment();
            Bundle miIntento = new Bundle();
            miIntento.putInt("position", car.getCarID());
            miIntento.putString("name", car.getCarName());
            miIntento.putString("model", car.getCarModel());
            miIntento.putString("manuf", car.getCarManuf());
            miIntento.putString("imagePath", car.getCarImage());
            miIntento.putString("mileage", car.getCarMileage());
            miIntento.putString("oilChangeDate", car.getCarOilDate());
            miIntento.putInt("status", car.getCarStatus());
            miIntento.putString("plate", car.getCarPlate());
            fragment.setArguments(miIntento);
            FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
            fragment.show(fm, "edit car picker fragment");
            if (fragment.isDetached())
                notifyItemChanged(getAdapterPosition(), car);
        }
    }
}
