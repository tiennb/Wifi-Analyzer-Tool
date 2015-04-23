package com.apps.nbt.wifianalyzertool.activity;

/**
 * Created by Tien Nguyen on 4/16/2015.
 */
public class WiFi {

    private String wifiName;
    private String MACAdress;
    private String security;
    private int RSSI;
    private int frequency;
    private String timeTemp;

    //by calculator
    private String vendor;
    private int noiseSignal; //min signal
    private int SNR; //Singal-to-Noise ratio
    private int distance;
    private int angle; // random

    //Get

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getWifiName() {
        return wifiName;
    }

    public String getMACAdress() {
        return MACAdress;
    }

    public String getSecurity() {
        return security;
    }

    public int getRSSI() {
        return RSSI;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getTimeTemp() {
        return timeTemp;
    }

    public String getVendor() {
        return vendor;
    }

    public int getNoiceSignal() {
        return noiseSignal;
    }

    public int getSNR() {
        return SNR;
    }
    //Set

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public void setMACAdress(String MACAdress) {
        this.MACAdress = MACAdress;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public void setRSSI(int RSSI) {
        this.RSSI = RSSI;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setTimeTemp(String timeTemp) {
        this.timeTemp = timeTemp;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setNoiceSignal(int noiceSignal) {
        this.noiseSignal = noiceSignal;
    }

    public void setSNR(int s) {
        this.SNR = s;
    }
}
