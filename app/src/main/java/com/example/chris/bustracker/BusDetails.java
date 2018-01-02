package com.example.chris.bustracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.github.halcyon_daze.TranslinkTracker.BusStop;

/*
    Activity that shows the details of a bus when it is clicked in the listview in the mainActivity
 */
public class BusDetails extends AppCompatActivity {

    BusStop thisStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        TextView stopNoDetails = (TextView) findViewById(R.id.stopNoDetails);
        TextView routeNoDetails = (TextView) findViewById(R.id.routeNoDetails);
        TextView routeDetails = (TextView) findViewById(R.id.routeDetails);
        TextView directionDetails = (TextView) findViewById(R.id.directionDetails);
        TextView nextBusDetails = (TextView) findViewById(R.id.nextBusses);

        //gets intent passed with activity call, which is the index of the BusStop in the list
        Intent in = getIntent();
        int index = in.getIntExtra("BusStopPosition", 0);

        //gets the stop from the stop list from it's index
        thisStop = Singleton.getInstance().getStopList().get(index);

        //sets the info for the buses in the list
        stopNoDetails.setText("Stop #: " + thisStop.getStopNo());
        routeNoDetails.setText("Route #: " + thisStop.getRouteNo());
        routeDetails.setText("Route Name: " + thisStop.getRouteName());
        directionDetails.setText("Direction: " + thisStop.getDirection());
        nextBusDetails.setText("Next Times: \n" + thisStop.getNextDepartureTimes().toString().replace(",", "\n").replace("[", " ").replace("]", ""));

        // implements button to delete stops from the list
        Button deleteBusButton = (Button) findViewById(R.id.deleteBusButton);
        deleteBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().removeStop(thisStop);

                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }
}
