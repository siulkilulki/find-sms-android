package com.example.siulkilulki.findsmsmessage;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.text.SpannableString;
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

    SmsMmsLoader(Context mContext, String[] bundleData) {
        super(mContext);
        this.mContext = mContext;
        this.bundleData = bundleData;
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
        Cursor smsCursor = dataProvider.smsQuery(smsUri, query, bundleData[Constants.REGEX]);
        //Cursor mmsCursor = mmsQuery(mmsUri, query); TODO: mms are still on todo list

        //TODO: move below code to convenient method

        if (smsCursor.getCount() != 0) {
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
        mSmsList = new ArrayList<>();
        SmsDataOrganizer dataOrganizer = new SmsDataOrganizer();
        Cursor contactsCursor = dataProvider.getContacts();
        HashMap<String, Object[]> hashedContacts = dataOrganizer.hashContacts(contactsCursor);
        smsCursor.moveToFirst();
        int bodyIndex =  smsCursor.getColumnIndex(Telephony.Sms.BODY);
        int phoneIndex = smsCursor.getColumnIndex(Telephony.Sms.ADDRESS);
        int typeIndex = smsCursor.getColumnIndex(Telephony.Sms.TYPE);
        int dateIndex = smsCursor.getColumnIndex(Telephony.Sms.DATE);
        int dateSentIndex = smsCursor.getColumnIndex(Telephony.Sms.DATE_SENT);
        SimpleDateFormat months = new SimpleDateFormat("d MMM"); // creating here becouse
        SimpleDateFormat years = new SimpleDateFormat("d MMM y");// creating many intances of
        Date currentDate = new Date();                           // SimpleDateFormat inside loop
        do {                                                     // slows down list loading
            Sms sms = new Sms();
            String body = smsCursor.getString(bodyIndex);
            sms.body = body;
            //sms.bodyColored = ;
            sms.phoneNr = dataOrganizer.prettifyNumber(smsCursor.getString(phoneIndex));
            sms.type = smsCursor.getInt(typeIndex);
            //sms.rawDateSent = smsCursor.getLong(dateSentIndex); //TODO: add later
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
            mSmsList.add(sms);
        } while (smsCursor.moveToNext());
        smsCursor.close();
        return mSmsList;
    }

    /**
     *  Colors query within body. Cut body so that the colored query would be displayed in the
     *  middle of the list body TextView.
     * @return returns SpannableString containg cutted body from both sides with colored query
     */
   /* private SpannableString getColoredBody(String body, String query) {


    }*/

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
        Log.d(TAG, "deliverResult");
        // The Loader has been reset; ignore the result and invalidate the data.
        if (isReset()) {
            Log.d(TAG, "deliverResult - maybe mistake here");
            releaseResources(data);
            return;
        }
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<Sms> oldData = data;
        mSmsList = data;
        // If the Loader is in a started state, deliver the results to the
        // client. The superclass method does this for  us.
        if (isStarted()) {
            Log.d(TAG, "super.deliverResult");
            super.deliverResult(data);
        }
        if (oldData != data && oldData != null) {
            releaseResources(oldData);
        }
    }

    //TODO: maybe will use it, so its still here, if not delete this method since it's doesn't
    // TODO: make sense to release List data
    private void releaseResources(List<Sms> data) {
        Log.d(TAG, "releaseResources");
        //data.close();
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading()");
        if (mSmsList != null) {
            Log.d(TAG, "onStartLoading() deliverResult");
            // Deliver any previously loaded data immediately.
            deliverResult(mSmsList);
        }
        // TODO: Add Obserwer.
        // TODO: Sms intent reciever to automatically update list when recieved
        // That's how we start every AsyncTaskLoader.
        if(takeContentChanged() || mSmsList == null) {
            Log.d(TAG, "onStartLoading() ForceLoad");
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        Log.d(TAG, "onStopLoading()");
        cancelLoad();
    }

    /**
     * Take the current flag indicating whether the loader's content had
     * changed while it was stopped.  If it had, true is returned and the
     * flag is cleared.
     */
    @Override
    protected void onReset() {
        Log.d(TAG, "onReset()");
        // Ensure the loader has been stopped. Stops the loader
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (mSmsList != null) {
            releaseResources(mSmsList);
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
        Log.d(TAG, "onCanceled()");
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);
        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }
}
