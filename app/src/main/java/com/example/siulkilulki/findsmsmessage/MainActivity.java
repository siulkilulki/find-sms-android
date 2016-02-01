package com.example.siulkilulki.findsmsmessage;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.app.DatePickerDialog.OnDateSetListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    final static String PHRASE_KEY = "query";
    private boolean inboxSwitchState = true; // starting value
    private boolean sentSwitchState = false; // starting value
    private boolean regexSwitchState = false; // starting value
    protected static long dateFromInMillis = (new Date(0).getTime());
    protected static long dateToInMillis = (new Date().getTime());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setDateButtons();
        switchActions();
        searchButtonAction();
    }

    private void setDateButtons() {
        Button fromDate = (Button) findViewById(R.id.button_date_from);
        Button toDate = (Button) findViewById(R.id.button_date_to);
        SimpleDateFormat format = new SimpleDateFormat("d MMMM y");
        /*Date now = new Date();
        Date beginning = new Date(0);*/
        fromDate.setText(format.format(new Date(dateFromInMillis)));
        toDate.setText(format.format(new Date(dateToInMillis)));

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v, true);
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v, false);
            }
        });
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

        regexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                regexSwitchState = isChecked;
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
                String[] bundleData = new String[5];

                // Determine where to search for sms
                if (inboxSwitchState && sentSwitchState) {
                    bundleData[Constants.SWITCHES_STATE] = "both";
                } else if (inboxSwitchState) {
                    bundleData[Constants.SWITCHES_STATE] ="inbox";
                } else if (sentSwitchState) {
                    bundleData[Constants.SWITCHES_STATE] ="sent";
                }
                bundleData[Constants.REGEX] = regexSwitchState ? "true" : "false";

                // Get user input(search phrase)
                bundleData[Constants.SEARCH_PHRASE] = editText.getText().toString();
                bundleData[Constants.DATE_FROM] = String.valueOf(dateFromInMillis);
                bundleData[Constants.DATE_TO] = String.valueOf(dateToInMillis);
                intent.putExtra(PHRASE_KEY, bundleData);
                startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment
                        implements DatePickerDialog.OnDateSetListener {
        private boolean isFromDate;

        private static final String KEY = "KEY";
        static DatePickerFragment newInstance(boolean isFromDate) {
            DatePickerFragment f = new DatePickerFragment();
            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putBoolean(KEY, isFromDate);
            f.setArguments(args);
            return f;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            isFromDate = (boolean) getArguments().getBoolean(KEY);
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int monthOfYear = c.get(Calendar.MONTH);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, monthOfYear, dayOfMonth);
        }
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat format = new SimpleDateFormat("d MMMM y");
            Calendar date = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            if (isFromDate) {
                Button fromDate = (Button) getActivity().findViewById(R.id.button_date_from);
                dateFromInMillis = date.getTimeInMillis();
                fromDate.setText(format.format(new Date(dateFromInMillis)));
            } else {
                Button toDate = (Button) getActivity().findViewById(R.id.button_date_to);
                dateToInMillis = date.getTimeInMillis();
                toDate.setText(format.format(new Date(dateToInMillis)));
            }
        }
        String[] getDates() {
            String[] arrString = new String[2];
            arrString[0] = String.valueOf(dateFromInMillis);
            arrString[1] = String.valueOf(dateToInMillis);
            return arrString;
        }
    }
    private void showDatePickerDialog(View v, boolean isFromDate) {
        DialogFragment newFragment = DatePickerFragment.newInstance(isFromDate);
        newFragment.show(getSupportFragmentManager(), "datePicker");
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