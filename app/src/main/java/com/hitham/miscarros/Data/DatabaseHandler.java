package com.hitham.miscarros.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hitham.miscarros.Model.Cars;
import com.hitham.miscarros.Util.Constants;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    Context ctx;
    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null,Constants.DB_VERSION);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+ Constants.DB_TABLE + "(" +Constants.KEY_ID +
                " INTEGER PRIMARY KEY, " + Constants.KEY_MANUF
                +" TEXT, " + Constants.KEY_NAME +" TEXT, "+ Constants.KEY_MODEL +" INTEGER, "+
                Constants.KEY_IMAGE+" TEXT, " + Constants.KEY_MILEAGE +" TEXT, "+
                Constants.KEY_OILDATE +" TEXT, "+Constants.KEY_PLATE+" TEXT, "+Constants.KEY_STATUS+" INTEGER );";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Constants.DB_TABLE);
        onCreate(db);
    }
    public void addCar(Cars carro){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_NAME,carro.getCarName());
        values.put(Constants.KEY_MODEL,carro.getCarModel());
        values.put(Constants.KEY_MANUF,carro.getCarManuf());
        values.put(Constants.KEY_IMAGE,carro.getCarImage());
        values.put(Constants.KEY_MILEAGE,carro.getCarMileage());
        values.put(Constants.KEY_OILDATE,carro.getCarOilDate());
        values.put(Constants.KEY_STATUS,carro.getCarStatus());
        values.put(Constants.KEY_PLATE,carro.getCarPlate());
        // the insertion of the row
        db.insert(Constants.DB_TABLE,null,values);
    }
    // get the cars
    public Cars getCars(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor miCursor = db.query(Constants.DB_TABLE,new String[]{Constants.KEY_ID,Constants.KEY_MANUF,Constants.KEY_NAME,Constants.KEY_MODEL,Constants.KEY_IMAGE}
        ,Constants.KEY_ID + "=?",new String[]{String.valueOf(id)},null,null,null,null);
        if (miCursor != null)
            miCursor.moveToFirst();

            Cars miCarro = new Cars();
            miCarro.setCarID(Integer.parseInt(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_ID))));
            miCarro.setCarManuf(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_MANUF)));
            miCarro.setCarName(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_NAME)));
            miCarro.setCarModel(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_MODEL)));
            miCarro.setCarImage(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_IMAGE)));
            miCarro.setCarMileage(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_MILEAGE)));
            miCarro.setCarOilDate(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_OILDATE)));
            miCarro.setCarStatus(miCursor.getInt(miCursor.getColumnIndex(Constants.KEY_STATUS)));
            miCarro.setCarPlate(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_PLATE)));
            return miCarro;
    }
    //list all cars
    public List<Cars> getAllCars(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Cars> misCarros= new ArrayList<>();
        Cursor miCursor = db.query(Constants.DB_TABLE,new String[]{Constants.KEY_ID,Constants.KEY_MANUF,Constants.KEY_NAME,Constants.KEY_MODEL,Constants.KEY_IMAGE,Constants.KEY_MILEAGE,Constants.KEY_OILDATE,Constants.KEY_PLATE,Constants.KEY_STATUS}
                ,null,null,null,null,Constants.KEY_STATUS+" DESC, "+Constants.KEY_MANUF+" ASC;");
        if (miCursor.moveToFirst())
        {
            do{
                Cars miCarro = new Cars();
                miCarro.setCarID(miCursor.getInt(miCursor.getColumnIndex(Constants.KEY_ID)));
                miCarro.setCarManuf(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_MANUF)));
                miCarro.setCarName(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_NAME)));
                miCarro.setCarModel(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_MODEL)));
                miCarro.setCarImage(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_IMAGE)));
                miCarro.setCarMileage(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_MILEAGE)));
                miCarro.setCarOilDate(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_OILDATE)));
                miCarro.setCarStatus(miCursor.getInt(miCursor.getColumnIndex(Constants.KEY_STATUS)));
                miCarro.setCarPlate(miCursor.getString(miCursor.getColumnIndex(Constants.KEY_PLATE)));
                // add the shitty Car to the big list :)
                misCarros.add(miCarro);
            }while(miCursor.moveToNext());
        }
        return misCarros;
    }
    //update Cars
    public int updateCars(Cars carros){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_MANUF,carros.getCarManuf());
        values.put(Constants.KEY_NAME,carros.getCarName());
        values.put(Constants.KEY_MODEL,carros.getCarModel());
        values.put(Constants.KEY_IMAGE,carros.getCarImage());
        values.put(Constants.KEY_MILEAGE,carros.getCarMileage());
        values.put(Constants.KEY_OILDATE,carros.getCarOilDate());
        values.put(Constants.KEY_PLATE,carros.getCarPlate());
        values.put(Constants.KEY_STATUS,carros.getCarStatus());
        return db.update(Constants.DB_TABLE,values,Constants.KEY_ID + "=?",new String[] {String.valueOf(carros.getCarID())});
    }
    //delete cars
    public void deleteCars(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.DB_TABLE,Constants.KEY_ID + "=?",new String[] {String.valueOf(id)});
        db.close();
    }
    //get cars count
    public int getCarsCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor miCursor = db.rawQuery("SELECT * FROM "+Constants.DB_TABLE,null);
        return miCursor.getCount();
    }
}
