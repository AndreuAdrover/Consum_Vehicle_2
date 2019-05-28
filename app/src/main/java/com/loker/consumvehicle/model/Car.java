package com.loker.consumvehicle.model;

import android.graphics.Bitmap;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Car {

    private String carName;
    private String UID;
    private float totalKm;
    private float inicialKm;
    private float tankLitres = 30;
    private String urlImageCar = null;
    @Exclude
    private transient Bitmap bitmapImageCar = null;

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

    public float getTankLitres() {
        return tankLitres;
    }

    public void setTankLitres(float tankLitres) {
        this.tankLitres = tankLitres;
    }

    public void setUrlImageCar(String urlImageCar){this.urlImageCar = urlImageCar;}

    public String getUrlImageCar(){ return urlImageCar;}

    @Exclude
    public Bitmap getBitmapImageCar() {
        return bitmapImageCar;
    }
    @Exclude
    public void setBitmapImageCar(Bitmap bitmapImageCar) {
        this.bitmapImageCar = bitmapImageCar;
    }

    //return de kms since we start to using the app
    public float calcKmsLogged(){
        return totalKm-inicialKm;
    }
    //total litres we strat with the inicial litres on tank and stop at next-to-last refill
    public float getTotalLitres(List<Refill> refills){
        float totalLitres = 0;//tankLitres;
        float litresLastRefill = 0;
        for(Refill refill: refills){
            if(refill.getCarName().equals(carName)){
                litresLastRefill = refill.getLitres();
                totalLitres=totalLitres+litresLastRefill;
            }

        }
        //we have to substract the litres of the last refill because it hasn't been spentyet
        totalLitres-=litresLastRefill;
        return totalLitres;
    }
    // stop at next-to-last refill
    public float getTotalEuros(List<Refill> refills){
        float totalEuros = 0;
        float refillCost = 0;
        for(Refill refill: refills){
            if(refill.getCarName().equals(carName)){
                refillCost = refill.getPrice();
                totalEuros+=refillCost;
            }
        }
        //substart last refill price
        totalEuros-=refillCost;
        return totalEuros;
    }

    public float getAVGLitres(List<Refill> refills){
        float avgLitres = 0;
        float totalKm = calcKmsLogged();//-getKmLastRefill(refills);
        float totalLitres = getTotalLitres(refills)+tankLitres;
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
    //return the Kms done between de last refill and the next to last refill
    private float getKmLastRefill(List<Refill> refills){
        float kmsLastREfill =0;
        int index = refills.size()-1;
        //get the next to last refill kms starting from last refills and going backwards
        boolean found = false;
        while(!found && index>=0){
            if(refills.get(index).getCarName().equals(carName)){
                //if the refill of the current car is not the last one it means it the next to last one
                if(refills.get(index).getKms()<totalKm){
                    found=true;
                    break;
                }
            }
            index--;
        }
        if(found){
            kmsLastREfill=totalKm-refills.get(index).getKms();
        }
        return kmsLastREfill;
    }


}
