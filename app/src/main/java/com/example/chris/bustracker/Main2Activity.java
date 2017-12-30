package com.example.chris.bustracker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import io.github.halcyon_daze.TranslinkTracker.BusStop;
import io.github.halcyon_daze.TranslinkTracker.TranslinkTracker;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FloatingActionButton searchBtn = (FloatingActionButton) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncBusStop().execute(findViewById(R.id.returnTxt), findViewById(R.id.searchBar), getApplicationContext() );
            }
        });
    }

    private static class AsyncBusStop extends AsyncTask<Object, Void, BusStop> {
        protected BusStop doInBackground(Object ... input) {
            BusStop newStop = null;
            final TextView returnText = (TextView) input[0];
            final EditText searchText = (EditText) input[1];

            try {
                newStop = TranslinkTracker.getRouteInfo((Context) input[2], Integer.parseInt(searchText.getText().toString()));
                returnText.setText("Next bus #" + newStop.getRouteNo() + " coming at " + newStop.getNextDepartureTimes().get(0));
                return newStop;
            } catch (FileNotFoundException e) {
                returnText.setText("Invalid Api Key!");
            }  catch (UnsupportedEncodingException e) {
                returnText.setText("Parsing failed!");
            } catch (IOException e) {
                returnText.setText("Something went wrong!");
            }

            return newStop;
        }

        protected void onPostExecute(BusStop stop) {
            if(stop == null) {
            }
        }
    }
}
