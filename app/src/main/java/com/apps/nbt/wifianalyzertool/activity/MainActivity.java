package com.apps.nbt.wifianalyzertool.activity;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.nbt.wifianalyzertool.R;
import com.apps.nbt.wifianalyzertool.adapter.WifiFragmentAdapter;
import com.apps.nbt.wifianalyzertool.fagment.ListWifiScannerFragment;
import com.apps.nbt.wifianalyzertool.utility.ConstUtil;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //UI
    private WifiFragmentAdapter mAdapter;
    private ViewPager mPager;
    private PageIndicator mIndicator;

    // //Fucstions
    private UIHandler mHandler;
    private WifiManager wifiManager;
    private WifiBroad wifiBroad;
    private List<ScanResult> wifiScanList;
    private TextView tvTitleMenu, tvSettingMenu;
    private ImageView imvReload;

    private AutoWifiScanner autoWifiScanner;
    private Timer timer;

    private List<Fragment> lst ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wifi_scan);

        initUI();
    }

    public void initUI() {

        lst = getFragments("hihi");

        mAdapter = new WifiFragmentAdapter(getSupportFragmentManager(), lst);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (IconPageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        tvTitleMenu = (TextView) findViewById(R.id.tvTitleMenu);
        imvReload = (ImageView) findViewById(R.id.imvReload);

        tvTitleMenu.setText("Wi-Fi");
        imvReload.setOnClickListener(this);

        //
        mHandler = new UIHandler();
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiBroad = new WifiBroad();
        wifiBroad.bindHandler(mHandler);

        //Timer
        timer = new Timer();
        autoWifiScanner = new AutoWifiScanner();
        timer.scheduleAtFixedRate(autoWifiScanner, 0, 10000); //auto scan 10s

    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiManager.startScan();

        registerReceiver(wifiBroad, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
        unregisterReceiver(wifiBroad);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        wifiBroad.unbindHandler();
    }

    //Methods
    private List<Fragment> getFragments(String name) {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(ListWifiScannerFragment.newInstance(name, ""));

        return fList;
    }
    private class AutoWifiScanner extends TimerTask {
        @Override
        public void run() {

            mHandler.sendEmptyMessage(ConstUtil.AUTO_SCAN_TIMER);

        }
    }

    private class UIHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ConstUtil.SCAN_RESULTS_AVAILABLE_ACTION:
//                    wifiScanList = wifiManager.getScanResults();

                    break;
                case ConstUtil.AUTO_SCAN_TIMER:
//                    wifiManager.startScan();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvReload:

                wifiManager.startScan();

                break;

            default:
                break;
        }
    }
}
