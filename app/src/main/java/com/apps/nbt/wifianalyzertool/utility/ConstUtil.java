package com.apps.nbt.wifianalyzertool.utility;

/**
 * Created by Tien Nguyen on 4/16/2015.
 */
public class ConstUtil {

    private static final int MSG_BASE = 1000;

    public static final int WIFI_DISCONNECTED = MSG_BASE + 1;
    public static final int WIFI_CONNECTED = MSG_BASE + 2;
    public static final int RSSI_CHANGED_ACTION = MSG_BASE + 3;
    public static final int WIFI_STATE_DISABLED = MSG_BASE + 4;
    public static final int WIFI_STATE_ENABLED = MSG_BASE + 5;
    public static final int SCAN_RESULTS_AVAILABLE_ACTION = MSG_BASE + 6;
    public static final int AUTO_SCAN_TIMER = MSG_BASE + 7;

}
