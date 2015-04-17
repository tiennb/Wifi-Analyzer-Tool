package com.apps.nbt.wifianalyzertool.activity;

/**
 * Created by Tien Nguyen on 4/16/2015.
 */
public class WifiConnector extends WiFi {

    private String IPAdress;
    private String subnetMark;
    private String defaultGateway;
    private String linkSpeed;

    public String getIPAdress() {
        return IPAdress;
    }

    public void setIPAdress(String IPAdress) {
        this.IPAdress = IPAdress;
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
