package com.apps.nbt.wifianalyzertool.fagment;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.apps.nbt.wifianalyzertool.R;
import com.apps.nbt.wifianalyzertool.activity.WifiBroad;
import com.apps.nbt.wifianalyzertool.activity.WifiConnector;
import com.apps.nbt.wifianalyzertool.adapter.ListWiFiAdapter;
import com.apps.nbt.wifianalyzertool.utility.ConstUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ListWifiScannerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //UI
    private View root;
    private ListView listViewWifi;
    private Context mContext;

    //Prams
    private ArrayList<WifiConnector> mListWifiConnectors;
    private ListWiFiAdapter mAdapter;
    private UIHandler mHandler;
    private WifiManager wifiManager;
    private WifiBroad wifiBroad;
    private List<ScanResult> wifiScanList;
    private AutoWifiScanner autoWifiScanner;
    private Timer timer;


//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListWifiScannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListWifiScannerFragment newInstance(String param1, String param2) {
        ListWifiScannerFragment fragment = new ListWifiScannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListWifiScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = LayoutInflater.from(mContext).inflate(R.layout.fragment_list_wifi_scanner, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUI();
    }

    public TextView textView;

    public void initUI() {
        listViewWifi = (ListView) root.findViewById(R.id.lstWifi);

        //
        mHandler = new UIHandler();
        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wifiBroad = new WifiBroad();
        wifiBroad.bindHandler(mHandler);

        //Timer
        timer = new Timer();
        autoWifiScanner = new AutoWifiScanner();
        timer.scheduleAtFixedRate(autoWifiScanner, 0, 10000); //auto scan 10s

        //
        mListWifiConnectors = new ArrayList<WifiConnector>();
        mAdapter = new ListWiFiAdapter(mContext, mListWifiConnectors);
        listViewWifi.setAdapter(mAdapter);


    }

    public void updateList() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        wifiManager.startScan();

        mContext.registerReceiver(wifiBroad, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
        mContext.unregisterReceiver(wifiBroad);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        wifiBroad.unbindHandler();
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
                    wifiScanList = wifiManager.getScanResults();
                    mListWifiConnectors.clear();

                    for (ScanResult result : wifiScanList) {
                        WifiConnector wifi = new WifiConnector();
                        wifi.setWifiName(result.SSID);

                        mListWifiConnectors.add(wifi);
                    }

                    mAdapter.notifyDataSetChanged();

                    break;
                case ConstUtil.AUTO_SCAN_TIMER:
                    wifiManager.startScan();
                    break;
                default:
                    break;
            }
        }
    }
}
