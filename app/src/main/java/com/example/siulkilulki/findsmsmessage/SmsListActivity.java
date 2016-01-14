package com.example.siulkilulki.findsmsmessage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;


public class SmsListActivity extends AppCompatActivity {
    public static final String QUERY_KEY = "query";
    public static final int SMS_QUERY_LOADER = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);
        Intent intent = getIntent();
        String query = intent.getStringExtra(MainActivity.PHRASE_KEY);
        //Toast.makeText(this, query, Toast.LENGTH_SHORT).show();

        //Bundle containg query string
        Bundle bundle = new Bundle();
        bundle.putString(QUERY_KEY, query);

        SmsLoaderCallbacks loaderCallbacks = new SmsLoaderCallbacks(this);

        //Start the loader with the query string inside bundle and
        //an object that will handle all callbacks.
        getLoaderManager().restartLoader(SMS_QUERY_LOADER, bundle, loaderCallbacks);

    }
    private void initializeList() {
        ListView list = (ListView) findViewById(R.id.queried_message_list);
    }
}
