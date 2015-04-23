package com.apps.nbt.wifianalyzertool.activity;

/**
 * Created by Tien Nguyen on 4/16/2015.
 */
public class WifiConnector extends WiFi {

    private String IPAddress;
    private String subnetMark;
    private String defaultGateway;
    private String linkSpeed;
    private boolean isConnected;

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getSubnetMark() {
        return subnetMark;
    }

    public void setSubnetMark(String subnetMark) {
        this.subnetMark = subnetMark;
    }

    public String getDefaultGateway() {
        return defaultGateway;
    }

    public void setDefaultGateway(String defaultGateway) {
        this.defaultGateway = defaultGateway;
    }

    public String getLinkSpeed() {
        return linkSpeed;
    }

    public void setLinkSpeed(String linkSpeed) {
        this.linkSpeed = linkSpeed;
    }
}
