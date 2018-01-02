package com.example.chris.bustracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.halcyon_daze.TranslinkTracker.BusStop;

/**
 * Created by Chris on 2017-12-31.
 */

public class BusTimeAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ArrayList<BusStop> stopList;

    public BusTimeAdapter(Context c, ArrayList<BusStop> stopList) {
        this.stopList = stopList;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return stopList.size();
    }

    @Override
    public Object getItem(int position) {
        return stopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.stop_list_detail, null);
        TextView routeNoTextView = (TextView) v.findViewById(R.id.detailsRow1);
        TextView nextTimeTextView = (TextView) v.findViewById(R.id.nextTime);
        TextView laterTimeTextView = (TextView) v.findViewById(R.id.laterTime);

        routeNoTextView.setText(stopList.get(position).getStopNo());
        if(stopList.get(position).getNextDepartureTimes().size() > 0) {
            nextTimeTextView.setText(stopList.get(position).getRouteNo() + " at " + stopList.get(position).getNextDepartureTimes().get(0).split(" ")[0]);
        }

        if(stopList.get(position).getNextDepartureTimes().size() > 1) {
            laterTimeTextView.setText("The " + stopList.get(position).getRouteNo()+ " bus after will arrive at " + stopList.get(position).getNextDepartureTimes().get(1).split(" ")[0]);
        }

        return v;
    }
}
