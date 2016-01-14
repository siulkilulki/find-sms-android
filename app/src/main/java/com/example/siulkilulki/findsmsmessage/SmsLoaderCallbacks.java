package com.example.siulkilulki.findsmsmessage;

/**
 * Created by siulkilulki on 13.01.16.
 */

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 *   Helper class to handle all the callbacks that occur when interacting with loaders.
 */
public class SmsLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    Context mContext;
    SmsAdapter mSmsAdapter;
    public static final String QUERY_KEY = "query";
    public static final String TAG = "SmsLoaderCallbacks";

    public SmsLoaderCallbacks(Context mContext, SmsAdapter mSmsAdapter) {
        this.mContext = mContext;
        this.mSmsAdapter = mSmsAdapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderIndex, Bundle args) {
        // BEGIN_INCLUDE(uri_with_query)
        String query = args.getString(QUERY_KEY);
        Uri inboxUri = Uri.parse("content://sms/inbox");
        //Uri uri = Uri.withAppendedPath(inboxUri, query); content://sms/"inbox lub sent"
        // END_INCLUDE(uri_with_query)
        Log.i(TAG, ("i am in onCreateLoader"));
       String[] projection = {"_id","address","date","date_sent", "body"};
        return new CursorLoader(
            mContext,   // Context
            inboxUri,   // URI representing the table/resource to be queried
            null,       // projection - the list of columns to return.  Null means "all"
            null,       // selection - Which rows to return (condition rows must match)
            null,       // selection args - can be provided separately and subbed into selection.
            null);      // string specifying sort order
        // END_INCLUDE(cursor_loader)

    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        Log.i(TAG, (String.valueOf(cursor.getCount())));
        if (cursor.getCount() == 0) {
            Log.i(TAG, "zero elements in cursor");
            return;
        }
        mSmsAdapter.swapCursor(cursor);
        /*String[] array = cursor.getColumnNames();
        int id = cursor.getColumnIndex("_id");
        int adress = cursor.getColumnIndex("address");
        int date = cursor.getColumnIndex("date");
        int date_sent = cursor.getColumnIndex("date_sent");
        int body = cursor.getColumnIndex("body");
        cursor.moveToFirst();
        List<Sms> smses = new ArrayList<>();
        do {
            Sms sms = new Sms(
                    cursor.getInt(id),
                    cursor.getString(adress),
                    cursor.getString(date),
                    cursor.getString(date_sent),
                    cursor.getString(body)
                    );
            smses.add(sms);
        } while (cursor.moveToNext());*/



    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        mSmsAdapter.swapCursor(null);
    }


}
