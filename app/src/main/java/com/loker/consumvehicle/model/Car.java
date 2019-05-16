package com.loker.consumvehicle.model;

import android.util.Log;

import java.sql.Ref;
import java.util.List;

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

    //return de kms since we start to using the app
    public float calcKmsLogged(){
        return totalKm-inicialKm;
    }

    public float getTotalLitres(List<Refill> refills){
        float totalLitres = 0;
        for(Refill refill: refills){
            if(refill.getCarName().equals(carName)){
                totalLitres=totalLitres+refill.getLitres();
            }
        }
        return totalLitres;
    }
    public float getTotalEuros(List<Refill> refills){
        float totalEuros = 0;
        for(Refill refill: refills){
            if(refill.getCarName().equals(carName)){
                totalEuros+=refill.getPrice();
            }
        }
        return totalEuros;
    }

    public float getAVGLitres(List<Refill> refills){
        float avgLitres = 0;

        float totalKm = calcKmsLogged();
        float totalLitres = getTotalLitres(refills);
        if(totalKm>0)
            avgLitres =(totalLitres/totalKm)*100;
        return avgLitres;
    }

    public float getAVGEuros(List<Refill> refills){
        float avgEuros = 0;
        float totalEuros = getTotalEuros(refills);
        float totalKm =  calcKmsLogged();
        if(totalKm>0)
            avgEuros = (totalEuros/totalKm)*100;
        return avgEuros;
    }
}
