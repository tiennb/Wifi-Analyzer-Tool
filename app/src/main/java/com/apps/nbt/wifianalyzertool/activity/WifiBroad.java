package com.apps.nbt.wifianalyzertool.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.apps.nbt.wifianalyzertool.utility.ConstUtil;

import android.os.Handler;

/**
 * Created by Tien Nguyen on 4/16/2015.
 */
public class WifiBroad extends BroadcastReceiver {

    private Handler mHandler;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {

            mHandler.sendEmptyMessage(ConstUtil.RSSI_CHANGED_ACTION);

        } else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

            if (info.getState().equals(NetworkInfo.State.DISCONNECTED) && null != mHandler) {

                mHandler.sendEmptyMessage(ConstUtil.WIFI_DISCONNECTED);

            } else if (info.isConnected() && null != mHandler) {

                mHandler.sendEmptyMessage(ConstUtil.WIFI_CONNECTED);
            }

        } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

            if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                mHandler.sendEmptyMessage(ConstUtil.WIFI_STATE_DISABLED);
            } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                mHandler.sendEmptyMessage(ConstUtil.WIFI_STATE_ENABLED);
            }
        } else if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            mHandler.sendEmptyMessage(ConstUtil.SCAN_RESULTS_AVAILABLE_ACTION);
        }
    }

    public synchronized void bindHandler(Handler handler) {
        this.mHandler = handler;
    }

    public synchronized void unbindHandler() {
        this.mHandler = null;
    }
}
