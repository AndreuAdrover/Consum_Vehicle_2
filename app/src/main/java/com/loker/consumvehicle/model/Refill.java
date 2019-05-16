package com.loker.consumvehicle.model;

import java.util.Date;

public class Refill {

    private float kms;
    private float litres;
    private String fuelType;
    private float price;
    private Date date;
    private String carName;
    private String uid;//owner of car

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Refill(){

    }

    public Refill(String carName, float kms, float litres, String fuelType, float price, Date date) {
        this.carName = carName;
        this.kms = kms;
        this.litres = litres;
        this.price = price;
        this.date = date;
        this.fuelType = fuelType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public float getKms() {
        return kms;
    }

    public void setKms(float kms) {
        this.kms = kms;
    }

    public float getLitres() {
        return litres;
    }

    public void setLitres(float litres) {
        this.litres = litres;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCarName(){
        return carName;
    }

    public void setCarName(String carName){
        this.carName = carName;
    }
}
