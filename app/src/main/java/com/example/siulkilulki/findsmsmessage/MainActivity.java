package com.example.siulkilulki.findsmsmessage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    public final static String PHRASE_KEY = "query";
    private final static int switchesState = 0;
    private final static int searchPhrase = 1;
    private final static int regexState = 2;
    private boolean inboxSwitchState = true; //starting values
    private boolean sentSwitchState = false; //starting values
    private boolean regexSwitchState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switchActions();
        searchButtonAction();
    }

    private void switchActions() {
        final Switch inboxSwitch = (Switch) findViewById(R.id.inbox_switch);
        final Switch sentSwitch = (Switch) findViewById(R.id.sent_switch);
        final Switch regexSwitch = (Switch) findViewById(R.id.regex_switch);
        inboxSwitch.setChecked(true);

        inboxSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inboxSwitchState = true;
                } else {
                    inboxSwitchState = false;
                    if (!sentSwitchState) {
                        sentSwitch.toggle();
                    }
                }
            }
        });

        sentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sentSwitchState = true;
                } else {
                    sentSwitchState = false;
                    if (!inboxSwitchState) {
                        inboxSwitch.toggle();
                    }

                }
            }
        });

        regexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    regexSwitchState = true;
                } else {
                    regexSwitchState = false;
                }
            }
        });
    }

    private void searchButtonAction() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SmsListActivity.class);
                EditText editText = (EditText) findViewById(R.id.search_input);
                String[] bundleData = new String[3];

                // Determine where to search for sms
                if (inboxSwitchState && sentSwitchState) {
                    bundleData[switchesState] = "both";
                } else if (inboxSwitchState) {
                    bundleData[switchesState] ="inbox";
                } else if (sentSwitchState) {
                    bundleData[switchesState] ="sent";
                }
                bundleData[regexState] = regexSwitchState ? "true" : "false";

                // Get user input(search phrase)
                bundleData[searchPhrase] = editText.getText().toString();
                intent.putExtra(PHRASE_KEY, bundleData);
                startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }


    // TODO: setting not used. Maybe add here info for user about how to query sms
    /*@Override
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
    }*/
}