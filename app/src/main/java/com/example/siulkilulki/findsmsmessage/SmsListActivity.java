package com.example.siulkilulki.findsmsmessage;

import android.content.Intent;
import android.database.Cursor;
import android.provider.Telephony;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class SmsListActivity extends AppCompatActivity {
    private static final int SMS_QUERY_LOADER = 0;

    // The adapter that binds our data to the ListView
    private SmsAdapter mSmsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queried_message_list);
        Intent intent = getIntent();

        // Bundle containg query string
        String[] bundleData = intent.getStringArrayExtra(MainActivity.PHRASE_KEY);
        Bundle bundle = new Bundle();
        bundle.putStringArray(Constants.QUERY_KEY, bundleData);

        // Initialize the adapter
        // We will pass the adapter a Data only when the
        // data has finished loading for the first time (for example when the
        // LoaderManager delivers the data to onLoadFinished)
        mSmsAdapter = new SmsAdapter(this);

        // Associate the (now empty) adapter with the ListView.
        ListView listView = (ListView) findViewById(R.id.queried_message_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sms sms =  mSmsAdapter.getItem(position);
                showSmsDialog(sms);
            }
        });

        // Create SmsLoaderCallbacks instantion
        SmsLoaderCallbacks loaderCallbacks = new SmsLoaderCallbacks(this, mSmsAdapter, listView);

        // Start the loader with the query string inside bundle and
        // an object that will handle all callbacks.
        getLoaderManager().restartLoader(SMS_QUERY_LOADER, bundle, loaderCallbacks);

    }

    private void showSmsDialog(Sms sms) {
        FragmentManager fm = getSupportFragmentManager();
        SmsDialogFragment smsDialogFragment = SmsDialogFragment.newInstance(sms);
        smsDialogFragment.show(fm, "fragment_edit_name");
    }
}
