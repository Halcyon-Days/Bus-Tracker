package com.example.chris.bustracker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import io.github.halcyon_daze.TranslinkTracker.BusStop;
import io.github.halcyon_daze.TranslinkTracker.TranslinkTracker;

public class Main2Activity extends AppCompatActivity {

    TextView returnText;
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        returnText = (TextView) findViewById(R.id.returnTxt);
        searchText = (EditText) findViewById(R.id.searchBar);

        //adds search button to right of textbox, which starts asynchronous task when clicked
        FloatingActionButton searchBtn = (FloatingActionButton) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncBusStop().execute(getApplicationContext() );
            }
        });
    }

    //asynchronous task to find stop info for given route number
    private class AsyncBusStop extends AsyncTask<Context, Void, BusStop> {
        protected BusStop doInBackground(Context ... input) {
            BusStop newStop = null;

            try {
                //gets route info for given route number
                newStop = TranslinkTracker.getRouteInfo( input[0], searchText.getText().toString());
                return newStop;
            }  catch (IOException e) {
            }

            return newStop;
        }

        protected void onPostExecute(BusStop stop) {
            //updates text boxes based on result of searching for stop
            if(stop != null) {
                if(!Singleton.getInstance().isStopInList(stop.getStopNo())) {
                    returnText.setText("Added " + stop.getStopNo() + " to list!");

                    //adds stop to Singleton list
                    Singleton.getInstance().addStop(stop);
                } else {
                    returnText.setText("Stop has already been added to List!" );
                }
            } else {
                returnText.setText("Error finding stop!" );
            }

        }
    }
}
