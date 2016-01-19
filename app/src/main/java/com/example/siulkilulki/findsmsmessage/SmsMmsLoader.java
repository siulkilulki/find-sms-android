package com.example.siulkilulki.findsmsmessage;

import android.content.AsyncTaskLoader;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siulkilulki on 15.01.16.
 */


public class SmsMmsLoader extends AsyncTaskLoader<List<Sms>> {
    private static final String TAG = "SmsMmsLoader";
    private static final String mmsTag = "mmsQuery";
    Context mContext;
    private Cursor mCursorData;
    private List<Sms> mSmsList;

    private final int switchesState = 0;
    private final int searchPhrase = 1;

    String[] bundleData;
    public SmsMmsLoader(Context mContext, String[] bundleData) {
        super(mContext);
        this.mContext = mContext;
        this.bundleData = bundleData;
        if (mSmsList == null)
            Log.d(TAG, "mSmsBundle == null");
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
        String query = bundleData[searchPhrase];
        Uri smsUri, mmsUri;
        mmsUri = Uri.parse("content://mms/inbox");
        switch (bundleData[switchesState]) {
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
        Cursor smsCursor = dataProvider.smsQuery(smsUri, query);
        //Cursor mmsCursor = mmsQuery(mmsUri, query);
        Cursor contactsCursor = dataProvider.getContacts();

        mSmsList = new ArrayList<>();
        if (smsCursor.getCount() != 0) {
            smsCursor.moveToFirst();
            int bodyIndex =  smsCursor.getColumnIndex("body");
            int phoneIndex = smsCursor.getColumnIndex("address");
            do {
                Sms sms = new Sms();
                sms.body = smsCursor.getString(bodyIndex);
                sms.phoneNr = smsCursor.getString(phoneIndex);
               /* Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                        Uri.encode(sms.phoneNr));
                //Log.d(TAG, contactUri.toString());
                Cursor contactCursor = dataProvider.getContact(contactUri);
                //Log.d(TAG, sms.phoneNr);
                if (contactCursor != null) {
                    if (contactCursor.moveToFirst()) {
                        sms.name = contactCursor.getString(contactCursor.getColumnIndexOrThrow(
                                ContactsContract.PhoneLookup.DISPLAY_NAME));
                        sms.contactId = contactCursor.getInt(contactCursor.getColumnIndexOrThrow(
                                ContactsContract.PhoneLookup._ID));
                        //Log.d(TAG, sms.name + " " + sms.contactId);
                    }

                }
                contactCursor.close();*/
                mSmsList.add(sms);
            } while (smsCursor.moveToNext());
            smsCursor.close();
            return mSmsList;
        }

        return mSmsList;
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
        // TODO: Sms intent reciever to automatically update list whent/recieved
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
