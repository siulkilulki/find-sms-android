package com.example.siulkilulki.findsmsmessage;

import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;


public class SmsListActivity extends AppCompatActivity {
    public static final String QUERY_KEY = "query";
    public static final int SMS_QUERY_LOADER = 0;

    // The adapter that binds our data to the ListView
    private SmsAdapter mSmsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queried_message_list);
        Intent intent = getIntent();
        String query = intent.getStringExtra(MainActivity.PHRASE_KEY);
        // Toast.makeText(this, query, Toast.LENGTH_SHORT).show();

        // Bundle containg query string
        Bundle bundle = new Bundle();
        bundle.putString(QUERY_KEY, query);

        // Initialize the adapter
        // we pass a 'null' Cursor as the
        // third argument. We will pass the adapter a Cursor only when the
        // data has finished loading for the first time (for example when the
        // LoaderManager delivers the data to onLoadFinished)

        mSmsAdapter = new SmsAdapter(this, R.layout.queried_message_row,null,0);

        // Associate the (now empty) adapter with the ListView.
        ListView listView = (ListView) findViewById(R.id.queried_message_list);
        listView.setAdapter(mSmsAdapter);

        //create SmsLoaderCallbacks instantion
        SmsLoaderCallbacks loaderCallbacks = new SmsLoaderCallbacks(this, mSmsAdapter);

        // Start the loader with the query string inside bundle and
        // an object that will handle all callbacks.
        getLoaderManager().restartLoader(SMS_QUERY_LOADER, bundle, loaderCallbacks);

    }
    private void initializeList() {
        ListView list = (ListView) findViewById(R.id.queried_message_list);
    }
}
