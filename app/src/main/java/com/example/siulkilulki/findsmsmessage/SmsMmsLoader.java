package com.example.siulkilulki.findsmsmessage;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Telephony;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by siulkilulki on 15.01.16.
 */


/**
 * Custom Loader for loading sms data
 */
class SmsMmsLoader extends AsyncTaskLoader<List<Sms>> {
    private static final String TAG = "SmsMmsLoader";
    private Context mContext;
    private List<Sms> mSmsList;
    private String mQuery;
    private final int CONTACT_ID = 0;
    private final int NAME = 1;
    private final int PHOTO_THUMBNAIL_URI = 2;
    private String[] bundleData;
    private SmsAdapter mSmsAdpater;
    private int dataCount = 0;

    SmsMmsLoader(Context mContext, String[] bundleData, SmsAdapter mSmsAdapter) {
        super(mContext);
        this.mContext = mContext;
        this.bundleData = bundleData;
        this.mSmsAdpater = mSmsAdapter;
        Log.d(TAG, "Constructor fired");
        onContentChanged();
    }

    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     */
    @Override
    public List<Sms> loadInBackground() {
        Log.d(TAG, "loadInBackground");
        mQuery = bundleData[Constants.SEARCH_PHRASE];
        String query = correctQuery(mQuery);
        Uri smsUri;
        //mmsUri = Uri.parse("content://mms/inbox"); TODO: mms are still on todo list
        switch (bundleData[Constants.SWITCHES_STATE]) {
            case ("both"):
                smsUri = Uri.parse("content://sms");
                break;
            case ("inbox"):
                smsUri = Uri.parse("content://sms/inbox");
                break;
            case ("sent"):
                smsUri = Uri.parse("content://sms/sent");
                break;
            default:
                smsUri = Uri.parse("content://sms");// never goes here
        }
        CursorDataProviders dataProvider = new CursorDataProviders(mContext);
        Cursor smsCursor = dataProvider.smsQuery(smsUri, query, bundleData);
        //Cursor mmsCursor = mmsQuery(mmsUri, query); TODO: mms are still on todo list
        dataCount = smsCursor.getCount();
        if (dataCount != 0) {
            return getSmsList(dataProvider, smsCursor);
        }
        return new ArrayList<>();
    }


    /**
     * Change query String so that we can search for "%" and "_".
     *
     * This means the method change every "%" to "\%" and every "_" to "\_"
     */
    private String correctQuery(String query) {
        String newQuery = query.replace("%", "†%");
        return newQuery.replace("_","†_");
    }

    private List<Sms> getSmsList(CursorDataProviders dataProvider, Cursor smsCursor) {
        Log.d(TAG,"getSms");
        int i = 0;
        mSmsList = new ArrayList<>();
        SmsDataOrganizer dataOrganizer = new SmsDataOrganizer();
        Cursor contactsCursor = dataProvider.getContacts();
        HashMap<String, Object[]> hashedContacts = dataOrganizer.hashContacts(contactsCursor);
        smsCursor.moveToFirst();
        int bodyIndex =  smsCursor.getColumnIndex(Telephony.Sms.BODY);
        int phoneIndex = smsCursor.getColumnIndex(Telephony.Sms.ADDRESS);
        int typeIndex = smsCursor.getColumnIndex(Telephony.Sms.TYPE);
        int dateIndex = smsCursor.getColumnIndex(Telephony.Sms.DATE);
        SimpleDateFormat months = new SimpleDateFormat("d MMM"); // creating here becouse
        SimpleDateFormat years = new SimpleDateFormat("d MMM y");// creating many intances of
        Date currentDate = new Date();                           // SimpleDateFormat inside loop
        do {                                                     // slows down list loading
            Sms sms = new Sms();
            String body = smsCursor.getString(bodyIndex);
            sms.body = body;

            //turn off color in regex aka complex search
            if (bundleData[Constants.REGEX].equals("true")) {
                sms.bodyColored = new SpannableString(body);
            } else if (bundleData[Constants.REGEX].equals("false")) {
                sms.bodyColored = getColoredBody(body, mQuery);
            }
            sms.phoneNr = dataOrganizer.prettifyNumber(smsCursor.getString(phoneIndex));
            sms.type = smsCursor.getInt(typeIndex);
            long rawDate = smsCursor.getLong(dateIndex);
            sms.rawDate = rawDate;
            sms.date = getDate(rawDate, months, years, currentDate);
            Object[] hashedContact = hashedContacts.get(sms.phoneNr);
            if (hashedContact != null) {
                sms.contactId = (long) hashedContact[CONTACT_ID];
                sms.name = (String) hashedContact[NAME];
                String photoUri = (String) hashedContact[PHOTO_THUMBNAIL_URI];
                sms.photoThumbnailUri = (photoUri != null) ? Uri.parse(photoUri) : null;
            }
            if (isLoadInBackgroundCanceled()){
                Log.d(TAG,"load in Background canceled");
            }
            i++;
            Log.d(TAG,String.valueOf(i));
            if (isAbandoned()) {
                Log.d(TAG,"load abandoned");
            }
            mSmsList.add(sms);
        } while (smsCursor.moveToNext());
        smsCursor.close();
        Log.d(TAG,"cursor closed");
        return mSmsList;
    }

    /**
     *  Colors query within body. Cut body so that the colored query would be displayed in the
     *  middle of the list body TextView.
     * @return returns SpannableString containg cutted body from both sides with colored query
     */
    //TODO: get visible nr of characters
    private SpannableString getColoredBody(String body, String query) {
        int startOfColor = body.toLowerCase().indexOf(query.toLowerCase()),
            endOfColor = startOfColor + query.length();
        SpannableString coloredBody = new SpannableString(body);
        //TODO: get color from colors.xml
        coloredBody.setSpan(new ForegroundColorSpan(Color.parseColor("#FF4081")), startOfColor, endOfColor, 0);
        return coloredBody;
    }

    private String getDate(long smsTime, SimpleDateFormat months, SimpleDateFormat years,
                           Date currentDate) {
        Date date = new Date(smsTime);
        if (date.getYear() == currentDate.getYear()) { // its faster than creating calendar instances
            return months.format(date);
        } else {
            return years.format(date);
        }
    }

    /**
     * Sends the result of the load to the registered listener.
     *
     * @param data the result of the load
     */
    @Override
    public void deliverResult(List<Sms> data) {
        // The Loader has been reset; ignore the result and invalidate the data.
        if (isReset()) {
            Log.d(TAG,"deliver result Loader reset");
            return;
        }
        // Hold a reference to the old data so it doesn't get garbage collected.

        /*List<Sms> oldData = data;
        mSmsList = data;*/
        // If the Loader is in a started state, deliver the results to the
        // client. The superclass method does this for  us.
        Log.d(TAG,"deliver results");
        if (isStarted()) {
            Log.d(TAG,"deliver Results isStarted");
            super.deliverResult(data);
        }
    }


    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        Log.d(TAG,"onStartLoading");
        if (mSmsList != null && dataCount == mSmsList.size()) {
            // Deliver any previously loaded data immediately.
            Log.d(TAG, "onStartLoading not null");
            //forceLoad();
            deliverResult(mSmsList);
            return;
        }
        // TODO: Add Obserwer.
        // TODO: Sms intent reciever to automatically update list when recieved
        // That's how we start every AsyncTaskLoader.
        if(takeContentChanged() || mSmsList == null) {
            Log.d(TAG, "onStartLoading forceLoad");
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        Log.d(TAG,"onStopLoading");
        cancelLoad();
        
    }

    /**
     * Take the current flag indicating whether the loader's content had
     * changed while it was stopped.  If it had, true is returned and the
     * flag is cleared.
     */
    @Override
    protected void onReset() {

        // Ensure the loader has been stopped. Stops the loader
        Log.d(TAG,"onReset");
        onStopLoading();
        
        // At this point we can release the resources associated with 'mSmsList'.
        if (mSmsList != null) {
            mSmsList = null;
        }
    }



    /**
     * Called if the task was canceled before it was completed.
       Gives the class a chance to clean up post-cancellation and to properly dispose of the result.
     * @param data data to be canceled
     */
    @Override
    public void onCanceled(List<Sms> data) {
        // Attempt to cancel the current asynchronous load.
        Log.d(TAG,"onCanceled");
        if (mSmsList != null && dataCount == mSmsList.size()) {
            deliverResult(mSmsList);
        }
        super.onCanceled(data);
        // The load has been canceled, so we should release the resources
        // associated with 'data'.
    }
}
