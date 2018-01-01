package com.example.chris.bustracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import io.github.halcyon_daze.TranslinkTracker.BusStop;

public class MainActivity extends AppCompatActivity {

    private TextView BusIDText;
    private TextView NextTimeText;
    private TextView lastUpdatedText;
    ListView nextTimesList;

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

        Singleton.getInstance().addStopList(new BusStop("123", "123", "123", "123", new ArrayList<String>()));//

        BusTimeAdapter busAdapter = new BusTimeAdapter(this, Singleton.getInstance().getStopList());
        nextTimesList.setAdapter(busAdapter);

        lastUpdatedText.setText(String.valueOf(Singleton.getInstance().getStopList().size()));//

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        FloatingActionButton newBusButton = (FloatingActionButton) findViewById(R.id.addNewBusBtn);
        newBusButton.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View v) {
              Intent startIntent = new Intent(getApplicationContext(), Main2Activity.class);
              startActivity(startIntent);
          }
        });
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
            return true;
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

        lastUpdatedText.setText(String.valueOf(Singleton.getInstance().getStopList().size()));
        BusTimeAdapter busAdapter = new BusTimeAdapter(this, Singleton.getInstance().getStopList());
        nextTimesList.setAdapter(busAdapter);
    }

    public void displayStops() {

    }
}
