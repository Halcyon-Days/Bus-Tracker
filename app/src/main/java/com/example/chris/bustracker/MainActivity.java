package com.example.chris.bustracker;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import io.github.halcyon_daze.TranslinkTracker.BusStop;
import io.github.halcyon_daze.TranslinkTracker.TranslinkTracker;

public class MainActivity extends AppCompatActivity {

    private TextView BusIDText;
    private TextView NextTimeText;
    private TextView lastUpdatedText;
    ListView nextTimesList;
    int refreshTimer;
    private int progressStatus = 0;
    private Handler progressHandler = new Handler();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nextTimesList = (ListView) findViewById(R.id.nextTimesList);
        BusIDText = (TextView) findViewById(R.id.BusIDText);
        NextTimeText = (TextView) findViewById(R.id.NextTimeText);
        lastUpdatedText = (TextView) findViewById(R.id.lastUpdated);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Creates adapter which shows the details of a bus when it is clicked in the listview
        BusTimeAdapter busAdapter = new BusTimeAdapter(this, Singleton.getInstance().getStopList());
        nextTimesList.setAdapter(busAdapter);
        nextTimesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showBusDetailsActivity = new Intent(getApplicationContext(), BusDetails.class);
                showBusDetailsActivity.putExtra("BusStopPosition", position);
                startActivity(showBusDetailsActivity);
            }
        });

        //sets text at bottom that shows the last updated time
        lastUpdatedText.setText("Last updated: " + Calendar.getInstance().getTime().toString());

        //sets timer for refreshing the stop in the list based on the settings
        refreshTimer = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("example_text", "5")) * 60 * 1000; //refresh timer set to 1 minutes initially
        if(refreshTimer < 1000) {                       //if refresh rate is shorter than a minute, set to a minute
            refreshTimer = 1000;
        } else if (refreshTimer > 1000*60*60*24) {      //if refresh rate is longer than a day, set it to one day
            refreshTimer = 1000*60*60*2;
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        // Sets button which changes views when adding a new bus
        FloatingActionButton newBusButton = (FloatingActionButton) findViewById(R.id.addNewBusBtn);
        newBusButton.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View v) {
              Intent startIntent = new Intent(getApplicationContext(), Main2Activity.class);
              startActivity(startIntent);
          }
        });

        //sets button to refresh all the stop in the list when clicked
        FloatingActionButton refreshButton = (FloatingActionButton) findViewById(R.id.refreshBtn);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshStops();
            }
        });

        //handler for periodically refreshing the stops in the list according to the refreshtimer value
        final Handler refreshHandler = new Handler();
        Runnable refreshCode = new Runnable() {
            @Override
            public void run() {

                if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("example_switch", true))
                {
                    refreshStops();
                }
                progressStatus = 0;
                refreshHandler.postDelayed(this, refreshTimer);
            }
        };

        //starts refresh handler
        refreshHandler.post(refreshCode);

        //new thread that updates the progress bar on the bottom of the view
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 100) {
                    if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("example_switch", true))
                    {
                        progressStatus += 1;
                    }
                    progressHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });

                    try {
                        Thread.sleep(refreshTimer/100);     //thread sleeps for 100th of the refreshTimer value, and repeats 100 times to fill the bar
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }}).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent startIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(startIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();

        BusTimeAdapter busAdapter = new BusTimeAdapter(this, Singleton.getInstance().getStopList());
        nextTimesList.setAdapter(busAdapter);
    }

    public void refreshStops() {
        //starts asynchronous task to update all stops in the list
        new AsyncBusStop().execute( );
    }


    private class AsyncBusStop extends AsyncTask<Void, Void, ArrayList<BusStop>> {
        protected ArrayList<BusStop> doInBackground(Void ... input) {
            ArrayList<BusStop> newList = new ArrayList<>();

            try {
                //for each stop in the list, gets the stop number, requests its stop info from the translink api, and adds it to a new list
                for(BusStop s: Singleton.getInstance().getStopList()) {
                    newList.add(TranslinkTracker.getRouteInfo(getApplicationContext(), s.getStopNo()));
                }

                //updates the list in the Singleton with the new list of updated stops
                Singleton.getInstance().changeStopList(newList);
            }  catch (IOException e) {
            }

            return newList;
        }

        protected void onPostExecute(ArrayList<BusStop> stopList) {
            if(stopList != null) {
                //changes listview to display new stops
                BusTimeAdapter busAdapter = new BusTimeAdapter(getApplicationContext(), Singleton.getInstance().getStopList());
                nextTimesList.setAdapter(busAdapter);

                //updates last updated text at bottom of view
                lastUpdatedText.setText("Last updated: " + Calendar.getInstance().getTime().toString());
            } else {
            }

        }
    }
}
