package com.hitham.miscarros.Model;

import java.util.Date;

public class Cars {
    private int carID;
    private String carManuf;
    private String carName;
    private String carModel;
    private String carImage;
    private String carMileage;
    private String carOilDate;
    private String carPlate;
    private int carStatus;

    public Cars(int carID, String carManuf, String carName, String carModel, String carImage, String carMileage, String carOilDate, String carPlate, int carStatus) {
        this.carID = carID;
        this.carManuf = carManuf;
        this.carName = carName;
        this.carModel = carModel;
        this.carImage = carImage;
        this.carMileage = carMileage;
        this.carOilDate = carOilDate;
        this.carPlate = carPlate;
        this.carStatus = carStatus;
    }

    public Cars(){}

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getCarManuf() {
        return carManuf;
    }

    public void setCarManuf(String carManuf) {
        this.carManuf = carManuf;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getCarMileage() {
        return carMileage;
    }

    public void setCarMileage(String carMileage) {
        this.carMileage = carMileage;
    }

    public String getCarOilDate() {
        return carOilDate;
    }

    public void setCarOilDate(String carOilDate) {
        this.carOilDate = carOilDate;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public int getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(int carStatus) {
        this.carStatus = carStatus;
    }

}
