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
import android.util.Log;

/**
 * Created by siulkilulki on 15.01.16.
 */


//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // TODO: don't perform query when screen rotates, but reuse existing data

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
public class SmsMmsCursorLoader extends AsyncTaskLoader<Cursor> {
    private static final String TAG = "SmsMmsCursorLoader";
    private static final String mmsTag = "mmsQuery";
    Context mContext;
    private Cursor mCursorData;

    private final int switchesState = 0;
    private final int searchPhrase = 1;

    String[] bundleData;
    public SmsMmsCursorLoader(Context mContext, String[] bundleData) {
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
    public Cursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        String query = bundleData[searchPhrase];
        // BEGIN_INCLUDE(uri_with_query)
        Uri smsUri, mmsUri;
/*        Uri.Builder builder = Uri.parse("content://mms").buildUpon();
        builder.appendPath(String.valueOf(2)).appendPath("addr");
        Log.i(mmsTag,builder.build().toString());*/
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
        //Uri uri = Uri.withAppendedPath(inboxUri, query); content://sms/"inbox lub sent"
        Cursor smsCursor = smsQuery(smsUri, query);
        Cursor mmsCursor = mmsQuery(mmsUri, query);
        return smsCursor;
    }
    private Cursor getContacts() {
        return null;
    }
    private Cursor mmsQuery(Uri uri, String query) {

        Cursor mmsCursor = mContext.getContentResolver().query(uri, null, null, null, null);
        //Log.i(mmsTag, String.valueOf(mmsCursor.getCount()));
        String[] columnNames = mmsCursor.getColumnNames();

        for (String str : columnNames
             ) {
            Log.i(mmsTag,str);
        }
        mmsCursor.moveToFirst();
        do {
            //Log.i("mms",mmsCursor.);
        } while (mmsCursor.moveToNext());
        return mmsCursor;
    }
    private Cursor smsQuery(Uri uri, String query) {
        Log.i(TAG,uri.toString());
        String selection = "body LIKE ?";
        String[] selectionArgs = {"%"+query+"%"};
        // END_INCLUDE(uri_with_query)

        Log.i(TAG, ("i am in onCreateLoader"));
        String[] projection = {"_id","address","date","date_sent", "body"};
        Cursor smsCursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        return smsCursor;
    }

    /**
     * Sends the result of the load to the registered listener.
     *
     * @param data the result of the load
     */

    @Override
    public void deliverResult(Cursor data) {
        Log.d(TAG, "deliverResult");
        // The Loader has been reset; ignore the result and invalidate the data.
        if (isReset()) {
            releaseResources(data);
            return;
        }
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        Cursor oldData = data;
        mCursorData = data;
        // If the Loader is in a started state, deliver the results to the
        // client. The superclass method does this for  us.
        if (isStarted()) {
            super.deliverResult(data);
        }
        if (oldData != data && oldData != null) {
            releaseResources(oldData);
        }
    }

    private void releaseResources(Cursor data) {
        Log.d(TAG, "releaseResources");
        data.close();
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading()");
        if (mCursorData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mCursorData);
        }
        // TODO: Add Obserwer.
        // TODO: Sms intent reciever to automatically update list whent/recieved
        // That's how we start every AsyncTaskLoader.
        if(takeContentChanged() || mCursorData == null) {
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
        if (mCursorData != null) {
            releaseResources(mCursorData);
            mCursorData = null;
        }
    }



    /**
     * Called if the task was canceled before it was completed.
       Gives the class a chance to clean up post-cancellation and to properly dispose of the result.
     * @param data data to be canceled
     */
    @Override
    public void onCanceled(Cursor data) {
        Log.d(TAG, "onCanceled()");
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);
        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }
}
