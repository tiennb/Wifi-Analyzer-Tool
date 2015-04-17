package com.apps.nbt.wifianalyzertool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apps.nbt.wifianalyzertool.R;
import com.apps.nbt.wifianalyzertool.activity.WifiConnector;

import java.util.ArrayList;

/**
 * Created by Tien Nguyen on 4/16/2015.
 */
public class ListWiFiAdapter extends BaseAdapter {

    private ArrayList<WifiConnector> mList;
    private LayoutInflater mInflater;

     public ListWiFiAdapter(Context mContext, ArrayList<WifiConnector> list){
        mList = list;
        mInflater = LayoutInflater.from(mContext);
     }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_list_wifi_layout,parent,false);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        WifiConnector wifi = mList.get(position);
        holder.tvWifiname.setText(wifi.getWifiName());

        return convertView;
    }

    class ViewHolder{
        TextView tvWifiname;

        public ViewHolder (View v){
            tvWifiname = (TextView) v.findViewById(R.id.tvWifiName);
        }
    }
}
