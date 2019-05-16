package com.loker.consumvehicle.model;

public class Car {

    private String carName;
    private String UID;
    private float totalKm;
    private float inicialKm;

    public Car(){}

    public Car(String carName, String UID, float inicialKm) {
        this.carName = carName;
        this.UID = UID;
        this.inicialKm = inicialKm;
        this.totalKm = inicialKm;
    }

    public Car(String carName, String UID, float inicialKm, float totalKm) {
        this.carName = carName;
        this.UID = UID;
        this.totalKm = totalKm;
        this.inicialKm = inicialKm;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public float getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(float totalKm) {
        this.totalKm = totalKm;
    }

    public float getInicialKm() {
        return inicialKm;
    }

    public void setInicialKm(float inicialKm) {
        this.inicialKm = inicialKm;
    }
}
