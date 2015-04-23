package com.apps.nbt.wifianalyzertool.fagment;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apps.nbt.wifianalyzertool.R;
import com.apps.nbt.wifianalyzertool.activity.WifiBroad;
import com.apps.nbt.wifianalyzertool.activity.WifiConnector;
import com.apps.nbt.wifianalyzertool.utility.ConstUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //
    private View root;
    private Context mContext;
    RadarView radarView;

    private UIHandler mHandler;
    private WifiManager wifiManager;
    private WifiBroad wifiBroad;
    private List<ScanResult> wifiScanList;
    private AutoWifiScanner autoWifiScanner;
    private ArrayList<WifiConnector> mWifiList;
    private Timer timer;

//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BlankFragment() {
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
        root = LayoutInflater.from(mContext).inflate(R.layout.fragment_blank, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUI();
    }

    public void initUI() {


        radarView = (RadarView) root.findViewById(R.id.radarView);

        //
        mHandler = new UIHandler();
        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wifiBroad = new WifiBroad();
        wifiBroad.bindHandler(mHandler);

        //Timer
        timer = new Timer();
        autoWifiScanner = new AutoWifiScanner();
        timer.scheduleAtFixedRate(autoWifiScanner, 0, 10000); //auto scan 10s
        mWifiList = new ArrayList<>();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;
    }

    @Override
    public void onResume() {
        super.onResume();

        radarView.startSweep();

        wifiManager.startScan();

        mContext.registerReceiver(wifiBroad, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        radarView.stopSweep();
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
                    mWifiList.clear();

                    for (ScanResult result : wifiScanList) {

                        Random random = new Random();
                        WifiConnector wifi = new WifiConnector();
                        wifi.setWifiName(result.SSID);
                        int dis = calculateDistance2(result.level);
                        wifi.setDistance(dis > 80 ? 60 : dis);
                        wifi.setAngle(random.nextInt(360));
                        wifi.setRSSI(result.level);
                        wifi.setConnected(isWifiConnected(mContext,wifiManager ,result.BSSID));

                        Log.d("Tiennb", "RSS: " + result.level + " SSID: " + wifi.getWifiName() + " Dis: " + wifi.getDistance());

                        mWifiList.add(wifi);
                    }

                    Toast.makeText(mContext, "update", Toast.LENGTH_SHORT).show();

                    radarView.updateRadar(mWifiList);

                    break;
                case ConstUtil.AUTO_SCAN_TIMER:
                    wifiManager.startScan();
                    break;
                default:
                    break;
            }
        }
    }

    public int calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return (int) Math.pow(10.0, exp);
    }

    public int calculateDistance2(double rss) {
        double exp = (rss + 44) / (-10 * 2);
        return (int) Math.pow(10.0, exp);
    }

    public boolean isWifiConnected(Context context, WifiManager manager, String bssid) {

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo.isConnected()) {
            final WifiInfo connectionInfo = manager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getBSSID()) && connectionInfo.getBSSID().equals(bssid)) {
                Log.d("Tiennb", connectionInfo.getBSSID() + "  " + bssid);
                return true;
            }
        }
        return false;
    }

    //    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

}
